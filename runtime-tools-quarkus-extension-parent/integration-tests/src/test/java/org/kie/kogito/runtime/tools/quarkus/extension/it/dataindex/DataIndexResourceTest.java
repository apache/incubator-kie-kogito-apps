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

package org.kie.kogito.runtime.tools.quarkus.extension.it.dataindex;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class DataIndexResourceTest {

    @Test
    public void testQueryEndpoint() {
        Response post = given()
                .when()
                .header("Content-Type",
                        "application/json")
                .body("{\n" +
                        "    \"operationName\": \"getAllTasksIds\",\n" +
                        "    \"query\": \"query getAllTasksIds{  UserTaskInstances{ id } }\"\n" +
                        "}")
                .post("/graphql");
        System.out.println(post.asString());
        post

                .then()
                .statusCode(200)
                .body("$.size()", is(1),
                        "[0].alpha2Code", is("GR"),
                        "[0].capital", is("Athens"),
                        "[0].currencies.size()", is(1),
                        "[0].currencies[0].name", is("Euro"));
    }
}
