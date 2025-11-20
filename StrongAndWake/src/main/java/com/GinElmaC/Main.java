package com.GinElmaC;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class Main {
    public static void main(String[] args) {
        Object o1 = new Object();
        WeakReference<Object> weako2 = new WeakReference<Object>(o1);

        System.out.println(o1);
        System.out.println(weako2.get());

        o1 = null;


        System.out.println(o1);
        System.out.println(weako2.get());
    }
}
