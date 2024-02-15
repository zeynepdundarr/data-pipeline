package processorTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import processor.LogProcessor;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class LogProcessorTest {

    private LogProcessor logProcessor;

    @BeforeEach
    void setUp() {
        logProcessor = new LogProcessor();
    }

    @Test
    void testProcessLog() {
        logProcessor.processLog(Arrays.asList("Sample log entry 1", "Sample log entry 2"));
        assertNotEquals(0, logProcessor.getSummaryAndReportCombined().length(), "Processed log must not be empty");
    }

    @Test
    void testGetLogReport() {
        logProcessor.processLog(Arrays.asList("Sample log entry 1", "Sample log entry 2"));
        String report = logProcessor.getLogReport();
        assertNotNull(report, "Report should not be null");
        assertTrue(report.contains("##Log Report"), "Report should contain the correct header");
    }

    @Test
    void testGetLogSummary() {
        logProcessor.processLog(Arrays.asList("Sample log entry 1", "Sample log entry 2"));
        String summary = logProcessor.getLogSummary();
        assertNotNull(summary, "Summary should not be null");
        assertTrue(summary.contains("## Executive Log Summary"), "Summary should contain the correct header");
    }

    @Test
    void testGetSummaryAndReportCombined() {
        logProcessor.processLog(Arrays.asList("Sample log entry 1", "Sample log entry 2"));
        String combined = logProcessor.getSummaryAndReportCombined();
        assertNotNull(combined, "Combined summary and report should not be null");
        assertTrue(combined.contains("##Log Report"), "Combined content should contain the log report");
        assertTrue(combined.contains("## Executive Log Summary"), "Combined content should contain the log summary");
    }
}
