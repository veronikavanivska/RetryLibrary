import com.vanivska.retryhelper.*;
import com.vanivska.retryhelper.strategies.FixedBackOffStrategy;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RetryExecutorExceptionTest {

    @Test
    void successOnFirstAttempt() throws Exception {
        Callable<String> task = () -> "OK";

        RetryPolicy<String> policy = new RetryPolicy.Builder<String>()
                .maxAttempts(3)
                .predicate(e -> true)
                .backOffStrategy(new FixedBackOffStrategy(Duration.ZERO))
                .build();

        String result = RetryExecutor.retryOnException(task, policy);

        assertEquals("OK", result);
    }

    @Test
    void retryOnIOExceptionThenSuccess() throws Exception {
        AtomicInteger attempts = new AtomicInteger(0);

        Callable<String> task = () -> {
            if (attempts.incrementAndGet() < 3) {
                throw new IOException("fail");
            }
            return "Success";
        };

        RetryListener listener = mock(RetryListener.class);

        RetryPolicy<String> policy = new RetryPolicy.Builder<String>()
                .maxAttempts(5)
                .predicate(e -> e instanceof IOException)
                .retryListener(listener)
                .backOffStrategy(new FixedBackOffStrategy(Duration.ZERO))
                .build();

        String result = RetryExecutor.retryOnException(task, policy);

        assertEquals("Success", result);
        verify(listener, times(2)).onRetry(anyInt(), any(IOException.class));
    }

    @Test
    void nonRetriableExceptionFailsImmediately() {
        Callable<String> task = () -> {
            throw new IllegalArgumentException("non-retriable");
        };

        RetryPolicy<String> policy = new RetryPolicy.Builder<String>()
                .maxAttempts(3)
                .predicate(e -> e instanceof IOException) // only IOException is retried
                .backOffStrategy(new FixedBackOffStrategy(Duration.ZERO))
                .build();

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> RetryExecutor.retryOnException(task, policy));

        assertEquals("non-retriable", thrown.getMessage());
    }

    @Test
    void retriesExhaustedThrowsLastException() {
        Callable<String> task = () -> {
            throw new IOException("always fails");
        };

        RetryPolicy<String> policy = new RetryPolicy.Builder<String>()
                .maxAttempts(3)
                .predicate(e -> e instanceof IOException)
                .backOffStrategy(new FixedBackOffStrategy(Duration.ZERO))
                .build();

        IOException thrown = assertThrows(IOException.class,
                () -> RetryExecutor.retryOnException(task, policy));

        assertEquals("always fails", thrown.getMessage());
    }
}
