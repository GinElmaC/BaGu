package com.GinElmaC;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GinEventLoop implements EventLoop{

    private static final AtomicInteger THREAD_NAME_INDEX = new AtomicInteger(0);
    //需要立即执行的队列
    private final BlockingQueue<Runnable> taskQueue;
    //需要执行的所有的定时任务
    private final PriorityBlockingQueue<ScheduleTask> scheduleTaskBlockingQueue;

    private final Thread thread;

    public GinEventLoop(){
        taskQueue = new ArrayBlockingQueue<>(1024);
        thread = new EventLoopThread("Gin-EventLoop-Thread"+THREAD_NAME_INDEX.incrementAndGet());
        scheduleTaskBlockingQueue = new PriorityBlockingQueue<>(1024);
        this.thread.start();
    }

    @Override
    public void execute(Runnable runnable) {
        if (!taskQueue.offer(runnable)) {
            throw new RuntimeException("阻塞队列已经满了");
        }
    }

    @Override
    public void schedule(Runnable task, long delay, TimeUnit unit) {
        ScheduleTask scheduleTask = new ScheduleTask(task,this,deadlineMs(delay,unit),-1);
        try {
            scheduleTaskBlockingQueue.offer(scheduleTask);
        } catch (Exception e) {
            throw new RuntimeException("延迟队列满了");
        }
    }
    private long deadlineMs(Long delay,TimeUnit unit){
        return unit.toMillis(delay) + System.currentTimeMillis();
    }

    @Override
    public void scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        ScheduleTask scheduleTask = new ScheduleTask(task,this,deadlineMs(initialDelay,unit),unit.toMillis(period));
        try {
            scheduleTaskBlockingQueue.offer(scheduleTask);
        } catch (Exception e) {
            throw new RuntimeException("延迟队列满了");
        }
    }

    @Override
    public EventLoop next() {
        return this;
    }

    private Runnable getTask(){
        ScheduleTask scheduleTask = scheduleTaskBlockingQueue.peek();
        if(scheduleTask != null){
            Runnable task = null;
            try {
                task = taskQueue.take();
            } catch (InterruptedException e) {
                return null;
            }
            return task;
        }
        if(scheduleTask.getDeadline()<=System.currentTimeMillis()){
            return scheduleTaskBlockingQueue.poll();
        }
        Runnable task = null;
        try {
            task = taskQueue.poll(scheduleTask.getDeadline()-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {

        }
        return task;
    }

    @Override
    public Queue<ScheduleTask> getScheduleTask() {
        return scheduleTaskBlockingQueue;
    }

    class EventLoopThread extends Thread{

        public EventLoopThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            /**
             * 这里应该有一个shutdown函数进行控制，但是这里只在乎最核心的部分
             */
            while(true){
                    Runnable task = getTask();
                    if(task != null){
                        task.run();
                    }
            }
        }
    }
}
