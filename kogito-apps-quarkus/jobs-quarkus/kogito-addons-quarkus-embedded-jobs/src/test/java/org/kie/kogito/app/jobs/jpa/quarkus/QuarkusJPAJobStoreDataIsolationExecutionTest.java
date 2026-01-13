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
package org.kie.kogito.app.jobs.jpa.quarkus;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.Model;
import org.kie.kogito.app.jobs.quarkus.QuarkusJobsService;
import org.kie.kogito.jobs.ExactExpirationTime;
import org.kie.kogito.jobs.JobsService;
import org.kie.kogito.jobs.descriptors.ProcessInstanceJobDescription;
import org.kie.kogito.process.Process;
import org.kie.kogito.process.Processes;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for verifying data isolation in job execution when Processes bean is available.
 * Tests that jobs are executed when their process ID matches a locally deployed process.
 */
@QuarkusTest
@TestProfile(QuarkusJPAJobStoreDataIsolationExecutionTest.DataIsolationProfile.class)
public class QuarkusJPAJobStoreDataIsolationExecutionTest {

    @Inject
    JobsService jobsService;

    @Inject
    TestJobSchedulerListener listener;

    @Inject
    TestJobExecutor testJobExecutor;

    @Inject
    TestExceptionHandler exceptionHandler;

    @Inject
    EntityManager entityManager;

    private static final Set<String> LOCAL_PROCESS_IDS = Set.of("localProcess1", "localProcess2");

    /**
     * Mock Processes bean that simulates locally deployed processes.
     */
    @Alternative
    @ApplicationScoped
    public static class MockProcesses implements Processes {

        @Override
        public Collection<String> processIds() {
            return LOCAL_PROCESS_IDS;
        }

        @Override
        public Process<? extends Model> processById(String processId) {
            return null;
        }
    }

    /**
     * Test profile that enables the mock alternatives
     */
    public static class DataIsolationProfile implements QuarkusTestProfile {
        @Override
        public Set<Class<?>> getEnabledAlternatives() {
            return Set.of(MockProcesses.class);
        }
    }

    @BeforeEach
    public void setup() {
        ((QuarkusJobsService) jobsService).init();
        testJobExecutor.reset();
        exceptionHandler.reset();
    }

    @AfterEach
    @Transactional
    public void cleanup() {
        ((QuarkusJobsService) jobsService).destroy();

        // Clean up database after each test to ensure test isolation
        entityManager.createQuery("DELETE FROM JobDetailsEntity").executeUpdate();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testJobWithLocalProcessIdIsExecuted() throws Exception {
        // Schedule a job for a local process
        ProcessInstanceJobDescription localJob = new ProcessInstanceJobDescription(
                "local-job-1",
                "-1",
                ExactExpirationTime.of(Instant.now().plus(Duration.ofSeconds(2)).atZone(ZoneId.of("UTC"))),
                5,
                "processInstanceId1",
                null,
                "localProcess1", // This matches our local process IDs
                null,
                "nodeInstanceId1");

        listener.setCount(1);
        jobsService.scheduleJob(localJob);

        // Job should be executed because it belongs to a local process
        assertThat(listener.await(5, TimeUnit.SECONDS)).isTrue();
        assertThat(listener.getExecutedJobIds()).containsExactly("local-job-1");
    }

    @Test
    public void testJobWithRemoteProcessIdIsNotExecuted() throws Exception {
        // Schedule a job for a remote process (not in LOCAL_PROCESS_IDS)
        ProcessInstanceJobDescription remoteJob = new ProcessInstanceJobDescription(
                "remote-job-1",
                "-1",
                ExactExpirationTime.of(Instant.now().plus(Duration.ofSeconds(2)).atZone(ZoneId.of("UTC"))),
                5,
                "processInstanceId2",
                null,
                "remoteProcess1", // This does NOT match our local process IDs
                null,
                "nodeInstanceId2");

        listener.setCount(1);
        jobsService.scheduleJob(remoteJob);

        // Job should NOT be executed because it belongs to a remote process
        assertThat(listener.await(5, TimeUnit.SECONDS)).isFalse();
        assertThat(listener.getExecutedJobIds()).isEmpty();
    }

    @Test
    public void testMixedLocalAndRemoteJobs() throws Exception {
        // Schedule both local and remote jobs
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

        ProcessInstanceJobDescription remoteJob = new ProcessInstanceJobDescription(
                "remote-job-2",
                "-1",
                ExactExpirationTime.of(Instant.now().plus(Duration.ofSeconds(2)).atZone(ZoneId.of("UTC"))),
                5,
                "processInstanceId5",
                null,
                "remoteProcess2",
                null,
                "nodeInstanceId5");

        // Expect only the 2 local jobs to be executed
        listener.setCount(2);
        jobsService.scheduleJob(localJob1);
        jobsService.scheduleJob(localJob2);
        jobsService.scheduleJob(remoteJob);

        // Only local jobs should be executed
        assertThat(listener.await(5, TimeUnit.SECONDS)).isTrue();
        assertThat(listener.getExecutedJobIds()).containsExactlyInAnyOrder("local-job-2", "local-job-3");
    }
}
