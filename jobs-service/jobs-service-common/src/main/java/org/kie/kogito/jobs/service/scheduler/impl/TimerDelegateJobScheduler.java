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
package org.kie.kogito.jobs.service.scheduler.impl;

import java.util.Optional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.kogito.jobs.service.executor.JobExecutorResolver;
import org.kie.kogito.jobs.service.job.DelegateJob;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.JobDetailsContext;
import org.kie.kogito.jobs.service.model.ManageableJobHandle;
import org.kie.kogito.jobs.service.repository.JobRepository;
import org.kie.kogito.jobs.service.scheduler.AbstractTimerJobScheduler;
import org.kie.kogito.jobs.service.stream.JobEventPublisher;
import org.kie.kogito.timer.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

/**
 * Job Scheduler based on Vert.x engine.
 */
@ApplicationScoped
public class TimerDelegateJobScheduler extends AbstractTimerJobScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerDelegateJobScheduler.class);

    @Inject
    protected JobExecutorResolver jobExecutorResolver;

    @Inject
    protected VertxTimerServiceScheduler delegate;

    @Inject
    Instance<JobEventPublisher> jobEventPublisher;

    protected TimerDelegateJobScheduler() {
    }

    @Inject
    public TimerDelegateJobScheduler(JobRepository jobRepository,
            @ConfigProperty(name = "kogito.jobs-service.backoffRetryMillis", defaultValue = "1000") long backoffRetryMillis,
            @ConfigProperty(name = "kogito.jobs-service.maxIntervalLimitToRetryMillis", defaultValue = "60000") long maxIntervalLimitToRetryMillis,
            @ConfigProperty(name = "kogito.jobs-service.schedulerChunkInMinutes", defaultValue = "10") long schedulerChunkInMinutes,
            @ConfigProperty(name = "kogito.jobs-service.forceExecuteExpiredJobs", defaultValue = "true") boolean forceExecuteExpiredJobs,
            @ConfigProperty(name = "kogito.jobs-service.forceExecuteExpiredJobsOnServiceStart", defaultValue = "true") boolean forceExecuteExpiredJobsOnServiceStart) {
        super(jobRepository, backoffRetryMillis, maxIntervalLimitToRetryMillis, schedulerChunkInMinutes, forceExecuteExpiredJobs, forceExecuteExpiredJobsOnServiceStart);
        LOGGER.info(
                "Creating JobScheduler with backoffRetryMillis={}, maxIntervalLimitToRetryMillis={}, schedulerChunkInMinutes={}, forceExecuteExpiredJobs={}, forceExecuteExpiredJobsOnServiceStart={}",
                backoffRetryMillis, maxIntervalLimitToRetryMillis, schedulerChunkInMinutes, forceExecuteExpiredJobs, forceExecuteExpiredJobsOnServiceStart);
    }

    @Override
    protected Optional<JobEventPublisher> getJobEventPublisher() {
        return jobEventPublisher.isResolvable() ? Optional.of(jobEventPublisher.get()) : Optional.empty();
    }

    @Override
    public ManageableJobHandle doSchedule(JobDetails job, Trigger trigger) {
        LOGGER.debug("Job Scheduling job: {}, trigger: {}", job, trigger);
        JobDetailsContext jobDeailsContext = new JobDetailsContext(job);
        ManageableJobHandle jobHandle = delegate.scheduleJob(new DelegateJob(jobExecutorResolver, this), jobDeailsContext, trigger);
        return jobHandle;
    }

    @Override
    public ManageableJobHandle doCancel(JobDetails scheduledJob) {
        if (scheduledJob.getScheduledId() == null) {
            return null;
        }
        ManageableJobHandle handle = new ManageableJobHandle(scheduledJob.getScheduledId());
        handle.setCancel(delegate.removeJob(handle));
        return handle;
    }

    /**
     * Removes only the programed in-memory timers.
     */
    public void unscheduleTimers() {
        LOGGER.debug("Removing in-memory scheduled timers");
        super.getScheduledJobs().forEach(record -> {
            boolean removed = delegate.removeJob(new ManageableJobHandle(record.getHandleId()));
            LOGGER.debug("Vertex timer: {} for jobId: {}, was removed: {}", record.getHandleId(), record.getJobId(), removed);
            super.unregisterScheduledJob(JobDetails.builder().id(record.getJobId()).build());
        });
    }
}
