package com.GinElmaC.proxy;

/**
 * 实现每个函数打印自己的函数名字
 */
public class NameImpl implements MyInter{
    @Override
    public void fun1() {
        System.out.println("fun1");
    }

    @Override
    public void fun2() {
        System.out.println("fun2");
    }

    @Override
    public void fun3() {
        System.out.println("fun3");
    }
}
