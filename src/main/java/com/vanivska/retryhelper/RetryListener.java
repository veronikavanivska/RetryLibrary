package com.vanivska.retryhelper;

/**
 * Listener interface to receive callbacks on retry attempts
 *
 * Implementations of this interface can be used to get notified
 * whenever a retry is performed, for purposses such as logging,
 * metrics or custom handling
 */
public interface RetryListener {

    /**
     * Called when a retry attempt occurs
     *
     * @param attempt the current retry attempt number (starting from 1)
     * @param exception the exception that caused the retry
     */
    void onRetry(int attempt, Exception exception);
}
