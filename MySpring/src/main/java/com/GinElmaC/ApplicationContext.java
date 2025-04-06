package com.GinElmaC;

import com.GinElmaC.annotation.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
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

    /**
     * 储存Bean，key为bean的名字，value为bean
     */
    private Map<String,Object> ioc = new HashMap<>();
    private Map<String,BeanDefinition> beanDefinitionMap = new HashMap<>();


    public Object getBean(String name){
        return this.ioc.get(name);
    }

    /**
     * 这里也即使为什么Spring容器在使用class获取bean时需要遍历整个容器，因为底层的map并没有用class作为key
     * 如果使用了class作为key，假如现在需要获得一个object类型的bean，那么容器中的所有对象都满足isAssignableFrom
     */
    public <T> T getBean(Class<T> beanType){
        /**
         * a.isAssignableFrom(b.getClass()) 可以用来检查a是否是b的同类或者子类实现类
         */
        return this.ioc.values().stream()
                .filter(bean -> beanType.isAssignableFrom(bean.getClass()))
                .map(bean->(T)bean)
                .findAny()
                .orElseGet(null);
    }
    public <T> List<T> getBeans(Class<T> beanType){
        return this.ioc.values().stream()
                .filter(bean -> beanType.isAssignableFrom(bean.getClass()))
                .map(bean->(T)bean)
                .toList();
    }


    public void initContext(String packageName) throws Exception {
        /**
         * isAnnotationPresent是java反射的一个APi，可以检查某个类是否被某个注解标注
         */
        scanpackage(packageName).stream()
                .filter(this::scanCreate) // 过滤出需要初始化的Bean
                .map(this::wrapper) // 将类封装成BeanDefinition
                .forEach(this::createBean); //利用BeanDefinition创建Bean
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
         */
        Files.walkFileTree(path,new SimpleFileVisitor<>(){
            /**
             *file就是遍历到的文件
             */
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                //意思就是每一个文件都进行遍历
                Path abs = file.toAbsolutePath();
                if(abs.toString().endsWith(".class")){
                    String replace = abs.toString().replace("\\",".");
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
    protected  void createBean(BeanDefinition beanDefinition){
        String name = beanDefinition.getName();
        if(ioc.containsKey(name)){
            return;
        }
        doCreateBean(beanDefinition);
    }

    /**
     * 真正的创建Bean
     * @param beanDefinition
     */
    private void doCreateBean(BeanDefinition beanDefinition){
        Constructor<?> constructor = beanDefinition.getConstructor();
        Object bean = null;
        try {
            //调用构造函数来创造bean
            bean = constructor.newInstance();
            //获取PostConstruct标注的方法
//            Method postConstruct = beanDefinition.getPostConstruct();
            List<Method> postConstructList = beanDefinition.getPostConstructs();
            if(postConstructList != null && postConstructList.size()>0){
                //使用创造出来的bean进行调用
                for (Method postConstruct:postConstructList) {
                    postConstruct.invoke(bean);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ioc.put(beanDefinition.getName(),bean);

    }

}
