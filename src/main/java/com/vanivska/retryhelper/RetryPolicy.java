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

    private RetryPolicy(Builder builder){
        this.maxAttempts = builder.maxAttempts;
        this.predicate = builder.predicate;
        this.backOffStrategy = builder.backOffStrategy;
    }
    public int getMaxAttempts() { return maxAttempts; }
    public BackOffStrategy getBackOffStrategy() { return backOffStrategy; }
    public Predicate<Throwable> getPredicate() { return predicate; }



    public static class Builder {
        private int maxAttempts =3 ;
        private Predicate<Throwable> predicate = e -> true;
        private BackOffStrategy backOffStrategy = new FixedBackOffStrategy(Duration.ofMillis(500));

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
        public RetryPolicy build() {
            if (maxAttempts <= 0) throw new IllegalArgumentException("maxAttempts must be > 0");
            return new RetryPolicy(this);
        }
    }
}
