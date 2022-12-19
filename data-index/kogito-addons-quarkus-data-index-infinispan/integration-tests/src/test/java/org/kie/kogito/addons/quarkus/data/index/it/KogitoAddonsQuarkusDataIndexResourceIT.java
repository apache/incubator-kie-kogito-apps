/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.addons.quarkus.data.index.it;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusIntegrationTest
public class KogitoAddonsQuarkusDataIndexResourceIT {

    @Test
    void testGreetRest() {
        given().contentType(ContentType.JSON).body("{ \"query\" : \"{ProcessInstances{ id } }\" }")
                .when().post("/graphql")
                .then().log().ifValidationFails().statusCode(200)
                .body("data.ProcessInstances.size()", is(0));

        String id = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{\"workflowdata\" : {\"name\" : \"John\", \"language\":\"English\"}}").when()
                .post("/greet")
                .then()
                .statusCode(201)
                .body("workflowdata.greeting", is("Hello from JSON Workflow,"))
                .extract().path("id");

        given().contentType(ContentType.JSON).body("{ \"query\" : \"{ProcessInstances{ id, state } }\" }")
                .when().post("/graphql")
                .then().log().ifValidationFails().statusCode(200)
                .body("data.ProcessInstances.size()", is(1))
                .body("data.ProcessInstances[0].id", is(id))
                .body("data.ProcessInstances[0].state", is("COMPLETED"));
    }

}
