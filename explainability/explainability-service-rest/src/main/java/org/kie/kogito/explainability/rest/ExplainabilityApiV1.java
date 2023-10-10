package org.kie.kogito.explainability.rest;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.kie.kogito.explainability.ExplanationService;
import org.kie.kogito.explainability.api.BaseExplainabilityRequest;
import org.kie.kogito.explainability.handlers.LocalExplainerServiceHandlerRegistry;

import io.smallrye.mutiny.Uni;

@Path("/v1")
public class ExplainabilityApiV1 {

    protected ExplanationService explanationService;
    protected LocalExplainerServiceHandlerRegistry explainerServiceHandlerRegistry;

    @Inject
    public ExplainabilityApiV1(
            ExplanationService explanationService,
            LocalExplainerServiceHandlerRegistry explainerServiceHandlerRegistry) {
        this.explanationService = explanationService;
        this.explainerServiceHandlerRegistry = explainerServiceHandlerRegistry;
    }

    @POST
    @Path("/explain")
    @APIResponses(value = {
            @APIResponse(description = "Retrieve the explainability for a given decision.", responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = BaseExplainabilityRequest.class))),
            @APIResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    @Operation(summary = "Retrieve the explainability for a given decision.", description = "Retrieve the explainability for a given decision.")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> explain(@Valid BaseExplainabilityRequest request) {
        CompletionStage<Response> result = explanationService.explainAsync(request)
                .thenApply(x -> Response.ok(x).build());

        return Uni.createFrom().completionStage(result);
    }

}
