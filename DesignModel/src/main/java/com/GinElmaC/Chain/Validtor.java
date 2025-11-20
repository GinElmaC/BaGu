package com.GinElmaC.Chain;

import java.lang.reflect.Field;

public class Validtor {

    public void doCheck(Object bean) throws IllegalAccessException {
        Class<?> aClass = bean.getClass();
        Field[] Fields = aClass.getDeclaredFields();
        for(Field field:Fields){
            field.setAccessible(true);
            Max max = field.getAnnotation(Max.class);
            if(max != null){
                //field.get(bean) 从bean中取出对应field的值
                checkMax(max,field.get(bean));
            }
            Min min = field.getAnnotation(Min.class);
            if(min != null){
                checkMin(min,field.get(bean));
            }
            Length length = field.getAnnotation(Length.class);
            if(length != null){
                checkLength(length,field.get(bean));
            }
        }
    }

    private void checkLength(Length length, Object o) {
        if(o instanceof String){
            String str = (String) o;
            if(str.length()> length.value()){
                throw new ValidErrorException("参数校验未通过");
            }
        }else{
            throw new ValidErrorException("注解类型错误");
        }
    }

    private void checkMin(Min min, Object o) {
        if(o instanceof Integer){
            Integer num = (Integer) o;
            if(num<min.value()){
                throw new ValidErrorException("参数校验未通过");
            }
        }else{
            throw new ValidErrorException("注解类型错误");
        }
    }

    private void checkMax(Max max, Object o) {
        if(o instanceof Integer){
            Integer num = (Integer) o;
            if(num>max.value()){
                throw new ValidErrorException("参数校验未通过");
            }
        }else{
            throw new ValidErrorException("注解类型错误");
        }
    }
}
