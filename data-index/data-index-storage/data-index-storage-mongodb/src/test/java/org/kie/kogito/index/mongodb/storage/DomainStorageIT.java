package org.kie.kogito.index.mongodb.storage;

import java.util.UUID;

import javax.inject.Inject;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.mongodb.model.DomainEntityMapper;
import org.kie.kogito.index.test.TestUtils;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.mongodb.client.MongoClientManager;
import org.kie.kogito.persistence.mongodb.storage.MongoStorage;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class DomainStorageIT extends StorageTestBase<String, ObjectNode> {

    @Inject
    MongoClientManager mongoClientManager;

    Storage<String, ObjectNode> storage;

    @BeforeEach
    void setUp() {
        this.storage = new MongoStorage<>(mongoClientManager.getCollection("travels_domain", Document.class),
                "org.acme.travels.travels.Travels", new DomainEntityMapper());
    }

    @AfterEach
    void tearDown() {
        storage.clear();
    }

    @Test
    void testCache() {
        String processInstanceId = UUID.randomUUID().toString();
        ObjectNode node1 = TestUtils.createDomainData(processInstanceId, "John", "Doe");
        ObjectNode node2 = TestUtils.createDomainData(processInstanceId, "Jane", "Toe");
        testStorage(storage, processInstanceId, node1, node2);
    }
}
