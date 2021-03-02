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

package org.kie.kogito.jitexecutor.dmn.api;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.kie.dmn.api.core.DMNMessage;
import org.kie.dmn.core.compiler.profiles.ExtendedDMNProfile;
import org.kie.dmn.validation.DMNValidator;
import org.kie.dmn.validation.DMNValidator.Validation;
import org.kie.dmn.validation.DMNValidatorFactory;
import org.kie.kogito.dmn.rest.KogitoDMNMessage;

@Path("jitdmn/validate")
public class ValidationResource {

    // trick for resolver/implementation for NI
    static final DMNValidator validator = DMNValidatorFactory.newValidator(Arrays.asList(new ExtendedDMNProfile()));

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response schema(String payload) {
        List<DMNMessage> validate =
                validator.validate(new StringReader(payload), Validation.VALIDATE_SCHEMA, Validation.VALIDATE_MODEL, Validation.VALIDATE_COMPILATION, Validation.ANALYZE_DECISION_TABLE);
        List<KogitoDMNMessage> result = validate.stream().map(KogitoDMNMessage::of).collect(Collectors.toList());
        return Response.ok(result).build();
    }
}
