package org.kie.kogito.index.mongodb.query;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.kie.kogito.index.mongodb.model.ProcessIdEntity;
import org.kie.kogito.index.mongodb.model.ProcessIdEntityMapper;
import org.kie.kogito.index.test.query.AbstractProcessIdQueryIT;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.mongodb.client.MongoClientManager;
import org.kie.kogito.persistence.mongodb.storage.MongoStorage;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static org.kie.kogito.index.storage.Constants.PROCESS_ID_MODEL_STORAGE;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class ProcessIdQueryIT extends AbstractProcessIdQueryIT {

    @Inject
    MongoClientManager mongoClientManager;

    Storage<String, String> storage;

    @BeforeEach
    void setUp() {
        storage = new MongoStorage<>(mongoClientManager.getCollection(PROCESS_ID_MODEL_STORAGE, ProcessIdEntity.class),
                String.class.getName(), new ProcessIdEntityMapper());
        storage.clear();
    }

    @Override
    public Storage<String, String> getStorage() {
        return storage;
    }
}
