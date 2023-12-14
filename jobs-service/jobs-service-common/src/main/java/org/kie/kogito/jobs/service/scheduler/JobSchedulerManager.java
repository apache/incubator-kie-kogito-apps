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

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.kie.kogito.jobs.service.management.MessagingChangeEvent;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.JobStatus;
import org.kie.kogito.jobs.service.repository.ReactiveJobRepository;
import org.kie.kogito.jobs.service.scheduler.impl.TimerDelegateJobScheduler;
import org.kie.kogito.jobs.service.utils.DateUtil;
import org.kie.kogito.jobs.service.utils.ErrorHandling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.mutiny.core.Vertx;

@ApplicationScoped
public class JobSchedulerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerManager.class);

    /**
     * The current chunk size in minutes the scheduler handles, it is used to keep a limit number of jobs scheduled
     * in the in-memory scheduler.
     */
    @ConfigProperty(name = "kogito.jobs-service.schedulerChunkInMinutes", defaultValue = "10")
    long schedulerChunkInMinutes;

    /**
     * The interval the job loading method runs to fetch the persisted jobs from the repository.
     */
    @ConfigProperty(name = "kogito.jobs-service.loadJobIntervalInMinutes", defaultValue = "10")
    long loadJobIntervalInMinutes;

    /**
     * The interval based on the current time the job loading method uses to fetch jobs "FROM (now -
     * {@link #loadJobFromCurrentTimeIntervalInMinutes}) TO {@link #schedulerChunkInMinutes}"
     */
    @ConfigProperty(name = "kogito.jobs-service.loadJobFromCurrentTimeIntervalInMinutes", defaultValue = "0")
    long loadJobFromCurrentTimeIntervalInMinutes;

    @Inject
    TimerDelegateJobScheduler scheduler;

    @Inject
    ReactiveJobRepository repository;

    @Inject
    Vertx vertx;
    final AtomicBoolean enabled = new AtomicBoolean(false);

    final AtomicLong periodicTimerIdForLoadJobs = new AtomicLong(-1l);

    private void startJobsLoadingFromRepositoryTask() {
        //guarantee it starts the task just in case it is not already active
        if (periodicTimerIdForLoadJobs.get() < 0) {
            if (loadJobIntervalInMinutes > schedulerChunkInMinutes) {
                LOGGER.warn("The loadJobIntervalInMinutes ({}) cannot be greater than schedulerChunkInMinutes ({}), " +
                        "setting value {} for both",
                        loadJobIntervalInMinutes,
                        schedulerChunkInMinutes,
                        schedulerChunkInMinutes);
                loadJobIntervalInMinutes = schedulerChunkInMinutes;
            }
            //first execution
            vertx.runOnContext(this::loadJobDetails);
            //next executions to run periodically
            periodicTimerIdForLoadJobs.set(vertx.setPeriodic(TimeUnit.MINUTES.toMillis(loadJobIntervalInMinutes), id -> loadJobDetails()));
        }
    }

    private void cancelJobsLoadingFromRepositoryTask() {
        if (periodicTimerIdForLoadJobs.get() > 0) {
            vertx.cancelTimer(periodicTimerIdForLoadJobs.get());
            //negative id indicates this is not active anymore
            periodicTimerIdForLoadJobs.set(-1);
        }
    }

    protected synchronized void onMessagingStatusChange(@Observes MessagingChangeEvent event) {
        boolean wasEnabled = enabled.getAndSet(event.isEnabled());
        if (enabled.get() && !wasEnabled) {
            // good, avoid starting twice if we receive two consecutive enabled = true
            startJobsLoadingFromRepositoryTask();
        } else if (!enabled.get()) {
            // but only cancel if we receive enabled = false, otherwise with two consecutive enable we are also cancelling.
            cancelJobsLoadingFromRepositoryTask();
        }
    }

    //Runs periodically loading the jobs from the repository in chunks
    void loadJobDetails() {
        if (!enabled.get()) {
            LOGGER.info("Skip loading scheduled jobs");
            return;
        }
        loadJobsInCurrentChunk()
                .filter(j -> !scheduler.scheduled(j.getId()).isPresent())//not consider already scheduled jobs
                .flatMapRsPublisher(t -> ErrorHandling.skipErrorPublisher(scheduler::schedule, t))
                .forEach(a -> LOGGER.debug("Loaded and scheduled job {}", a))
                .run()
                .whenComplete((v, t) -> Optional.ofNullable(t)
                        .map(ex -> {
                            LOGGER.error("Error Loading scheduled jobs!", ex);
                            return null;
                        })
                        .orElseGet(() -> {
                            LOGGER.info("Loading scheduled jobs completed !");
                            return null;
                        }));
    }

    private PublisherBuilder<JobDetails> loadJobsInCurrentChunk() {
        return repository.findByStatusBetweenDatesOrderByPriority(DateUtil.now().minusMinutes(loadJobFromCurrentTimeIntervalInMinutes),
                DateUtil.now().plusMinutes(schedulerChunkInMinutes),
                JobStatus.SCHEDULED, JobStatus.RETRY);
    }
}
