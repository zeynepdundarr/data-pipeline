package service;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import processor.LogProcessor;
import adapter.MongoDBManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogService {
    private static final String DB_URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "usersDB3";
    private static final String LOG_COLLECTION_NAME = "log7";
    public static MongoDBManager mongoDBManager;
    public static LogProcessor logProcessor = new LogProcessor();
    private static MongoClient mongoClient;

    public LogService(){
        mongoClient = MongoClients.create(DB_URI);
        mongoDBManager =  new MongoDBManager(mongoClient, DB_NAME);

    }
    public static void init(String collectionName){
        mongoDBManager.createCollection(collectionName);
    }
    public static void transformRecordsToLogDocument(ConsumerRecord<String, String> record) {
        mongoDBManager.transformRecordsToLogDocument(record);
    }

    public static String getLogSummaryString(String collectionName) {
        List<String> recordStringList = getDocumentsAsString(collectionName);
        writeLogsToFile(recordStringList);
        logProcessor.processLog(recordStringList);
        return logProcessor.getSummaryAndReportCombined();
    }

    public static String getLogSummaryStringWithDateInterval(Date start, Date end) {
        List<String> recordStringList = getLogsByDateInterval(start, end);
        logProcessor.processLog(recordStringList);
        writeLogsToFile(recordStringList);
        return logProcessor.getSummaryAndReportCombined();
    }

    public static MongoCollection<Document> getCollection(String collectionName) {
        return mongoDBManager.getCollection(collectionName);
    }

    public static List<JSONObject> convertDocumentsToJsonObjects(String collectionName) {
        List<JSONObject> jsonList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        MongoCollection<Document> alldocs = mongoDBManager.getCollection(collectionName);
        FindIterable<Document> iterDoc = alldocs.find();

        for (Document doc : iterDoc) {
            try {
                String jsonString = doc.toJson();
                JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
                jsonList.add(jsonObject);
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }
        }
        return jsonList;
    }

    public static List<String> getDocumentsAsString(String collectionName) {
        // TODO: Move it to Mango db manager
        //
        MongoCollection<Document> collection = mongoDBManager.getCollection(collectionName);
        FindIterable<Document> iterDoc = collection.find();
        List<String> jsonList = new ArrayList<>();

        for (Document doc : iterDoc) {
            String jsonString = doc.toJson();
            jsonList.add(jsonString);
        }
        return jsonList;
    }

    public static List<String> getLogsByDateInterval(Date start, Date end){
        List<String> stringList = mongoDBManager.searchRecordsByDateInterval(start, end);
        return stringList;
    }

    public static void deleteAllLogs(String collectionName){
        mongoDBManager.deleteCollection(collectionName);
    }


    public static void writeLogsToFile(List<String> summary) {
        try {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
            String filePath = timeStamp + "Extended-Log.txt";

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
