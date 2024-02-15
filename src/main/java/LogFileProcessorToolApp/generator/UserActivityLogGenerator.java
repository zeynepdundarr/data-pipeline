package LogFileProcessorToolApp.generator;

import LogFileProcessorToolApp.strategy.LogGenerationStrategy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class UserActivityLogGenerator implements LogGenerationStrategy {
    private static final String[] USER_ACTIONS = {
            "logged in", "logged out", "viewed page", "clicked item"
    };

    private static final String[] PAGES = {
            "Homepage", "Product Page", "Checkout", "Profile"
    };
    private final Random random = new Random();

    @Override
    public String generateLogEntry() {
        return generateUserLog();
    }

    public String generateUserLog() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date());
        String user = "User" + (100 + random.nextInt(900));
        String action = USER_ACTIONS[random.nextInt(USER_ACTIONS.length)];
        String page = PAGES[random.nextInt(PAGES.length)];
        return String.format("[UserActivity] [%s] %s %s on %s.", timestamp, user, action, page);
    }
}
