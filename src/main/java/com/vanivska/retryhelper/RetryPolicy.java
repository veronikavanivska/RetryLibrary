package com.vanivska.retryhelper;


import com.vanivska.retryhelper.strategies.FixedBackOffStrategy;

import java.time.Duration;
import java.util.function.Predicate;


/**
 * Configuration for retry behavior.
 * Includes max attempts, retry condition, backoff strategy, and optional retry listener.
 */
public class RetryPolicy<T> {
    private final int maxAttempts;
    private final Predicate<Throwable> predicate;
    private final Predicate<T> result;
    private final BackOffStrategy backOffStrategy;
    private final RetryListener retryListener;



    RetryPolicy(Builder<T> builder){
        this.maxAttempts = builder.maxAttempts;
        this.predicate = builder.predicate;
        this.backOffStrategy = builder.backOffStrategy;
        this.retryListener = builder.retryListener;
        this.result = builder.result;
    }

    /**
     * Returns maximum number of attempts (including initial try).
     */
    public int getMaxAttempts() { return maxAttempts; }

    /**
     * Returns the strategy to compute delays between retries.
     */
    public BackOffStrategy getBackOffStrategy() { return backOffStrategy; }

    /**
     * * Returns the predicate that determines if a result is retriable.
     */
    public Predicate<T> getResult() { return result; }

    /**
     * * Returns the predicate that determines if an exception is retriable.
     */
    public Predicate<Throwable> getPredicate() { return predicate; }

    /**
     * Returns the listener called on retry attempts.
     */
    public RetryListener getRetryListener() { return retryListener; }


    /**
     * Builder for creating a {@link RetryPolicy} instanse.
     * Allows configuring retry parameters in a fluent manner.
     */
    public static class Builder<T> {
        /**
         * Maximum number of retry attempts (default is 3).
         */
        private int maxAttempts =3 ;

        /**
         * Predicate to determine if an exception is retriable (default is always true).
         */
        private Predicate<Throwable> predicate = e -> true;

        /**
         * Strategy to calculate delay between retries (default is fixed 500ms).
         */
        private BackOffStrategy backOffStrategy = new FixedBackOffStrategy(Duration.ofMillis(500));

        /**
         * Listener to be notified on each retry attempt (default does nothing).
         */
        private RetryListener retryListener = (attempt, exception) -> {};


        private Predicate<T> result =r -> true;
        /**
         * Sets the maximum number of retry attempts.
         *
         * @param maxAttempts the maximum retry attempts; must be greater than 0
         * @return this builder instance for chaining
         */
        public Builder<T> maxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
            return this;
        }

        public Builder<T> result(Predicate<T> result){
            this.result = result;
            return this;
        }
        /**
         * Sets the predicate to decide if an exception is retriable.
         *
         * @param predicate predicate that tests the exception; must not be null
         * @return this builder instance for chaining
         */
        public Builder<T> predicate(Predicate<Throwable> predicate) {
            this.predicate = predicate;
            return this;
        }

        /**
         * Sets the backoff strategy to calculate delay between retries.
         *
         * @param backOffStrategy the backoff strategy; must not be null
         * @return this builder instance for chaining
         */
        public Builder<T> backOffStrategy(BackOffStrategy backOffStrategy) {
            this.backOffStrategy = backOffStrategy;
            return this;
        }

        /**
         * Sets the retry listener to receive callbacks on retry attempts.
         *
         * @param retryListener the listener; must not be null
         * @return this builder instance for chaining
         */
        public Builder<T> retryListener(RetryListener retryListener) {
            this.retryListener = retryListener;
            return this;
        }

        /**
         * Builds and returns a {@link RetryPolicy} instance configured with the
         * current builder settings.
         *
         * @return the constructed RetryPolicy
         * @throws IllegalArgumentException if any parameter is invalid or null
         */
        public RetryPolicy<T> build() {
            if (maxAttempts <= 0) throw new IllegalArgumentException("maxAttempts must be > 0");
            if(backOffStrategy == null) throw new IllegalArgumentException("backOffStrategy must be not null");
            if(predicate == null) throw new IllegalArgumentException("predicate must be not null");
            if(retryListener == null) throw new IllegalArgumentException("retryListener must be not null");
            if(result == null) throw new IllegalArgumentException("result must be not null");
            return new RetryPolicy<>(this);
        }
    }
}
