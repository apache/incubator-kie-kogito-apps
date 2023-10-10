package org.kie.kogito.index.mongodb.query;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.mongodb.model.ProcessDefinitionEntity;
import org.kie.kogito.index.mongodb.model.ProcessDefinitionEntityMapper;
import org.kie.kogito.index.test.query.AbstractProcessDefinitionQueryIT;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.mongodb.client.MongoClientManager;
import org.kie.kogito.persistence.mongodb.storage.MongoStorage;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static org.kie.kogito.index.storage.Constants.PROCESS_DEFINITIONS_STORAGE;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class ProcessDefinitionQueryIT extends AbstractProcessDefinitionQueryIT {

    @Inject
    MongoClientManager mongoClientManager;

    Storage<String, ProcessDefinition> storage;

    @BeforeEach
    void setUp() {
        this.storage = new MongoStorage<>(mongoClientManager.getCollection(PROCESS_DEFINITIONS_STORAGE, ProcessDefinitionEntity.class),
                ProcessDefinition.class.getName(), new ProcessDefinitionEntityMapper());
    }

    @Override
    public Storage<String, ProcessDefinition> getStorage() {
        return storage;
    }
}
