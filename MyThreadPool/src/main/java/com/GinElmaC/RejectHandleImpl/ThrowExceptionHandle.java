package com.GinElmaC.RejectHandleImpl;

import com.GinElmaC.MyPool;
import com.GinElmaC.RejectHandle;

public class ThrowExceptionHandle implements RejectHandle {
    @Override
    public void reject(Runnable rejectTask, MyPool threadPool) {
        throw new RuntimeException("阻塞队列满了");
    }
}
