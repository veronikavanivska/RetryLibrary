import com.vanivska.retryhelper.*;
import com.vanivska.retryhelper.strategies.FixedBackOffStrategy;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RetryExecutorResultTest {

    @Test
    void returnsResultImmediatelyIfValid() throws Exception {
        Callable<Integer> task = () -> 10;

        RetryPolicy<Integer> policy = new RetryPolicy.Builder<Integer>()
                .maxAttempts(3)
                .result(i -> i > 0)
                .backOffStrategy(new FixedBackOffStrategy(Duration.ZERO))
                .build();

        int result = RetryExecutor.retryOnResult(task, policy, () -> -1);

        assertEquals(10, result);
    }

    @Test
    void retriesUntilValidResult() throws Exception {
        AtomicInteger attempts = new AtomicInteger(0);

        Callable<Integer> task = () -> {
            if (attempts.incrementAndGet() < 3) {
                return -1;
            }
            return 42;
        };

        RetryListener listener = mock(RetryListener.class);

        RetryPolicy<Integer> policy = new RetryPolicy.Builder<Integer>()
                .maxAttempts(5)
                .result(i -> i > 0)
                .retryListener(listener)
                .backOffStrategy(new FixedBackOffStrategy(Duration.ZERO))
                .build();

        int result = RetryExecutor.retryOnResult(task, policy, () -> -999);

        assertEquals(42, result);
        verify(listener, times(2)).onRetry(anyInt(), isNull());
    }

    @Test
    void returnsFallbackAfterAllRetriesFail() throws Exception {
        Callable<Integer> task = () -> -1;

        Supplier<Integer> fallback = () -> 100;

        RetryPolicy<Integer> policy = new RetryPolicy.Builder<Integer>()
                .maxAttempts(3)
                .result(i -> i > 0)
                .backOffStrategy(new FixedBackOffStrategy(Duration.ZERO))
                .build();

        int result = RetryExecutor.retryOnResult(task, policy, fallback);

        assertEquals(100, result);
    }
}
