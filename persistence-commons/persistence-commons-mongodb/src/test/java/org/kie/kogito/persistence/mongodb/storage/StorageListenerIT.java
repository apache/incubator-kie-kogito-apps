package org.kie.kogito.persistence.mongodb.storage;

import javax.inject.Inject;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.StorageService;
import org.kie.kogito.persistence.mongodb.client.MongoClientManager;
import org.kie.kogito.persistence.mongodb.storage.StorageUtilsIT.TestListener;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class StorageListenerIT {

    @Inject
    MongoClientManager mongoClientManager;

    @Inject
    StorageService storageService;

    @BeforeEach
    void setup() {
        mongoClientManager.getCollection("test", Document.class).insertOne(new Document("test", "test"));
    }

    @AfterEach
    void tearDown() {
        mongoClientManager.getCollection("test", Document.class).drop();
    }

    @Test
    void testObjectCreatedListener() throws Exception {
        TestListener testListener = new TestListener(3);
        Storage<String, String> storage = storageService.getCache("test");
        storage.objectCreatedListener().subscribe().with(testListener::add);
        storage.put("testKey_insert_1", "testValue1");
        storage.put("testKey_insert_2", "testValue2");
        storage.put("testKey_insert_3", "testValue3");

        testListener.await();
        assertEquals(3, testListener.items.size(), "values: " + testListener.items.keySet());
        assertTrue(testListener.items.keySet().containsAll(asList("testValue1", "testValue2", "testValue3")));
    }

    @Test
    void testObjectUpdatedListener() throws Exception {
        TestListener testListener = new TestListener(2);
        Storage<String, String> cache = storageService.getCache("test");
        cache.objectUpdatedListener().subscribe().with(testListener::add);
        cache.put("testKey_update_1", "testValue1");
        cache.put("testKey_update_1", "testValue2");
        cache.put("testKey_update_2", "testValue3");
        cache.put("testKey_update_2", "testValue4");

        testListener.await();
        assertEquals(2, testListener.items.size());
        assertTrue(testListener.items.keySet().containsAll(asList("testValue2", "testValue4")));
    }

    @Test
    void testObjectRemovedListener() throws Exception {
        TestListener testListener = new TestListener(2);
        Storage<String, String> cache = storageService.getCache("test");
        cache.objectRemovedListener().subscribe().with(testListener::add);
        cache.put("testKey_remove_1", "testValue1");
        cache.put("testKey_remove_2", "testValue2");
        cache.remove("testKey_remove_1");
        cache.remove("testKey_remove_2");

        testListener.await();
        assertEquals(2, testListener.items.size());
        assertTrue(testListener.items.keySet().containsAll(asList("testKey_remove_1", "testKey_remove_2")));
    }
}
