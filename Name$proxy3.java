package com.GinElmaC.proxy;

public class Name$proxy3 implements MyInter{
    MyInter myInter;
    @Override
    public void fun1() {
        System.out.println("before");
        myInter.fun1();
        System.out.println("after");
    }

    @Override
    public void fun2() {
        System.out.println("before");
        myInter.fun2();
        System.out.println("after");
    }

    @Override
    public void fun3() {
        System.out.println("before");
        myInter.fun3();
        System.out.println("after");
    }
}
