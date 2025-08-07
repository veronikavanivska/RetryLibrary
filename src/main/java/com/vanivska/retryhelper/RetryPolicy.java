package com.vanivska.retryhelper;


import com.vanivska.retryhelper.strategies.FixedBackOffStrategy;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class RetryPolicy {
    private final int maxAttempts;
    private final Predicate<Throwable> predicate;
    private final BackOffStrategy backOffStrategy;
    private final RetryListener retryListener;

    RetryPolicy(Builder builder){
        this.maxAttempts = builder.maxAttempts;
        this.predicate = builder.predicate;
        this.backOffStrategy = builder.backOffStrategy;
        this.retryListener = builder.retryListener;
    }
    public int getMaxAttempts() { return maxAttempts; }
    public BackOffStrategy getBackOffStrategy() { return backOffStrategy; }
    public Predicate<Throwable> getPredicate() { return predicate; }
    public RetryListener getRetryListener() { return retryListener; }



    public static class Builder {
        private int maxAttempts =3 ;
        private Predicate<Throwable> predicate = e -> true;
        private BackOffStrategy backOffStrategy = new FixedBackOffStrategy(Duration.ofMillis(500));
        private RetryListener retryListener = (attempt, exception) -> {};
        public Builder maxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
            return this;
        }
        public Builder predicate(Predicate<Throwable> predicate) {
            this.predicate = predicate;
            return this;
        }
        public Builder backOffStrategy(BackOffStrategy backOffStrategy) {
            this.backOffStrategy = backOffStrategy;
            return this;
        }
        public Builder retryListener(RetryListener retryListener) {
            this.retryListener = retryListener;
            return this;
        }
        public RetryPolicy build() {
            if (maxAttempts <= 0) throw new IllegalArgumentException("maxAttempts must be > 0");
            if(backOffStrategy == null) throw new IllegalArgumentException("backOffStrategy must be not null");
            if(predicate == null) throw new IllegalArgumentException("predicate must be not null");
            if(retryListener == null) throw new IllegalArgumentException("retryListener must be not null");
            return new RetryPolicy(this);
        }
    }
}
