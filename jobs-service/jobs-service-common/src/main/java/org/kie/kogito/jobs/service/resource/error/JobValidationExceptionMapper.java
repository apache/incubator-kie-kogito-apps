package org.kie.kogito.jobs.service.resource.error;

import javax.ws.rs.ext.Provider;

import org.kie.kogito.jobs.service.exception.JobValidationException;

@Provider
public class JobValidationExceptionMapper extends BaseExceptionMapper<JobValidationException> {

    public JobValidationExceptionMapper() {
        super(400, false);
    }
}
