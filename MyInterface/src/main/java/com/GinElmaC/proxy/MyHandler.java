package com.GinElmaC.proxy;

public interface MyHandler {
    String functionBody(String methodName);

    /**
     * default可以允许函数有默认的方法体，也就是说实现了这个接口的类没有必要一定重写这个方法
     * @param proxy
     */
    default void setProxy(MyInter proxy){

    }
}
