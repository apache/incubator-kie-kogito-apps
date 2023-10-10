package org.kie.kogito.jitexecutor.bpmn.api;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.kie.kogito.jitexecutor.bpmn.JITBPMNService;
import org.kie.kogito.jitexecutor.bpmn.responses.JITBPMNValidationResult;
import org.kie.kogito.jitexecutor.common.requests.MultipleResourcesPayload;

@Path("jitbpmn/validate")
public class BPMNValidationResource {

    @Inject
    JITBPMNService jitbpmnService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response schema(MultipleResourcesPayload payload) {
        JITBPMNValidationResult result = jitbpmnService.validatePayload(payload);
        return Response.ok(result.getErrors()).build();
    }

}
