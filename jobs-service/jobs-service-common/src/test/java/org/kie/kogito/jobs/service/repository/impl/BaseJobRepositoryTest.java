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
package org.kie.kogito.jobs.service.repository.impl;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.jobs.service.api.recipient.http.HttpRecipient;
import org.kie.kogito.jobs.service.api.recipient.http.HttpRecipientStringPayloadData;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.JobStatus;
import org.kie.kogito.jobs.service.model.Recipient;
import org.kie.kogito.jobs.service.model.RecipientInstance;
import org.kie.kogito.jobs.service.repository.JobRepository;
import org.kie.kogito.jobs.service.stream.JobEventPublisher;
import org.kie.kogito.jobs.service.utils.DateUtil;
import org.kie.kogito.timer.impl.PointInTimeTrigger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.kogito.jobs.service.repository.JobRepository.SortTerm.byFireTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

public abstract class BaseJobRepositoryTest {

    public static final String ID = UUID.randomUUID().toString();

    private JobDetails job;

    @BeforeEach
    public void setUp() throws Exception {
        createAndSaveJob(ID);
    }

    @AfterEach
    public void tearDown() throws Exception {
        tested().delete(ID);
    }

    public JobEventPublisher mockJobEventPublisher() {
        final JobEventPublisher mock = mock(JobEventPublisher.class);
        lenient().when(mock.publishJobStatusChange(any(JobDetails.class))).thenAnswer(a -> a.getArgument(0));
        return mock;
    }

    public abstract JobRepository tested();

    @Test
    void testSaveAndGet() throws ExecutionException, InterruptedException {
        JobDetails scheduledJob = tested().get(ID);
        assertEqualsToReturnedJob(job, scheduledJob);
        JobDetails notFound = tested().get(UUID.randomUUID().toString());
        assertThat(notFound).isNull();
    }

    private void createAndSaveJob(String id) throws Exception {
        job = JobDetails.builder()
                .id(id)
                .trigger(new PointInTimeTrigger(System.currentTimeMillis(), null, null))
                .priority(1)
                .recipient(new RecipientInstance(HttpRecipient.builder()
                        .forStringPayload().url("url")
                        .payload(HttpRecipientStringPayloadData.from("payload test"))
                        .build()))
                .build();
        tested().save(job);
    }

    @Test
    void testExists() throws ExecutionException, InterruptedException {
        Boolean exists = tested().exists(ID);
        assertThat(exists).isTrue();
        Boolean notFound = tested().exists(UUID.randomUUID().toString());
        assertThat(notFound).isFalse();
    }

    @Test
    void testDelete() throws ExecutionException, InterruptedException {
        JobDetails deletedJob = tested().delete(ID);
        assertEqualsToReturnedJob(job, deletedJob);
        JobDetails notFound = tested().get(ID);
        assertThat(notFound).isNull();
    }

    @Test
    void testFindByStatusBetweenDates() throws ExecutionException, InterruptedException {
        ZonedDateTime now = DateUtil.now();
        List<JobDetails> jobs = IntStream.rangeClosed(1, 10).boxed()
                .map(id -> JobDetails.builder()
                        .status(JobStatus.SCHEDULED)
                        .id(String.valueOf(id))
                        .priority(id)
                        .trigger(new PointInTimeTrigger(now.plusMinutes(id).toInstant().toEpochMilli(), null, null))
                        .priority(id)
                        .build())
                .map(j -> tested().save(j))
                .toList();

        List<JobDetails> fetched = tested().findByStatusBetweenDates(now,
                now.plusMinutes(5),
                new JobStatus[] { JobStatus.SCHEDULED },
                new JobRepository.SortTerm[] { byFireTime(true) });

        assertThat(fetched.size()).isEqualTo(5);

        for (int i = 0; i < fetched.size(); i++) {
            assertThat(fetched.get(i)).isEqualTo(jobs.get(i));
        }

        List<JobDetails> fetchedDesc = tested().findByStatusBetweenDates(now,
                now.plusMinutes(5),
                new JobStatus[] { JobStatus.SCHEDULED },
                new JobRepository.SortTerm[] { byFireTime(false) });

        assertThat(fetchedDesc.size()).isEqualTo(5);
        for (int i = 0; i < fetchedDesc.size(); i++) {
            assertThat(fetchedDesc.get(i)).isEqualTo(jobs.get(4 - i));
        }

        //not found test
        List<JobDetails> fetchedNotFound = tested().findByStatusBetweenDates(now,
                now.plusMinutes(5),
                new JobStatus[] { JobStatus.CANCELED },
                new JobRepository.SortTerm[] { byFireTime(true) });

        assertThat(fetchedNotFound.size()).isZero();

        fetchedNotFound = tested().findByStatusBetweenDates(now.plusMinutes(10).plusSeconds(1),
                now.plusMinutes(20),
                new JobStatus[] { JobStatus.SCHEDULED },
                new JobRepository.SortTerm[] { byFireTime(true) });

        assertThat(fetchedNotFound.size()).isZero();
    }

    @Test
    void testMergeCallbackEndpoint() throws Exception {
        String id = UUID.randomUUID().toString();
        createAndSaveJob(id);
        final String newCallbackEndpoint = "http://localhost/newcallback";
        final Recipient recipient = new RecipientInstance(HttpRecipient.builder().forStringPayload().url(newCallbackEndpoint).build());
        final JobDetails toMerge = JobDetails.builder()
                .id(id)
                .recipient(recipient)
                .build();

        JobDetails merged = tested().merge(id, toMerge);
        assertThat(merged.getRecipient()).isEqualTo(recipient);
        assertThat(merged.getId()).isEqualTo(job.getId());
        assertThat(merged.getTrigger().hasNextFireTime()).isEqualTo(job.getTrigger().hasNextFireTime());
        tested().delete(id);
    }

    private static void assertEqualsToReturnedJob(JobDetails job, JobDetails returnedJob) {
        assertThat(job.getId()).isEqualTo(returnedJob.getId());
        assertThat(job.getCorrelationId()).isEqualTo(returnedJob.getCorrelationId());
        assertThat(job.getStatus()).isEqualTo(returnedJob.getStatus());
        assertThat(job.getScheduledId()).isEqualTo(returnedJob.getScheduledId());
        assertThat(job.getRetries()).isEqualTo(returnedJob.getRetries());
        assertThat(job.getExecutionCounter()).isEqualTo(returnedJob.getExecutionCounter());
        assertThat(job.getExecutionTimeout()).isEqualTo(returnedJob.getExecutionTimeout());
        assertThat(job.getExecutionTimeoutUnit()).isEqualTo(returnedJob.getExecutionTimeoutUnit());
        assertThat(job.getTrigger().hasNextFireTime()).isEqualTo(returnedJob.getTrigger().hasNextFireTime());
        assertThat(job.getRecipient()).isEqualTo(returnedJob.getRecipient());
        assertThat(returnedJob.getCreated()).isNotNull();
        assertThat(returnedJob.getLastUpdate()).isNotNull();
    }
}
