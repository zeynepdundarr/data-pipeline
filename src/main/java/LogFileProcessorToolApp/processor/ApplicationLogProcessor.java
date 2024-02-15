package LogFileProcessorToolApp.processor;

import LogFileProcessorToolApp.matcher.LogPatternMatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;


public class ApplicationLogProcessor {
    public final LogPatternMatcher patternMatcher;
    public int totalLogCounts;

    public ApplicationLogProcessor() {
        this.patternMatcher = new LogPatternMatcher();
        totalLogCounts=0;
    }

    public String processApplicationLogs(List<String> logMessages) {
        Map<String, Map<String, Integer>> groupedContext = new HashMap<>();

        for (String log : logMessages) {
            Matcher matcher = patternMatcher.getApplicatonMatcher(log);
            if (matcher.find()) {
                String severity = matcher.group(2);
                String context = matcher.group(3);

                groupedContext.putIfAbsent(severity, new HashMap<>());
                Map<String, Integer> contextMap = groupedContext.get(severity);
                contextMap.put(context, contextMap.getOrDefault(context, 0) + 1);
            }
        }
        StringBuilder summary = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        String contextFormat = "    %-30s Count: %d%n";

        for (String severity : groupedContext.keySet()) {
            summary.append(lineSeparator)
                    .append("Severity: ")
                    .append(severity)
                    .append(lineSeparator)
                    .append("----------------------------------------------------")
                    .append(lineSeparator);

            Map<String, Integer> contextMap = groupedContext.get(severity);
            int total = 0;
            for (String context : contextMap.keySet()) {
                int count = contextMap.get(context);
                summary.append(String.format(contextFormat, context, count));
                total += count;
                totalLogCounts += count;
            }
            summary.append("----------------------------------------------------")
                    .append(lineSeparator)
                    .append(String.format("    Total for %s: %d", severity, total))
                    .append(lineSeparator);
        }
        return summary.toString();
    }
    public int getTotalLogCounts(){
        return totalLogCounts;
    }
}