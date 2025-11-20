package com.GinElmaC;

public class ScheduleTask implements Runnable,Comparable<ScheduleTask>{

    private final Runnable task;

    private long deadline;
    //周期，下一次执行时间为上一次时间+周期
    private long period;

    private EventLoop eventLoop;

    public ScheduleTask(Runnable task,EventLoop eventLoop,Long deadline,long period){
        this.task = task;
    }

    @Override
    public void run() {
        try {
            task.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(period>0){
                deadline+=period;
                eventLoop.getScheduleTask().offer(this);
            }
        }
    }

    @Override
    public int compareTo(ScheduleTask o) {
        return Long.compare(this.deadline,o.deadline);
    }

    public long getDeadline() {
        return deadline;
    }
}
