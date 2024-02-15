package LogFileProcessorToolApp.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogPatternMatcher {
    private static final Pattern USER_ACTIVITY_PATTERN = Pattern.compile("\\[UserActivity\\] \\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\] (User\\d+) (.*?)[.]");
    private static final Pattern SYSTEM_PATTERN = Pattern.compile("\\[Date: (.+?)\\] \\[System\\] \\[(.+?)\\] - (.+?) - (.+)");
    private static final Pattern APPLICATION_PATTERN = Pattern.compile("\\[Date: (.+?)\\] \\[Application\\] \\[(.+?)\\] - (.+?) - (.+)");

    public Matcher getApplicatonMatcher(String log) {
        return APPLICATION_PATTERN.matcher(log);
    }

    public Matcher getUserActivityMatcher(String log) {
        return USER_ACTIVITY_PATTERN.matcher(log);
    }

    public Matcher getSystemMatcher(String log) {
        return SYSTEM_PATTERN.matcher(log);
    }
}
