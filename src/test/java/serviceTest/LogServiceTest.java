package serviceTest;

import LogFileProcessorToolApp.service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LogServiceTest {

    private LogService logService;
    private final Random random = new Random();
    @BeforeEach
    void setUp() {
        int randInt = random.nextInt(900);
        logService = new LogService("testDB"+randInt);
        logService.init("testLogCollection55");
    }

//    @Test
//    void testLogDocumentTransformation() {
//        ConsumerRecord<String, String> mockRecord = new ConsumerRecord<>("topic", 0, 0, "key", "Test log message");
//        logService.transformRecordsToLogDocument(mockRecord, "testLogCollection");
//
//        List<String> logs = logService.getAllLogs("testLogCollection");
//        assertFalse(logs.isEmpty());
//        assertTrue(logs.get(0).contains("Test log message"));
//    }

    @Test
    void testLogSummaryGeneration() {
        String summary = logService.getLogSummaryString("testLogCollection");
        assertNotNull(summary);
        assertFalse(summary.isEmpty());
    }
}
