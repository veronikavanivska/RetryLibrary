package com.vanivska.retryhelper.strategies;

import com.vanivska.retryhelper.BackOffStrategy;

import java.time.Duration;

public class IncrementalBackOffStategy implements BackOffStrategy {

    private final Duration duration;

    public IncrementalBackOffStategy(Duration duration) {
        this.duration = duration;
    }

    @Override
    public Duration nextAttemptTime(int attempt) {
        return duration.multipliedBy(attempt);
    }
}
