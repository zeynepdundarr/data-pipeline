package LogFileProcessorToolApp.adapter;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.bson.Document;
import com.mongodb.client.result.InsertOneResult;
import org.bson.conversions.Bson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MongoDBManager {
    private final MongoClient mongoClient;
    private static MongoDatabase database;
//    private static final String LOG_COLLECTION_NAME = "log7";
    public MongoDBManager(MongoClient mongoClient, String dbName) {
        this.mongoClient = mongoClient;
        this.database = mongoClient.getDatabase(dbName);
    }

    public void createCollection(String collectionName) {
        database.createCollection(collectionName);
        System.out.println("Collection Created Successfully: " + collectionName);
    }

    public static void insertDocument(String collectionName, Document document) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        try {
            InsertOneResult result = collection.insertOne(document);
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
    }

    public void queryCollection(String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        FindIterable<Document> iterDoc = collection.find();

        for (Document doc : iterDoc) {
            System.out.println(doc.toJson());
        }
    }

    public void deleteCollection(String collectionName){
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.deleteMany(new Document());
        mongoClient.close();
    }

    public static MongoCollection<Document> getCollection(String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection;
    }

    public static void transformRecordsToLogDocument(ConsumerRecord<String, String> record, String collectionName) {
        String recordValue = record.value();
        System.out.println("Received: " + recordValue);

        Pattern datePattern = Pattern.compile("\\[Date: (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\]");
        Matcher matcher = datePattern.matcher(recordValue);
        Date logDate = null;

        if (matcher.find()) {
            String dateString = matcher.group(1);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                logDate = dateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Document newLog = new Document("logMessage", recordValue);
        if (logDate != null) {
            newLog.append("logDate", logDate);
        }
        insertDocument(collectionName, newLog);
    }

    public List<String> searchRecordsByDateInterval(Date startDate, Date endDate, String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);

        Bson dateFilter = Filters.and(
                Filters.gte("logDate", startDate),
                Filters.lte("logDate", endDate)
        );
        List<String> jsonList = new ArrayList<>();

        FindIterable<Document> result = collection.find(dateFilter);

        for (Document doc : result) {
            String jsonString = doc.toJson();
            jsonList.add(jsonString);
        }
        return jsonList;
    }

    public static List<String> getDocumentsAsString(String collectionName) {
        List<String> jsonList = new ArrayList<>();

        MongoCollection<Document> collection = getCollection(collectionName);
        FindIterable<Document> iterDoc = collection.find();
        for (Document doc : iterDoc) {
            String jsonString = doc.toJson();
            jsonList.add(jsonString);
        }
        return jsonList;
    }

}