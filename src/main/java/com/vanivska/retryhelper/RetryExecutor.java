package com.vanivska.retryhelper;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

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
    public static <T> T retryOnException(Callable<T> task, RetryPolicy<T> retryPolicy) throws Exception {
        for (int i = 1; i <= retryPolicy.getMaxAttempts(); i++) {
            try{
               return task.call();
            }catch (Exception e) {
                if (i == retryPolicy.getMaxAttempts() || !retryPolicy.getPredicate().test(e)) {
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

    /**
     * Executes the given {@link Callable} task according to the rules defined in the provided
     * {@link RetryPolicy}, retrying based on the result predicate.
     * <p>
     * The task will be attempted up to {@code retryPolicy.getMaxAttempts()} times.
     * After each attempt, if the result satisfies the retry predicate, the result is returned immediately.
     * Otherwise, the retry listener is notified, and the thread waits according to the backoff strategy before retrying.
     * If all retries fail, the fallback supplier is used to provide a result.
     *
     * @param <T>          the result type of the task
     * @param task         the task to be executed
     * @param retryPolicy  the retry policy that controls max attempts, result predicate, backoff, and listener
     * @param fallback     the fallback supplier that provides a result if retries are exhausted
     * @return the result of the task if it satisfies the predicate, or the fallback result after exhausting retries
     * @throws Exception
     */
    public static <T> T retryOnResult(Callable<T> task, RetryPolicy<T> retryPolicy,Supplier<T> fallback) throws Exception {
            for (int i = 1; i <= retryPolicy.getMaxAttempts(); i++) {
                T result = task.call();

                if(retryPolicy.getResult().test(result)) {
                    return result;
                }
                if(i == retryPolicy.getMaxAttempts()) {
                    return fallback.get();
                }

                retryPolicy.getRetryListener().onRetry(i,null);
                try{
                    Thread.sleep(retryPolicy.getBackOffStrategy().nextAttemptTime(i).toMillis());
                }catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw ie;
                }

            }
            throw new IllegalStateException("Not work");
        }
}
