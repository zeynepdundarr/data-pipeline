package LogFileProcessorToolApp.generator;

import LogFileProcessorToolApp.strategy.LogGenerationStrategy;
import java.text.SimpleDateFormat;
import java.util.*;

public class SystemLogGenerator implements LogGenerationStrategy {
    private static final Random random = new Random();
    private static final Map<String, List<String>> severityContextMap = new HashMap<>();
    public static final String[] SEVERITY_LEVELS = {
            "OK",
            "Warning",
            "Error",
            "Info",
            "Unknown",
            "Critical",
            "Degraded",
            "Minor",
            "Major",
            "Maintenance"
    };

    public static final String[] CONTEXT_MESSAGES = {
            "Overall health",
            "Cooling Health",
            "Processors Health",
            "Memory Health",
            "Power Health",
            "Storage Health",
            "CPU Health",
            "Memory Usage",
            "Network Health",
            "Database Health",
            "Service Availability",
            "Application Latency",
            "Disk Space Usage",
            "Memory Consumption by Process",
            "Total Used and Free Memory",
            "Memory Swapping",
            "Page Faults",
            "Cache Usage",
            "Buffer Usage",
            "Garbage Collection Activity",
            "Heap and Non-Heap Memory Usage",
            "Memory Leaks",
            "Virtual Memory Size",
            "Resident Set Size (RSS)",
            "Shared and Private Memory",
            "Memory Fragmentation"
    };

    public static final String[] SYSTEM_CONTEXT = {
            "Health Overview",
            "Memory Management",
            "Network Performance",
            "Database Operations",
            "Service Status",
            "Application Performance",
            "Storage Usage",
            "CPU Performance",
            "Power Management",
            "Cooling System"
    };


    static {
        severityContextMap.put("Critical", Arrays.asList("Potential issues identified. Investigation required.", "System performance is reduced but not critical."));
        severityContextMap.put("Maintenance", Arrays.asList("Major issues detected, significant impact.", "Routine maintenance underway."));
        severityContextMap.put("Info", Arrays.asList("No issues detected. System is running smoothly.", "System status is normal."));
        severityContextMap.put("Minor", Arrays.asList("Minor issues detected, not critical.", "Small deviations observed."));
        severityContextMap.put("Major", Arrays.asList("Major issues detected, significant impact.", "Potential system impact observed."));
        severityContextMap.put("Error", Arrays.asList("Critical issues detected. Immediate action needed.", "System failure imminent."));
    }

    @Override
    public String generateLogEntry() {
        return generateSystemLog();
    }

    public static String generateSystemLog() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date());
        String severity = SEVERITY_LEVELS[random.nextInt(SEVERITY_LEVELS.length)];
        String context = CONTEXT_MESSAGES[random.nextInt(CONTEXT_MESSAGES.length)];
        List<String> systemMessages = severityContextMap.getOrDefault(severity, Collections.singletonList("No specific context available."));
        String systemMesssage = systemMessages.get(random.nextInt(systemMessages.size()));
        return "[Date: " + timestamp + "] [System] [" + severity + "] - " + context + " - " + systemMesssage;
    }

}


