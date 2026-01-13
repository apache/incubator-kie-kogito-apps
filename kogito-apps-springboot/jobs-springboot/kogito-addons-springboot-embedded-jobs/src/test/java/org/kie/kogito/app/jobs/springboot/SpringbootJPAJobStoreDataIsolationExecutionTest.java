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
package org.kie.kogito.app.jobs.springboot;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.jobs.ExactExpirationTime;
import org.kie.kogito.jobs.JobsService;
import org.kie.kogito.jobs.descriptors.ProcessInstanceJobDescription;
import org.kie.kogito.process.Processes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import jakarta.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test class for verifying data isolation in job execution when Processes bean is available.
 * Tests end-to-end that only jobs for locally deployed process IDs are actually executed.
 *
 * Uses @MockitoBean to provide a mock Processes bean that only affects this test class.
 */
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SpringbootJPAJobStoreDataIsolationExecutionTest {

    @MockitoBean
    Processes processes;

    @Autowired
    JobsService jobsService;

    @Autowired
    TestJobSchedulerListener listener;

    @Autowired
    TestJobExecutor testJobExecutor;

    @Autowired
    TestExceptionHandler exceptionHandler;

    @Autowired
    EntityManager entityManager;

    private static final Set<String> LOCAL_PROCESS_IDS = Set.of("localProcess1", "localProcess2");

    @BeforeEach
    public void setup() {
        testJobExecutor.reset();
        exceptionHandler.reset();

        // Configure the mock Processes bean to return local process IDs
        when(processes.processIds()).thenReturn(LOCAL_PROCESS_IDS);
        when(processes.processById(org.mockito.ArgumentMatchers.anyString())).thenReturn(null);
        ((SpringbootJobsService) jobsService).init();
    }

    @AfterEach
    public void cleanup() {
        ((SpringbootJobsService) jobsService).destroy();
    }

    /**
     * Helper method to schedule a remote job, simulating a job created by another business service.
     * Uses scheduleJob() which commits immediately, making the job visible to the scheduler.
     */
    void scheduleRemoteJob(String jobId, String processId) {
        ProcessInstanceJobDescription remoteJob = new ProcessInstanceJobDescription(
                jobId,
                "-1",
                ExactExpirationTime.of(Instant.now().plus(Duration.ofSeconds(10)).atZone(ZoneId.of("UTC"))),
                5,
                "remoteProcessInstanceId",
                null,
                processId,
                null,
                "remoteNodeInstanceId");
        jobsService.scheduleJob(remoteJob);
    }

    @Test
    public void testLocalJobIsExecuted() throws Exception {
        // Schedule a job for a local process using jobsService
        ProcessInstanceJobDescription localJob = new ProcessInstanceJobDescription(
                "local-job-1",
                "-1",
                ExactExpirationTime.of(Instant.now().plus(Duration.ofSeconds(2)).atZone(ZoneId.of("UTC"))),
                5,
                "processInstanceId1",
                null,
                "localProcess1", // This is in our local process IDs
                null,
                "nodeInstanceId1");

        listener.setCount(1);
        jobsService.scheduleJob(localJob);

        // Job should be executed because it belongs to a local process
        assertThat(listener.await(25, TimeUnit.SECONDS)).isTrue();
    }

    @Test
    public void testRemoteJobIsNotExecuted() throws Exception {
        // Schedule a remote job (simulating another business service)
        scheduleRemoteJob("remote-job-1", "remoteProcess1");

        // Also schedule a local job to verify the scheduler is running
        ProcessInstanceJobDescription localJob = new ProcessInstanceJobDescription(
                "local-job-verify",
                "-1",
                ExactExpirationTime.of(Instant.now().plus(Duration.ofSeconds(2)).atZone(ZoneId.of("UTC"))),
                5,
                "processInstanceId2",
                null,
                "localProcess1",
                null,
                "nodeInstanceId2");

        listener.setCount(1);
        jobsService.scheduleJob(localJob);

        // Wait for job to execute
        assertThat(listener.await(25, TimeUnit.SECONDS)).isTrue();

        // Verify exactly 1 job was executed and it's the local one
        Set<String> executedJobIds = listener.getExecutedJobIds();
        assertThat(executedJobIds)
                .hasSize(1)
                .containsExactly("local-job-verify")
                .doesNotContain("remote-job-1");
    }

    @Test
    public void testMixedJobsOnlyLocalExecuted() throws Exception {
        // Schedule remote jobs (simulating other business services)
        scheduleRemoteJob("remote-job-2", "remoteProcess2");
        scheduleRemoteJob("remote-job-3", "remoteProcess3");

        // Schedule local jobs
        ProcessInstanceJobDescription localJob1 = new ProcessInstanceJobDescription(
                "local-job-2",
                "-1",
                ExactExpirationTime.of(Instant.now().plus(Duration.ofSeconds(2)).atZone(ZoneId.of("UTC"))),
                5,
                "processInstanceId3",
                null,
                "localProcess1",
                null,
                "nodeInstanceId3");

        ProcessInstanceJobDescription localJob2 = new ProcessInstanceJobDescription(
                "local-job-3",
                "-1",
                ExactExpirationTime.of(Instant.now().plus(Duration.ofSeconds(2)).atZone(ZoneId.of("UTC"))),
                5,
                "processInstanceId4",
                null,
                "localProcess2",
                null,
                "nodeInstanceId4");

        // Expect only 2 local jobs to be executed
        listener.setCount(2);
        jobsService.scheduleJob(localJob1);
        jobsService.scheduleJob(localJob2);

        // Wait for jobs to execute
        assertThat(listener.await(25, TimeUnit.SECONDS)).isTrue();

        // Verify exactly 2 jobs were executed and they are the local ones
        Set<String> executedJobIds = listener.getExecutedJobIds();
        assertThat(executedJobIds)
                .hasSize(2)
                .containsExactlyInAnyOrder("local-job-2", "local-job-3")
                .doesNotContain("remote-job-2", "remote-job-3");
    }
}
