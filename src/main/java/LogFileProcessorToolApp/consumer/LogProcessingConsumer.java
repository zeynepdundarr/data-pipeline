package LogFileProcessorToolApp.consumer;

import LogFileProcessorToolApp.util.EnvUtil;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import LogFileProcessorToolApp.service.LogService;


public class LogProcessingConsumer {
    private static final String BOOTSTRAP_SERVERS = EnvUtil.get("BOOTSTRAP_SERVERS");
    private static final String GROUP_ID = EnvUtil.get("GROUP_ID");;
    private static final String DB_NAME = EnvUtil.get("DB_NAME");
    private static final String TOPIC_NAME = EnvUtil.get("TOPIC_NAME");
    private static final LogService logService = new LogService(DB_NAME);
    private static final String collectionName = EnvUtil.get("COLLECTION_NAME");

    public static void main(String[] args){

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");


        // init(collectionName);
        try (Consumer<String, String> consumer = new KafkaConsumer<>(properties)) {

            consumer.subscribe(Collections.singletonList(TOPIC_NAME));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("Received: " + record);
                    transformRecordsToLogDocument(record, collectionName);
                }
                consumer.commitSync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void init(String collectionName) {
        logService.init(collectionName);
    }

    private static void transformRecordsToLogDocument(ConsumerRecord<String, String> record, String collectionName) {
        logService.transformRecordsToLogDocument(record, collectionName);
    }
}
