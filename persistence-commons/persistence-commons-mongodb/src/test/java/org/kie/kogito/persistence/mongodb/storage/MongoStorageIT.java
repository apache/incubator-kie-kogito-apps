package org.kie.kogito.persistence.mongodb.storage;

import javax.inject.Inject;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.mongodb.client.MongoClientManager;
import org.kie.kogito.persistence.mongodb.mock.MockMongoEntityMapper;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.kie.kogito.persistence.mongodb.mock.MockMongoEntityMapper.TEST_ATTRIBUTE;
import static org.kie.kogito.persistence.mongodb.model.ModelUtils.MONGO_ID;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class MongoStorageIT {

    @Inject
    MongoClientManager mongoClientManager;

    MongoStorage<String, Document> storage;

    MongoCollection<Document> collection;

    @BeforeEach
    void setup() {
        collection = mongoClientManager.getCollection("test", Document.class);
        storage = new MongoStorage(collection, String.class.getName(), new MockMongoEntityMapper());
    }

    @AfterEach
    void tearDown() {
        collection.drop();
    }

    @Test
    void testContainsKey() {
        String testId = "testContains";
        collection.insertOne(new Document(MONGO_ID, testId));
        assertTrue(storage.containsKey(testId));
    }

    @Test
    void testGet() {
        String testId = "testGet";
        String testValue = "testValue";
        collection.insertOne(new Document(MONGO_ID, testId).append(TEST_ATTRIBUTE, testValue));
        assertEquals(testValue, storage.get(testId));
    }

    @Test
    void testPut() {
        String testId = "testPut";
        String testValue = "testValue";
        storage.put(testId, testValue);
        FindIterable<Document> findIterable = collection.find(new Document(MONGO_ID, testId));
        Document document = findIterable.first();
        assertNotNull(document);
        assertEquals(testValue, document.get(TEST_ATTRIBUTE));
    }

    @Test
    void testClear() {
        String testId = "testClear";
        String testValue = "testValue";
        collection.insertOne(new Document(MONGO_ID, testId).append(TEST_ATTRIBUTE, testValue));
        storage.clear();
        assertEquals(0, collection.countDocuments());
    }

    @Test
    void testRemove() {
        String testId = "testRemove";
        String testValue = "testValue";
        collection.insertOne(new Document(MONGO_ID, testId).append(TEST_ATTRIBUTE, testValue));
        storage.remove(testId);
        assertEquals(0, collection.countDocuments());
    }
}
