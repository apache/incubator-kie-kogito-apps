/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.task.console;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class VertxRouterTest {

    @Test
    void testHandlePath() {

        given().when().get("/UserTasks")
                .then()
                .statusCode(200);

        given().when().get("/Task/a1e139d5-4e77-48c9-84ae-34578e904e5a")
                .then()
                .statusCode(200);

        given().when().get("/Another")
                .then()
                .statusCode(404);
    }
}
