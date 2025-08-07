package com.vanivska.retryhelper;

import java.time.Duration;

/**
 * Strategy interface to calculate the delay duration before the next retry attempt.
 * <p>
 *   Implementations define different backoff behaviors such as fixed delay,
 *   exponential backoff or incremental backoff.
 */
public interface BackOffStrategy {

    /**
     * Computes the delay duration to wait before the given retry attempt.
     *
     * @param attempt the current retry attempt number (starting at 1)
     * @return the {@link Duration} to wait before making the next retry
     */
    Duration nextAttemptTime(int attempt);
}
