package generatorTest;

import LogFileProcessorToolApp.generator.ApplicationLogGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationLogGeneratorTest {

    private ApplicationLogGenerator logGenerator;

    @BeforeEach
    void setUp() {
        logGenerator = new ApplicationLogGenerator();
    }

    @Test
    void testGenerateLogEntry() {
        String logEntry = logGenerator.generateLogEntry();
        assertNotNull(logEntry, "Log must not be null");
        assertFalse(logEntry.isEmpty(), "Log entry must not be empty");
        assertTrue(logEntry.startsWith("[Date"), "Log entry must start with expected prefix");
        assertTrue(logEntry.contains("[Application]"), "Log entry must contain the application tag");
        assertTrue(logEntry.contains("] - "), "Log entry must contain the separators for splitting severity level");
        assertTrue(logEntry.contains("Error") || logEntry.contains("Warning"),
                "Log entry must contain a severity level: Error or Warning.");
    }
}
