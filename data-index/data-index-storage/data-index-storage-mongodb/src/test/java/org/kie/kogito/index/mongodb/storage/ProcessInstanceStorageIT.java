package org.kie.kogito.index.mongodb.storage;

import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.ProcessInstanceState;
import org.kie.kogito.index.mongodb.model.ProcessInstanceEntity;
import org.kie.kogito.index.mongodb.model.ProcessInstanceEntityMapper;
import org.kie.kogito.index.test.TestUtils;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.mongodb.client.MongoClientManager;
import org.kie.kogito.persistence.mongodb.storage.MongoStorage;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static org.kie.kogito.index.storage.Constants.PROCESS_INSTANCES_STORAGE;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class ProcessInstanceStorageIT extends StorageTestBase<String, ProcessInstance> {

    @Inject
    MongoClientManager mongoClientManager;

    Storage<String, ProcessInstance> storage;

    @BeforeEach
    void setUp() {
        this.storage = new MongoStorage<>(mongoClientManager.getCollection(PROCESS_INSTANCES_STORAGE, ProcessInstanceEntity.class),
                ProcessInstance.class.getName(), new ProcessInstanceEntityMapper());
    }

    @AfterEach
    void tearDown() {
        storage.clear();
    }

    @Test
    void testCache() {
        String processInstanceId = UUID.randomUUID().toString();
        ProcessInstance processInstance1 = TestUtils.createProcessInstance(processInstanceId, RandomStringUtils.randomAlphabetic(5), UUID.randomUUID().toString(),
                RandomStringUtils.randomAlphabetic(10), ProcessInstanceState.ACTIVE.ordinal(), 0L);
        ProcessInstance processInstance2 = TestUtils.createProcessInstance(processInstanceId, RandomStringUtils.randomAlphabetic(5), UUID.randomUUID().toString(),
                RandomStringUtils.randomAlphabetic(10), ProcessInstanceState.COMPLETED.ordinal(), 1000L);
        testStorage(storage, processInstanceId, processInstance1, processInstance2);
    }
}
