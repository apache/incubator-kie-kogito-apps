package org.kie.kogito.jobs.service.repository;

import java.time.ZonedDateTime;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.JobStatus;

public interface ReactiveJobRepository {

    CompletionStage<JobDetails> save(JobDetails job);

    CompletionStage<JobDetails> merge(String id, JobDetails job);

    CompletionStage<JobDetails> get(String id);

    CompletionStage<Boolean> exists(String id);

    CompletionStage<JobDetails> delete(String id);

    CompletionStage<JobDetails> delete(JobDetails job);

    PublisherBuilder<JobDetails> findByStatus(JobStatus... status);

    PublisherBuilder<JobDetails> findAll();

    PublisherBuilder<JobDetails> findByStatusBetweenDatesOrderByPriority(ZonedDateTime from, ZonedDateTime to, JobStatus... status);
}
