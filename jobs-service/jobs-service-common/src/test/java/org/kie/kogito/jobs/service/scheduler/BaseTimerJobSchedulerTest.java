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
package org.kie.kogito.jobs.service.scheduler;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.jobs.service.exception.InvalidScheduleTimeException;
import org.kie.kogito.jobs.service.exception.JobServiceException;
import org.kie.kogito.jobs.service.executor.JobExecutor;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.JobExecutionResponse;
import org.kie.kogito.jobs.service.model.JobStatus;
import org.kie.kogito.jobs.service.model.ManageableJobHandle;
import org.kie.kogito.jobs.service.repository.JobRepository;
import org.kie.kogito.jobs.service.utils.DateUtil;
import org.kie.kogito.timer.Trigger;
import org.kie.kogito.timer.impl.PointInTimeTrigger;
import org.kie.kogito.timer.impl.SimpleTimerTrigger;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.kie.kogito.jobs.service.model.JobStatus.CANCELED;
import static org.kie.kogito.jobs.service.model.JobStatus.SCHEDULED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class BaseTimerJobSchedulerTest {

    public static final String JOB_ID = UUID.randomUUID().toString();
    public static final String SCHEDULED_ID = "3";

    @Mock
    public JobExecutor jobExecutor;

    @Mock
    public JobRepository jobRepository;

    public JobDetails scheduled;

    @Captor
    private ArgumentCaptor<Trigger> delayCaptor;

    @Captor
    private ArgumentCaptor<JobDetails> scheduleCaptor;

    @Captor
    private ArgumentCaptor<JobDetails> scheduleCaptorFuture;

    public JobDetails scheduledJob;

    public JobDetails notScheduledJob;

    public JobExecutionResponse errorResponse;

    public JobExecutionResponse successResponse;

    public ZonedDateTime expirationTime;

    public Trigger trigger;

    public ZonedDateTime notExpirationTime;

    public Trigger notTrigger;

    @BeforeEach
    public void setUp() {
        tested().schedulerChunkInMinutes = 5;
        tested().forceExecuteExpiredJobs = false;
        //expiration on the current scheduler chunk
        expirationTime = DateUtil.now().plusMinutes(tested().schedulerChunkInMinutes - 1);
        notExpirationTime = DateUtil.now().plusMinutes(tested().schedulerChunkInMinutes + 1);
        errorResponse = JobExecutionResponse.builder()
                .jobId(JOB_ID)
                .message("error")
                .now()
                .build();
        successResponse = JobExecutionResponse.builder()
                .jobId(JOB_ID)
                .message("sucess")
                .now()
                .build();

        trigger = new PointInTimeTrigger(expirationTime.toInstant().toEpochMilli(), null, null);
        scheduledJob = JobDetails.builder().id(JOB_ID).trigger(trigger).status(SCHEDULED).build();

        notTrigger = new PointInTimeTrigger(notExpirationTime.toInstant().toEpochMilli(), null, null);
        notScheduledJob = JobDetails.builder().id(JOB_ID).trigger(notTrigger).status(SCHEDULED).build();

        scheduled = scheduledJob;

        lenient().when(jobRepository.get(JOB_ID)).thenReturn(scheduled);
        lenient().when(jobRepository.save(any(JobDetails.class))).thenAnswer(a -> a.getArgument(0));
        lenient().when(jobExecutor.execute(any())).thenReturn(successResponse);
    }

    public abstract AbstractTimerJobScheduler tested();

    @Test
    void testScheduleNotExistingJob() {
        JobDetails scheduleNotExistingJobDetails = JobDetails.builder().of(scheduledJob).build();
        when(jobRepository.exists(scheduleNotExistingJobDetails.getId())).thenReturn(false);
        tested().schedule(scheduleNotExistingJobDetails);
        verify(tested()).doSchedule(eq(scheduleNotExistingJobDetails), delayCaptor.capture());
        verify(jobRepository, times(2)).save(scheduleCaptor.capture());
        JobDetails scheduledJob = scheduleCaptor.getValue();
        assertThat(scheduledJob.getScheduledId()).isEqualTo(SCHEDULED_ID);
        assertThat(scheduledJob.getId()).isEqualTo(JOB_ID);
        assertThat(scheduledJob.getStatus()).isEqualTo(SCHEDULED);
    }

    @Test
    void testScheduleExistingJob() {
        testExistingJob(false, SCHEDULED);
    }

    @Test
    void testScheduleExistingJobExpired() {
        testExistingJob(true, SCHEDULED);
    }

    private void testExistingJob(boolean expired, JobStatus jobStatus) {

        JobDetails reschedule = expired ? scheduledJob : notScheduledJob;
        JobDetails toSchedule = JobDetails.builder().of(reschedule).status(jobStatus).build();
        String jobId = toSchedule.getId();

        when(jobRepository.exists(JOB_ID)).thenReturn(true);
        lenient().when(jobRepository.get(JOB_ID)).thenReturn(scheduledJob);
        lenient().when(jobRepository.delete(JOB_ID)).thenReturn(scheduledJob);
        lenient().when(jobRepository.delete(any(JobDetails.class))).thenReturn(scheduledJob);

        tested().schedule(toSchedule);

        verify(jobRepository, expired || SCHEDULED.equals(jobStatus) ? atLeastOnce() : never()).delete(any(JobDetails.class));
        verify(tested(), expired ? times(1) : never()).doSchedule(eq(reschedule), delayCaptor.capture());
        verify(jobRepository, expired ? times(2) : times(1)).save(scheduleCaptor.capture());

        //assert always a scheduled job is canceled (periodic or not)
        Optional.ofNullable(jobStatus)
                .filter(SCHEDULED::equals)
                .ifPresent(s -> {
                    verify(tested()).cancel(scheduleCaptorFuture.capture());
                    try {
                        JobDetails value = scheduleCaptorFuture.getValue();
                        assertThat(value.getId()).isEqualTo(jobId);
                        assertThat(value.getStatus()).isEqualTo(CANCELED);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        if (expired) {
            JobDetails returnedJobDetails = scheduleCaptor.getValue();
            assertThat(returnedJobDetails.getScheduledId()).isEqualTo(SCHEDULED_ID);
            assertThat(returnedJobDetails.getId()).isEqualTo(JOB_ID);
            assertThat(returnedJobDetails.getStatus()).isEqualTo(scheduledJob.getStatus());
        }
    }

    @Test
    void testScheduleExistingJobRetryExpired() {
        testExistingJob(true, JobStatus.RETRY);
    }

    @Test
    void testScheduleExistingJobRetry() {
        testExistingJob(false, SCHEDULED);
    }

    @Test
    void testScheduleExistingJobPeriodic() {
        createPeriodicJob();
        testExistingJob(false, SCHEDULED);
    }

    @Test
    void testHandleJobExecutionSuccess() {
        JobDetails handleJobExecutionSuccessJobDetails = JobDetails.builder().of(notScheduledJob).build();
        when(jobRepository.delete(handleJobExecutionSuccessJobDetails)).thenReturn(handleJobExecutionSuccessJobDetails);
        JobDetails executionSuccess = tested().handleJobExecutionSuccess(handleJobExecutionSuccessJobDetails);

        verify(jobRepository).delete(scheduleCaptorFuture.capture());
        assertEquals(handleJobExecutionSuccessJobDetails, scheduleCaptorFuture.getValue());
        assertThat(executionSuccess).extracting(JobDetails::getStatus).isEqualTo(JobStatus.EXECUTED);
    }

    @Test
    void testHandleJobExecutionSuccessPeriodicFirstExecution() {
        JobDetails handleJobExecutionSuccessPeriodicFirstExecution = createPeriodicJob();

        tested().handleJobExecutionSuccess(handleJobExecutionSuccessPeriodicFirstExecution);
        verify(tested(), never()).cancel(scheduleCaptorFuture.capture());
        verify(tested()).doSchedule(scheduleCaptor.capture(), delayCaptor.capture());
    }

    private JobDetails createPeriodicJob() {
        return JobDetails.builder()
                .id(JOB_ID)
                .trigger(new SimpleTimerTrigger(DateUtil.toDate(expirationTime.toOffsetDateTime()), 1, ChronoUnit.MILLIS, 10, null))
                .status(SCHEDULED)
                .build();
    }

    @Test
    void testHandleJobExecutionSuccessPeriodic() {
        JobDetails handleJobExecutionSuccessPeriodic = createPeriodicJob();

        tested().handleJobExecutionSuccess(handleJobExecutionSuccessPeriodic);
        verify(tested(), never()).cancel(scheduleCaptorFuture.capture());

        verify(jobRepository, times(3)).save(scheduleCaptor.capture());
        JobDetails scheduleCaptorValue = scheduleCaptor.getValue();
        assertThat(scheduleCaptorValue.getStatus()).isEqualTo(SCHEDULED);
        assertThat(scheduleCaptorValue.getExecutionCounter()).isEqualTo(1);
    }

    @Test
    void testHandleJobExecutionErrorWithRetry() {
        tested().handleJobExecutionError(errorResponse);

        verify(tested()).doSchedule(eq(scheduledJob), delayCaptor.capture());

        verify(jobRepository).save(scheduleCaptor.capture());
        JobDetails saved = scheduleCaptor.getValue();
        assertThat(saved.getStatus()).isEqualTo(JobStatus.RETRY);
    }

    @Test
    void testHandleJobExecutionErrorFinal() {
        scheduledJob = JobDetails.builder().of(scheduledJob).status(JobStatus.ERROR).build();
        when(jobRepository.get(JOB_ID)).thenReturn(scheduledJob);

        tested().handleJobExecutionError(errorResponse);

        verify(tested(), never()).doSchedule(eq(scheduledJob), delayCaptor.capture());
        verify(tested(), never()).doSchedule(eq(scheduledJob), delayCaptor.capture());
    }

    protected <T> Consumer<T> dummyCallback() {
        return t -> {
        };
    }

    @Test
    void testCancel() {
        JobDetails canceled = tested().cancel(JOB_ID);

        verify(jobRepository).get(JOB_ID);
        verify(tested()).cancel(scheduleCaptorFuture.capture());
        assertThat(canceled).extracting(JobDetails::getStatus).isEqualTo(JobStatus.CANCELED);
        assertThat(scheduleCaptorFuture.getValue().getId()).isEqualTo(JOB_ID);
    }

    @Test
    void testCancelJobDetails() {
        scheduledJob = JobDetails.builder().of(scheduledJob).status(SCHEDULED).scheduledId("1").build();
        when(tested().doCancel(scheduledJob)).thenReturn(new ManageableJobHandle(true));

        tested().cancel(scheduledJob);
        verify(tested()).doCancel(scheduledJob);
        verify(jobRepository).delete(scheduledJob);
    }

    @Test
    void testCancelNotJobDetails() {
        tested().cancel(scheduled);
        verify(tested(), never()).doCancel(scheduledJob);
        verify(jobRepository).delete(scheduledJob);
    }

    @Test
    void testScheduleOutOfCurrentChunk() {
        JobDetails testScheduleOutOfCurrentChunkJob = JobDetails.builder()
                .of(notScheduledJob)
                .build();

        when(jobRepository.exists(any())).thenReturn(Boolean.FALSE);

        tested().schedule(testScheduleOutOfCurrentChunkJob);

        verify(tested(), never()).doSchedule(eq(testScheduleOutOfCurrentChunkJob), delayCaptor.capture());
        verify(jobRepository).save(scheduleCaptor.capture());
        JobDetails current = scheduleCaptor.getValue();
        assertThat(current.getId()).isEqualTo(JOB_ID);
        assertThat(current.getStatus()).isEqualTo(SCHEDULED);
        assertThat(current.getScheduledId()).isNull();
    }

    @Test
    void testScheduleInCurrentChunk() {
        tested().schedule(scheduledJob);

        verify(tested()).doSchedule(eq(scheduledJob), delayCaptor.capture());
        verify(jobRepository, times(2)).save(scheduleCaptor.capture());
        JobDetails current = scheduleCaptor.getValue();
        assertThat(current.getId()).isEqualTo(JOB_ID);
        assertThat(current.getStatus()).isEqualTo(SCHEDULED);
        assertThat(current.getScheduledId()).isNotNull();
    }

    @Test
    void testScheduled() {
        testExistingJob(true, SCHEDULED);
        Optional<ZonedDateTime> scheduled = tested().scheduled(JOB_ID);
        assertThat(scheduled).isNotNull().isPresent();
    }

    @Test
    void testForceExpiredJobToBeExecuted() {
        when(jobRepository.exists(any())).thenReturn(Boolean.FALSE);

        JobDetails forceExpiredJobDetails = scheduledJob = JobDetails.builder()
                .of(scheduledJob)
                .trigger(new SimpleTimerTrigger(DateUtil.toDate(OffsetDateTime.now().minusHours(1)), 1, ChronoUnit.MILLIS, 0, null))
                .build();

        //testing with forcing disabled
        tested().forceExecuteExpiredJobs = false;
        assertThrows(InvalidScheduleTimeException.class, () -> tested().schedule(forceExpiredJobDetails));
        verify(tested(), never()).doSchedule(eq(forceExpiredJobDetails), delayCaptor.capture());

        //testing with forcing enabled
        tested().forceExecuteExpiredJobs = true;
        tested().schedule(forceExpiredJobDetails);
        verify(tested(), times(1)).doSchedule(eq(scheduledJob), delayCaptor.capture());
    }

    @Test
    void testRescheduleAndMerge() {
        ZonedDateTime newTime = DateUtil.now().plusMinutes(1);
        PointInTimeTrigger newTrigger = new PointInTimeTrigger(newTime.toInstant().toEpochMilli(), null, null);
        JobDetails jobToMerge =
                JobDetails.builder()
                        .id(JOB_ID)
                        .trigger(newTrigger)
                        .build();

        when(jobRepository.get(JOB_ID)).thenReturn(scheduledJob);
        JobDetails merged = JobDetails.builder().of(scheduledJob).merge(jobToMerge).build();
        when(jobRepository.merge(JOB_ID, jobToMerge)).thenReturn(merged);

        tested().reschedule(JOB_ID, jobToMerge.getTrigger());

        verify(tested()).doCancel(merged);
        verify(tested()).schedule(merged);
    }

    @Test
    void handleJobExecutionSuccess() throws Exception {
        scheduledJob = JobDetails.builder().id(JOB_ID).trigger(trigger).status(SCHEDULED).build();
        doReturn(scheduledJob).when(jobRepository).get(JOB_ID);
        doReturn(scheduledJob).when(jobRepository).delete(any(JobDetails.class));
        JobExecutionResponse response = new JobExecutionResponse("execution successful", "200", ZonedDateTime.now(), JOB_ID);

        JobDetails result = tested().handleJobExecutionSuccess(response);

        verify(jobRepository).delete(scheduleCaptor.capture());
        JobDetails deletedJob = scheduleCaptor.getValue();
        assertThat(deletedJob).isNotNull();
        assertThat(deletedJob.getId()).isEqualTo(JOB_ID);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(JOB_ID);
    }

    @Test
    void handleJobExecutionSuccessJobNotFound() {
        scheduledJob = JobDetails.builder().id(JOB_ID).trigger(trigger).status(SCHEDULED).build();
        when(jobRepository.get(JOB_ID)).thenReturn(null);
        JobExecutionResponse response = new JobExecutionResponse("execution successful", "200", ZonedDateTime.now(), JOB_ID);

        assertThatThrownBy(() -> tested().handleJobExecutionSuccess(response))
                .isInstanceOf(JobServiceException.class)
                .hasMessageContaining("Job: %s was not found in database.", JOB_ID);
        verify(jobRepository, never()).delete(JOB_ID);
        verify(jobRepository, never()).delete(any(JobDetails.class));
        verify(jobRepository, never()).save(any());
    }
}
