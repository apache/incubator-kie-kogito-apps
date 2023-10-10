package org.kie.kogito.jobs.service.scheduler;

import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.JobExecutionResponse;
import org.kie.kogito.timer.Trigger;
import org.reactivestreams.Publisher;

public interface ReactiveJobScheduler extends JobScheduler<Publisher<JobDetails>, CompletionStage<JobDetails>> {

    Publisher<JobDetails> schedule(JobDetails job);

    CompletionStage<JobDetails> cancel(String jobId);

    PublisherBuilder<JobDetails> reschedule(String id, Trigger trigger);

    PublisherBuilder<JobDetails> handleJobExecutionError(JobExecutionResponse errorResponse);

    PublisherBuilder<JobDetails> handleJobExecutionSuccess(JobExecutionResponse errorResponse);
}
