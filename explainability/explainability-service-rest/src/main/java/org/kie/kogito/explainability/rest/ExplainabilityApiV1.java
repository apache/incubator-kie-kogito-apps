package org.kie.kogito.explainability.rest;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.kie.kogito.explainability.IExplanationService;
import org.kie.kogito.explainability.api.ExplainabilityRequestDto;
import org.kie.kogito.explainability.models.ExplainabilityRequest;

@Path("/v1")
public class ExplainabilityApiV1 {

    @Inject
    IExplanationService explanationService;

    @POST
    @Path("/explain")
    @APIResponses(value = {
            @APIResponse(description = "Retrieve the explainability for a given decision.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = ExplainabilityRequestDto.class))),
            @APIResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    }
    )
    @Operation(summary = "Retrieve the explainability for a given decision.", description = "Retrieve the explainability for a given decision.")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> test(ExplainabilityRequestDto request) {

        CompletableFuture<Response> result = explanationService.explainAsync(ExplainabilityRequest.from(request))
                .thenApply(x -> Response.ok(x).build());

        return Uni.createFrom().completionStage(result);
    }
}

