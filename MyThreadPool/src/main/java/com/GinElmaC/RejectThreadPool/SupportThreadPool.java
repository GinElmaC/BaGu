package com.GinElmaC.RejectThreadPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 扩展自定义拒绝的线程池
 */
public class SupportThreadPool extends ThreadPoolExecutor {
    /**
     * 继承后重写
     */


    private final AtomicInteger rejectCount = new AtomicInteger();

    public SupportThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public SupportThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
     * 拒绝次数增加
     */
    public void rejectCountUp(){
        rejectCount.incrementAndGet();
    }

    /**
     * 获取拒绝次数
     */
    public Integer getRejectCount(){
        return rejectCount.get();
    }
}
