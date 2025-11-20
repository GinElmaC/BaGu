package com.GinElmaC;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GinEventLoopGroup implements EventLoopGroup{

    private final EventLoop[] children;

    private final AtomicInteger index = new AtomicInteger(0);

    public GinEventLoopGroup(int threadNum){
        this.children = new EventLoop[threadNum];
        for(int i = 0;i<threadNum;i++){
            children[i] = new GinEventLoop();
        }
    }

    @Override
    public void execute(Runnable runnable) {
        next().execute(runnable);
    }

    @Override
    public void schedule(Runnable task, long delay, TimeUnit unit) {
        next().schedule(task,delay,unit);
    }

    @Override
    public void scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        next().scheduleAtFixedRate(task,initialDelay,period,unit);
    }

    @Override
    public EventLoop next() {
        return children[index.getAndIncrement() % children.length];
    }
}
