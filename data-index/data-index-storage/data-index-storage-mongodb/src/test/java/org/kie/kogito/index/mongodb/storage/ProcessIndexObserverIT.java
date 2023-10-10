package org.kie.kogito.index.mongodb.storage;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.mongodb.mock.MockIndexCreateOrUpdateEventListener;
import org.kie.kogito.index.mongodb.model.ProcessIdEntity;
import org.kie.kogito.index.mongodb.model.ProcessIdEntityMapper;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.schema.ProcessDescriptor;
import org.kie.kogito.persistence.mongodb.client.MongoClientManager;
import org.kie.kogito.persistence.mongodb.index.ProcessIndexEvent;
import org.kie.kogito.persistence.mongodb.storage.MongoStorage;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.kie.kogito.index.storage.Constants.PROCESS_ID_MODEL_STORAGE;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class ProcessIndexObserverIT {

    @Inject
    MongoClientManager mongoClientManager;

    @Inject
    MockIndexCreateOrUpdateEventListener mockIndexCreateOrUpdateEventListener;

    @Inject
    ProcessIndexObserver processIndexObserver;

    @BeforeEach
    void setup() {
        mockIndexCreateOrUpdateEventListener.reset();
    }

    @AfterEach
    void tearDown() {
        mockIndexCreateOrUpdateEventListener.reset();
    }

    @Test
    void testOnProcessIndexEvent() {
        String processId = "testProcess";
        String processType = "testProcessType";

        ProcessDescriptor processDescriptor = new ProcessDescriptor(processId, processType);
        ProcessIndexEvent processIndexEvent = new ProcessIndexEvent(processDescriptor);

        processIndexObserver.onProcessIndexEvent(processIndexEvent);

        Storage<String, String> processIdStorage = new MongoStorage<>(mongoClientManager.getCollection(PROCESS_ID_MODEL_STORAGE, ProcessIdEntity.class),
                String.class.getName(), new ProcessIdEntityMapper());
        assertTrue(processIdStorage.containsKey(processId));
        assertEquals(processType, processIdStorage.get(processId));

        mockIndexCreateOrUpdateEventListener.assertFire("testProcess_domain", processType);
    }
}
