package org.kie.kogito.jobs.service.resource.error;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class WebApplicationExceptionMapper extends BaseExceptionMapper<WebApplicationException> {

    public WebApplicationExceptionMapper() {
        super(false);
    }

    @Override
    public Response toResponse(WebApplicationException exception) {
        log(exception);
        return buildResponse(exception, exception.getResponse().getStatus());
    }
}
