package com.GinElmaC;

import com.GinElmaC.annotation.Component;
import com.GinElmaC.annotation.PostConstruct;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 框架的核心类，用来造对象和拿对象
 */
public class ApplicationContext {

    public ApplicationContext(String packageName) throws Exception {
        initContext(packageName);
    }
    public void initContext(String packageName) throws Exception {
        /**
         * 初始化上下文，获取所有的beanDefinition
         */
        scanpackage(packageName).stream() // 获取packageName下所有的.class文件
                .filter(this::scanCreate) // 过滤出需要初始化的Bean
                .forEach(this::wrapper); // 将类封装成BeanDefinition
        /**
         * 初始化BeanPostProcessor
         */
        initBeanPostProcessor();
        /**
         * 创建bean
         */
        beanDefinitionMap.values().forEach(this::createBean);//利用BeanDefinition创建Bean
    }
    private void initBeanPostProcessor() {
        beanDefinitionMap.values().stream()
                .filter(f-> BeanPostProcessor.class.isAssignableFrom(f.getBeanType())) // 证明当前要创建的是BeanPostProcessor类或者实现类
                .map(this::createBean)
                .map(o->(BeanPostProcessor)o)
                .forEach(beanPostProcessors::add);
    }

    /**
     * 储存Bean，key为bean的名字，value为bean
     */
    private Map<String,Object> loadingIoc = new HashMap<>(); //一级缓存，放置被初始化的bean
    private Map<String,BeanDefinition> beanDefinitionMap = new HashMap<>(); //二级缓存，bean工厂
    private Map<String,Object> ioc = new HashMap<>(); //三级缓存，完成的bean

    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public Object getBean(String name){
        if(name == null){
            return null;
        }
        Object Bean = this.ioc.get(name);
        if(Bean != null){
            return Bean;
        }
        if(beanDefinitionMap.containsKey(name)){
            return createBean(beanDefinitionMap.get(name));
        }
        return null;
    }

    public <T> T getBean(Class<T> beanType){
        /**
         * 获取bean的名字
         */
        String name = this.beanDefinitionMap.values().stream()
                .filter(bd -> beanType.isAssignableFrom(bd.getBeanType()))
                .map(BeanDefinition::getName)
                .findFirst()
                .orElse(null);
        return (T)getBean(name);
    }

    public <T> List<T> getBeans(Class<T> beanType){
        return this.beanDefinitionMap.values().stream()
                .filter(bd -> beanType.isAssignableFrom(bd.getBeanType()))
                .map(BeanDefinition::getName)
                .map(this::getBean)
                .map(bean->(T)bean).toList();
    }

    /**
     * 包扫描的函数
     * @param packageName
     * @return
     */
    private List<Class<?>> scanpackage(String packageName) throws Exception {
        List<Class<?>> classList = new ArrayList<>();
        URL resource = this.getClass().getClassLoader().getResource(packageName.replace(".","/"));
        /**
         * resource.getPath() 获取resource的path路径
         * Path.of 根据路径生成一个Path对象
         */
        String file = resource.getFile();
        char[] ch = file.toCharArray();
        file = "";
        for(int i = 1;i<ch.length;i++){
            file+=ch[i];
        }
        Path path = Path.of(file);
        /**
         * 遍历文件夹中的文件，并对于每一个文件执行后面的方法
         * walkFileTree(Path path,FileVisitor<? super Path> visitor) 可以从path开始遍历文件树，对于每一个遍历到的文件执行后面的回调方法
         * SimpleFileVisitor就是FileVisitor<? super Path>的实现类
         */
        Files.walkFileTree(path,new SimpleFileVisitor<>(){
            /**
             *file就是遍历到的文件
             * FileVisitResult是文件的遍历结果枚举，其中有四个结果：
             * CONTINUE -继续正常遍历
             * TERMINATE -立即终止整个过程
             * SKIP_SUBTREE -跳过当前目录的子项
             * SKIP_SIBLINGS -跳过当前文件/目录的所有兄弟项
             */
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                //意思就是每一个文件都进行遍历
                //转化为绝对路径
                Path abs = file.toAbsolutePath();
                //如果是".class"结尾的，则代表使我们需要进行加载的bean
                if(abs.toString().endsWith(".class")){
                    //转化回全限定包名
                    String replace = abs.toString().replace("\\",".");
                    //截取目标包名（相对路径）
                    int packageIndex = replace.indexOf(packageName);
                    String className = replace.substring(packageIndex, replace.length() - ".class".length());
                    try {
                        classList.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return classList;
    }

    /**
     * 判断一个类是否需要被创建
     * @param type
     * @return
     */
    protected boolean scanCreate(Class<?> type){
        return type.isAnnotationPresent(Component.class);
    }

    /**
     * 利用BeanDefinition创建Bean
     * @param type
     * @return
     */
    protected BeanDefinition wrapper(Class<?> type){
        BeanDefinition beanDefinition = new BeanDefinition(type);
        if(beanDefinitionMap.containsKey(beanDefinition.getName())){
            throw new RuntimeException("this bean has been exists!");
        }
        beanDefinitionMap.put(beanDefinition.getName(),beanDefinition);
        return beanDefinition;
    }

    /**
     * 判断bean是否已经被创建
     * @param beanDefinition
     */
    protected Object createBean(BeanDefinition beanDefinition){
        String name = beanDefinition.getName();
        /**
         * 三级缓存存在，直接返回
         */
        if(ioc.containsKey(name)){
            return ioc.get(name);
        }
        /**
         * 为了提前暴露，单例模式下的循环依赖解决方案
         */
        if(loadingIoc.containsKey(name)){
            return loadingIoc.get(name);
        }
        /**
         * 如果都没有，则创建
         */
        return doCreateBean(beanDefinition);
    }

    /**
     * 真正的创建Bean
     * @param beanDefinition
     */
    private Object doCreateBean(BeanDefinition beanDefinition){
        Constructor<?> constructor = beanDefinition.getConstructor();
        Object bean = null;
        try {
            /**
             * 创造Bean实例
             */
            bean = constructor.newInstance();
            /**
             * 先放入一级缓存
             */
            loadingIoc.put(beanDefinition.getName(),bean);
            /**
             * 注入Bean属性
             */
            autowiredBean(bean,beanDefinition);
            /**
             * 调用PostConstruct方法
             */
            bean = initializedBean(bean,beanDefinition);
            /*
            放入三级缓存并删除一级缓存中的内容
             */
            loadingIoc.remove(beanDefinition.getName());
            ioc.put(beanDefinition.getName(),bean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bean;
    }

    /**
     * 调用PostConstruct方法
     * @param bean
     * @param beanDefinition
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private Object initializedBean(Object bean, BeanDefinition beanDefinition) throws InvocationTargetException, IllegalAccessException {
        /**
         * before方法
         */
        for(BeanPostProcessor beanPostProcessor:beanPostProcessors){
            bean = beanPostProcessor.beforeInitializeBean(bean,beanDefinition.getName());
        }

        List<Method> postConstructList = beanDefinition.getPostConstructs();
        if(postConstructList != null && postConstructList.size()>0){
            //使用创造出来的bean进行调用
            for (Method postConstruct:postConstructList) {
                postConstruct.invoke(bean);
            }
        }

        /**
         * after方法
         */
        for(BeanPostProcessor beanPostProcessor:beanPostProcessors){
            bean = beanPostProcessor.afterInitializeBean(bean,beanDefinition.getName());
        }

        return bean;
    }

    /**
     * 自动注入
     * @param bean
     * @param beanDefinition
     * @throws IllegalAccessException
     */
    private void autowiredBean(Object bean, BeanDefinition beanDefinition) throws IllegalAccessException {
        for(Field field:beanDefinition.getAutowiredFields()){
            field.setAccessible(true);
            Object autowiredBean = null;
            field.set(bean,getBean(field.getType()));//根据类型自动注入
        }
    }

}
