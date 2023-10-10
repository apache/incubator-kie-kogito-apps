package org.kie.kogito.jobs.service.validation;

import org.kie.kogito.jobs.service.api.Job;

public class ValidatorContext {
    private final Job job;

    public ValidatorContext() {
        this(null);
    }

    public ValidatorContext(Job job) {
        this.job = job;
    }

    public Job getJob() {
        return job;
    }
}
