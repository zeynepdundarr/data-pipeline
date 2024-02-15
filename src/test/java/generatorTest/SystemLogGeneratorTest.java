package generatorTest;

import LogFileProcessorToolApp.generator.SystemLogGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SystemLogGeneratorTest {

    private SystemLogGenerator logGenerator;

    @BeforeEach
    void setUp() {
        logGenerator = new SystemLogGenerator();
    }

    @Test
    void testGenerateLogEntry() {
        String logEntry = logGenerator.generateLogEntry();
        assertNotNull(logEntry, "Log entry must not be null");
        assertFalse(logEntry.isEmpty(), "Log entry must not be empty");
        assertTrue(logEntry.startsWith("[Date"), "Log entry must start with expected prefix");
        assertTrue(logEntry.contains("[System]"), "Log entry must contain the system tag");
        assertTrue(logEntry.contains("] - "), "Log entry must contain the separators for splitting severity level");
        assertTrue(Arrays.stream(SystemLogGenerator.SEVERITY_LEVELS).anyMatch(logEntry::contains),
                "Log entry should contain a valid severity level");
        assertTrue(Arrays.stream(SystemLogGenerator.CONTEXT_MESSAGES).anyMatch(logEntry::contains),
                "Log entry should contain a valid system message");
    }
}
