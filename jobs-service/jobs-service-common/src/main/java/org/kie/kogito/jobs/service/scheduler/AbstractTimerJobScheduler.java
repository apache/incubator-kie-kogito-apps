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

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.kie.kogito.jobs.service.exception.InvalidScheduleTimeException;
import org.kie.kogito.jobs.service.exception.JobServiceException;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.JobExecutionResponse;
import org.kie.kogito.jobs.service.model.JobStatus;
import org.kie.kogito.jobs.service.model.ManageableJobHandle;
import org.kie.kogito.jobs.service.repository.JobRepository;
import org.kie.kogito.jobs.service.stream.JobEventPublisher;
import org.kie.kogito.jobs.service.utils.DateUtil;
import org.kie.kogito.timer.Trigger;
import org.kie.kogito.timer.impl.PointInTimeTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.kie.kogito.jobs.service.utils.ModelUtil.jobWithStatus;
import static org.kie.kogito.jobs.service.utils.ModelUtil.jobWithStatusAndHandle;

/**
 * Base reactive Job Scheduler that performs the fundamental operations and let to the concrete classes to
 * implement the scheduling actions.
 */
public abstract class AbstractTimerJobScheduler implements JobScheduler<JobDetails> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTimerJobScheduler.class);

    long backoffRetryMillis;

    long maxIntervalLimitToRetryMillis;

    /**
     * Flag to allow and force a job with expirationTime in the past to be executed immediately. If false an
     * exception will be thrown.
     */
    boolean forceExecuteExpiredJobs;

    /**
     * Flag to allow that jobs that might have overdue during an eventual service shutdown should be fired at the
     * next service start.
     */
    boolean forceExecuteExpiredJobsOnServiceStart;

    /**
     * The current chunk size in minutes the scheduler handles, it is used to keep a limit number of jobs scheduled
     * in the in-memory scheduler.
     */
    long schedulerChunkInMinutes;

    private JobRepository jobRepository;

    private final Map<String, SchedulerControlRecord> schedulerControl;

    protected static class SchedulerControlRecord {
        private final String jobId;
        private final long handleId;
        private final ZonedDateTime scheduledTime;

        public SchedulerControlRecord(String jobId, long handleId, ZonedDateTime scheduledTime) {
            this.jobId = jobId;
            this.handleId = handleId;
            this.scheduledTime = scheduledTime;
        }

        public String getJobId() {
            return jobId;
        }

        public long getHandleId() {
            return handleId;
        }

        public ZonedDateTime getScheduledTime() {
            return scheduledTime;
        }
    }

    protected AbstractTimerJobScheduler() {
        this(null, 0, 0, 0, true, true);
    }

    abstract protected Optional<JobEventPublisher> getJobEventPublisher();

    protected AbstractTimerJobScheduler(JobRepository jobRepository,
            long backoffRetryMillis,
            long maxIntervalLimitToRetryMillis,
            long schedulerChunkInMinutes,
            boolean forceExecuteExpiredJobs,
            boolean forceExecuteExpiredJobsOnServiceStart) {
        this.jobRepository = jobRepository;
        this.backoffRetryMillis = backoffRetryMillis;
        this.maxIntervalLimitToRetryMillis = maxIntervalLimitToRetryMillis;
        this.schedulerControl = new ConcurrentHashMap<>();
        this.schedulerChunkInMinutes = schedulerChunkInMinutes;
        this.forceExecuteExpiredJobs = forceExecuteExpiredJobs;
        this.forceExecuteExpiredJobsOnServiceStart = forceExecuteExpiredJobsOnServiceStart;
    }

    /**
     * Executed from the API to reflect client invocations.
     */
    @Override
    public JobDetails schedule(JobDetails job) {
        LOGGER.debug("Scheduling job: {}", job);
        if (jobRepository.exists(job.getId())) {
            LOGGER.trace("Job already exists {}", job);
            jobRepository.delete(cancel(handleExistingJob(job)));
        }
        if (isOnCurrentSchedulerChunk(job)) {
            LOGGER.trace("Job {} will be scheduled right away", job);
            job = doJobScheduling(job);
        } else {
            LOGGER.trace("Job will not be scheduled {} but will be saved", job);
            JobDetails savedJob = jobRepository.save(jobWithStatus(job, JobStatus.SCHEDULED));
            getJobEventPublisher().ifPresent(p -> p.publishJobStatusChange(savedJob));
        }
        return job;
    }

    /**
     * Internal use, executed by the periodic loader only. Jobs processed by this method belongs to the current chunk.
     */
    @Override
    public JobDetails internalSchedule(JobDetails job, boolean onServiceStart) {
        LOGGER.debug("Internal Scheduling, onServiceStart: {}, job: {}", onServiceStart, job);
        if (jobRepository.exists(job.getId())) {
            return handleInternalSchedule(job, onServiceStart);
        } else {
            return handleInternalScheduleDeletedJob(job);
        }
    }

    @Override
    public JobDetails reschedule(String jobId, Trigger trigger) {
        JobDetails currentJobDetails = jobRepository.get(jobId);
        if (currentJobDetails == null) {
            return null;
        }
        LOGGER.trace("about to reschedule {} with new trigger {}", currentJobDetails, trigger);
        JobDetails rescheduleJobDetails = JobDetails.builder().id(jobId).trigger(trigger).build();
        JobDetails mergedJobDetails = jobRepository.merge(jobId, rescheduleJobDetails);
        if (mergedJobDetails == null) {
            return null;
        }

        LOGGER.trace("about to reschedule the current merge {}", currentJobDetails);
        getJobEventPublisher().ifPresent(p -> p.publishJobStatusChange(jobWithStatus(currentJobDetails, JobStatus.CANCELED)));
        this.doCancel(mergedJobDetails);

        if (this.isOnCurrentSchedulerChunk(mergedJobDetails)) {
            return schedule(mergedJobDetails);
        } else {
            JobDetails newJobDetails = this.jobRepository.save(jobWithStatus(mergedJobDetails, JobStatus.SCHEDULED));
            getJobEventPublisher().ifPresent(p -> p.publishJobStatusChange(newJobDetails));
            return newJobDetails;
        }

    }

    /**
     * Performs the given job scheduling process on the scheduler, after all the validations already made.
     */
    private JobDetails doJobScheduling(JobDetails job) {
        Date date = job.getTrigger().hasNextFireTime();
        ZonedDateTime dateTime = DateUtil.fromDate(new Date(date.getTime()));
        Duration delay = this.calculateDelay(dateTime);

        if (!forceExecuteExpiredJobs && delay.isNegative()) {
            throw new InvalidScheduleTimeException(
                    String.format("The expirationTime: %s, for job: %s should be greater than current time: %s.",
                            job.getTrigger().hasNextFireTime(), job.getId(), ZonedDateTime.now()));
        }
        LOGGER.trace("Job details before scheduling {}", job);
        JobDetails savedJobDetails = jobRepository.save(jobWithStatus(job, JobStatus.SCHEDULED));
        LOGGER.trace("Saved job details before scheduling {} in {}", savedJobDetails, jobRepository.getClass().getName());
        ManageableJobHandle manageableJobHandle = scheduleRegistering(savedJobDetails, job.getTrigger());
        JobDetails scheduledJob = jobWithStatusAndHandle(savedJobDetails, JobStatus.SCHEDULED, manageableJobHandle);
        getJobEventPublisher().ifPresent(p -> p.publishJobStatusChange(scheduledJob));
        return jobRepository.save(scheduledJob);
    }

    /**
     * Check if the job should be scheduled on the current chunk or saved to be scheduled later.
     */
    private boolean isOnCurrentSchedulerChunk(JobDetails job) {
        ZonedDateTime jobDateTime = DateUtil.fromDate(job.getTrigger().hasNextFireTime());
        ZonedDateTime maxSchedulerChunk = DateUtil.now().plusMinutes(schedulerChunkInMinutes);
        boolean isOnCurrentSchedulerChunk = jobDateTime.isBefore(maxSchedulerChunk);
        LOGGER.debug("isOnCurrentSchedulerChunk job time {} < window time {} is {} for job {}", jobDateTime, maxSchedulerChunk, isOnCurrentSchedulerChunk, job);
        return isOnCurrentSchedulerChunk;
    }

    private JobDetails handleExistingJob(JobDetails job) {
        JobDetails savedJobDetails = jobRepository.get(job.getId());
        switch (savedJobDetails.getStatus()) {
            case SCHEDULED:
            case RETRY:
                // cancel the job.
                return jobWithStatus(savedJobDetails, JobStatus.CANCELED);
            default:
                // uncommon, break the stream processing
                return null;
        }
    }

    private JobDetails handleInternalSchedule(JobDetails job, boolean onStart) {
        unregisterScheduledJob(job);
        switch (job.getStatus()) {
            case SCHEDULED:
                Duration delay = calculateRawDelay(DateUtil.fromDate(job.getTrigger().hasNextFireTime()));
                if (delay.isNegative() && onStart && !forceExecuteExpiredJobsOnServiceStart) {
                    return handleExpiredJob(job);
                } else {
                    // other cases of potential overdue are because of slow processing of the jobs service, or the user
                    // configured to fire overdue triggers at service startup. Always schedule.
                    if (job.getScheduledId() != null) {
                        // cancel the existing timer if any.
                        doCancel(job);
                    }

                    ManageableJobHandle handle = scheduleRegistering(job, job.getTrigger());
                    JobDetails scheduledJob = jobWithStatusAndHandle(job, JobStatus.SCHEDULED, handle);
                    jobRepository.save(scheduledJob);

                }
            case RETRY:
                return handleRetry(job);
            default:
                // by definition there are no more cases, only SCHEDULED and RETRY cases are picked by the loader.
                return job;
        }
    }

    private JobDetails handleInternalScheduleDeletedJob(JobDetails job) {
        LOGGER.warn("Job was removed from database: {}.", job);
        return job;
    }

    private Duration calculateDelay(ZonedDateTime expirationTime) {
        Duration delay = Duration.between(DateUtil.now(), expirationTime);
        if (!delay.isNegative()) {
            return delay;
        }
        //in case forceExecuteExpiredJobs is true, execute the job immediately.
        return forceExecuteExpiredJobs ? Duration.ofSeconds(1) : Duration.ofSeconds(-1);
    }

    private Duration calculateRawDelay(ZonedDateTime expirationTime) {
        return Duration.between(DateUtil.now(), expirationTime);
    }

    public JobDetails handleJobExecutionSuccess(JobDetails futureJob) {
        futureJob.getTrigger().nextFireTime();
        if (Objects.nonNull(futureJob.getTrigger().hasNextFireTime())) {
            JobDetails nextJobDetails = JobDetails.builder().of(futureJob).incrementExecutionCounter().status(JobStatus.SCHEDULED).build();
            JobDetails newScheduledJobDetails = doJobScheduling(nextJobDetails);
            jobRepository.save(newScheduledJobDetails);
            JobDetails excecutedJobDetails = jobWithStatus(futureJob, JobStatus.EXECUTED);
            getJobEventPublisher().ifPresent(p -> p.publishJobStatusChange(excecutedJobDetails));
            return excecutedJobDetails;
        } else {
            JobDetails deletedExecutedJobDetails = jobRepository.delete(futureJob);
            JobDetails excecutedJobDetails = JobDetails.builder().of(deletedExecutedJobDetails).incrementExecutionCounter().status(JobStatus.EXECUTED).build();
            getJobEventPublisher().ifPresent(p -> p.publishJobStatusChange(excecutedJobDetails));
            return excecutedJobDetails;
        }

    }

    @Override
    public JobDetails handleJobExecutionSuccess(JobExecutionResponse response) {
        String jobId = response.getJobId();
        Optional<JobDetails> jobDetails = this.readJob(jobId);
        return jobDetails.map(this::handleJobExecutionSuccess).orElseThrow(() -> new JobServiceException("Job: " + response.getJobId() + " was not found in database."));
    }

    private Optional<JobDetails> readJob(String jobId) {
        return Optional.ofNullable(jobRepository.get(jobId));
    }

    private boolean isExpired(ZonedDateTime expirationTime, int retries) {
        final Duration limit =
                Duration.ofMillis(maxIntervalLimitToRetryMillis)
                        .minus(Duration.ofMillis(retries * backoffRetryMillis));
        return calculateDelay(expirationTime).plus(limit).isNegative();
    }

    private JobDetails handleExpirationTime(JobDetails scheduledJob) {

        Trigger trigger = scheduledJob.getTrigger();
        Date nextFireTime = trigger.hasNextFireTime();
        ZonedDateTime dateTime = DateUtil.fromDate(nextFireTime);
        if (isExpired(dateTime, scheduledJob.getRetries())) {
            return handleExpiredJob(scheduledJob);
        } else {
            return scheduledJob;
        }

    }

    /**
     * Retries to schedule the job execution with a backoff time of {@link AbstractTimerJobScheduler#backoffRetryMillis}
     * between retries and a limit of max interval of {@link AbstractTimerJobScheduler#maxIntervalLimitToRetryMillis}
     * to retry, after this interval it the job it the job is not successfully executed it will remain in error
     * state, with no more retries.
     *
     * @param errorResponse
     * @return
     */
    @Override
    public JobDetails handleJobExecutionError(JobExecutionResponse errorResponse) {
        return handleRetry(jobRepository.get(errorResponse.getJobId()));
    }

    private JobDetails handleRetry(JobDetails futureJob) {
        try {
            JobDetails jobDetails = handleExpirationTime(futureJob);
            if (JobStatus.ERROR.equals(jobDetails.getStatus())) {
                return jobDetails;
            }
            ManageableJobHandle registeredJobHandle = scheduleRegistering(jobDetails, getRetryTrigger());

            JobDetails scheduledJobDetails = JobDetails.builder()
                    .of(jobWithStatusAndHandle(jobDetails, JobStatus.RETRY, registeredJobHandle))
                    .incrementRetries()
                    .build();

            getJobEventPublisher().ifPresent(p -> p.publishJobStatusChange(scheduledJobDetails));
            jobRepository.save(scheduledJobDetails);
            LOGGER.debug("Retry executed {}", futureJob);
            return futureJob;
        } catch (Exception ex) {
            LOGGER.error("Failed to retrieve job due to {}", ex.getMessage());
            return null;
        }

    }

    private PointInTimeTrigger getRetryTrigger() {
        return new PointInTimeTrigger(DateUtil.now().plus(backoffRetryMillis,
                ChronoUnit.MILLIS).toInstant().toEpochMilli(), null, null);
    }

    private JobDetails handleExpiredJob(JobDetails scheduledJob) {
        JobDetails errorJobDetails = jobWithStatus(scheduledJob, JobStatus.ERROR);
        jobRepository.delete(errorJobDetails);
        unregisterScheduledJob(errorJobDetails);
        LOGGER.warn("Retry limit exceeded for job{}", errorJobDetails);
        getJobEventPublisher().ifPresent(p -> p.publishJobStatusChange(errorJobDetails));
        return errorJobDetails;

    }

    private ManageableJobHandle scheduleRegistering(JobDetails job, Trigger trigger) {
        ManageableJobHandle handle = doSchedule(job, trigger);
        return registerScheduledJob(handle, job);
    }

    protected ManageableJobHandle registerScheduledJob(ManageableJobHandle handle, JobDetails job) {
        schedulerControl.put(job.getId(), new SchedulerControlRecord(job.getId(), handle.getId(), DateUtil.now()));
        return handle;
    }

    public abstract ManageableJobHandle doSchedule(JobDetails job, Trigger trigger);

    protected SchedulerControlRecord unregisterScheduledJob(JobDetails job) {
        return schedulerControl.remove(job.getId());
    }

    protected Collection<SchedulerControlRecord> getScheduledJobs() {
        return new ArrayList<>(schedulerControl.values());
    }

    public JobDetails cancel(JobDetails job) {
        LOGGER.debug("Cancel Job Scheduling {}", job);
        if (job.getScheduledId() != null) {
            this.doCancel(job);
        }
        JobDetails deletedJobDetails = jobRepository.delete(job);
        this.unregisterScheduledJob(job);
        JobDetails canceledJobDetails = jobWithStatus(deletedJobDetails == null ? job : deletedJobDetails, JobStatus.CANCELED);
        getJobEventPublisher().ifPresent(p -> p.publishJobStatusChange(canceledJobDetails));
        return canceledJobDetails;

    }

    @Override
    public JobDetails cancel(String jobId) {
        JobDetails persistedJobDetails = jobRepository.get(jobId);
        if (persistedJobDetails != null) {
            return cancel(persistedJobDetails);
        } else {
            return null;
        }
    }

    public abstract ManageableJobHandle doCancel(JobDetails scheduledJob);

    @Override
    public Optional<ZonedDateTime> scheduled(String jobId) {
        SchedulerControlRecord record = schedulerControl.get(jobId);
        return Optional.ofNullable(record != null ? record.getScheduledTime() : null);
    }

    public void setForceExecuteExpiredJobs(boolean forceExecuteExpiredJobs) {
        this.forceExecuteExpiredJobs = forceExecuteExpiredJobs;
    }
}
