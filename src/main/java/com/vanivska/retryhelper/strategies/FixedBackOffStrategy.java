package com.vanivska.retryhelper.strategies;

import com.vanivska.retryhelper.BackOffStrategy;

import java.time.Duration;

/**
 * Implements a fixed backoff strategy where the delay between
 * retry attempts is constant and does not change with the attempt number.
 * <p>
 * Each retry will wait for the same fixed duration.
 */
public class FixedBackOffStrategy implements BackOffStrategy {

    private final Duration duration;

    public FixedBackOffStrategy(Duration duration) {
        this.duration = duration;

    }

    /**
     * Returns the fixed delay duration regardless of the attempt number.
     *
     * @param attempt the retry attempt number (ignored in this implementation)
     * @return the fixed delay duration
     */
    @Override
    public Duration nextAttemptTime(int attempt) {
        return duration;
    }
}
