package com.GinElmaC;

public interface RejectHandle {
    void reject(Runnable rejectTask,MyPool threadPool);
}
