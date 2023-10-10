package org.kie.kogito.jobs.service.executor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.kie.kogito.jobs.service.model.JobDetails;

@ApplicationScoped
public class DefaultJobExecutorResolver implements JobExecutorResolver {

    private Instance<JobExecutor> executors;

    @Inject
    public DefaultJobExecutorResolver(Instance<JobExecutor> executors) {
        this.executors = executors;
    }

    @Override
    public JobExecutor get(JobDetails jobDetails) {
        return executors.stream()
                .filter(executor -> executor.accept(jobDetails))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No JobExecutor found for " + jobDetails));
    }
}
