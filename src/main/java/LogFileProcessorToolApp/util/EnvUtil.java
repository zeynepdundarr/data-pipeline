package LogFileProcessorToolApp.util;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvUtil {
    private static Dotenv dotenv = Dotenv.configure().load();

    public static String get(String variableName) {
        return dotenv.get(variableName);
    }
}