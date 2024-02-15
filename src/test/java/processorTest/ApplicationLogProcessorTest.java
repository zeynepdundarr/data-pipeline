package processorTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import LogFileProcessorToolApp.processor.ApplicationLogProcessor;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

class ApplicationLogProcessorTest {

    private ApplicationLogProcessor logProcessor;

    @BeforeEach
    void setUp() {
        logProcessor = new ApplicationLogProcessor();
    }

    @Test
    void testProcessApplicationLogs() {
        List<String> logMessages = Arrays.asList(
                "[Date: 2023-12-13 02:42:18] [Application] [Error] - Database Issue - System status is not normal.",
                "[Date: 2023-12-13 02:42:18] [Application] [Warning] - Memory Usage - Memory usage is high."
        );
        String result = logProcessor.processApplicationLogs(logMessages);
        assertNotNull(result, "Result must not be null");
        assertFalse(result.isEmpty(), "Result must not be empty");
        assertTrue(result.contains("Severity: Error"), "Must contain 'Severity: Error'");
        assertTrue(result.contains("Severity: Warning"), "Must contain 'Severity: Warning'");
        assertTrue(result.contains("Database Issue                 Count: 1"), "Must contain 'Database Issue                 Count: 1");
        assertTrue(result.contains("Memory Usage                   Count: 1"), "Must contain 'Memory Usage                   Count: 1");
    }

    @Test
    void testGetTotalLogCounts() {
        List<String> logMessages = Arrays.asList(
                "[Date: 2023-12-13 02:42:18] [Application] [Error] - Database Issue - System status is not normal.",
                "[Date: 2023-12-13 02:42:18] [Application] [Warning] - Memory Usage - Memory usage is high."
        );
        logProcessor.processApplicationLogs(logMessages);
        assertEquals(2, logProcessor.getTotalLogCounts(), "Total log counts should be 2");
    }
}
