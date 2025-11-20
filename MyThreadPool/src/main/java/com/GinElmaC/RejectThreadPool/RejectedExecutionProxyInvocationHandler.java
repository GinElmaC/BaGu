package com.GinElmaC.RejectThreadPool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.RejectedExecutionHandler;

/**
 * 使用动态代理增强拒绝策略
 * InvocationHandler-动态代理的核心接口
 */
public class RejectedExecutionProxyInvocationHandler implements InvocationHandler {

    private RejectedExecutionHandler rejectHandle;

    private SupportThreadPool supportThreadPool;

    public RejectedExecutionProxyInvocationHandler(RejectedExecutionHandler rejectHandle, SupportThreadPool supportThreadPool) {
        this.rejectHandle = rejectHandle;
        this.supportThreadPool = supportThreadPool;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        supportThreadPool.rejectCountUp();
        System.out.println("动态代理拒绝策略生效...");
        return method.invoke(rejectHandle,args);
    }
}
