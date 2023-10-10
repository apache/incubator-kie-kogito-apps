package org.kie.kogito.index.oracle.storage;

import java.util.UUID;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.oracle.model.JobEntity;
import org.kie.kogito.index.oracle.model.JobEntityRepository;
import org.kie.kogito.index.test.TestUtils;
import org.kie.kogito.persistence.api.StorageService;
import org.kie.kogito.testcontainers.quarkus.OracleSqlQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(OracleSqlQuarkusTestResource.class)
public class JobStorageIT extends AbstractStorageIT<JobEntity, Job> {

    @Inject
    JobEntityRepository repository;

    @Inject
    StorageService storage;

    public JobStorageIT() {
        super(Job.class);
    }

    @Override
    public JobEntityRepository getRepository() {
        return repository;
    }

    @Override
    public StorageService getStorage() {
        return storage;
    }

    @BeforeEach
    @Transactional
    public void setup() {
        repository.deleteAll();
    }

    @Test
    @Transactional
    public void testJobEntity() {
        String jobId = UUID.randomUUID().toString();
        String processInstanceId = UUID.randomUUID().toString();

        Job job1 = TestUtils
                .createJob(jobId, processInstanceId, RandomStringUtils.randomAlphabetic(5), UUID.randomUUID().toString(),
                        RandomStringUtils.randomAlphabetic(10), "EXPECTED", 0L);
        Job job2 = TestUtils
                .createJob(jobId, processInstanceId, RandomStringUtils.randomAlphabetic(5), UUID.randomUUID().toString(),
                        RandomStringUtils.randomAlphabetic(10), "SCHEDULED", 1000L);
        testStorage(jobId, job1, job2);
    }

}
