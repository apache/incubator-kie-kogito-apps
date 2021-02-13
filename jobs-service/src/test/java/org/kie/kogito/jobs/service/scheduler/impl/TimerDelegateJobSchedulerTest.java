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
package org.kie.kogito.jobs.service.scheduler.impl;

import java.util.Optional;
import java.util.UUID;

import io.reactivex.Flowable;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.kogito.jobs.service.model.JobExecutionResponse;
import org.kie.kogito.jobs.service.model.job.HttpJob;
import org.kie.kogito.jobs.service.model.job.HttpJobContext;
import org.kie.kogito.jobs.service.model.job.JobDetails;
import org.kie.kogito.jobs.service.model.job.ManageableJobHandle;
import org.kie.kogito.jobs.service.scheduler.BaseTimerJobScheduler;
import org.kie.kogito.jobs.service.scheduler.BaseTimerJobSchedulerTest;
import org.kie.kogito.jobs.service.utils.DateUtil;
import org.kie.kogito.timer.Job;
import org.kie.kogito.timer.JobContext;
import org.kie.kogito.timer.Trigger;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TimerDelegateJobSchedulerTest extends BaseTimerJobSchedulerTest {

    @Spy
    @InjectMocks
    private TimerDelegateJobScheduler tested;

    @Mock
    private VertxTimerServiceScheduler timer;

    @BeforeEach
    public void setUp() {
        super.setUp();
        ManageableJobHandle handle = new ManageableJobHandle(SCHEDULED_ID);
        handle.setScheduledTime(DateUtil.now());
        lenient().when(timer.scheduleJob(any(Job.class), any(JobContext.class), any(Trigger.class))).thenReturn(handle);
    }

    @Override
    public BaseTimerJobScheduler tested() {
        return tested;
    }

    @Test
    void testDoSchedule() {
        PublisherBuilder<ManageableJobHandle> schedule = tested.doSchedule(scheduledJob, Optional.empty());
        Flowable.fromPublisher(schedule.buildRs()).subscribe(dummyCallback(), dummyCallback());
        verify(timer).scheduleJob(any(HttpJob.class), any(HttpJobContext.class), eq(scheduledJob.getTrigger()));
    }

    @Test
    void testDoCancel() {
        Publisher<ManageableJobHandle> cancel = tested.doCancel(JobDetails.builder().of(scheduledJob).scheduledId(SCHEDULED_ID).build());
        Flowable.fromPublisher(cancel).subscribe(dummyCallback(), dummyCallback());
        verify(timer).removeJob(any(ManageableJobHandle.class));
    }

    @Test
    void testDoCancelNullId() {
        Publisher<ManageableJobHandle> cancel =
                tested.doCancel(JobDetails.builder().of(scheduledJob).scheduledId(null).build());
        Flowable.fromPublisher(cancel).subscribe(dummyCallback(), dummyCallback());
        verify(timer, never()).removeJob(any(ManageableJobHandle.class));
    }

    @Test
    void testJobSuccessProcessor() {
        JobExecutionResponse response = getJobResponse();
        doReturn(ReactiveStreams.of(JobDetails.builder().build()))
                .when(tested).handleJobExecutionSuccess(response);
        tested.jobSuccessProcessor(response).thenAccept(r -> assertThat(r).isTrue());
        verify(tested).handleJobExecutionSuccess(response);
    }

    @Test
    void testJobSuccessProcessorFail() {
        JobExecutionResponse response = getJobResponse();
        doReturn(ReactiveStreams.failed(new RuntimeException()))
                .when(tested).handleJobExecutionSuccess(response);
        tested.jobSuccessProcessor(response).thenAccept(r -> assertThat(r).isFalse());
        verify(tested).handleJobExecutionSuccess(response);
    }

    @Test
    void testJobErrorProcessor() {
        JobExecutionResponse response = getJobResponse();
        doReturn(ReactiveStreams.of(JobDetails.builder().build()))
                .when(tested).handleJobExecutionError(response);
        tested.jobErrorProcessor(response).thenAccept(r -> assertThat(r).isTrue());
        verify(tested).handleJobExecutionError(response);
    }

    @Test
    void testJobErrorProcessorFail() {
        JobExecutionResponse response = getJobResponse();
        doReturn(ReactiveStreams.failed(new RuntimeException()))
                .when(tested).handleJobExecutionError(response);
        tested.jobErrorProcessor(response).thenAccept(r -> assertThat(r).isFalse());
        verify(tested).handleJobExecutionError(response);
    }

    private JobExecutionResponse getJobResponse() {
        return JobExecutionResponse.builder()
                .jobId(UUID.randomUUID().toString())
                .message("Processing job")
                .code("123")
                .now()
                .build();
    }
}