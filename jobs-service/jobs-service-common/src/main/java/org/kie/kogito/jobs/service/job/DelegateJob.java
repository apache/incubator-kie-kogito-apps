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
package org.kie.kogito.jobs.service.job;

import org.kie.kogito.jobs.service.exception.JobExecutionException;
import org.kie.kogito.jobs.service.executor.JobExecutor;
import org.kie.kogito.jobs.service.executor.JobExecutorResolver;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.JobDetailsContext;
import org.kie.kogito.jobs.service.model.JobExecutionResponse;
import org.kie.kogito.jobs.service.scheduler.JobScheduler;
import org.kie.kogito.timer.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

/**
 * The job that delegates the execution to the {@link JobExecutorResolver} with the {@link JobDetailsContext}.
 */
public class DelegateJob implements Job<JobDetailsContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelegateJob.class);

    private JobExecutorResolver jobExecutorResolver;

    private JobScheduler<JobDetails> scheduler;

    public DelegateJob(JobExecutorResolver executorResolver, JobScheduler<JobDetails> scheduler) {
        this.jobExecutorResolver = executorResolver;
        this.scheduler = scheduler;
    }

    @Override
    public void execute(JobDetailsContext ctx) {
        JobDetails jobDetails = requireNonNull(ctx.getJobDetails(), () -> String.format("JobDetails cannot be null for context: %s", ctx));
        try {
            JobExecutor executor = jobExecutorResolver.get(jobDetails);
            executor = requireNonNull(executor, () -> String.format("No JobExecutor was found for jobDetails: %s", jobDetails));
            LOGGER.trace("Executing job for context: {}", jobDetails);
            JobExecutionResponse response = executor.execute(jobDetails);
            LOGGER.trace("Job execution response processing has finished: {}", response);
            handleJobExecutionSuccess(response);
        } catch (JobExecutionException ex) {
            LOGGER.error("Executing job error: {}", ex.getMessage());
            JobExecutionResponse errorResponse = JobExecutionResponse.builder()
                    .message(ex.getMessage())
                    .now()
                    .jobId(jobDetails.getId())
                    .code("500")
                    .build();

            handleJobExecutionError(errorResponse);
        } catch (Exception ex) {
            LOGGER.error("Unexpected error during the job execution: {}", ex.getMessage());
            JobExecutionResponse errorResponse = JobExecutionResponse.builder()
                    .message(ex.getMessage())
                    .now()
                    .jobId(jobDetails.getId())
                    .code("500")
                    .build();

            handleJobExecutionError(errorResponse);
        }

    }

    public JobDetails handleJobExecutionSuccess(JobExecutionResponse response) {
        LOGGER.info("Job execution success response received: {}", response);
        return scheduler.handleJobExecutionSuccess(response);
    }

    public JobDetails handleJobExecutionError(JobExecutionResponse response) {
        LOGGER.error("Job execution error response received: {}", response);
        return scheduler.handleJobExecutionError(response);
    }
}
