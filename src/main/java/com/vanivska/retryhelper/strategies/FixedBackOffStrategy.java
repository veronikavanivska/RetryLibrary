package com.vanivska.retryhelper.strategies;

import com.vanivska.retryhelper.BackOffStrategy;

import java.time.Duration;

public class FixedBackOffStrategy implements BackOffStrategy {

    private final Duration duration;

    public FixedBackOffStrategy(Duration duration) {
        this.duration = duration;

    }

    @Override
    public Duration nextAttemptTime(int attempt) {
        return duration;
    }
}
