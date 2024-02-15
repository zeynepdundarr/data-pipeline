package processorTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import LogFileProcessorToolApp.processor.SystemHealthLogProcessor;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class SystemHealthLogProcessorTest {
    private SystemHealthLogProcessor logProcessor;

    @BeforeEach
    void setUp() {
        logProcessor = new SystemHealthLogProcessor();
    }

    @Test
    void testParseMessages() {
        List<String> logMessages = Arrays.asList("[Date: 2023-12-13 02:42:18] [System] [Error] - CPU Health - High temperature detected.");
        List<SystemHealthLogProcessor.SystemMessage> parsedMessages = logProcessor.parseMessages(logMessages);
        assertNotNull(parsedMessages, "Parsed messages should not be null");
        assertEquals(1, parsedMessages.size(), "Should parse one message");
        assertEquals("Error", parsedMessages.get(0).getStatus(), "Status should be 'Error'");
        assertEquals("High temperature detected.", parsedMessages.get(0).getMessage(), "Message content should match");
        assertEquals("CPU Health", parsedMessages.get(0).getContext(), "Message content should match");
    }

    @Test
    void testCreateMessageSeverityMap() {
        List<SystemHealthLogProcessor.SystemMessage> messages = Arrays.asList(
                new SystemHealthLogProcessor.SystemMessage("Error", "CPU Health" ,"High temperature detected."),
                new SystemHealthLogProcessor.SystemMessage("Error", "CPU Health","High temperature detected.")
        );
        Map<String, Map<String, Integer>> messageSeverity = SystemHealthLogProcessor.createContextSeverityMap(messages);

        assertNotNull(messageSeverity, "Message severity map should not be null");
        assertTrue(messageSeverity.containsKey("CPU Health"), "Map should contain the specific message");
        assertEquals(1, messageSeverity.size(), "Map should have one entry");
        assertEquals(2, messageSeverity.get("CPU Health").get("Error"), "Error count should be 2");
    }

    @Test
    void testCalculatePercentageByMessage() {
        Map<String, Map<String, Integer>> contextSeverity = Map.of(
                "CPU Health", Map.of("Error", 2, "Warning", 1)
        );
        Map<String, Map<String, Double>> messagePercentages = SystemHealthLogProcessor.calculatePercentageByMessage(contextSeverity);
        assertNotNull(messagePercentages, "Message percentages map should not be null");
        assertTrue(messagePercentages.containsKey("CPU Health"), "Map should contain the specific message");
        assertEquals(66.67, messagePercentages.get("CPU Health").get("Error"), 0.01, "Error percentage should be correct");
        assertEquals(33.33, messagePercentages.get("CPU Health").get("Warning"), 0.01, "Warning percentage must be correct");
    }

    @Test
    void testProcessSystemHealthLogs() {
        List<String> logMessages = Arrays.asList("[Date: 2023-12-13 02:42:18] [System] [Error] - CPU Health - High temperature detected.");
        String summary = logProcessor.processSystemHealthLogs(logMessages);

        assertNotNull(summary, "Summary must not be null");
        assertTrue(summary.contains("Context: CPU Health"), "Summary must contain context");
        assertTrue(summary.contains("Severity: Error"), "Summary must contain severity");
        assertTrue(summary.contains("Count: 1"), "Summary must contain count");
        assertTrue(summary.contains("Percentage: 100.00%"), "Summary must contain percentage");
    }

    @Test
    void testGetTotalLogCounts() {
        logProcessor.processSystemHealthLogs(Arrays.asList("[Date: 2023-12-13 02:42:18] [System] [Error] - CPU Health - High temperature detected.\""));
        assertEquals(1, logProcessor.getTotalLogCounts(), "Total log counts must be correct");
    }
}
