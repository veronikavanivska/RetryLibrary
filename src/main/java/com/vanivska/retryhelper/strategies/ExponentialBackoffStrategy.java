package com.vanivska.retryhelper.strategies;

import com.vanivska.retryhelper.BackOffStrategy;

import java.time.Duration;

public class ExponentialBackoffStrategy implements BackOffStrategy {

    private final Duration duration;

    public ExponentialBackoffStrategy(Duration duration) {
        this.duration = duration;
    }

    @Override
    public Duration nextAttemptTime(int attempt) {
        long multiplier = 1L << (attempt - 1);
        return duration.multipliedBy(multiplier);
    }
}
