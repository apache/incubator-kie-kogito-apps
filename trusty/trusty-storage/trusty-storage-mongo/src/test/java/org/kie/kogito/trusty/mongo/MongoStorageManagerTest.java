package org.kie.kogito.trusty.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.trusty.storage.mongo.MongoStorageManager;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

public class MongoStorageManagerTest {

    @Test
    public void GivenObjectToStore_WhenCreateIsCalled_ThenTheObjectIsStored() {
        MongoDatabase db = Mockito.mock(MongoDatabase.class);
        MongoCollection collection = Mockito.mock(MongoCollection.class);

        Mockito.when(collection.insertOne(any(Object.class))).thenReturn(null);

        Mockito.when(db.getCollection(any(String.class), any(Class.class))).thenReturn(collection);

        MongoStorageManager storageManager = new MongoStorageManager(db);

        Assertions.assertTrue(storageManager.create("key", new Object(), "index"));
    }
}
