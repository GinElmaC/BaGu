package com.GinElmaC;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池
 */
public class MyPool {
    //任务队列
    public final BlockingQueue<Runnable> taskQueue;
    //线程数量
    private final int corePoolSize;
    //最大线程树龄
    private final int maxPoolSize;
    //辅助线程超时时间
    private final int timeout;
    //超时时间单位
    private final TimeUnit timeUnit;
    //拒绝策略
    private final RejectHandle rejectHandle;
    //核心线程list
    private final List<Thread> coreList = new ArrayList<>();
    //额外线程list
    private final List<Thread> supportList = new ArrayList<>();

    public MyPool(int corePoolSize, int maxPoolSize, int timeout, TimeUnit timeUnit, BlockingQueue<Runnable> taskQueue, RejectHandle rejectHandle) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = taskQueue;
        this.rejectHandle = rejectHandle;
    }

    /**
     * 在除了LockSupport（记录中断位置）所有的等待方法都会抛出InterruptedException异常，所以一般等待更喜欢使用LockSupport的park方法
     */

    public void execute(Runnable task){
        /**
         * 判断核心线程数，如果核心线程数没有满，则创建新线程并执行通用任务
         */
        if(coreList.isEmpty() || coreList.size()<corePoolSize){
            //创建线程
            Thread thread = new CoreThread();
            coreList.add(thread);
            thread.start();
        }
        if(taskQueue.offer(task)){
            return;
        }
        /**
         * 如果向任务队列假如任务失败，则代表任务队列满了，需要
         * 尽量不使用add，要使用offer，因为add在队列满的时候是会抛出异常的，而offer有一个返回值
         */
        //任务满了
        //判断辅助线程长度
        if(coreList.size()+supportList.size()<maxPoolSize){
            Thread thread = new SupportThread();
            supportList.add(thread);
            thread.start();
        }
        if(!taskQueue.offer(task)){
            rejectHandle.reject(task,this);
        }
    }

    /**
     * 将线程的固定方法封装成内部类
     */
    class CoreThread extends Thread{
        @Override
        public void run() {
            while(true){
                try {
                    Runnable take = taskQueue.take();
                    take.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    class SupportThread extends Thread{
        @Override
        public void run() {
            while(true){
                try {
                    /**
                     * poll方法会阻塞设定的时间，如果在设定的时间内没有获取到，则会返回null
                     * 这样可以保证我们的辅助线程会被回收而不是不断地出现在辅助队列中
                     * 跳出循环后，线程的任务就结束了，线程也会被销毁
                     */
                    Runnable take = taskQueue.poll(timeout, timeUnit);
                    if(take == null){
                        break;
                    }
                    take.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("辅助线程"+Thread.currentThread().getName()+"结束了");
        }
    }
}
