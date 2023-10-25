/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.audit.tck;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.kogito.app.audit.api.DataAuditStoreProxyService;
import org.kie.kogito.app.audit.service.api.DataAuditStoreProxyService;
import org.kie.kogito.audit.support.DataAuditTestCompatibilityKit;
import org.kie.kogito.audit.support.EventPublisherDriver;
import org.kie.kogito.jobs.service.api.Job;
import org.kie.kogito.jobs.service.api.Job.State;
import org.kie.kogito.jobs.service.api.TemporalUnit;
import org.kie.kogito.jobs.service.api.event.JobCloudEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.kogito.audit.tck.DataAuditTestUtils.deriveNewState;
import static org.kie.kogito.audit.tck.DataAuditTestUtils.newJobEvent;

import graphql.ExecutionResult;


public class AuditJobServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditJobServiceTest.class);
    
    
    @BeforeAll
    public static void init() {

        // we populate the database with simple dataset
        DataAuditStoreProxyService proxy = DataAuditStoreProxyService.newAuditStoreSerice();

        JobCloudEvent<Job> jobEvent;
        jobEvent = newJobEvent("job1", "correlation1", State.SCHEDULED, 10L, TemporalUnit.MILLIS, null, null);
        proxy.storeJobDataEvent(jobEvent);

        jobEvent = deriveNewState(jobEvent, State.EXECUTED);
        proxy.storeJobDataEvent(jobEvent);

        jobEvent = newJobEvent("job2", "correlation2", State.SCHEDULED, 10L, TemporalUnit.MILLIS, null, null);
        proxy.storeJobDataEvent(jobEvent);

        jobEvent = newJobEvent("job3", "correlation3", State.SCHEDULED, 10L, TemporalUnit.MILLIS, null, null);
        proxy.storeJobDataEvent(jobEvent);

        jobEvent = deriveNewState(jobEvent, State.CANCELED);
        proxy.storeJobDataEvent(jobEvent);

        jobEvent = newJobEvent("job4", "correlation4", State.SCHEDULED, 10L, TemporalUnit.MILLIS, null, null);
        proxy.storeJobDataEvent(jobEvent);

        jobEvent = deriveNewState(jobEvent, State.RETRY);
        proxy.storeJobDataEvent(jobEvent);

        jobEvent = deriveNewState(jobEvent, State.EXECUTED);
        proxy.storeJobDataEvent(jobEvent);

        jobEvent = newJobEvent("job5", "correlation5", State.SCHEDULED, 10L, TemporalUnit.MILLIS, null, null);
        proxy.storeJobDataEvent(jobEvent);

        jobEvent = deriveNewState(jobEvent, State.ERROR);
        proxy.storeJobDataEvent(jobEvent);
    }


    @Test
    public void testGetAllScheduledJobs() {
        DataAuditStoreProxyService service = DataAuditStoreProxyService.newAuditQuerySerice();

        ExecutionResult executionResult = service.executeQuery("{ GetAllScheduledJobs { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp } }");
        List<Map<String, Object>> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetAllScheduledJobs"));
        LOGGER.info("Outcome {}", executionResult);
        assertThat(executionResult.getErrors()).hasSize(0);
        assertThat(data).hasSize(1);
        assertThat(data).first()
                .hasFieldOrPropertyWithValue("state", "SCHEDULED");

    }

    @Test
    public void testGetJobById() {

        DataAuditStoreProxyService service = DataAuditStoreProxyService.newAuditQuerySerice();

        ExecutionResult executionResult = service.executeQuery("{ GetJobById ( jobId : \"job1\") { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }");
        LOGGER.info("Outcome {}", executionResult);
        assertThat(executionResult.getErrors()).hasSize(0);
        List<?> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetJobById"));
        assertThat(data).hasSize(1);

    }

    @Test
    public void testGetJobHistoryById() {

        DataAuditStoreProxyService service = DataAuditStoreProxyService.newAuditQuerySerice();

        ExecutionResult executionResult =
                executionResult = service.executeQuery("{ GetJobHistoryById ( jobId : \"job4\") { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }");
        LOGGER.info("Outcome {}", executionResult);
        assertThat(executionResult.getErrors()).hasSize(0);
        List<Map<String, Object>> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetJobHistoryById"));
        assertThat(data)
                .hasSize(3)
                .allMatch(e -> "job4".equals(e.get("jobId")))
                .extracting(e -> e.get("state"))
                .containsExactlyInAnyOrder("SCHEDULED", "RETRY", "EXECUTED");

    }

    @Test
    public void testGetJobHistoryByCorrelationId() {

        DataAuditStoreProxyService service = DataAuditStoreProxyService.newAuditQuerySerice();

        ExecutionResult executionResult =
                service.executeQuery(
                        "{ GetJobHistoryByCorrelationId ( correlationId : \"correlation4\") { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }");
        LOGGER.info("Outcome {}", executionResult);
        assertThat(executionResult.getErrors()).hasSize(0);
        List<Map<String, Object>> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetJobHistoryByCorrelationId"));
        assertThat(data)
                .hasSize(3)
                .allMatch(e -> "job4".equals(e.get("jobId")))
                .allMatch(e -> "correlation4".equals(e.get("correlationId")))
                .extracting(e -> e.get("state"))
                .containsExactlyInAnyOrder("SCHEDULED", "RETRY", "EXECUTED");
    }

    @Test
    public void testGetAllPendingJobs() {

        DataAuditStoreProxyService service = DataAuditStoreProxyService.newAuditQuerySerice();

        ExecutionResult executionResult = service.executeQuery("{ GetAllPendingJobs { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }");
        LOGGER.info("Outcome {}", executionResult);
        assertThat(executionResult.getErrors()).hasSize(0);
        List data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetAllPendingJobs"));
        assertThat(data).hasSize(1);
    }

    @Test
    public void testGetAllEligibleJobsForExecution() {

        DataAuditStoreProxyService service = DataAuditStoreProxyService.newAuditQuerySerice();

        ExecutionResult executionResult = service.executeQuery("{ GetAllEligibleJobsForExecution { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }");
        LOGGER.info("Outcome {}", executionResult);
        assertThat(executionResult.getErrors()).hasSize(0);
        List data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetAllEligibleJobsForExecution"));
        assertThat(data).hasSize(1);

    }

    @Test
    public void testGetAllEligibleJobsForRetry() {

        DataAuditStoreProxyService service = DataAuditStoreProxyService.newAuditQuerySerice();
        ExecutionResult executionResult = service.executeQuery("{ GetAllEligibleJobsForRetry { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }");
        LOGGER.info("Outcome {}", executionResult);
        assertThat(executionResult.getErrors()).hasSize(0);
        List data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetAllEligibleJobsForRetry"));
        assertThat(data).hasSize(1);
    }

    @Test
    public void testGetAllJobs() {

        DataAuditStoreProxyService service = DataAuditStoreProxyService.newAuditQuerySerice();
        ExecutionResult executionResult = service.executeQuery("{ GetAllJobs { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }");
        LOGGER.info("Outcome {}", executionResult);
        assertThat(executionResult.getErrors()).hasSize(0);
        List<Map<String, Object>> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetAllJobs"));
        assertThat(data)
                .hasSize(5)
                .extracting(e -> e.get("jobId"))
                .containsExactlyInAnyOrder("job1", "job2", "job3", "job4", "job5");
    }

    @Test
    public void testGetAllCompletedJobs() {

        DataAuditStoreProxyService service = DataAuditStoreProxyService.newAuditQuerySerice();
        ExecutionResult executionResult = service.executeQuery("{ GetAllCompletedJobs { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }");
        LOGGER.info("Outcome {}", executionResult);
        assertThat(executionResult.getErrors()).hasSize(0);
        List<Map<String, Object>> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetAllCompletedJobs"));
        assertThat(data)
                .hasSize(2)
                .allMatch(e -> "EXECUTED".equals(e.get("state")))
                .extracting(e -> e.get("jobId"))
                .containsExactlyInAnyOrder("job1", "job4");
    }

    @Test
    public void testGetAllInErrorJobs() {

        DataAuditStoreProxyService service = DataAuditStoreProxyService.newAuditQuerySerice();
        ExecutionResult executionResult = service.executeQuery("{ GetAllInErrorJobs { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }");
        LOGGER.info("Outcome {}", executionResult);
        assertThat(executionResult.getErrors()).hasSize(0);
        List<Map<String, Object>> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetAllInErrorJobs"));
        assertThat(data)
                .hasSize(1)
                .allMatch(e -> "ERROR".equals(e.get("state")))
                .extracting(e -> e.get("jobId"))
                .containsExactlyInAnyOrder("job5");
    }

    @Test
    public void testGetAllJobsByState() {

        DataAuditStoreProxyService service = DataAuditStoreProxyService.newAuditQuerySerice();
        ExecutionResult executionResult =
                service.executeQuery("{ GetAllJobsByState (state : \"EXECUTED\") { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }");
        LOGGER.info("Outcome {}", executionResult);
        assertThat(executionResult.getErrors()).hasSize(0);
        List<Map<String, Object>> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetAllJobsByState"));
        assertThat(data).hasSize(2);

        assertThat(data)
                .allMatch(e -> "EXECUTED".equals(e.get("state")))
                .extracting(e -> e.get("jobId"))
                .containsExactlyInAnyOrder("job1", "job4");
    }

    @Test
    public void testGetJobByCorrelationId() {

        DataAuditStoreProxyService service = DataAuditStoreProxyService.newAuditQuerySerice();
        ExecutionResult executionResult =
                service.executeQuery("{ GetJobByCorrelationId (correlationId : \"correlation1\") { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }");
        LOGGER.info("Outcome {}", executionResult);
        assertThat(executionResult.getErrors()).hasSize(0);
        List<Map<String, Object>> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetJobByCorrelationId"));
        assertThat(data).hasSize(1);

        assertThat(data).first()
                .hasFieldOrPropertyWithValue("correlationId", "correlation1");

    }

}
