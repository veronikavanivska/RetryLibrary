package com.vanivska.retryhelper.strategies;

import com.vanivska.retryhelper.BackOffStrategy;

import java.time.Duration;
/**
 * Implements an exponential backoff strategy where the wait time
 * between retries increases exponentially based on the attempt number.
 * <p>
 * The delay for attempt {@code n} is calculated as:
 * <pre>
 *     baseDuration * 2^(n-1)
 * </pre>
 * For example, if the base duration is 500ms:
 * - Attempt 1: 500ms
 * - Attempt 2: 1000ms
 * - Attempt 3: 2000ms
 * and so on.
 */
public class ExponentialBackoffStrategy implements BackOffStrategy {

    private final Duration duration;

    public ExponentialBackoffStrategy(Duration duration) {
        this.duration = duration;
    }

    /**
     * Computes the delay before the given retry attempt.
     *
     * @param attempt the retry attempt number (starting at 1)
     * @return the calculated exponential delay duration
     */
    @Override
    public Duration nextAttemptTime(int attempt) {
        long multiplier = 1L << (attempt - 1);
        return duration.multipliedBy(multiplier);
    }
}
