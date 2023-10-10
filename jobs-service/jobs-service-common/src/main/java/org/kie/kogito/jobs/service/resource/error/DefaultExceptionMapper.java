package org.kie.kogito.jobs.service.resource.error;

import javax.ws.rs.ext.Provider;

@Provider
public class DefaultExceptionMapper extends BaseExceptionMapper<Exception> {
    public DefaultExceptionMapper() {
        super(true);
    }
}