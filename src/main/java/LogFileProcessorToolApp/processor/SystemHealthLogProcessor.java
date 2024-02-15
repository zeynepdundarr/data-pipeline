package LogFileProcessorToolApp.processor;

import LogFileProcessorToolApp.matcher.LogPatternMatcher;

import java.util.*;
import java.util.regex.Matcher;


public class SystemHealthLogProcessor {
    public int totalLogCounts;
    public final LogPatternMatcher patternMatcher;
    public SystemHealthLogProcessor() {
        this.patternMatcher = new LogPatternMatcher();
        totalLogCounts=0;
    }
    public static class SystemMessage {
        private String status;
        private String message;
        private String context;

        public SystemMessage(String status, String context, String message) {
            this.status = status;
            this.context = context;
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
        public String getContext() {
            return context;
        }
    }

    public List<SystemMessage> parseMessages(List<String> logMessages) {
        List<SystemMessage> parsedMessages = new ArrayList<>();

        for (String logMessage : logMessages) {
            Matcher matcher = patternMatcher.getSystemMatcher(logMessage);
            if (matcher.find()) {
                String severity = matcher.group(2);
                String context = matcher.group(3);
                String message = matcher.group(4);
                SystemMessage systemMessage = new SystemMessage(severity, context, message);
                parsedMessages.add(systemMessage);
            }
        }
        return parsedMessages;
    }

    public static Map<String, Map<String, Integer>> createContextSeverityMap(List<SystemMessage> messages) {
        Map<String, Map<String, Integer>> messageSeverity = new HashMap<>();

        for (SystemMessage message : messages) {
            String messageContext = message.getContext();
            String severity = message.getStatus();
            Map<String, Integer> severityMap = messageSeverity.computeIfAbsent(messageContext, k -> new HashMap<>());
            severityMap.put(severity, severityMap.getOrDefault(severity, 0) + 1);
        }
        return messageSeverity;
    }

    public static Map<String, Map<String, Double>> calculatePercentageByMessage(Map<String, Map<String, Integer>> messageSeverity) {
        Map<String, Map<String, Double>> messagePercentages = new HashMap<>();

        for (Map.Entry<String, Map<String, Integer>> entry : messageSeverity.entrySet()) {
            String messageContent = entry.getKey();
            Map<String, Integer> severityCounts = entry.getValue();

            int total = severityCounts.values().stream().mapToInt(Integer::intValue).sum();
            Map<String, Double> percentages = new HashMap<>();
            for (Map.Entry<String, Integer> severityEntry : severityCounts.entrySet()) {
                String severity = severityEntry.getKey();
                int count = severityEntry.getValue();
                double percentage = (double) count / total * 100;
                percentages.put(severity, percentage);
            }
            messagePercentages.put(messageContent, percentages);
        }
        return messagePercentages;
    }

    public String processSystemHealthLogs(List<String> logMessages) {
        List<SystemMessage> parsedMessages = parseMessages(logMessages);
        Map<String, Map<String, Integer>> severityCountForContext = createContextSeverityMap(parsedMessages);
        Map<String, Map<String, Double>> messagePercentages = calculatePercentageByMessage(severityCountForContext);

        StringBuilder summaryBuilder = new StringBuilder();
        for (Map.Entry<String, Map<String, Integer>> severityEntry : severityCountForContext.entrySet()) {
            String context = severityEntry.getKey();
            Map<String, Integer> severities = severityEntry.getValue();
            Map<String, Double> percentages = messagePercentages.get(context);
            summaryBuilder.append("\n=== Context: ").append(context).append(" ===");

            for (Map.Entry<String, Integer> countEntry : severities.entrySet()) {
                String severityType = countEntry.getKey();
                int count = countEntry.getValue();
                double percentage = percentages.getOrDefault(severityType, 0.0);
                totalLogCounts += count;
                summaryBuilder.append("\n - Severity: ").append(severityType)
                        .append("\n     - Count: ").append(count)
                        .append("\n     - Percentage: ").append(String.format("%.2f%%", percentage));
            }
            summaryBuilder.append("\n");
        }
        return summaryBuilder.toString();
    }
    public int getTotalLogCounts(){
        return totalLogCounts;
    }
}
