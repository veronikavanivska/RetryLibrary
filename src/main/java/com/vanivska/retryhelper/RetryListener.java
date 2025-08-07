package com.vanivska.retryhelper;

public interface RetryListener {
    void onRetry(int attempt, Exception exception);
}
