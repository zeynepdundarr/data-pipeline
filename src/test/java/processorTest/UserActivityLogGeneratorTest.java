package processorTest;

import LogFileProcessorToolApp.generator.UserActivityLogGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserActivityLogGeneratorTest {

    private UserActivityLogGenerator logGenerator;

    @BeforeEach
    void setUp() {
        logGenerator = new UserActivityLogGenerator();
    }

    @Test
    void testGenerateLogEntry() {
        String logEntry = logGenerator.generateLogEntry();
        assertNotNull(logEntry, "Log entry should not be null");
        assertFalse(logEntry.isEmpty(), "Log entry should not be empty");
        assertTrue(logEntry.startsWith("[UserActivity]"), "Log entry should start with expected prefix");
        assertTrue(logEntry.matches("\\[UserActivity] \\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}] User\\d{3} .* on .*\\."),
                "Log entry should contain a valid timestamp, user, action, and page");
    }
}
