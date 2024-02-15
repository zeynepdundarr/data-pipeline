package generatorTest;

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
        assertNotNull(logEntry, "Log entry must not be null");
        assertFalse(logEntry.isEmpty(), "Log entry must not be empty");
        assertTrue(logEntry.startsWith("[UserActivity]"), "Log entry must start with expected prefix");
        assertTrue(logEntry.matches("\\[UserActivity] \\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}] User\\d{3} .* on .*\\."),
                "Log entry must contain a valid timestamp, user, action, and page");
    }
}
