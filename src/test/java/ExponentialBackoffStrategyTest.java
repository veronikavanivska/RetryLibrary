import com.vanivska.retryhelper.strategies.ExponentialBackoffStrategy;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExponentialBackoffStrategyTest {

    @Test
    public void testExponentialBackoffStrategy() {
        ExponentialBackoffStrategy strategy = new ExponentialBackoffStrategy(Duration.ofMillis(500));

        assertEquals(Duration.ofMillis(500), strategy.nextAttemptTime(1));
        assertEquals(Duration.ofMillis(1000), strategy.nextAttemptTime(2));
        assertEquals(Duration.ofMillis(2000), strategy.nextAttemptTime(3));
        assertEquals(Duration.ofMillis(4000), strategy.nextAttemptTime(4));
    }
}
