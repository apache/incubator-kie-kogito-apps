package org.kie.kogito.index.oracle.storage;

import java.util.UUID;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.ProcessInstanceState;
import org.kie.kogito.index.oracle.model.ProcessInstanceEntity;
import org.kie.kogito.index.oracle.model.ProcessInstanceEntityRepository;
import org.kie.kogito.index.test.TestUtils;
import org.kie.kogito.persistence.api.StorageService;
import org.kie.kogito.testcontainers.quarkus.OracleSqlQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(OracleSqlQuarkusTestResource.class)
public class ProcessInstanceStorageIT extends AbstractStorageIT<ProcessInstanceEntity, ProcessInstance> {

    @Inject
    ProcessInstanceEntityRepository repository;

    @Inject
    StorageService storage;

    public ProcessInstanceStorageIT() {
        super(ProcessInstance.class);
    }

    @Override
    public ProcessInstanceEntityRepository getRepository() {
        return repository;
    }

    @Override
    public StorageService getStorage() {
        return storage;
    }

    @Test
    @Transactional
    public void testProcessInstanceEntity() {
        String processInstanceId = UUID.randomUUID().toString();
        ProcessInstance processInstance1 = TestUtils
                .createProcessInstance(processInstanceId, RandomStringUtils.randomAlphabetic(5), UUID.randomUUID().toString(),
                        RandomStringUtils.randomAlphabetic(10), ProcessInstanceState.ACTIVE.ordinal(), 0L);
        ProcessInstance processInstance2 = TestUtils
                .createProcessInstance(processInstanceId, RandomStringUtils.randomAlphabetic(5), UUID.randomUUID().toString(),
                        RandomStringUtils.randomAlphabetic(10), ProcessInstanceState.COMPLETED.ordinal(), 1000L);
        testStorage(processInstanceId, processInstance1, processInstance2);
    }

}
