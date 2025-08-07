package com.vanivska.retryhelper.strategies;

import com.vanivska.retryhelper.BackOffStrategy;

import java.time.Duration;

/**
 * Implements an incremental backoff strategy where the delay between
 * retry attempts increases linearly based on the attempt number.
 * <p>
 * The delay for the nth attempt is calculated as:
 * delay = base duration * attempt number.
 * This provides a gradual, linear increase in wait times.
 */
public class IncrementalBackOffStategy implements BackOffStrategy {

    private final Duration duration;

    public IncrementalBackOffStategy(Duration duration) {
        this.duration = duration;
    }

    /**
     * Calculates the delay for the given attempt by multiplying
     * the base duration by the attempt number.
     *
     * @param attempt the retry attempt number (starting from 1)
     * @return the calculated incremental delay duration
     */
    @Override
    public Duration nextAttemptTime(int attempt) {
        return duration.multipliedBy(attempt);
    }
}
