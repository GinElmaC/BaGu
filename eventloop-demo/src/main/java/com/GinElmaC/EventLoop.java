package com.GinElmaC;

import java.util.Queue;

public interface EventLoop extends EventLoopGroup{

    Queue<ScheduleTask> getScheduleTask();
}
