package com.GinElmaC;

import java.util.concurrent.TimeUnit;

public interface EventLoopGroup {
    //执行任务
    void execute(Runnable runnable);
    //延迟执行任务
    void schedule(Runnable task, long delay, TimeUnit unit);
    //定时任务
    void scheduleAtFixedRate(Runnable task,long initialDelay,long period,TimeUnit unit);
    //拿到下一个该执行的eventloop
    EventLoop next();
}
