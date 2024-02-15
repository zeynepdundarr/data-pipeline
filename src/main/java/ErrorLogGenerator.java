import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ErrorLogGenerator implements LogGenerationStrategy {
    private static final String[] ERROR_MESSAGES = {"Page not found", "Database connection failed", "Network timeout", "IO Exception"};
    private final Random random = new Random();

    @Override
    public String generateLogEntry() {
        return generateErrorLog();
    }

    public String generateErrorLog() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date());
        int errorCode = 100 + random.nextInt(900);
        return String.format("[Error] [%s] [ErrCode: %d] %s", timestamp, errorCode, ERROR_MESSAGES[random.nextInt(ERROR_MESSAGES.length)]);
    }
}
