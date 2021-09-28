/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms;

import java.io.FileNotFoundException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.model.FormFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/forms")
public class FormsService {

    private FormsStorage storage;

    private static final Logger LOGGER = LoggerFactory.getLogger(FormsService.class);

    @Inject
    public FormsService(FormsStorage storage) {
        this.storage = storage;
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFormsList(@QueryParam("names") FormFilter filter) {
        try {
            return Response.ok(storage.getFormInfoList(filter)).build();
        } catch (Exception e) {
            LOGGER.error("Error while getting forms list: ", e);
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response formsCount() {
        try {
            return Response.ok(storage.getFormsCount()).build();
        } catch (Exception e) {
            LOGGER.error("Error while getting forms count: ", e);
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/formName/{formName:\\S+}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFormContent(@PathParam("formName") String formName) {
        try {
            Response.ResponseBuilder responseBuilder = Response.ok(storage.getFormContent(formName).toString());
            return responseBuilder.build();
        } catch (FileNotFoundException fe) {
            return Response.status(NOT_FOUND.getStatusCode(), fe.getMessage()).build();
        } catch (Exception e) {
            LOGGER.error("Error while getting form content: ", e);
            return Response.serverError().build();
        }
    }
}
