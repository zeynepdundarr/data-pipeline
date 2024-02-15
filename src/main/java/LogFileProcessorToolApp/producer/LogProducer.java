package LogFileProcessorToolApp.producer;

import LogFileProcessorToolApp.util.EnvUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import LogFileProcessorToolApp.strategy.LogGenerationStrategy;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class LogProducer implements Runnable {
    private static final String BOOTSTRAP_SERVERS = EnvUtil.get("BOOTSTRAP_SERVERS");
    private static final String TOPIC_NAME = EnvUtil.get("TOPIC_NAME");
    private final KafkaProducer<String, String> producer;
    private final LogGenerationStrategy logGenerationStrategy;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final long logGenerationInterval;

    public LogProducer(LogGenerationStrategy logGenerationStrategy, long logGenerationInterval) {
        this.producer = new KafkaProducer<>(getKafkaProperties());
        this.logGenerationStrategy = logGenerationStrategy;
        this.logGenerationInterval = logGenerationInterval;
    }

    private Properties getKafkaProperties() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());return props;
    }

    public void run() {
        running.set(true);
        while (running.get()) {
            String logMessage = logGenerationStrategy.generateLogEntry();
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, logMessage);
            producer.send(record);
            try {
                Thread.sleep(logGenerationInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running.set(false);
            }
        }
        producer.close();
    }

    public void stop() {
        running.set(false);
    }
}
