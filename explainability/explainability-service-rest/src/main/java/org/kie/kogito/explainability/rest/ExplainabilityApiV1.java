/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.kogito.explainability.rest;

import java.util.concurrent.CompletionStage;

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
import org.kie.kogito.explainability.ExplanationService;
import org.kie.kogito.explainability.PredictionProviderFactory;
import org.kie.kogito.explainability.api.ExplainabilityRequestDto;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.models.ExplainabilityRequest;

@Path("/v1")
public class ExplainabilityApiV1 {

    protected ExplanationService explanationService;
    protected PredictionProviderFactory predictionProviderFactory;

    @Inject
    public ExplainabilityApiV1(
            ExplanationService explanationService,
            PredictionProviderFactory predictionProviderFactory) {
        this.explanationService = explanationService;
        this.predictionProviderFactory = predictionProviderFactory;
    }

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
    public Uni<Response> explain(ExplainabilityRequestDto requestDto) {
        ExplainabilityRequest request = ExplainabilityRequest.from(requestDto);
        PredictionProvider provider = predictionProviderFactory.createPredictionProvider(request);
        CompletionStage<Response> result = explanationService.explainAsync(request, provider)
                .thenApply(x -> Response.ok(x).build());

        return Uni.createFrom().completionStage(result);
    }
}

