package LogFileProcessorToolApp;
import LogFileProcessorToolApp.service.LogService;
import LogFileProcessorToolApp.util.DateParser;
import LogFileProcessorToolApp.util.EnvUtil;

import java.util.Date;
import java.util.Scanner;

public class LogFileProcessorTool {
    private static final DateParser dateParser = new DateParser();
    private static final String collectionName = EnvUtil.get("COLLECTION_NAME");
    private static final String DB_NAME = EnvUtil.get("DB_NAME");
    private static final LogService logService = new LogService(DB_NAME);


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Do you want to specify a date interval? (yes/no): ");
            String response = scanner.nextLine();

            if ("yes".equalsIgnoreCase(response)) {
                String start = dateParser.promptForDate(scanner, "start");
                String end = dateParser.promptForDate(scanner, "end");

                if (start != null && end != null) {
                    displaySummaryWithDateInterval(start, end);
                } else {
                    System.out.println("Invalid date format entered. Exiting.");
                }
            } else {
                displaySummary(collectionName);
            }
        } finally {
            scanner.close();
        }
    }

    private static void displaySummary(String collectionName) {
        String summary = logService.getLogSummaryString(collectionName);
        System.out.println(summary);
    }

    private static void displaySummaryWithDateInterval(String start, String end) {
        try {
            Date startDate = dateParser.parseDate(start);
            Date endDate = dateParser.parseDate(end);
            String summary = logService.getLogSummaryStringWithDateInterval(startDate, endDate, collectionName);
            System.out.println(summary);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteAllLogs(String collectionName) {
        logService.deleteAllLogs(collectionName);
    }
}

