package com.GinElmaC;

import com.GinElmaC.RejectHandleImpl.DiscardRejectHandle;
import com.GinElmaC.RejectHandleImpl.ThrowExceptionHandle;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        MyPool myPool = new MyPool(2,4,1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2),new DiscardRejectHandle());
        for(int i = 0;i<5;i++){
            final int fi = i;
            myPool.execute(()->{
                try {
                    Thread.sleep(1000l);
                    System.out.println(Thread.currentThread().getName()+"第"+fi+"次任务");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        System.out.println("主线程没有被阻塞");
    }
}
