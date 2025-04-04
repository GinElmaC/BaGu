package com.GinElmaC;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MySchedule mySchedule = new MySchedule();
        mySchedule.schedule(()->{
            System.out.println("111");
        },100l);

        Thread.sleep(1000);
        System.out.println("添加200ms打印2");
        mySchedule.schedule(()->{
            System.out.println("222");
        },200l);
    }
}
