package com.GinElmaC.proxy;

/**
 * 打印自己的名字和长度
 */
public class NameAndLengthImpl implements MyInter{
    @Override
    public void fun1() {
        System.out.println("fun1"+"fun1".length());
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
