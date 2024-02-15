package LogFileProcessorToolApp.processor;

import java.util.List;

public class LogProcessor {
    private int applicationLogSummary;
    private int systemHealthLogSummary;
    private String systemHealthReport;
    private String applicationReport;
    private String userActivityReport;

    public LogProcessor(){
        applicationLogSummary = 0;
        systemHealthLogSummary = 0;
        systemHealthReport = "";
        applicationReport = "";
        userActivityReport = "";
    }

    public void processLog(List<String> logEntries) {
        SystemHealthLogProcessor systemHealthProcessor = new SystemHealthLogProcessor();
        PerformanceLogProcessor performanceProcessor = new PerformanceLogProcessor();
        ApplicationLogProcessor applicationProcessor = new ApplicationLogProcessor();

        systemHealthReport = systemHealthProcessor.processSystemHealthLogs(logEntries);
        userActivityReport = performanceProcessor.processUserActivityLogs(logEntries);
        applicationReport = applicationProcessor.processApplicationLogs(logEntries);

        applicationLogSummary = applicationProcessor.getTotalLogCounts();
        systemHealthLogSummary = systemHealthProcessor.getTotalLogCounts();
    }

    public String getLogReport() {
        StringBuilder builder = new StringBuilder();

        builder.append("\nExecutive Log Report\n");
        builder.append("===========\n\n");

        // Application Log
        builder.append("Application Log Report\n");
        builder.append("----------------------\n");
        builder.append(indentContent(applicationReport));
        builder.append("\n\n");

        // System Log
        builder.append("System Log Report\n");
        builder.append("-----------------\n");
        builder.append(indentContent(systemHealthReport));
        builder.append("\n\n");

        // User Activity Log
        builder.append("User Activity Log Report\n");
        builder.append("------------------------\n");
        builder.append(indentContent(userActivityReport));
        builder.append("\n");

        return builder.toString();
    }

    private String indentContent(String content) {
        return content.replaceAll("(?m)^", "    ");
    }

    public String getLogSummary() {
        StringBuilder builder = new StringBuilder();

        builder.append("Executive Log Summary\n");
        builder.append("=====================\n\n");

        builder.append("Application Log Summary\n");
        builder.append("-----------------------\n");
        builder.append("• Log count: ").append(applicationLogSummary).append("\n\n");

        builder.append("System Log Report\n");
        builder.append("-----------------\n");
        builder.append("• Log count: ").append(systemHealthLogSummary).append("\n\n");

        builder.append("User Activity Log Report\n");
        builder.append("------------------------\n");
        if (userActivityReport.isEmpty()) {
            builder.append("• No user activities recorded.\n");
        } else {
            builder.append(userActivityReport).append("\n");
        }

        return builder.toString();
    }

    public String getSummaryAndReportCombined(){
        return getLogSummary() + getLogReport();
    }
}
