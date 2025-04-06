package com.GinElmaC.RejectHandleImpl;

import com.GinElmaC.MyPool;
import com.GinElmaC.RejectHandle;

public class DiscardRejectHandle implements RejectHandle {
    @Override
    public void reject(Runnable rejectTask, MyPool threadPool) {
        threadPool.taskQueue.poll();
        threadPool.execute(rejectTask);
    }
}
