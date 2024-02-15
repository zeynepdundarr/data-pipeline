package LogFileProcessorToolApp.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import LogFileProcessorToolApp.processor.LogProcessor;
import LogFileProcessorToolApp.adapter.MongoDBManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LogService {
    private static final String DB_URI = "mongodb://localhost:27017";
    public static MongoDBManager mongoDBManager;
    public static LogProcessor logProcessor = new LogProcessor();

    public LogService(String dbName){
        MongoClient mongoClient = MongoClients.create(DB_URI);
        mongoDBManager =  new MongoDBManager(mongoClient, dbName);
    }
    public static void init(String collectionName){
        mongoDBManager.createCollection(collectionName);
    }
    public static void transformRecordsToLogDocument(ConsumerRecord<String, String> record, String collectionName) {
        MongoDBManager.transformRecordsToLogDocument(record, collectionName);
    }

    public static String getLogSummaryString(String collectionName) {
        List<String> recordStringList = MongoDBManager.getDocumentsAsString(collectionName);
        writeLogsToFile(recordStringList);
        logProcessor.processLog(recordStringList);
        return logProcessor.getSummaryAndReportCombined();
    }

    public static String getLogSummaryStringWithDateInterval(Date start, Date end, String collectionName) {
        List<String> recordStringList = getLogsByDateInterval(start, end, collectionName);
        logProcessor.processLog(recordStringList);
        writeLogsToFile(recordStringList);
        return logProcessor.getSummaryAndReportCombined();
    }

    public static List<String> getLogsByDateInterval(Date start, Date end, String collectionName){
        return mongoDBManager.searchRecordsByDateInterval(start, end, collectionName);
    }
    public static List<String> getAllLogs(String collectionName){
        return mongoDBManager.getDocumentsAsString(collectionName);
    }

    public static void deleteAllLogs(String collectionName){
        mongoDBManager.deleteCollection(collectionName);
    }

    public static void writeLogsToFile(List<String> summary) {
        try {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
            String filePath = "logs/" + timeStamp + " Extended-Log.txt";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : summary) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
