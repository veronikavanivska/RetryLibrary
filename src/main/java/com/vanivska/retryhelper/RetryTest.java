package com.vanivska.retryhelper;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Callable;

public class RetryTest {
    public static void main(String[] args) {
    testImmediateSuccess();
    testImmediateFailure();
    testAlwaysFailure();
    }

    static void testImmediateSuccess() {
        try {
            String result = RetryHelper.exponentialRetry(() -> "Success!", 3);
            System.out.println("✅ testImmediateSuccess passed: " + result);
        } catch (Exception e) {
            System.out.println("❌ testImmediateSuccess failed: " + e.getMessage());
        }
    }

    static void testImmediateFailure() {
        try {
        Callable<String> retryTask = new Callable<String>() {
            int attempts = 0;

            @Override
            public String call() throws Exception {
                attempts++;
                if(attempts< 4){
                    throw new RuntimeException("Failure " + attempts);
                }
                return "Recover";
            }

        };
        String result = RetryHelper.exponentialRetry(retryTask, 5);

        System.out.println("✅ testFailThenSuccess passed: " + result);
        } catch (Exception e) {
            System.out.println("❌ testFailThenSuccess failed: " + e.getMessage());
        }
    }

    static void testAlwaysFailure() {
        try{
            Callable<String> failure = () -> {
                throw new RuntimeException("Always fails");
            };

            String result = RetryHelper.retry(failure, 3, Duration.ofMillis(100), e -> e instanceof IOException);
            System.out.println("❌ testAlwaysFail failed: Exception expected but not thrown" + result);
        }catch (Exception e){
            System.out.println("✅ testAlwaysFail passed: Caught exception: " + e.getMessage());
    }
    }
}
