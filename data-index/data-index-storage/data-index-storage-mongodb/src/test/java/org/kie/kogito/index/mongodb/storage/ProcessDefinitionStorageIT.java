package org.kie.kogito.index.mongodb.storage;

import java.util.Set;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.mongodb.model.ProcessDefinitionEntity;
import org.kie.kogito.index.mongodb.model.ProcessDefinitionEntityMapper;
import org.kie.kogito.index.test.TestUtils;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.mongodb.client.MongoClientManager;
import org.kie.kogito.persistence.mongodb.storage.MongoStorage;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static org.kie.kogito.index.storage.Constants.PROCESS_DEFINITIONS_STORAGE;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class ProcessDefinitionStorageIT extends StorageTestBase<String, ProcessDefinition> {

    @Inject
    MongoClientManager mongoClientManager;

    Storage<String, ProcessDefinition> storage;

    @BeforeEach
    void setUp() {
        this.storage = new MongoStorage<>(mongoClientManager.getCollection(PROCESS_DEFINITIONS_STORAGE, ProcessDefinitionEntity.class),
                ProcessDefinition.class.getName(), new ProcessDefinitionEntityMapper());
    }

    @AfterEach
    void tearDown() {
        storage.clear();
    }

    @Test
    void testCache() {
        String processId = "travels";
        String version = "1.0";
        ProcessDefinition pdv1 = TestUtils.createProcessDefinition(processId, version, Set.of("admin", "kogito"));
        ProcessDefinition pdv2 = TestUtils.createProcessDefinition(processId, version, Set.of("kogito"));
        testStorage(storage, pdv1.getKey(), pdv1, pdv2);
    }
}
