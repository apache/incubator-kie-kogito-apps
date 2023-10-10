package org.kie.kogito.jobs.service.resource.error;

import javax.ws.rs.ext.Provider;

import org.kie.kogito.jobs.service.exception.InvalidScheduleTimeException;

@Provider
public class InvalidScheduleTimeExceptionMapper extends BaseExceptionMapper<InvalidScheduleTimeException> {

    public InvalidScheduleTimeExceptionMapper() {
        super(400, false);
    }
}
