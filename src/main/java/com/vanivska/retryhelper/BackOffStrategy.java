package com.vanivska.retryhelper;

import java.time.Duration;

public interface BackOffStrategy {

    Duration nextAttemptTime(int attempt);


}
