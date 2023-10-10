package org.kie.kogito.jobs.service.executor;

import org.kie.kogito.jobs.service.api.Recipient;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.JobExecutionResponse;

import io.smallrye.mutiny.Uni;

public interface JobExecutor {

    Uni<JobExecutionResponse> execute(JobDetails job);

    default boolean accept(JobDetails job) {
        return type().isAssignableFrom(job.getRecipient().getRecipient().getClass());
    }

    Class<? extends Recipient> type();
}
