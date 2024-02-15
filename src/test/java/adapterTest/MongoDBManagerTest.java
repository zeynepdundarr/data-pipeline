package adapterTest;

import LogFileProcessorToolApp.adapter.MongoDBManager;
import com.mongodb.client.*;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class MongoDBManagerTest {

    @Mock
    private MongoClient mockMongoClient;
    @Mock
    private MongoDatabase mockDatabase;
    @Mock
    private MongoCollection<Document> mockCollection;
    @Mock
    private FindIterable<Document> mockFindIterable;
    @Mock
    private MongoCursor<Document> mockCursor;
    private MongoDBManager mongoDBManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockMongoClient.getDatabase(anyString())).thenReturn(mockDatabase);
        when(mockDatabase.getCollection(anyString())).thenReturn(mockCollection);

        when(mockCollection.find()).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);
        when(mockCursor.hasNext()).thenReturn(false); // Indicates no more elements in the iterator
        mongoDBManager = new MongoDBManager(mockMongoClient, "testDB");
    }

    @Test
    void testCreateCollection() {
        mongoDBManager.createCollection("testCollection");
        verify(mockDatabase, times(1)).createCollection("testCollection");
    }

    @Test
    void testInsertDocument() {
        Document testDocument = new Document("key", "value");
        mongoDBManager.insertDocument("testCollection", testDocument);
        verify(mockCollection, times(1)).insertOne(testDocument);
    }

    @Test
    void testQueryCollection() {
        mongoDBManager.queryCollection("testCollection");
        verify(mockCollection, times(1)).find();
    }

    @Test
    void testDeleteCollection() {
        mongoDBManager.deleteCollection("testCollection");
        verify(mockCollection, times(1)).deleteMany(new Document());
    }

    @Test
    void testGetCollection() {
        MongoCollection<Document> returnedCollection = mongoDBManager.getCollection("testCollection");
        assertNotNull(returnedCollection, "Returned collection should not be null");
        verify(mockDatabase, times(1)).getCollection("testCollection");
    }
}
