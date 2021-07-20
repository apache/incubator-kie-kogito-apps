/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.index.postgresql.storage;

import java.util.UUID;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.postgresql.model.JobEntity;
import org.kie.kogito.index.postgresql.model.JobEntityRepository;
import org.kie.kogito.index.test.TestUtils;
import org.kie.kogito.persistence.api.StorageService;
import org.kie.kogito.testcontainers.quarkus.PostgreSqlQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(PostgreSqlQuarkusTestResource.class)
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
