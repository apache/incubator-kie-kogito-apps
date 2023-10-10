package org.kie.kogito.jobs.service.resource.error;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper extends BaseExceptionMapper<NotFoundException> {

    public NotFoundExceptionMapper() {
        super(404, false);
    }
}
