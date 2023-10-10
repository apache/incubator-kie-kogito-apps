package org.kie.kogito.index.mongodb.query;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.mongodb.model.ProcessInstanceEntity;
import org.kie.kogito.index.mongodb.model.ProcessInstanceEntityMapper;
import org.kie.kogito.index.test.query.AbstractProcessInstanceQueryIT;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.mongodb.client.MongoClientManager;
import org.kie.kogito.persistence.mongodb.storage.MongoStorage;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static org.kie.kogito.index.storage.Constants.PROCESS_INSTANCES_STORAGE;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class ProcessInstanceQueryIT extends AbstractProcessInstanceQueryIT {

    @Inject
    MongoClientManager mongoClientManager;

    Storage<String, ProcessInstance> storage;

    @BeforeEach
    void setUp() {
        this.storage = new MongoStorage<>(mongoClientManager.getCollection(PROCESS_INSTANCES_STORAGE, ProcessInstanceEntity.class),
                ProcessInstance.class.getName(), new ProcessInstanceEntityMapper());
    }

    @Override
    public Storage<String, ProcessInstance> getStorage() {
        return storage;
    }
}
