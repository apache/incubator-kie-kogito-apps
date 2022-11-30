/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.jobs.service.adapter;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.kogito.job.http.recipient.HTTPRecipient;
import org.kie.kogito.jobs.api.JobBuilder;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.JobDetailsBuilder;
import org.kie.kogito.jobs.service.model.JobStatus;
import org.kie.kogito.jobs.service.model.ScheduledJob;
import org.kie.kogito.jobs.service.utils.DateUtil;
import org.kie.kogito.timer.impl.IntervalTrigger;
import org.kie.kogito.timer.impl.PointInTimeTrigger;

import com.fasterxml.jackson.databind.ObjectMapper;

class ScheduledJobAdapterTest {

    public static final String ENDPOINT = "url";
    public static final String ID = UUID.randomUUID().toString();
    public static final String SCHEDULED_ID = "scheduledId";
    public static final int RETRIES = 10;
    public static final int COUNTER = 5;
    public static final ZonedDateTime LAST_UPDATE = DateUtil.now().minusMinutes(1);
    public static final int PRIORITY = 2;
    public static final JobStatus STATUS = JobStatus.SCHEDULED;
    public static final ZonedDateTime TIME = DateUtil.now().plusMinutes(5);
    public static final int REPEAT_LIMIT = 50;
    public static final long INTERVAL = 500;
    public static final String ROOT_PROCESS_INSTANCE_ID = "ID";
    public static final String ROOT_PROCESS_ID = "";
    public static final String PROCESS_ID = "ID";
    public static final String PROCESS_INSTANCE_ID = "ID";
    private static final String NODE_INSTANCE_ID = "nodeId";
    public static String payload;

    @BeforeAll
    public static void before() throws Exception {
        payload = new ObjectMapper().writeValueAsString(new ScheduledJobAdapter.ProcessPayload(PROCESS_INSTANCE_ID,
                ROOT_PROCESS_INSTANCE_ID,
                PROCESS_ID,
                ROOT_PROCESS_ID,
                NODE_INSTANCE_ID));
    }

    @Test
    void testOfJobDetailsPointInTime() {
        JobDetails jobDetails = getJobDetailsCommonBuilder()
                .trigger(new PointInTimeTrigger(TIME.toInstant().toEpochMilli(), null, null))
                .build();

        ScheduledJob scheduledJob = ScheduledJobAdapter.of(jobDetails);
        assertScheduledJob(scheduledJob);
        Assertions.assertThat(scheduledJob.getRepeatLimit()).isNull();
        Assertions.assertThat(scheduledJob.getRepeatInterval()).isNull();
    }

    @Test
    void testOfJobDetailsInterval() {
        JobDetails jobDetails = getJobDetailsCommonBuilder()
                .trigger(new IntervalTrigger(0, DateUtil.toDate(TIME), null, REPEAT_LIMIT, 0, INTERVAL, null, null))
                .build();

        ScheduledJob scheduledJob = ScheduledJobAdapter.of(jobDetails);
        assertScheduledJob(scheduledJob);
        Assertions.assertThat(scheduledJob.getRepeatLimit()).isEqualTo(REPEAT_LIMIT + 1);
        Assertions.assertThat(scheduledJob.getRepeatInterval()).isEqualTo(INTERVAL);
    }

    @Test
    void testToJobDetailsInterval() {
        JobBuilder jobBuilder = JobBuilder.builder().repeatLimit(REPEAT_LIMIT).repeatInterval(INTERVAL);
        ScheduledJob scheduledJob = getScheduledJobCommonBuilder(jobBuilder).build();

        JobDetails jobDetails = ScheduledJobAdapter.to(scheduledJob);
        assertJobDetails(jobDetails);
        Assertions.assertThat(jobDetails.getTrigger()).isInstanceOf(IntervalTrigger.class);
        IntervalTrigger intervalTrigger = (IntervalTrigger) jobDetails.getTrigger();
        Assertions.assertThat(intervalTrigger.getNextFireTime()).isEqualTo(DateUtil.toDate(TIME));
        Assertions.assertThat(intervalTrigger.getRepeatLimit()).isEqualTo(REPEAT_LIMIT);
        Assertions.assertThat(intervalTrigger.getPeriod()).isEqualTo(INTERVAL);
    }

    @Test
    void testToJobDetailsPointInTIme() {
        ScheduledJob scheduledJob = getScheduledJobCommonBuilder(JobBuilder.builder()).build();

        JobDetails jobDetails = ScheduledJobAdapter.to(scheduledJob);
        assertJobDetails(jobDetails);
        Assertions.assertThat(jobDetails.getTrigger()).isInstanceOf(PointInTimeTrigger.class);
        PointInTimeTrigger trigger = (PointInTimeTrigger) jobDetails.getTrigger();
        Assertions.assertThat(trigger.nextFireTime()).isEqualTo(DateUtil.toDate(TIME));
    }

    @Test
    void testToJobDetailsWithEmptyPayload() {
        ScheduledJob scheduledJob = ScheduledJob.builder()
                .job(JobBuilder.builder().id(UUID.randomUUID().toString()).build())
                .build();
        JobDetails jobDetails = ScheduledJobAdapter.to(scheduledJob);
        Assertions.assertThat(jobDetails.getPayload()).isNull();
    }

    @Test
    void testToScheduledJobWithEmptyPayload() {
        JobDetails jobDetails = JobDetails.builder().id(UUID.randomUUID().toString()).build();
        ScheduledJob scheduledJob = ScheduledJobAdapter.of(jobDetails);
        Assertions.assertThat(scheduledJob.getProcessId()).isNull();
        Assertions.assertThat(scheduledJob.getProcessInstanceId()).isNull();
        Assertions.assertThat(scheduledJob.getRootProcessId()).isNull();
        Assertions.assertThat(scheduledJob.getRootProcessInstanceId()).isNull();
    }

    private ScheduledJob.ScheduledJobBuilder getScheduledJobCommonBuilder(JobBuilder jobBuilder) {
        return ScheduledJob.builder()
                .job(jobBuilder
                        .id(ID)
                        .priority(PRIORITY)
                        .expirationTime(TIME)
                        .callbackEndpoint(ENDPOINT)
                        .rootProcessId(ROOT_PROCESS_ID)
                        .rootProcessInstanceId(ROOT_PROCESS_INSTANCE_ID)
                        .processId(PROCESS_ID)
                        .processInstanceId(PROCESS_INSTANCE_ID)
                        .nodeInstanceId(NODE_INSTANCE_ID)
                        .build())
                .executionCounter(COUNTER)
                .retries(RETRIES)
                .scheduledId(SCHEDULED_ID)
                .status(STATUS)
                .lastUpdate(LAST_UPDATE);
    }

    private JobDetailsBuilder getJobDetailsCommonBuilder() {
        return JobDetails.builder()
                .id(ID)
                .priority(PRIORITY)
                .recipient(new HTTPRecipient(ENDPOINT))
                .scheduledId(SCHEDULED_ID)
                .status(STATUS)
                .correlationId(ID)
                .lastUpdate(LAST_UPDATE)
                .executionCounter(COUNTER)
                .priority(PRIORITY)
                .retries(RETRIES)
                .payload(payload);
    }

    private void assertScheduledJob(ScheduledJob scheduledJob) {
        Assertions.assertThat(scheduledJob.getId()).isEqualTo(ID);
        Assertions.assertThat(scheduledJob.getScheduledId()).isEqualTo(SCHEDULED_ID);
        Assertions.assertThat(scheduledJob.getRetries()).isEqualTo(RETRIES);
        Assertions.assertThat(scheduledJob.getExecutionCounter()).isEqualTo(COUNTER);
        Assertions.assertThat(scheduledJob.getLastUpdate()).isEqualTo(LAST_UPDATE);
        Assertions.assertThat(scheduledJob.getPriority()).isEqualTo(PRIORITY);
        Assertions.assertThat(scheduledJob.getStatus()).isEqualTo(STATUS);
        Assertions.assertThat(scheduledJob.getExpirationTime()).isEqualTo(TIME);

        Assertions.assertThat(scheduledJob.getRootProcessInstanceId()).isEqualTo(ROOT_PROCESS_INSTANCE_ID);
        Assertions.assertThat(scheduledJob.getRootProcessId()).isEqualTo(ROOT_PROCESS_ID);
        Assertions.assertThat(scheduledJob.getProcessId()).isEqualTo(PROCESS_ID);
        Assertions.assertThat(scheduledJob.getProcessInstanceId()).isEqualTo(PROCESS_INSTANCE_ID);
        Assertions.assertThat(scheduledJob.getNodeInstanceId()).isEqualTo(NODE_INSTANCE_ID);

        Assertions.assertThat(scheduledJob.getCallbackEndpoint()).isEqualTo(ENDPOINT);
    }

    private void assertJobDetails(JobDetails jobDetails) {
        Assertions.assertThat(jobDetails.getId()).isEqualTo(ID);
        Assertions.assertThat(jobDetails.getScheduledId()).isEqualTo(SCHEDULED_ID);
        Assertions.assertThat(jobDetails.getExecutionCounter()).isEqualTo(COUNTER);
        Assertions.assertThat(jobDetails.getRetries()).isEqualTo(RETRIES);
        Assertions.assertThat(jobDetails.getCorrelationId()).isEqualTo(ID);
        Assertions.assertThat(jobDetails.getLastUpdate()).isEqualTo(LAST_UPDATE);
        Assertions.assertThat(jobDetails.getPriority()).isEqualTo(PRIORITY);
        Assertions.assertThat(jobDetails.getStatus()).isEqualTo(STATUS);
        Assertions.assertThat(jobDetails.getRecipient()).isInstanceOf(HTTPRecipient.class);
        Assertions.assertThat(((HTTPRecipient) jobDetails.getRecipient()).getEndpoint()).isEqualTo(ENDPOINT);
        Assertions.assertThat(jobDetails.getPayload()).isEqualTo(payload);
    }
}
