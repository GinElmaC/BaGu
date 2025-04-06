package com.GinElmaC;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int[] count = new int[]{1000};
        List<Thread> list = new ArrayList<>();

        MyLock lock = new MyLock();

        for(int i = 0;i<10;i++){
            list.add(new Thread(()->{
                lock.lock();
                for(int i1 = 0;i1<1;i1++){
                    count[0]-=100;
                }
                lock.unlock();
            }));
        }

        for(Thread thread:list){
            thread.start();
        }
        for(Thread thread:list){
            thread.join();
        }

        System.out.println(count[0]);
    }
}
