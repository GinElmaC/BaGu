package com.GinElmaC;

import com.GinElmaC.annotation.Autowired;
import com.GinElmaC.annotation.Component;
import com.GinElmaC.annotation.PostConstruct;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 创建Bean的Bean工厂
 */
public class BeanDefinition {
    //bean名字
    private String name;
    //bean的构造方法
    private Constructor<?> constructor;
    //被PostConstruct标注的方法
    private List<Method> postConstructs;
    //需要自动注入的属性
    private List<Field> autowiredFields;
    //bean的类型
    private Class<?> beanType;

    public BeanDefinition(Class<?> type){
        this.beanType = type;
        //获取注解
        Component declaredAnnotation = type.getDeclaredAnnotation(Component.class);
        //获取名字，如果为空则用类名，如果不为空则用名字
        this.name = declaredAnnotation.name().isEmpty()?type.getSimpleName(): declaredAnnotation.name();
        //获取构造方法，假设都有无参构造函数
        try {
            this.constructor = type.getConstructor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        /**
         * 解析被PostConstruct标注的方法
         */
        this.postConstructs = Arrays.stream(type.getDeclaredMethods())
                .filter(m->m.isAnnotationPresent(PostConstruct.class))
                .toList();
        /**
         * 解析autowired注解
         */
        this.autowiredFields = Arrays.stream(type.getDeclaredFields())
                .filter(f->f.isAnnotationPresent(Autowired.class))
                .toList();
    }

    /**
     * 获取Bean的名字
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     * 获取Bean的构造方法
     */
    public Constructor<?> getConstructor(){
        return constructor;
    }


    public List<Method> getPostConstructs(){
        return postConstructs;
    }

    public List<Field> getAutowiredFields() {
        return autowiredFields;
    }

    public Class<?> getBeanType() {
        return beanType;
    }
}
