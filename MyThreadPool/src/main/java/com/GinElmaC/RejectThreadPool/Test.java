package com.GinElmaC.RejectThreadPool;

import java.lang.reflect.Proxy;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        //静态代理测试
//        SupportThreadPool executor = new SupportThreadPool(
//                1,
//                1,
//                1024,
//                TimeUnit.SECONDS,
//                new LinkedBlockingQueue(1),
//                // 使用自定义拒绝策略
//                new SupportAbortPolicyRejected()
//        );
//
//        // 测试流程
//        for (int i = 0; i < 10; i++) {
//            try {
//                // 无限睡眠, 以此触发拒绝策略.(此处有异常, 为了减少无用代码, 省略...)
//                executor.execute(() -> {
//                    try {
//                        Thread.sleep(Integer.MAX_VALUE);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//            } catch (Exception ignored) {
//            }
//        }
//
//        Thread.sleep(50);
//        System.out.println(String.format("线程池拒绝策略次数 :: %d", executor.getRejectCount()));
        //动态代理测试
        SupportThreadPool supportThreadPool = new SupportThreadPool(1,1,1024,TimeUnit.SECONDS,new LinkedBlockingQueue<>(1));
        //创建默认拒绝策略
        ThreadPoolExecutor.AbortPolicy abortPolicy = new ThreadPoolExecutor.AbortPolicy();
        //创建拒绝策略代理类
        /**
         * 方法签名：
         * classLoader -要使用什么类加载器加载类
         * interfaces -要为哪些接口创造代理对象
         * Object -实现了InvocationHandler接口的对象，对于被代理接口的调用都会被转接到这个对象的invoke方法中，
         *          其中invoke方法的参数有method，可以用来区分是哪个方法被调用
         */
        RejectedExecutionHandler rejectHandle = (RejectedExecutionHandler) Proxy.newProxyInstance(
                abortPolicy.getClass().getClassLoader(),
                abortPolicy.getClass().getInterfaces(),
                new RejectedExecutionProxyInvocationHandler(abortPolicy,supportThreadPool)
        );
        //设置拒绝策略
        supportThreadPool.setRejectedExecutionHandler(rejectHandle);

        // 测试流程
        for (int i = 0; i < 3; i++) {
            try {
                // 无限睡眠, 以此触发拒绝策略.(此处有异常, 为了减少无用代码, 省略...)
                supportThreadPool.execute(() -> {
                    try {
                        Thread.sleep(Integer.MAX_VALUE);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (Exception ex) {
                // ignore
            }
        }

        Thread.sleep(50);
        System.out.println(String.format("线程池拒绝策略次数 :: %d", supportThreadPool.getRejectCount()));
    }
}
