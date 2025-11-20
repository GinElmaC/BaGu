package com.GinElmaC.RejectThreadPool;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 公共拒绝策略增强接口
 */
public interface RejectHandle extends RejectedExecutionHandler {
    default void beforeReject(ThreadPoolExecutor executor){
        if(executor instanceof SupportThreadPool){
            SupportThreadPool supportThreadPool = (SupportThreadPool) executor;
            supportThreadPool.rejectCountUp();
            System.out.println("自定义的拒绝策略触发...");
        }
    }
}