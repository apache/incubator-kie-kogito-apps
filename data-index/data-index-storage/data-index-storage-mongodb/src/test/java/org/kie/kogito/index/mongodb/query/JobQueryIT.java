/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.index.mongodb.query;

import java.util.UUID;

import javax.inject.Inject;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.mongodb.TestUtils;
import org.kie.kogito.index.mongodb.model.JobEntity;
import org.kie.kogito.index.mongodb.model.JobEntityMapper;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.query.SortDirection;
import org.kie.kogito.persistence.mongodb.MongoServerTestResource;
import org.kie.kogito.persistence.mongodb.client.MongoClientManager;
import org.kie.kogito.persistence.mongodb.storage.MongoStorage;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.kie.kogito.index.Constants.JOBS_STORAGE;
import static org.kie.kogito.index.mongodb.query.QueryTestUtils.assertWithId;
import static org.kie.kogito.index.mongodb.query.QueryTestUtils.assertWithIdInOrder;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.and;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.between;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.contains;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.containsAll;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.containsAny;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.equalTo;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.greaterThan;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.greaterThanEqual;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.in;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.isNull;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.lessThan;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.lessThanEqual;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.like;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.notNull;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.or;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.orderBy;

@QuarkusTest
@QuarkusTestResource(MongoServerTestResource.class)
class JobQueryIT extends QueryTestBase<String, Job> {

    @Inject
    MongoClientManager mongoClientManager;

    Storage<String, Job> storage;

    @BeforeEach
    void setUp() {
        this.storage = new MongoStorage<>(mongoClientManager.getCollection(JOBS_STORAGE, JobEntity.class),
                                          mongoClientManager.getReactiveCollection(JOBS_STORAGE, JobEntity.class),
                                          Job.class.getName(), new JobEntityMapper());
    }

    @AfterEach
    void tearDown() {
        storage.clear();
    }

    @Test
    void test() {
        String jobId1 = UUID.randomUUID().toString();
        String processInstanceId1 = UUID.randomUUID().toString();
        String jobId2 = UUID.randomUUID().toString();
        String processInstanceId2 = UUID.randomUUID().toString();

        Job job1 = TestUtils.createJob(jobId1, processInstanceId1, RandomStringUtils.randomAlphabetic(5), UUID.randomUUID().toString(), RandomStringUtils.randomAlphabetic(10), "EXPECTED", 0L);
        Job job2 = TestUtils.createJob(jobId2, processInstanceId2, RandomStringUtils.randomAlphabetic(5), null, null, "SCHEDULED", 1000L);
        storage.put(jobId1, job1);
        storage.put(jobId2, job2);

        queryAndAssert(assertWithId(), storage, singletonList(in("status", asList("EXPECTED", "SCHEDULED"))), null, null, null, jobId1, jobId2);
        queryAndAssert(assertWithId(), storage, singletonList(equalTo("status", "EXPECTED")), null, null, null, jobId1);
        queryAndAssert(assertWithId(), storage, singletonList(greaterThan("priority", 1)), null, null, null);
        queryAndAssert(assertWithId(), storage, singletonList(greaterThanEqual("priority", 1)), null, null, null, jobId1, jobId2);
        queryAndAssert(assertWithId(), storage, singletonList(lessThan("priority", 1)), null, null, null);
        queryAndAssert(assertWithId(), storage, singletonList(lessThanEqual("priority", 1)), null, null, null, jobId1, jobId2);
        queryAndAssert(assertWithId(), storage, singletonList(between("priority", 0, 3)), null, null, null, jobId1, jobId2);
        queryAndAssert(assertWithId(), storage, singletonList(isNull("rootProcessInstanceId")), null, null, null, jobId2);
        queryAndAssert(assertWithId(), storage, singletonList(notNull("rootProcessInstanceId")), null, null, null, jobId1);
        queryAndAssert(assertWithId(), storage, singletonList(contains("id", jobId1)), null, null, null, jobId1);
        queryAndAssert(assertWithId(), storage, singletonList(containsAny("processInstanceId", asList(processInstanceId1, processInstanceId2))), null, null, null, jobId1, jobId2);
        queryAndAssert(assertWithId(), storage, singletonList(containsAll("processInstanceId", asList(processInstanceId1, processInstanceId2))), null, null, null);
        queryAndAssert(assertWithId(), storage, singletonList(like("status", "EX*")), null, null, null, jobId1);
        queryAndAssert(assertWithId(), storage, singletonList(and(asList(lessThan("retries", 11), greaterThanEqual("retries", 10)))), null, null, null, jobId1, jobId2);
        queryAndAssert(assertWithId(), storage, singletonList(or(asList(equalTo("id", jobId1), equalTo("id", jobId2)))), null, null, null, jobId1, jobId2);
        queryAndAssert(assertWithId(), storage, asList(equalTo("id", jobId1), equalTo("processInstanceId", processInstanceId2)), null, null, null);

        queryAndAssert(assertWithIdInOrder(), storage, asList(in("id", asList(jobId1, jobId2)), in("processInstanceId", asList(processInstanceId1, processInstanceId2))), singletonList(orderBy("status", SortDirection.ASC)), 1, 1, jobId2);
        queryAndAssert(assertWithIdInOrder(), storage, null, singletonList(orderBy("status", SortDirection.DESC)), null, null, jobId2, jobId1);
        queryAndAssert(assertWithIdInOrder(), storage, null, null, 1, 1, jobId2);
        queryAndAssert(assertWithIdInOrder(), storage, null, asList(orderBy("status", SortDirection.ASC), orderBy("priority", SortDirection.ASC)), 1, 1, jobId2);
    }
}
