package org.kie.kogito.jobs.service.resource.error;

import javax.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionMapper extends BaseExceptionMapper<IllegalArgumentException> {

    public IllegalArgumentExceptionMapper() {
        super(400, false);
    }
}
