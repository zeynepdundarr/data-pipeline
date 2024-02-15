package LogFileProcessorToolApp.producer;

import LogFileProcessorToolApp.generator.ApplicationLogGenerator;
import LogFileProcessorToolApp.generator.SystemLogGenerator;
import LogFileProcessorToolApp.generator.UserActivityLogGenerator;

public class LogProducerApp {
    public static void main(String[] args) {
        long logIntervalMilliseconds = 1000;

        LogProducer UserActivityLogProducer = new LogProducer(new UserActivityLogGenerator(), logIntervalMilliseconds);
        LogProducer SystemLogProducer = new LogProducer(new SystemLogGenerator(), logIntervalMilliseconds);
        LogProducer ApplicationLogProducer = new LogProducer(new ApplicationLogGenerator(), logIntervalMilliseconds);
        
        Thread UserActivityThread = new Thread(UserActivityLogProducer);
        Thread SystemThread = new Thread(SystemLogProducer);
        Thread ApplicationThread = new Thread(ApplicationLogProducer);

        UserActivityThread.start();
        SystemThread.start();
        ApplicationThread.start();
    }
}
