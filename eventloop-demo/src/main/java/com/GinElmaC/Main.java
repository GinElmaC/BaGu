package com.GinElmaC;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;

public class Main {
    public static void main(String[] args) {
        //这是一个最简单的线程池(EventLoopGroup)
        EventLoopGroup eventExecutors = new DefaultEventLoopGroup(9);
        for(int i = 0;i<10;i++){
            eventExecutors.execute(()->{
                System.out.println(Thread.currentThread().getName());
            });
        }
    }
}
