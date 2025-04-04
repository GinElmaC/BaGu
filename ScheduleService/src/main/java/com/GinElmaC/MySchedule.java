package com.GinElmaC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * 执行延时任务的类
 */
public class MySchedule {

    ExecutorService threadpool = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() << 1,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadPoolExecutor.DiscardPolicy()
    );

    /**
     * 具体执行延时任务的方法，这里可以在这个方法中直接将任务扔进线程池中，但是我们的线程池有线程限制，
     * 也就是说如果用户调用这个方法超过一定次数，这个线程池就炸了，所以我们需要一个组件来定时将任务一次次放入线程池而不是让任务一直占据线程，即使任务没有触发
     *     void schedule(Runnable task,Long delay){
     *         threadpool.execute(()->{
     *             while(true){
     *                 Thread.sleep(delay);
     *                 task.run();
     *             }
     *         });
     *     }
     * @param task 要执行的任务
     * @param delay 延时的时间
     */

    /**
     * 将任务一次次放进线程池的组件
     */
    class Trigger{
        //任务列表
//        List<Job> jobList = new ArrayList<>();
        /**
         * PriorityBlockingQueue的特性
         * 线程安全：PriorityBlockingQueue 使用了内部锁和其他形式的并发控制来确保在多线程环境下操作的安全性。
         * 无界队列：虽然它是无界的，但实际容量受限于系统内存限制。尝试添加元素不会失败，除非由于内存不足导致无法创建新的对象。
         * 优先级排序：队列头部是最小（或最大，取决于比较器）的元素，即根据指定的排序规则，该元素应被首先取出。
         * 阻塞操作：提供了一种机制，在获取元素时如果队列为空，则等待直到队列中有可用元素为止。
         * 非公平性策略：默认采用非公平性策略，这意味着允许插队行为。
         */
        PriorityBlockingQueue<Job> jobList = new PriorityBlockingQueue<>();

        Thread thread = new Thread(()->{
            while(true){
                /**
                 * 如果第一个任务的执行时间为1s，第二个为500ms，那么第二个任务至少等待1s才会被有可能执行,所以我们需要排序
                 * 1.排序的时间复杂度为：O(N*log2N)，这是个很耗费CPU的过程
                 * 2.假如在排序的时候又有个新的任务加进来，是否会存在线程安全问题？
                 * 所以我们需要换成PriorityBlockingQueue
                 */
//                Collections.sort(jobList);
//                for(Job job:jobList){
//                    try {
//                        long waitTime = job.getStartTime()-System.currentTimeMillis();
//                        if(waitTime>0){
//                            Thread.sleep(waitTime);
//                        }
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                if(!jobList.isEmpty()){
//                    Job job = jobList.poll();
//                    long waitTime = job.getStartTime()-System.currentTimeMillis();
//                    if(waitTime>0){
//                        LockSupport.park();
//                    }
//                    threadpool.execute(job.getTask());
//                }
                while(jobList.isEmpty()){
                    LockSupport.park();
                }
                //获取当前队列中的最需要被执行的任务
                Job job = jobList.peek();
                if(job.getStartTime()<System.currentTimeMillis()){
                    //这里的job与之前的job可能不是同一个，因为我们可能在poll之前用户又加入了一个优先级更高的任务
                    // 但是没有关系，因为PriorityBlockingQueue会排序，既然新插入的任务之后的任务都需要执行，那新插入的任务一定需要执行
                    //还有一个要点，就是这里可能是由于被虚假唤醒，代码依然会运行到这里，会出现空指针异常，所以我们上面使用while
                    job = jobList.poll();
                    threadpool.execute(job.getTask());

                    /**
                     * 实现无限循环定时任务
                     */
                    Job nextJob = new Job(job.getTask(),System.currentTimeMillis()+job.getDelay(),job.getDelay());
                    jobList.offer(nextJob);
                }else{
                    LockSupport.parkUntil(job.getStartTime());
                }
            }
        });

        {
            thread.start();
            System.out.println("触发器初始化完成，启动完毕");
        }

        /**
         * 当我们触发了等待1s，然后用户又传入一个任务，这个任务需要等待500ms，那么这个任务仍然需要等待1s，这时候就需要一个唤醒机制来唤醒等待1s的线程
         */
        void wakeUp(){
            LockSupport.unpark(thread);
        }
    }

    Trigger trigger = new Trigger();

    void schedule(Runnable task,Long delay){
        //封装任务
        Job job = new Job(task,System.currentTimeMillis()+delay,delay);
        //放入任务
        trigger.jobList.offer(job);
        //唤醒一次
        trigger.wakeUp();
    }

}
