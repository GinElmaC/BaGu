package com.GinElmaC;

/**
 * 对于要执行的任务的封装
 */
public class Job implements Comparable<Job>{
    private Runnable task;
    private long startTime;
    private long delay;

    public Job() {
    }

    public Job(Runnable task, long startTime) {
        this.task = task;
        this.startTime = startTime;
    }

    public Job(Runnable task, long startTime, long delay) {
        this.task = task;
        this.startTime = startTime;
        this.delay = delay;
    }

    public Runnable getTask() {
        return task;
    }

    public void setTask(Runnable task) {
        this.task = task;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }



    @Override
    public int compareTo(Job o) {
        return Long.compare(this.startTime,o.startTime);
    }

    public String toString() {
        return "Job{task = " + task + ", startTime = " + startTime + "}";
    }

    /**
     * 获取
     * @return delay
     */
    public long getDelay() {
        return delay;
    }

    /**
     * 设置
     * @param delay
     */
    public void setDelay(long delay) {
        this.delay = delay;
    }
}
