package LogFileProcessorToolApp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Scanner;

public class DateParser {
    public static Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Date format not supported: " + dateString);
        }
    }

    public static String promptForDate(Scanner scanner, String dateType) {
        String date = null;
        int attempts = 0;
        while (attempts < 3 && date == null) {
            System.out.print("Enter " + dateType + " date (yyyy-MM-dd HH:mm): ");
            String input = scanner.nextLine();
            if (isValidDateFormat(input, "yyyy-MM-dd HH:mm")) {
                date = input;
            } else {
                System.out.println("Invalid date format. Please try again.");
                attempts++;
            }
            if (attempts >= 3) {
                System.out.println("Maximum attempts reached. Exiting program.");
                System.exit(0);
            }
        }
        return date;
    }

    private static boolean isValidDateFormat(String value, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDateTime.parse(value, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
