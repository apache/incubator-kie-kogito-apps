package org.kie.kogito.index.postgresql.storage;

import java.util.UUID;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.postgresql.model.UserTaskInstanceEntity;
import org.kie.kogito.index.postgresql.model.UserTaskInstanceEntityRepository;
import org.kie.kogito.index.test.TestUtils;
import org.kie.kogito.persistence.api.StorageService;
import org.kie.kogito.testcontainers.quarkus.PostgreSqlQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(PostgreSqlQuarkusTestResource.class)
public class UserTaskInstanceStorageIT extends AbstractStorageIT<UserTaskInstanceEntity, UserTaskInstance> {

    @Inject
    UserTaskInstanceEntityRepository repository;

    @Inject
    StorageService storage;

    public UserTaskInstanceStorageIT() {
        super(UserTaskInstance.class);
    }

    @Override
    public StorageService getStorage() {
        return storage;
    }

    @Override
    public UserTaskInstanceEntityRepository getRepository() {
        return repository;
    }

    @Test
    @Transactional
    public void testUserTaskInstanceEntity() {
        String taskId = UUID.randomUUID().toString();
        String processInstanceId = UUID.randomUUID().toString();
        UserTaskInstance userTaskInstance1 = TestUtils
                .createUserTaskInstance(taskId, processInstanceId, RandomStringUtils.randomAlphabetic(5),
                        UUID.randomUUID().toString(),
                        RandomStringUtils.randomAlphabetic(10), "InProgress", 0L);
        UserTaskInstance userTaskInstance2 = TestUtils
                .createUserTaskInstance(taskId, processInstanceId, RandomStringUtils.randomAlphabetic(5),
                        UUID.randomUUID().toString(),
                        RandomStringUtils.randomAlphabetic(10), "Completed", 1000L);
        testStorage(taskId, userTaskInstance1, userTaskInstance2);
    }

}
