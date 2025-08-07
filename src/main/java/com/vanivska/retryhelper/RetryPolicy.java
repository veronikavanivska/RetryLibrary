package com.vanivska.retryhelper;


import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class RetryPolicy {
    int maxAttempts;
    Duration time;
    Predicate<Throwable> predicate;



}
