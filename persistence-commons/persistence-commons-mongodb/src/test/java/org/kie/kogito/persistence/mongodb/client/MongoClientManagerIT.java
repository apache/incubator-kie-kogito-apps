package org.kie.kogito.persistence.mongodb.client;

import javax.inject.Inject;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import com.mongodb.client.MongoCollection;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class MongoClientManagerIT {

    @Inject
    MongoClientManager mongoClientManager;

    @Test
    void testGetCollection() {
        MongoCollection<Document> mongoCollection = mongoClientManager.getCollection("test");
        assertEquals(mongoClientManager.database, mongoCollection.getNamespace().getDatabaseName());
        assertEquals("test", mongoCollection.getNamespace().getCollectionName());
    }

    @Test
    void testGetCollection_withDocumentClass() {
        MongoCollection<TestClass> mongoCollection = mongoClientManager.getCollection("test", TestClass.class);
        assertEquals(mongoClientManager.database, mongoCollection.getNamespace().getDatabaseName());
        assertEquals("test", mongoCollection.getNamespace().getCollectionName());
        assertTrue(mongoCollection.getDocumentClass().isAssignableFrom(TestClass.class));
    }

    private static class TestClass {

    }
}
