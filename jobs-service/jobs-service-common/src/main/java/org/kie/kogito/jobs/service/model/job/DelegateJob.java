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
package org.kie.kogito.jobs.service.model.job;

import java.util.Optional;

import org.kie.kogito.jobs.service.exception.JobExecutionException;
import org.kie.kogito.jobs.service.executor.JobExecutorResolver;
import org.kie.kogito.jobs.service.model.JobExecutionResponse;
import org.kie.kogito.jobs.service.stream.JobStreams;
import org.kie.kogito.timer.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.arc.Arc;
import io.quarkus.arc.InstanceHandle;

/**
 * The job that sends an HTTP Request based on the {@link JobDetailsContext}.
 */
public class DelegateJob implements Job<JobDetailsContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelegateJob.class);

    private final JobExecutorResolver jobExecutorResolver;

    JobStreams jobStreams;

    public DelegateJob(JobExecutorResolver executorResolver, JobStreams jobStreams) {
        this.jobExecutorResolver = executorResolver;
        this.jobStreams = jobStreams;
    }

    public DelegateJob() {
        this(getFromContainer(JobExecutorResolver.class), getFromContainer(JobStreams.class));
    }

    private static <T> T getFromContainer(Class<T> type) {
        return Optional.ofNullable(Arc.container())
                .map(c -> c.instance(type))
                .map(InstanceHandle::get)
                .orElseThrow(() -> new IllegalArgumentException(type + " cannot be null"));
    }

    @Override
    public void execute(JobDetailsContext ctx) {
        LOGGER.info("Executing for context {}", ctx.getJobDetails());
        Optional.ofNullable(ctx)
                .map(JobDetailsContext::getJobDetails)
                .map(jobExecutorResolver::get)
                .map(executor -> executor.execute(ctx.getJobDetails()))
                .orElseThrow(() -> new IllegalStateException("JobDetails cannot be null from context " + ctx))
                .onItem().invoke(jobStreams::publishJobSuccess)
                .onFailure(JobExecutionException.class).invoke(ex -> {
                    String jobId = ((JobExecutionException) ex).getJobId();
                    LOGGER.error("Error executing job {}", jobId, ex);
                    jobStreams.publishJobError(JobExecutionResponse.builder()
                            .message(ex.getMessage())
                            .now()
                            .jobId(jobId)
                            .build());
                })
                .subscribe().with(response -> LOGGER.info("Executed successfully with response {}", response));

    }
}
