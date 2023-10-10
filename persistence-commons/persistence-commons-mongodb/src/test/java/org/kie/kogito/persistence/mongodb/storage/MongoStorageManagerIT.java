package org.kie.kogito.persistence.mongodb.storage;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class MongoStorageManagerIT {

    @Inject
    MongoStorageManager mongoStorageManager;

    Storage storage;

    @AfterEach
    void tearDown() {
        ((MongoStorage) storage).mongoCollection.drop();
    }

    @Test
    void testGetCache() {
        String storageName = "testCache";
        storage = mongoStorageManager.getCache(storageName);

        assertTrue(storage instanceof MongoStorage);
        assertEquals(storageName, ((MongoStorage) storage).mongoCollection.getNamespace().getCollectionName());
    }

    @Test
    void testGetCacheWithClass() {
        String storageName = "testCacheWithClass";
        storage = mongoStorageManager.getCache(storageName, String.class);

        assertTrue(storage instanceof MongoStorage);
        assertEquals(storageName, ((MongoStorage) storage).mongoCollection.getNamespace().getCollectionName());
    }

    @Test
    void getCacheWithDataFormat() {
        String storageName = "testCacheWithDataFormat";
        storage = mongoStorageManager.getCache(storageName, String.class, "type");

        assertTrue(storage instanceof MongoStorage);
        assertEquals(storageName, ((MongoStorage) storage).mongoCollection.getNamespace().getCollectionName());
    }
}
