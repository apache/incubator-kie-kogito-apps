/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.addons.quarkus.data.index.it;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

@QuarkusIntegrationTest
class MongoQuarkusAddonDataIndexIT {

    static {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void testDataIndexAddon() {
        given().contentType(ContentType.JSON).body("{ \"query\" : \"{ProcessDefinitions{ id, version, name } }\" }")
                .when().post("/graphql")
                .then().statusCode(200)
                .body("data.ProcessDefinitions.size()", is(1))
                .body("data.ProcessDefinitions[0].id", is("greet"))
                .body("data.ProcessDefinitions[0].version", is("1.0"))
                .body("data.ProcessDefinitions[0].name", is("Greeting workflow"));

        given().contentType(ContentType.JSON).body("{ \"query\" : \"{ProcessInstances{ id } }\" }")
                .when().post("/graphql")
                .then().statusCode(200)
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

        given().contentType(ContentType.JSON).body("{ \"query\" : \"{ProcessInstances{ id, state, diagram, source, nodeDefinitions { name } } }\" }")
                .when().post("/graphql")
                .then().statusCode(200)
                .body("data.ProcessInstances.size()", is(1))
                .body("data.ProcessInstances[0].id", is(id))
                .body("data.ProcessInstances[0].state", is("COMPLETED"))
                .body("data.ProcessInstances[0].diagram", is(nullValue()))
                .body("data.ProcessInstances[0].source", is(notNullValue()))
                .body("data.ProcessInstances[0].nodeDefinitions.size()", is(12));
    }

    @Test
    void testGraphQLUI() {
        given().contentType(ContentType.HTML)
                .when().get("/q/graphql-ui/")
                .then().statusCode(200)
                .body("html.head.title", is("GraphiQL"));
    }

}
