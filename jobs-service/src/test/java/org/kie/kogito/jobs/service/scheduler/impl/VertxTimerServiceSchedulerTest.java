/*
 *  Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.kie.kogito.jobs.service.scheduler.impl;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import io.vertx.mutiny.core.Vertx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.kogito.jobs.service.executor.HttpJobExecutor;
import org.kie.kogito.jobs.service.model.job.HttpJob;
import org.kie.kogito.jobs.service.model.job.HttpJobContext;
import org.kie.kogito.jobs.service.model.job.JobDetails;
import org.kie.kogito.jobs.service.model.job.ManageableJobHandle;
import org.kie.kogito.jobs.service.utils.DateUtil;
import org.kie.kogito.timer.Job;
import org.kie.kogito.timer.JobContext;
import org.kie.kogito.timer.Trigger;
import org.kie.kogito.timer.impl.PointInTimeTrigger;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VertxTimerServiceSchedulerTest {

    private VertxTimerServiceScheduler tested;
    private Job job;
    private JobContext context;
    private Trigger trigger;
    private JobDetails jobDetails;

    @Mock
    private HttpJobExecutor executor;

    @Captor
    private ArgumentCaptor<CompletionStage<JobDetails>> jobCaptor;

    @Captor
    private ArgumentCaptor<Long> timeCaptor;

    @Spy
    private Vertx vertx = Vertx.vertx();

    @BeforeEach
    public void setUp() {
        tested = new VertxTimerServiceScheduler(vertx);
    }

    @Test
    void testScheduleJob() {
        final long timestamp = DateUtil.now().plusSeconds(1).toInstant().toEpochMilli();
        trigger = new PointInTimeTrigger(timestamp, null, null);
        jobDetails = JobDetails.builder().build();
        context = new HttpJobContext(jobDetails);
        job = new HttpJob(executor);
        final ManageableJobHandle handle = tested.scheduleJob(job, context, trigger);
        verify(vertx).setTimer(timeCaptor.capture(), any());
        assertThat(timeCaptor.getValue()).isGreaterThan(timestamp - System.currentTimeMillis());
        given().await()
                .atMost(2, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(executor).execute(jobCaptor.capture()));
        assertThat(jobCaptor.getValue().toCompletableFuture().getNow(null)).isEqualTo(jobDetails);
        assertThat(handle.getId()).isNotNull();
        assertThat(handle.getScheduledTime()).isNotNull();
    }
}