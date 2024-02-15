package LogFileProcessorToolApp.generator;

import LogFileProcessorToolApp.strategy.LogGenerationStrategy;

import java.text.SimpleDateFormat;
import java.util.*;

public class ApplicationLogGenerator implements LogGenerationStrategy {
    private static final Random random = new Random();
    public static Map<String, List<String>> contextMap;
    public static Map<String, List<String>> messageMap;
    private static final List<String> ERROR_MESSAGES = Arrays.asList(
            "Unable to establish a connection to the database.",
            "The external API did not respond within the expected time frame.",
            "Received malformed data in the user input.",
            "User credentials are incorrect or expired.",
            "Unable to access the specified file."
    );

    private static final List<String> WARNING_MESSAGES = Arrays.asList(
            "The application is consuming more memory than usual.",
            "Detected use of a deprecated function or module.",
            "The application is close to exceeding its allocated resources.",
            "The current configuration may not be optimal.",
            "Some data entries are missing or incomplete."
    );

    private static final List<String> ERROR_CONTEXTS = Arrays.asList(
            "Memory Usage Alert",
            "Deprecated API",
            "Resource Limit Near",
            "Configuration Issue",
            "Incomplete Data"
    );

    private static final  List<String> WARNING_CONTEXTS = Arrays.asList(
            "Database Connection Issue",
            "API Timeout",
            "Invalid User Input",
            "Authentication Problem",
            "File Access Error"
    );

    public ApplicationLogGenerator(){
        this.contextMap = new HashMap<>();
        contextMap.put("Error", ERROR_CONTEXTS);
        contextMap.put("Warning", WARNING_CONTEXTS);
        this.messageMap = new HashMap<>();
        messageMap.put("Error", ERROR_MESSAGES);
        messageMap.put("Warning", WARNING_MESSAGES);

    }

    @Override
    public String generateLogEntry() {
        return generateSystemLog();
    }

    public static String generateSystemLog() {
        String[] severities = {"Error", "Warning"};
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date());
        String severity = severities[random.nextInt(severities.length)];
        List<String> contexts = contextMap.get(severity);
        String selectedContext = contexts.get(random.nextInt(contexts.size()));
        List<String> messages = messageMap.get(severity);
        String selectedMessage = messages.get(random.nextInt(messages.size()));
        return "[Date: " + timestamp + "] [Application] [" + severity + "] - " + selectedContext +" - " +selectedMessage;
    }
}