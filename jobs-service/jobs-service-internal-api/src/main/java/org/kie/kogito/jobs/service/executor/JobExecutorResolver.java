package org.kie.kogito.jobs.service.executor;

import org.kie.kogito.jobs.service.model.JobDetails;

/**
 * Create the Job with the proper Recipient based on the JobDetails
 */
public interface JobExecutorResolver {
    /**
     * - JobDetails -> create Recipient (HTTP, Sink, Kafka) : maybe a generic with a property to identify (Map<> config, String type)
     * - ExecutorManager -> find the proper executor
     *
     *
     */
    JobExecutor get(JobDetails jobDetails);

}
