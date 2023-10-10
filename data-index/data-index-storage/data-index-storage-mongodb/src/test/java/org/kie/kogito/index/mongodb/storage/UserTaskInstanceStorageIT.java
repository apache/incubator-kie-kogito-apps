package org.kie.kogito.index.mongodb.storage;

import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.mongodb.model.UserTaskInstanceEntity;
import org.kie.kogito.index.mongodb.model.UserTaskInstanceEntityMapper;
import org.kie.kogito.index.test.TestUtils;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.mongodb.client.MongoClientManager;
import org.kie.kogito.persistence.mongodb.storage.MongoStorage;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static org.kie.kogito.index.storage.Constants.USER_TASK_INSTANCES_STORAGE;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class UserTaskInstanceStorageIT extends StorageTestBase<String, UserTaskInstance> {

    @Inject
    MongoClientManager mongoClientManager;

    Storage<String, UserTaskInstance> storage;

    @BeforeEach
    void setUp() {
        this.storage = new MongoStorage<>(mongoClientManager.getCollection(USER_TASK_INSTANCES_STORAGE, UserTaskInstanceEntity.class),
                UserTaskInstance.class.getName(), new UserTaskInstanceEntityMapper());
    }

    @AfterEach
    void tearDown() {
        storage.clear();
    }

    @Test
    void testCache() {
        String taskId = UUID.randomUUID().toString();
        String processInstanceId = UUID.randomUUID().toString();
        UserTaskInstance userTaskInstance1 = TestUtils.createUserTaskInstance(taskId, processInstanceId, RandomStringUtils.randomAlphabetic(5), UUID.randomUUID().toString(),
                RandomStringUtils.randomAlphabetic(10), "InProgress", 0L);
        UserTaskInstance userTaskInstance2 = TestUtils.createUserTaskInstance(taskId, processInstanceId, RandomStringUtils.randomAlphabetic(5), UUID.randomUUID().toString(),
                RandomStringUtils.randomAlphabetic(10), "Completed", 1000L);
        testStorage(storage, taskId, userTaskInstance1, userTaskInstance2);
    }
}
