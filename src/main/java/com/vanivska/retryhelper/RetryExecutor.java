package com.vanivska.retryhelper;

import java.util.concurrent.Callable;

/**
 * Utility class responsible for executing tasks with retry logic
 * based on a given {@link RetryPolicy}.
 * <p>
 * It attempts to execute the given task up to the maximum number of attempts specified
 * in the policy, applying the backoff strategy between retries and invoking
 * the retry listener after each failure.
 */
public class RetryExecutor {

    /**
     * Executes the given {@link Callable} task according to the rules defined in the provided
     * {@link RetryPolicy}.
     * <p>
     * The task will be attempted up to {@code retryPolicy.getMaxAttempts()} times.
     * After each failure, if the exception matches the retry predicate, the retry listener is notified,
     * then the thread waits according to the backoff strategy before retrying.
     *
     * @param <T>         the result type of the task
     * @param task        the task to be executed
     * @param retryPolicy the retry policy that controls max attempts, predicate, backoff, and listener
     * @return the result of the task if it succeeds within the allowed attempts
     * @throws Exception if the task throws an exception and retries are exhausted or predicate disallows retry
     * @throws InterruptedException if the thread is interrupted while waiting between retries
     */
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

                retryPolicy.getRetryListener().onRetry(i, e);

                try{
                    Thread.sleep(retryPolicy.getBackOffStrategy().nextAttemptTime(i).toMillis());
                }catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw ie;
                }
            }
        }
        throw new IllegalStateException("Not work");
    }
}
