package org.kie.kogito.trusty.service.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.kie.kogito.trusty.service.responses.DecisionStructuredInputsResponse;
import org.kie.kogito.trusty.service.responses.FeatureImportanceResponse;
import org.kie.kogito.trusty.service.responses.FeaturesImportanceResponse;

@Path("executions/decisions")
public class ExplainabilityApiV1 {

    @GET
    @Path("/{executionId}/featureImportance")
    @APIResponses(value = {
            @APIResponse(description = "Gets the local explanation of a decision.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = DecisionStructuredInputsResponse.class))),
            @APIResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    }
    )
    @Operation(
            summary = "Returns the feature importance for a decision.",
            description = "Returns the feature importance for a particular decision calculated using the lime algorithm."
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStructuredInputs(
            @Parameter(
                    name = "executionId",
                    description = "The execution ID.",
                    required = true,
                    schema = @Schema(implementation = String.class)
            ) @PathParam("executionId") String executionId) {
        // TODO: implement this
        return Response.ok(new FeaturesImportanceResponse(List.of(
                new FeatureImportanceResponse("firstFeature", 0.12),
                new FeatureImportanceResponse("secondFeature", -1.3)
        ))).build();
    }

}
