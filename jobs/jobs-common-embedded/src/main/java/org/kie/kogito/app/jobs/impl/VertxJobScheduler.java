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
package org.kie.kogito.app.jobs.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.kie.kogito.app.jobs.api.JobDescriptionMerger;
import org.kie.kogito.app.jobs.api.JobDetailsEventAdapter;
import org.kie.kogito.app.jobs.api.JobExecutor;
import org.kie.kogito.app.jobs.api.JobScheduler;
import org.kie.kogito.app.jobs.api.JobSchedulerBuilder;
import org.kie.kogito.app.jobs.api.JobSchedulerListener;
import org.kie.kogito.app.jobs.api.JobSynchronization;
import org.kie.kogito.app.jobs.api.JobTimeoutExecution;
import org.kie.kogito.app.jobs.api.JobTimeoutInterceptor;
import org.kie.kogito.app.jobs.integrations.JobExceptionDetailsExtractor;
import org.kie.kogito.app.jobs.integrations.ProcessInstanceJobDescriptionMerger;
import org.kie.kogito.app.jobs.integrations.ProcessJobDescriptionMerger;
import org.kie.kogito.app.jobs.integrations.UserTaskInstanceJobDescriptorMerger;
import org.kie.kogito.app.jobs.spi.JobContext;
import org.kie.kogito.app.jobs.spi.JobContextFactory;
import org.kie.kogito.app.jobs.spi.JobStore;
import org.kie.kogito.app.jobs.spi.memory.MemoryJobContextFactory;
import org.kie.kogito.app.jobs.spi.memory.MemoryJobStore;
import org.kie.kogito.event.DataEvent;
import org.kie.kogito.event.EventPublisher;
import org.kie.kogito.jobs.JobDescription;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.JobExecutionExceptionDetails;
import org.kie.kogito.jobs.service.model.JobStatus;
import org.kie.kogito.jobs.service.model.RecipientInstance;
import org.kie.kogito.jobs.service.utils.DateUtil;
import org.kie.kogito.timer.Trigger;
import org.kie.kogito.timer.impl.SimpleTimerTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;

/**
 * Vertx-based implementation of JobScheduler that manages job lifecycle and execution.
 *
 * <h2>Architecture Overview</h2>
 * <p>
 * This scheduler uses a separation of concerns pattern with two main components:
 *
 * <h3>1. Job Lifecycle Transition Methods (do* pattern)</h3>
 * <p>
 * These methods handle status transitions and event firing:
 * <ul>
 * <li>{@link #doSchedule(JobDetails)} - Transitions to SCHEDULED status</li>
 * <li>{@link #doRun(JobDetails)} - Transitions to RUNNING status</li>
 * <li>{@link #doExecute(JobDetails)} - Executes job and transitions to EXECUTED status</li>
 * <li>{@link #doCancel(JobDetails)} - Transitions to CANCELED status</li>
 * <li>{@link #doRetryOrError(JobDetails, Exception)} - Transitions to RETRY or ERROR status</li>
 * </ul>
 *
 * <p>
 * All do* methods follow a consistent pattern:
 * <ol>
 * <li>Create a new JobDetails with the target status</li>
 * <li>Fire events to notify listeners of the status change</li>
 * <li>Return the new JobDetails (caller handles infrastructure)</li>
 * </ol>
 *
 * <h3>2. Infrastructure Management</h3>
 * <p>
 * {@link #reconcileScheduling(Long, JobContext, JobDetails)} centralizes timer and storage operations:
 * <ul>
 * <li><b>EXECUTED/CANCELED:</b> Removes timer and deletes job from storage</li>
 * <li><b>SCHEDULED/RETRY:</b> Adds/updates timer and updates job in storage</li>
 * <li><b>ERROR:</b> Removes timer but keeps job in storage for tracking</li>
 * </ul>
 *
 * <h2>Benefits of This Design</h2>
 * <ul>
 * <li><b>Single Responsibility:</b> Status transitions separated from infrastructure management</li>
 * <li><b>Consistency:</b> All status transitions follow the same pattern</li>
 * <li><b>Maintainability:</b> Clear separation makes code easier to understand and modify</li>
 * <li><b>Correctness:</b> Events fired once with complete information</li>
 * </ul>
 *
 * <h2>Timer Management</h2>
 * <p>
 * Uses Vertx timers as the first-class citizen for job scheduling. Timer operations are
 * transaction-aware via {@link JobSynchronization}, ensuring that timer changes are synchronized
 * with storage transactions. The {@link #addOrUpdateTxTimer(JobDetails)} method efficiently
 * updates existing timers without requiring explicit removal.
 *
 * <h2>Event Publishing Behavior</h2>
 * <p>
 * <b>Important:</b> Event publishing is <i>transaction-aware</i>. When {@link #fireEvents(JobDetails)} is called,
 * it invokes {@link org.kie.kogito.event.EventPublisher#publish(org.kie.kogito.event.DataEvent)} methods that are
 * annotated with {@code @Transactional}. This means:
 * <ul>
 * <li><b>Events participate in the transaction:</b> The publish() call joins the existing transaction</li>
 * <li><b>Events fire after commit:</b> Events are buffered and only sent after successful transaction commit</li>
 * <li><b>Rollback safety:</b> If the transaction rolls back, events are NOT published</li>
 * </ul>
 *
 * <p>
 * This transaction-aware behavior ensures:
 * <ul>
 * <li>Data consistency: Events only reflect committed state changes</li>
 * <li>No phantom events: Failed operations don't generate misleading events</li>
 * <li>Reliable event ordering: Events correspond to actual persisted state</li>
 * </ul>
 *
 * <p>
 * <b>Implementation Note:</b> The transaction awareness comes from the {@code EventPublisher} implementations
 * (e.g., {@code QuarkusDataAuditEventPublisher}, {@code SpringbootDataAuditEventPublisher}) which have
 * {@code @Transactional} annotations on their {@code publish()} methods.
 */
public class VertxJobScheduler implements JobScheduler, Handler<Long> {

    private record TimerInfo(String jobId, Integer retries, Long timerId, Date timeout) {

    }

    private static Logger LOG = LoggerFactory.getLogger(VertxJobScheduler.class);

    private Integer maxNumberOfRetries;

    private Long refreshJobsInterval;

    private List<EventPublisher> eventPublishers;

    private List<JobExecutor> jobExecutors;

    private JobStore jobStore;

    private Vertx vertx;

    private WorkerExecutor workerExecutor;

    private JobContextFactory jobContextFactory;

    private List<JobDetailsEventAdapter> jobEventAdapters;

    private List<JobSchedulerListener> jobSchedulerListeners;

    private List<JobTimeoutInterceptor> interceptors;

    private List<JobDescriptionMerger> jobDescriptionMergers;

    private ConcurrentMap<String, TimerInfo> jobsScheduled;

    private Long refreshJobsIntervalTimerId;

    private Long maxRefreshJobsIntervalWindow;

    private Long retryInterval;

    public Integer numberOfWorkerThreads;

    private JobSynchronization jobSynchronization;

    private JobExceptionDetailsExtractor exceptionDetailsExtractor;

    public class VertxJobSchedulerBuilder implements JobSchedulerBuilder {

        @Override
        public JobSchedulerBuilder withRetryInterval(Long retryInterval) {
            VertxJobScheduler.this.retryInterval = retryInterval;
            return this;
        }

        @Override
        public JobSchedulerBuilder withJobSynchronization(JobSynchronization jobSynchronization) {
            VertxJobScheduler.this.jobSynchronization = jobSynchronization;
            return this;
        }

        @Override
        public JobSchedulerBuilder withJobSchedulerListeners(JobSchedulerListener... jobSchedulerListeners) {
            VertxJobScheduler.this.jobSchedulerListeners.addAll(List.of(jobSchedulerListeners));
            return this;
        }

        @Override
        public JobSchedulerBuilder withMaxRefreshJobsIntervalWindow(Long maxRefreshJobsIntervalWindow) {
            VertxJobScheduler.this.maxRefreshJobsIntervalWindow = maxRefreshJobsIntervalWindow;
            return this;
        }

        @Override
        public JobSchedulerBuilder withRefreshJobsInterval(Long refreshJobsInterval) {
            VertxJobScheduler.this.refreshJobsInterval = refreshJobsInterval;
            return this;
        }

        @Override
        public JobSchedulerBuilder withMaxNumberOfRetries(Integer maxNumberOfRetries) {
            VertxJobScheduler.this.maxNumberOfRetries = maxNumberOfRetries;
            return this;
        }

        @Override
        public JobSchedulerBuilder withJobEventAdapters(JobDetailsEventAdapter... jobEventAdapters) {
            VertxJobScheduler.this.jobEventAdapters.addAll(List.of(jobEventAdapters));
            return this;
        }

        @Override
        public JobSchedulerBuilder withEventPublishers(EventPublisher... eventPublishers) {
            VertxJobScheduler.this.eventPublishers.addAll(List.of(eventPublishers));
            return this;
        }

        @Override
        public JobSchedulerBuilder withJobContextFactory(JobContextFactory jobContextFactory) {
            VertxJobScheduler.this.jobContextFactory = jobContextFactory;
            return this;
        }

        @Override
        public JobSchedulerBuilder withJobExecutors(JobExecutor... jobExecutors) {
            VertxJobScheduler.this.jobExecutors.addAll(List.of(jobExecutors));
            return this;
        }

        @Override
        public JobSchedulerBuilder withJobStore(JobStore jobStore) {
            VertxJobScheduler.this.jobStore = jobStore;
            return this;
        }

        @Override
        public JobScheduler build() {
            Collections.sort(VertxJobScheduler.this.interceptors);
            return VertxJobScheduler.this;
        }

        @Override
        public JobSchedulerBuilder withTimeoutInterceptor(JobTimeoutInterceptor... interceptors) {
            VertxJobScheduler.this.interceptors.addAll(List.of(interceptors));
            return this;
        }

        @Override
        public JobSchedulerBuilder withNumberOfWorkerThreads(Integer numberOfWorkerThreads) {
            VertxJobScheduler.this.numberOfWorkerThreads = numberOfWorkerThreads;
            return this;
        }

        @Override
        public JobSchedulerBuilder withJobDescriptorMergers(JobDescriptionMerger... jobDescriptionMergers) {
            VertxJobScheduler.this.jobDescriptionMergers.addAll(List.of(jobDescriptionMergers));
            return this;
        }

        @Override
        public JobSchedulerBuilder withExceptionDetailsExtractor(JobExceptionDetailsExtractor exceptionDetailsExtractor) {
            VertxJobScheduler.this.exceptionDetailsExtractor = exceptionDetailsExtractor;
            return this;
        }

    }

    public VertxJobScheduler() {
        this.jobExecutors = new ArrayList<>();
        this.jobStore = new MemoryJobStore();
        this.jobsScheduled = new ConcurrentHashMap<>();
        this.eventPublishers = new ArrayList<>();
        this.jobStore = new MemoryJobStore();
        this.jobContextFactory = new MemoryJobContextFactory();
        this.jobEventAdapters = new ArrayList<>();
        this.jobSchedulerListeners = new ArrayList<>();
        this.interceptors = new ArrayList<>();
        this.jobDescriptionMergers = new ArrayList<>();
        this.jobDescriptionMergers.add(new UserTaskInstanceJobDescriptorMerger());
        this.jobDescriptionMergers.add(new ProcessInstanceJobDescriptionMerger());
        this.jobDescriptionMergers.add(new ProcessJobDescriptionMerger());

        this.jobSynchronization = new JobSynchronization() {
            @Override
            public void synchronize(Runnable action) {
                action.run();
            }
        };

        this.numberOfWorkerThreads = 10;
        this.maxNumberOfRetries = 3;
        this.refreshJobsInterval = 1000L;
        this.retryInterval = 10 * 1000L; // ten seconds
        this.maxRefreshJobsIntervalWindow = 5 * 60 * 1000L; // every 5 minute
    }

    @Override
    public void handle(Long timerId) {
        Callable<JobTimeoutExecution> current = new Callable<JobTimeoutExecution>() {
            @Override
            public JobTimeoutExecution call() throws Exception {
                syncWithJobStores();
                return new JobTimeoutExecution(null);
            }
        };
        for (JobTimeoutInterceptor interceptor : interceptors) {
            current = interceptor.chainIntercept(current);
        }
        this.workerExecutor.executeBlocking(current);
    }

    private void syncWithJobStores() {
        ZonedDateTime maxWindowLoad = DateUtil.now().plus(Duration.ofMillis(maxRefreshJobsIntervalWindow));
        OffsetDateTime maxWindow = maxWindowLoad.toOffsetDateTime();
        LOG.debug("Syncing jobs with job store till {}", maxWindow);
        JobContext jobContext = jobContextFactory.newContext();
        List<JobDetails> jobDetailsList = jobStore.loadActiveJobs(jobContext, maxWindow);

        // this cover scenarios where the database jobs are already stored
        for (JobDetails currentJobDetails : jobDetailsList) {
            String mapKey = getMapKey(currentJobDetails);
            jobsScheduled.compute(mapKey, (key, timerInfo) -> {
                if (timerInfo == null) {
                    // we schedule this (no need to trigger an event as it was already trigger during scheduling)
                    // this is new job loaded by this instance
                    LOG.trace("sync job new job {}", currentJobDetails);
                    return addTimerInfo(currentJobDetails);
                }

                // there is timer and changed but we check the timeout is after. we remove it.
                // as it is not in this window
                if (DateUtil.dateToOffsetDateTime(timerInfo.timeout()).isAfter(maxWindow)) {
                    LOG.trace("sync job removed job {}", currentJobDetails);
                    // we remove it
                    removeTimerInfo(timerInfo);
                    return null;
                }

                if (DateUtil.dateToOffsetDateTime(timerInfo.timeout()).isBefore(maxWindow) && !timerInfo.timeout().equals(currentJobDetails.getTrigger().hasNextFireTime())) {
                    // timeout has changed and it is in our window. we should reschedule
                    LOG.trace("sync job changed job {}", currentJobDetails);
                    removeTimerInfo(timerInfo);
                    return addTimerInfo(currentJobDetails);
                }

                // timeout has not changed
                return timerInfo;
            });
        }

        // the ones left are the ones we need to be removed as they are not in database or active anymore
        List<String> databaseJobKeys = jobDetailsList.stream().map(this::getMapKey).toList();
        List<String> keysToBeRemoved = new ArrayList<>(jobsScheduled.keySet());
        keysToBeRemoved.removeAll(databaseJobKeys);

        for (String keyToBeRemoved : keysToBeRemoved) {
            jobsScheduled.compute(keyToBeRemoved, (key, timerInfo) -> {
                if (timerInfo != null) {
                    removeTimerInfo(timerInfo);
                }
                return null;
            });
        }
    }

    @Override
    public void init() {
        this.vertx = Vertx.builder().build();
        this.workerExecutor = this.vertx.createSharedWorkerExecutor("Jobs", numberOfWorkerThreads);
        this.maxRefreshJobsIntervalWindow = Math.max(maxRefreshJobsIntervalWindow, refreshJobsInterval);
        this.refreshJobsIntervalTimerId = this.vertx.setPeriodic(0L, refreshJobsInterval, this);

        LOG.info("Initializing Job Service Logic \n" +
                "\tMaxRefreshJobsIntervalWindow: {} (millis)\n" +
                "\tMaxIntervalLimitToRetryMillis: {} (millis)\n" +
                "\tMaxNumberOfRetries: {}\n" +
                "\tRefreshJobsInterval: {} (millis)\n" +
                "\tNumber of worker threads {}\n" +
                "\tStore: {}",
                maxRefreshJobsIntervalWindow,
                retryInterval,
                maxNumberOfRetries,
                refreshJobsInterval,
                numberOfWorkerThreads,
                jobStore);
    }

    @Override
    public void close() {
        this.vertx.cancelTimer(this.refreshJobsIntervalTimerId);

        // clean up
        this.workerExecutor.close();
        this.vertx.close();
        this.jobsScheduled.clear();

        this.refreshJobsIntervalTimerId = null;
        this.workerExecutor = null;
        this.vertx = null;
    }

    @Override
    public String schedule(JobDescription jobDescription) {
        JobDetails jobDetails = JobDetailsHelper.newScheduledJobDetails(jobDescription);
        JobDetails scheduledJobDetails = doSchedule(jobDetails);
        addOrUpdateTxTimer(scheduledJobDetails);
        jobSchedulerListeners.forEach(l -> l.onSchedule(scheduledJobDetails));
        jobStore.persist(jobContextFactory.newContext(), scheduledJobDetails);
        return scheduledJobDetails.getId();
    }

    @Override
    public String reschedule(JobDescription jobDescription) {
        JobContext jobContext = jobContextFactory.newContext();
        JobDetails oldJob = jobStore.find(jobContext, jobDescription.id());

        // Cancel old job (fires CANCELED event)
        JobDetails canceledJob = doCancel(oldJob);

        // Schedule new job (fires SCHEDULED event)
        JobDetails newJob = JobDetailsHelper.newScheduledJobDetails(jobDescription);
        JobDetails scheduledJob = doSchedule(newJob);

        // Update timer with new schedule (addOrUpdate handles existing timer)
        addOrUpdateTxTimer(scheduledJob);
        jobSchedulerListeners.forEach(l -> l.onReschedule(scheduledJob));
        jobStore.update(jobContext, scheduledJob);

        return scheduledJob.getId();
    }

    @Override
    public void cancel(String jobId) {
        JobContext jobContext = jobContextFactory.newContext();
        JobDetails jobDetails = jobStore.find(jobContext, jobId);
        JobDetails cancelledJobDetails = doCancel(jobDetails);
        jobSchedulerListeners.forEach(l -> l.onCancel(cancelledJobDetails));
        reconcileScheduling(null, jobContext, cancelledJobDetails);
    }

    private void timeout(Long timerId, String jobId) {
        LOG.debug("Executing timeout with timer Id {} and jobId {}", timerId, jobId);
        workerExecutor.executeBlocking(newTimeoutTask(timerId, jobId));
    }

    private Callable<JobTimeoutExecution> newTimeoutTask(Long timerId, String jobId) {
        Callable<JobTimeoutExecution> current = new Callable<JobTimeoutExecution>() {
            @Override
            public JobTimeoutExecution call() throws Exception {

                LOG.trace("Timeout task {} with jobId {} newTimeoutTask", timerId, jobId);
                JobContext jobContext = jobContextFactory.newContext();
                // we check now if we should run
                boolean shouldRun = jobStore.shouldRun(jobContext, jobId);
                if (!shouldRun) {
                    LOG.trace("Timeout {} with jobId {} won't run", timerId, jobId);
                    VertxJobScheduler.this.jobsScheduled.remove(jobId);
                    return null;
                }

                LOG.debug("Timeout {} with jobId {} will be executed", timerId, jobId);
                JobDetails jobDetails = jobStore.find(jobContext, jobId);
                try {
                    JobDetails runningJobDetails = doRun(jobDetails);
                    LOG.trace("Timeout {} with jobId {} have been executed", timerId, jobId);
                    JobDetails executeJobDetails = doExecute(runningJobDetails);
                    LOG.trace("Timeout {} with jobId {} will be rescheduled if required", timerId, jobId);
                    JobDetails nextJobDetails = computeAndScheduleNextJobIfAny(executeJobDetails);
                    reconcileScheduling(timerId, jobContext, nextJobDetails);
                    jobSchedulerListeners.forEach(l -> l.onExecution(jobDetails));
                    return new JobTimeoutExecution(nextJobDetails);
                } catch (Exception exception) {
                    LOG.trace("Timeout {} with jobId {} will be retried if possible", timerId, jobId, exception);
                    JobDetails nextJobDetails = doRetryOrError(jobDetails, exception);
                    reconcileScheduling(timerId, jobContext, nextJobDetails);
                    jobSchedulerListeners.forEach(l -> l.onFailure(jobDetails));
                    return new JobTimeoutExecution(nextJobDetails, exception);
                }

            }
        };
        for (JobTimeoutInterceptor interceptor : interceptors) {
            current = interceptor.chainIntercept(current);
        }
        return current;
    }

    /**
     * Reconciles the scheduling infrastructure (timers and storage) based on the job's status.
     * This method ensures that timers and persistent storage are kept in sync with the job's lifecycle state.
     *
     * <p>
     * Behavior by job status:
     * <ul>
     * <li><b>EXECUTED/CANCELED:</b> Removes the timer and deletes the job from storage (job lifecycle complete)</li>
     * <li><b>SCHEDULED/RETRY:</b> Adds or updates the timer and updates the job in storage (job needs to run/retry)</li>
     * <li><b>ERROR:</b> Removes the timer but keeps the job in storage for error tracking</li>
     * <li><b>RUNNING:</b> No action (should not occur as this is called after status transitions)</li>
     * </ul>
     *
     * @param timerId the ID of the timer being processed (may be null for non-timer-triggered calls)
     * @param jobContext the job context for storage operations
     * @param nextJobDetails the job details with the new status to reconcile
     */
    private void reconcileScheduling(Long timerId, JobContext jobContext, JobDetails nextJobDetails) {
        String jobId = nextJobDetails.getId();
        switch (nextJobDetails.getStatus()) {
            case EXECUTED:
            case CANCELED:
                LOG.trace("Timeout {} with jobId {} will be removed", timerId, jobId);
                removeTxTimer(nextJobDetails);
                jobStore.remove(jobContext, jobId);
                break;
            case SCHEDULED:
            case RETRY:
                LOG.trace("Timeout {} with jobId {} will be updated and scheduled", timerId, jobId);
                addOrUpdateTxTimer(nextJobDetails);
                jobStore.update(jobContext, nextJobDetails);
                break;
            case ERROR:
                LOG.trace("Timeout {} with jobId {} will be set to error", timerId, jobId);
                removeTxTimer(nextJobDetails);
                jobStore.update(jobContext, nextJobDetails);
                break;
            default:
                LOG.trace("Timeout {} with jobId {} is RUNNING and should not happen", timerId, jobId);
                break;
        }
    }

    // add tx timer and remove tx timer
    private void addOrUpdateTxTimer(JobDetails jobDetails) {
        this.jobSynchronization.synchronize(new Runnable() {
            @Override
            public void run() {
                String mapKey = getMapKey(jobDetails);
                jobsScheduled.compute(mapKey, (key, timerInfo) -> {
                    if (timerInfo != null) {
                        removeTimerInfo(timerInfo);
                    }
                    return addTimerInfo(jobDetails);
                });
            }
        });
    }

    private void removeTxTimer(JobDetails jobDetails) {
        this.jobSynchronization.synchronize(new Runnable() {
            @Override
            public void run() {
                String mapKey = getMapKey(jobDetails);
                jobsScheduled.computeIfPresent(mapKey, (key, timerInfo) -> {
                    removeTimerInfo(timerInfo);
                    return null;
                });
            }
        });
    }

    private String getMapKey(JobDetails jobDetails) {
        return jobDetails.getId() + "-" + jobDetails.getRetries();
    }

    // vertx calls
    private TimerInfo addTimerInfo(JobDetails jobDetails) {
        LOG.trace("addTimerInfo {}", jobDetails);
        // if it is negative means it should be executed right away
        ZonedDateTime trigger = DateUtil.fromDate(jobDetails.getTrigger().hasNextFireTime());
        ZonedDateTime now = DateUtil.now();
        Long diff = ChronoUnit.MILLIS.between(now, trigger);
        Long delay = Math.max(1, diff);
        Long timerId = this.vertx.setTimer(delay, new Handler<Long>() {
            @Override
            public void handle(Long timerId) {
                timeout(timerId, jobDetails.getId());
            }
        });
        return new TimerInfo(jobDetails.getId(), jobDetails.getRetries(), timerId, jobDetails.getTrigger().hasNextFireTime());
    }

    private void removeTimerInfo(TimerInfo timerInfo) {
        LOG.trace("removeTimerInfo {}", timerInfo);
        Long timerId = timerInfo.timerId();
        this.vertx.cancelTimer(timerId);
    }

    /**
     * Transitions a job to SCHEDULED status.
     * Creates a new JobDetails with SCHEDULED status and fires the corresponding event.
     *
     * @param jobDetails the job to schedule
     * @return new JobDetails with SCHEDULED status
     */
    private JobDetails doSchedule(JobDetails jobDetails) {
        JobDetails scheduledJobDetails = JobDetails.builder()
                .of(jobDetails)
                .status(JobStatus.SCHEDULED)
                .build();
        LOG.trace("doSchedule {}", scheduledJobDetails);
        fireEvents(scheduledJobDetails);
        return scheduledJobDetails;
    }

    /**
     * Transitions a job to RUNNING status.
     * Creates a new JobDetails with RUNNING status, clears exception details, and fires the corresponding event.
     *
     * @param jobDetails the job to run
     * @return new JobDetails with RUNNING status and cleared exception details
     */
    private JobDetails doRun(JobDetails jobDetails) {
        JobDetails runJobDetails = JobDetails.builder().of(jobDetails).status(JobStatus.RUNNING).exceptionDetails(null).build();
        LOG.trace("doRun {}", runJobDetails);
        fireEvents(runJobDetails);
        return runJobDetails;
    }

    /**
     * Transitions a job to CANCELED status.
     * Creates a new JobDetails with CANCELED status and fires the corresponding event.
     *
     * @param jobDetails the job to cancel
     * @return new JobDetails with CANCELED status
     */
    private JobDetails doCancel(JobDetails jobDetails) {
        JobDetails canceledJobDetails = JobDetails.builder().of(jobDetails).status(JobStatus.CANCELED).build();
        LOG.trace("doCancel {}", canceledJobDetails);
        fireEvents(canceledJobDetails);
        return canceledJobDetails;
    }

    /**
     * Executes a job and transitions it to EXECUTED status.
     * Invokes all applicable job executors, increments the execution counter, and fires the corresponding event.
     *
     * @param jobDetails the job to execute
     * @return new JobDetails with EXECUTED status and incremented execution counter
     */
    private JobDetails doExecute(JobDetails jobDetails) {
        List<JobExecutor> validExecutors = jobExecutors.stream().filter(executor -> executor.accept(jobDetails)).toList();
        LOG.trace("valid executors are: {}", validExecutors);
        validExecutors.forEach(executor -> executor.execute(jobDetails));
        JobDetails executedJobDetails = JobDetails.builder().of(jobDetails).status(JobStatus.EXECUTED).incrementExecutionCounter().build();
        LOG.trace("doExecute {}", executedJobDetails);
        fireEvents(executedJobDetails);
        return executedJobDetails;
    }

    /**
     * Handles job failure by transitioning to either RETRY or ERROR status.
     *
     * <p>
     * This method differs from other do* methods as it:
     * <ol>
     * <li>Determines the target status (RETRY or ERROR) based on retry count</li>
     * <li>Creates the JobDetails via helper methods (createRetryJobDetails/createErrorJobDetails)</li>
     * <li>Extracts and sets exception details BEFORE firing events</li>
     * <li>Fires events with exception details already populated</li>
     * </ol>
     *
     * @param jobDetails the failed job
     * @param exception the exception that caused the failure
     * @return new JobDetails with RETRY or ERROR status and exception details
     */
    private JobDetails doRetryOrError(JobDetails jobDetails, Exception exception) {
        Integer retryCounter = jobDetails.getRetries();
        // First, create the next JobDetails (RETRY or ERROR) without firing events
        JobDetails nextJobDetails;
        if (retryCounter < this.maxNumberOfRetries) {
            LOG.trace("doRetryOrError: Job {} will be retried (retry {} of {})", jobDetails.getId(), retryCounter + 1, maxNumberOfRetries);
            nextJobDetails = createRetryJobDetails(jobDetails);
        } else {
            LOG.trace("doRetryOrError: Job {} exceeded max retries ({}) and will transition to ERROR", jobDetails.getId(), maxNumberOfRetries);
            nextJobDetails = createErrorJobDetails(jobDetails);
        }
        // Then extract and set exception details BEFORE firing events
        extractAndSetExceptionDetails(nextJobDetails, exception);
        LOG.trace("doRetryOrError: Created {} with exception details: {}", nextJobDetails.getStatus(), nextJobDetails);
        // Finally fire events with exception details already set
        fireEvents(nextJobDetails);
        return nextJobDetails;
    }

    /**
     * Creates JobDetails for a retry attempt.
     * Updates the trigger to schedule the retry after the configured retry interval,
     * increments the retry counter, and adjusts the execution timeout.
     *
     * @param jobDetails the job to retry
     * @return new JobDetails with RETRY status and updated trigger/timeout
     */
    private JobDetails createRetryJobDetails(JobDetails jobDetails) {
        Date now = Date.from(DateUtil.now().plus(Duration.ofMillis(retryInterval)).toInstant());
        Trigger newTrigger = setTriggerDate(jobDetails.getTrigger(), now);
        JobDescription jobDescriptionMerged = setJobDescription(jobDetails, newTrigger);
        return JobDetails.builder().of(jobDetails)
                .trigger(newTrigger)
                .recipient(new RecipientInstance(new InVMRecipient(new InVMPayloadData(jobDescriptionMerged))))
                .status(JobStatus.RETRY)
                .executionTimeout(jobDetails.getExecutionTimeout() + retryInterval)
                .incrementRetries()
                .build();
    }

    /**
     * Creates JobDetails for a job that has exceeded its retry limit.
     *
     * @param jobDetails the job that failed all retries
     * @return new JobDetails with ERROR status
     */
    private JobDetails createErrorJobDetails(JobDetails jobDetails) {
        return JobDetails.builder().of(jobDetails).status(JobStatus.ERROR).build();
    }

    private Trigger setTriggerDate(Trigger oldTrigger, Date newOriginDate) {
        SimpleTimerTrigger oldSimpleTimerTrigger = (SimpleTimerTrigger) oldTrigger;
        SimpleTimerTrigger newTrigger = new SimpleTimerTrigger(
                newOriginDate,
                oldSimpleTimerTrigger.getPeriod(),
                oldSimpleTimerTrigger.getPeriodUnit(),
                oldSimpleTimerTrigger.getRepeatCount(),
                oldSimpleTimerTrigger.getEndTime(),
                oldSimpleTimerTrigger.getZoneId());
        return newTrigger;
    }

    private JobDescription setJobDescription(JobDetails jobDetails, Trigger newTrigger) {
        JobDescription jobDescription = jobDetails.getRecipient().<InVMPayloadData> getRecipient().getPayload().getJobDescription();

        JobDescription newJobDescription = jobDescriptionMergers.stream()
                .filter(merger -> merger.accept(jobDescription))
                .map(merger -> merger.mergeTrigger(jobDescription, newTrigger))
                .findFirst()
                .orElseThrow();
        return newJobDescription;
    }

    private JobDetails computeAndScheduleNextJobIfAny(JobDetails jobDetails) {
        // there is a problem here. If we retried the job the origin, the current time is different.
        // so we set the current time as the time of execution so we do execute things at fixed interval time.
        ((SimpleTimerTrigger) jobDetails.getTrigger()).setNextFireTime(Date.from(Instant.now()));
        jobDetails.getTrigger().nextFireTime();
        if (jobDetails.getTrigger().hasNextFireTime() != null) {
            // we set the date for the trigger so we compute new job description
            JobDescription jobDescriptionMerged = setJobDescription(jobDetails, jobDetails.getTrigger());
            JobDetails nextJobDetails = JobDetails.builder()
                    .of(jobDetails)
                    .recipient(new RecipientInstance(new InVMRecipient(new InVMPayloadData(jobDescriptionMerged))))
                    .retries(0)
                    .executionTimeout(jobDetails.getTrigger().hasNextFireTime().getTime())
                    .exceptionDetails(null) // Clear exception details on successful execution
                    .build();
            JobDetails scheduledJobDetails = doSchedule(nextJobDetails);
            LOG.trace("computeAndScheduleNextJobIfAny {}", scheduledJobDetails);
            return scheduledJobDetails;
        }
        LOG.trace("computeAndScheduleNextJobIfAny - no next job, returning executed job {}", jobDetails);
        return jobDetails;
    }

    /**
     * Extracts exception details from a failed job execution and sets them on the JobDetails object.
     * The extractor implementation controls whether details are captured based on configuration.
     *
     * <p>
     * This method is called by {@link #doRetryOrError(JobDetails, Exception)} after creating
     * the RETRY or ERROR JobDetails but before firing events. This ensures that exception details
     * are included in the published events.
     *
     * @param jobDetails The JobDetails object to update with exception information
     * @param exception The exception that caused the job to fail
     */
    private void extractAndSetExceptionDetails(JobDetails jobDetails, Exception exception) {
        if (exceptionDetailsExtractor != null) {
            JobExecutionExceptionDetails exceptionDetails = exceptionDetailsExtractor.extractExceptionDetails(exception);
            if (exceptionDetails != null) {
                jobDetails.setExceptionDetails(exceptionDetails);
            }
        }
    }

    /**
     * Publishes job status change events to all registered event publishers.
     *
     * <p>
     * <b>Transaction-Aware Publishing:</b> This method calls {@link org.kie.kogito.event.EventPublisher#publish}
     * methods that are annotated with {@code @Transactional}. This means:
     * <ul>
     * <li>Events participate in the current transaction</li>
     * <li>Events are buffered and only sent after successful transaction commit</li>
     * <li>If the transaction rolls back, events are NOT published</li>
     * </ul>
     *
     * <p>
     * This ensures data consistency - events only reflect committed state changes, and failed
     * operations don't generate misleading events.
     *
     * @param jobDetails the job details to publish events for
     * @see #doSchedule(JobDetails)
     * @see #doRun(JobDetails)
     * @see #doExecute(JobDetails)
     * @see #doCancel(JobDetails)
     * @see #doRetryOrError(JobDetails, Exception)
     */
    private void fireEvents(JobDetails jobDetails) {
        List<DataEvent<byte[]>> jobInstanceEvents = jobEventAdapters.stream().filter(e -> e.accept(jobDetails)).map(e -> e.adapt(jobDetails)).toList();
        for (DataEvent<byte[]> jobEvent : jobInstanceEvents) {
            eventPublishers.forEach(e -> e.publish(jobEvent));
        }
    }

}
