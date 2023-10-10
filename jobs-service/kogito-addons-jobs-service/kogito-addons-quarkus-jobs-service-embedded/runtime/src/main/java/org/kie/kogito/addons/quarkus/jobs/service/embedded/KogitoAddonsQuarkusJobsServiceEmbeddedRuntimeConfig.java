package org.kie.kogito.addons.quarkus.jobs.service.embedded;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(prefix = "kogito", name = "jobs-service", phase = ConfigPhase.RUN_TIME)
public class KogitoAddonsQuarkusJobsServiceEmbeddedRuntimeConfig {

    /**
     * The current chunk size in minutes the scheduler handles, it is used to keep a limited number of jobs scheduled
     * in the in-memory scheduler.
     */
    @ConfigItem(name = "schedulerChunkInMinutes", defaultValue = "10")
    public long schedulerChunkInMinutes;

    /**
     * The interval the jobs loading method runs to fetch the persisted jobs from the repository.
     */
    @ConfigItem(name = "loadJobIntervalInMinutes", defaultValue = "10")
    public long loadJobIntervalInMinutes;

    /**
     * The interval based on the current time the jobs loading method uses to fetch jobs "FROM (now -
     * loadJobFromCurrentTimeIntervalInMinutes) TO schedulerChunkInMinutes
     */
    @ConfigItem(name = "loadJobFromCurrentTimeIntervalInMinutes", defaultValue = "60")
    public long loadJobFromCurrentTimeIntervalInMinutes;

    /**
     * Maximum amount of time the jobs service will be retrying to get a successful execution for a job.
     */
    @ConfigItem(name = "maxIntervalLimitToRetryMillis", defaultValue = "60000")
    public long maxIntervalLimitToRetryMillis;

    /**
     * Delay between retries when a job execution fails, and it must be retried.
     */
    @ConfigItem(name = "backoffRetryMillis", defaultValue = "1000")
    public long backoffRetryMillis;

    /**
     * Flag to allow and force a job with expirationTime in the past to be executed immediately. If false an
     * exception will be thrown.
     */
    @ConfigItem(name = "forceExecuteExpiredJobs", defaultValue = "true")
    public boolean forceExecuteExpiredJobs;

}
