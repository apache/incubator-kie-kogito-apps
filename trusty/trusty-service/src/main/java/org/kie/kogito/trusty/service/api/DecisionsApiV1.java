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

package org.kie.kogito.trusty.service.api;

import java.util.Optional;

import javax.inject.Inject;
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
import org.kie.kogito.trusty.service.ITrustyService;
import org.kie.kogito.trusty.service.models.Execution;
import org.kie.kogito.trusty.service.responses.ExecutionHeaderResponse;

@Path("v1/executions/decisions")
public class DecisionsApiV1 {

    @Inject
    ITrustyService trustyService;

    @GET
    @Path("/{key}")
    @APIResponses(value = {
            @APIResponse(description = "Gets the decision detail header.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = ExecutionHeaderResponse.class))),
            @APIResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    }
    )
    @Operation(summary = "Gets The decision header with details.", description = "Gets the decision detail header.")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDecisionByKey(
            @Parameter(
                    name = "key",
                    description = "ID of the execution that needs to be fetched",
                    required = true,
                    schema = @Schema(implementation = String.class)
            ) @PathParam("key") String key) {
        Optional<Execution> execution = trustyService.getExecutionById(key);

        if (!execution.isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Event with id {} does not exist.", key)).build();
        }

        return Response.ok(ExecutionHeaderResponse.fromExecution(execution.get())).build();
    }
}
