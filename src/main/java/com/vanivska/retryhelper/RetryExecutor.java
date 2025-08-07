package com.vanivska.retryhelper;

import java.util.concurrent.Callable;

public class RetryExecutor {
    public static <T> T retry(Callable<T> task, RetryPolicy retryPolicy) throws Exception {
        for (int i = 1; i <= retryPolicy.getMaxAttempts(); i++) {
            try{
                return task.call();
            }catch (Exception e) {
                if (i == retryPolicy.getMaxAttempts()) {
                    throw e;
                }
                if(!retryPolicy.getPredicate().test(e)) {
                    throw e;
                }

                try{
                    retryPolicy.getRetryListener().onRetry(i, e);
                    Thread.sleep(retryPolicy.getBackOffStrategy().nextAttemptTime(retryPolicy.getMaxAttempts()));
                }catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw ie;
                }
            }
        }
        throw new IllegalStateException("Not work");
    }
}
