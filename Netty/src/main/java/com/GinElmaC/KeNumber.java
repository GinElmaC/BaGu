package com.GinElmaC;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;

public class KeNumber {
        public static AtomicInteger num = new AtomicInteger(0);
        public static void main(String[] args) throws InterruptedException {
            Runnable runnable=()->{
                //可数循环
                for (int i = 0; i < 1000000000; i++) {
                    num.getAndAdd(1);
                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println(Thread.currentThread().getName()+"执行结束!");
            };
            Thread t1 = new Thread(runnable);
            Thread t2 = new Thread(runnable);
            t1.start();
            t2.start();
            Thread.sleep(1000);
            System.out.println("num = " + num);
        }
//public static void main(String[] args) {
//    int i = 0;
//    while(true){
//        WeakReference<Integer> weak = new WeakReference<>(1);
//    }
//}
}
