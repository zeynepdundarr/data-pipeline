package LogFileProcessorToolApp.processor;

import LogFileProcessorToolApp.matcher.LogPatternMatcher;

import java.util.*;
import java.util.regex.Matcher;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

public class PerformanceLogProcessor {
    public final LogPatternMatcher patternMatcher;

    public PerformanceLogProcessor() {
        this.patternMatcher = new LogPatternMatcher();
    }

    public String processUserActivityLogs(List<String> logMessages) {

        Map<String, Date[]> userActivityTimestamps = new HashMap<>();
        Map<String, Integer> userActivityCount = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, List<String>> userActivities = new HashMap<>();
        Map<String, Integer> activityFrequency = new HashMap<>();
        Date[] newArray = new Date[2];

        for (String log : logMessages) {
            Matcher matcher = patternMatcher.getUserActivityMatcher(log);
            if (matcher.find()) {
                try {
                    Date logDate = dateFormat.parse(matcher.group(1));
                    String user = matcher.group(2);
                    String activity = matcher.group(3);

                    Date[] minMaxDates = userActivityTimestamps.getOrDefault(user, newArray);
                    activityFrequency.put(activity, activityFrequency.getOrDefault(activity, 0) + 1);

                    if (minMaxDates[0] == null || logDate.before(minMaxDates[0])) {
                        minMaxDates[0] = logDate;
                    }

                    if (minMaxDates[1] == null || logDate.after(minMaxDates[1])) {
                        minMaxDates[1] = logDate;
                    }

                    userActivityTimestamps.put(user, minMaxDates);
                    userActivityCount.put(user, userActivityCount.getOrDefault(user, 0) + 1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        int maxActivity = 0;
        for (Map.Entry<String, Integer> entry : userActivityCount.entrySet()) {
            if (entry.getValue() > maxActivity) {
                maxActivity = entry.getValue();
            }
        }

        return generateActivitySummary(activityFrequency,  userActivityTimestamps);
    }

    private String getMostFrequentActivity( Map<String, Integer> frequencyMap) {
        if (frequencyMap.isEmpty()) {
            return "No activities recorded.";
        }
        StringBuilder activitySummary = new StringBuilder();
        String mostFrequentActivity = "";
        String leastFrequentActivity = "";
        int maxFrequency = 0;
        int minFrequency = Integer.MAX_VALUE;
        int totalActivities = 0;

        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            int currentFrequency = entry.getValue();
            totalActivities += currentFrequency;

            if (currentFrequency > maxFrequency) {
                mostFrequentActivity = entry.getKey();
                maxFrequency = currentFrequency;
            }
            if (currentFrequency < minFrequency) {
                leastFrequentActivity = entry.getKey();
                minFrequency = currentFrequency;
            }
        }
        if (mostFrequentActivity.equals(leastFrequentActivity)) {
            return "All activities occurred with the same frequency of " + maxFrequency + " clicks.\nTotal activity clicks: " + totalActivities + ".";
        }
        activitySummary.append("• Most frequent activity: ").append(mostFrequentActivity).append(" with ").append(maxFrequency).append(" clicks.\n");
        activitySummary.append("• Least frequent activity: ").append(leastFrequentActivity).append(" with ").append(minFrequency).append(" clicks.\n");
        activitySummary.append("• Total activity clicks: ").append(totalActivities).append(".");

        return activitySummary.toString();
    }

    private String getLongestActivityDuration(Map<String, Date[]> userActivityTimestamps) {
        long longestInactivityDurationMillis = 0;

        for (Map.Entry<String, Date[]> entry : userActivityTimestamps.entrySet()) {
            Date[] timestamps = entry.getValue();
            if (timestamps[0] != null && timestamps[1] != null) {
                long inactivityDuration = timestamps[1].getTime() - timestamps[0].getTime();
                if (inactivityDuration > longestInactivityDurationMillis) {
                    longestInactivityDurationMillis = inactivityDuration;
                }
            }
        }

        long hours = TimeUnit.MILLISECONDS.toHours(longestInactivityDurationMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(longestInactivityDurationMillis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(longestInactivityDurationMillis) % 60;

        return String.format("%d hours, %d minutes, %d seconds", hours, minutes, seconds);
    }

    public String generateActivitySummary(Map<String, Integer> activityFrequencyMap, Map<String, Date[]> userActivityTimestamps) {
        String activityStatistics = getMostFrequentActivity(activityFrequencyMap);
        String activeDuration = getLongestActivityDuration(userActivityTimestamps);

        StringBuilder summary = new StringBuilder();
        summary.append("User Activity Summary\n");
        summary.append("---------------------\n");
        summary.append("• Longest Logged-In Duration: ").append(activeDuration).append("\n\n");
        summary.append("Activity Statistics\n");
        summary.append("-------------------\n");
        summary.append(activityStatistics);

        return summary.toString();
    }
}
