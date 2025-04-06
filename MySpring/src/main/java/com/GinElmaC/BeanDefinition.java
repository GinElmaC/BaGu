package com.GinElmaC;

import com.GinElmaC.annotation.Component;
import com.GinElmaC.annotation.PostConstruct;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 创建Bean
 */
public class BeanDefinition {

    private String name;
    private Constructor<?> constructor;
    private Method postConstruct;
    private List<Method> postConstructs;

    public BeanDefinition(Class<?> type){
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
        //获取被postConstruct标注的函数
//        this.postConstruct = Arrays.stream(type.getDeclaredMethods()).filter(m->m.isAnnotationPresent(PostConstruct.class)).findFirst().orElseGet(null);
        this.postConstructs = Arrays.stream(type.getDeclaredMethods()).filter(m->m.isAnnotationPresent(PostConstruct.class)).toList();
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

    /**
     * 返回被PostConstruct注解的方法
     * @return
     */
    public Method getPostConstruct(){
        return postConstruct;
    }

    public List<Method> getPostConstructs(){
        return postConstructs;
    }
}
