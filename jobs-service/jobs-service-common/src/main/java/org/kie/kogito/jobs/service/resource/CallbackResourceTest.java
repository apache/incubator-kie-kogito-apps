package org.kie.kogito.jobs.service.resource;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/callback")
public class CallbackResourceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackResourceTest.class);

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "postCallbackTest")
    public CompletionStage<String> post(@QueryParam("limit") String limit) {
        LOGGER.debug("post received with 'limit' param = {}", limit);
        return CompletableFuture.completedFuture("Post Success");
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(operationId = "getCallbackTest")
    public CompletionStage<String> get() {
        LOGGER.debug("get received");
        return CompletableFuture.completedFuture("Get Success");
    }
}
