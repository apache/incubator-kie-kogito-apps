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
package org.kie.kogito.jobs.service.scheduler;

import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.kie.kogito.jobs.service.model.JobExecutionResponse;
import org.kie.kogito.jobs.service.model.job.JobDetails;
import org.reactivestreams.Publisher;

public interface ReactiveJobScheduler extends JobScheduler<Publisher<JobDetails>, CompletionStage<JobDetails>> {

    Publisher<JobDetails> schedule(JobDetails job);

    CompletionStage<JobDetails> cancel(String jobId);

    PublisherBuilder<JobDetails> handleJobExecutionError(JobExecutionResponse errorResponse);

    PublisherBuilder<JobDetails> handleJobExecutionSuccess(JobExecutionResponse errorResponse);
}
