package com.GinElmaC.RejectThreadPool;

import java.util.concurrent.ThreadPoolExecutor;

public class SupportAbortPolicyRejected extends ThreadPoolExecutor.AbortPolicy implements RejectHandle {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        beforeReject(e);
        super.rejectedExecution(r, e);
    }
}
