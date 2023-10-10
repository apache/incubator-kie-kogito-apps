package org.kie.kogito.index.mongodb.query;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.mongodb.model.UserTaskInstanceEntity;
import org.kie.kogito.index.mongodb.model.UserTaskInstanceEntityMapper;
import org.kie.kogito.index.test.query.AbstractUserTaskInstanceQueryIT;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.mongodb.client.MongoClientManager;
import org.kie.kogito.persistence.mongodb.storage.MongoStorage;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static org.kie.kogito.index.storage.Constants.USER_TASK_INSTANCES_STORAGE;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class UserTaskInstanceQueryIT extends AbstractUserTaskInstanceQueryIT {

    @Inject
    MongoClientManager mongoClientManager;

    Storage<String, UserTaskInstance> storage;

    @BeforeEach
    void setUp() {
        this.storage = new MongoStorage<>(mongoClientManager.getCollection(USER_TASK_INSTANCES_STORAGE, UserTaskInstanceEntity.class),
                UserTaskInstance.class.getName(), new UserTaskInstanceEntityMapper());
    }

    @Override
    public Storage<String, UserTaskInstance> getStorage() {
        return storage;
    }

}
