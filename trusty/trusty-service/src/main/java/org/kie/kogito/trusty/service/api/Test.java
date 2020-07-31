package org.kie.kogito.trusty.service.api;

import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.kie.kogito.explainability.api.ExplainabilityRequestDto;
import org.kie.kogito.trusty.service.messaging.outgoing.ExplainabilityRequestProducer;

@Path("/test")
public class Test {

    @Inject
    ExplainabilityRequestProducer explainabilityRequestProducer;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response test() {
        explainabilityRequestProducer.sendEvent(new ExplainabilityRequestDto(UUID.randomUUID().toString()));
        return Response.ok().build();
    }
}
