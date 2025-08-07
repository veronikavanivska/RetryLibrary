import com.vanivska.retryhelper.strategies.IncrementalBackOffStategy;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IncrementalBackOffStrategyTest {
    @Test
    public void testIncrementalBackOffStrategy() {
        IncrementalBackOffStategy strategy = new IncrementalBackOffStategy(Duration.ofMillis(500));
        assertEquals(Duration.ofMillis(500), strategy.nextAttemptTime(1));
        assertEquals(Duration.ofMillis(1000), strategy.nextAttemptTime(2));
        assertEquals(Duration.ofMillis(1500), strategy.nextAttemptTime(3));
    }
}
