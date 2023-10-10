package org.kie.kogito.jobs.service.scheduler;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.kie.kogito.jobs.service.model.JobDetails;

public interface JobScheduler<T, C> {

    T schedule(JobDetails job);

    C cancel(String jobId);

    Optional<ZonedDateTime> scheduled(String jobId);
}
