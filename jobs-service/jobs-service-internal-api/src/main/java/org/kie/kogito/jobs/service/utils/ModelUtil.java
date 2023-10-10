package org.kie.kogito.jobs.service.utils;

import java.time.temporal.ChronoUnit;
import java.util.Objects;

import org.kie.kogito.jobs.service.adapter.JobDetailsAdapter;
import org.kie.kogito.jobs.service.api.Job;

public class ModelUtil {

    private ModelUtil() {
    }

    public static Long getExecutionTimeoutInMillis(Job job) {
        Objects.requireNonNull(job, "A Job is required to calculate the execution timeout in milliseconds.");
        if (job.getExecutionTimeout() == null) {
            return null;
        }
        ChronoUnit chronoUnit = job.getExecutionTimeoutUnit() != null ? JobDetailsAdapter.TemporalUnitAdapter.toChronoUnit(job.getExecutionTimeoutUnit()) : ChronoUnit.MILLIS;
        return chronoUnit.getDuration().multipliedBy(job.getExecutionTimeout()).toMillis();
    }
}
