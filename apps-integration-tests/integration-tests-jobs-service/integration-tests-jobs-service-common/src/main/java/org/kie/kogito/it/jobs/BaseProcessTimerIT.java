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
package org.kie.kogito.it.jobs;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public abstract class BaseProcessTimerIT implements JobServiceHealthAware {

    public static final String TIMERS = "timers";
    public static final String TIMERS_CYCLE = "timerscycle";
    public static final String TIMERS_ON_TASK = "timersOnTask";
    public static final Duration TIMEOUT = Duration.ofSeconds(10);

    static {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    //Timers Tests
    @Test
    public void testTimers() {
        String id = createTimer(new RequestPayload("PT01S"), TIMERS);
        Object id2 = getTimerById(id, TIMERS);
        assertThat(id).isEqualTo(id2);
        await().atMost(TIMEOUT)
                .untilAsserted(() -> getTimerWithStatusCode(id, 404, TIMERS));
        assertJobsAndProcessOnDataIndex(TIMERS, id, "COMPLETED", "EXECUTED");
    }

    public String dataIndexUrl() {
        return null;
    }

    @Test
    public void testCancelTimer() {
        String id = createTimer(new RequestPayload("PT030S"), TIMERS);
        Object id2 = deleteTimer(id, TIMERS);
        assertThat(id).isEqualTo(id2);
        getTimerWithStatusCode(id, 404, TIMERS);
        assertJobsAndProcessOnDataIndex(TIMERS, id, "ABORTED", "CANCELED");
    }

    private void assertJobsAndProcessOnDataIndex(String processId, String processInstanceId, String processStatus, String jobStatus) {
        if (dataIndexUrl() != null) {
            String query = "{  \"query\" : " +
                    "\"{ProcessInstances (where : {" +
                    "    id: {equal : \\\"" + processInstanceId + "\\\" }" +
                    "  }) {" +
                    "    id,processId,state" +
                    "  }" +
                    "}\"" +
                    "}";
            await()
                    .atMost(TIMEOUT)
                    .untilAsserted(() -> given()
                            .baseUri(dataIndexUrl())
                            .contentType(ContentType.JSON)
                            .body(query)
                            .when().post("/graphql")
                            .then().statusCode(200)
                            .body("data.ProcessInstances.size()", is(1))
                            .body("data.ProcessInstances[0].id", is(processInstanceId))
                            .body("data.ProcessInstances[0].processId", is(processId))
                            .body("data.ProcessInstances[0].state", is(processStatus)));

            String queryJobs = "{  \"query\" : " +
                    "\"{Jobs (where : {" +
                    "    processInstanceId: {equal : \\\"" + processInstanceId + "\\\" }" +
                    "  }) {" +
                    "    status" +
                    "  }" +
                    "}\"" +
                    "}";

            given()
                    .baseUri(dataIndexUrl())
                    .contentType(ContentType.JSON)
                    .body(queryJobs)
                    .when().post("/graphql")
                    .then().statusCode(200)
                    .body("data.Jobs.size()", is(1))
                    .body("data.Jobs[0].status", is(jobStatus));
        }
    }

    //Cycle Timers Tests
    @Test
    public void testTimerCycle() {
        String id = createTimer(new RequestPayload("R2/PT1S"), TIMERS_CYCLE);
        String id2 = getTimerById(id, TIMERS_CYCLE);
        assertThat(id).isEqualTo(id2);
        await().atMost(TIMEOUT)
                .untilAsserted(() -> getTimerWithStatusCode(id, 404, TIMERS_CYCLE));
        assertJobsAndProcessOnDataIndex(TIMERS_CYCLE, id, "COMPLETED", "EXECUTED");
    }

    @Test
    public void testDeleteTimerCycle() {
        String id = createTimer(new RequestPayload("R20/PT10S"), TIMERS_CYCLE);
        String id2 = getTimerById(id, TIMERS_CYCLE);
        assertThat(id).isEqualTo(id2);
        deleteTimer(id, TIMERS_CYCLE);
        await().atMost(TIMEOUT)
                .untilAsserted(() -> getTimerWithStatusCode(id, 404, TIMERS_CYCLE));
        await().atMost(TIMEOUT)
                .untilAsserted(() -> assertJobsAndProcessOnDataIndex(TIMERS_CYCLE, id, "ABORTED", "CANCELED"));
    }

    //Boundary Timers Tests
    @Test
    public void testBoundaryTimersOnTask() {
        String id = createTimer(new RequestPayload("PT01S"), TIMERS_ON_TASK);
        String id2 = getTimerById(id, TIMERS_ON_TASK);
        assertThat(id).isEqualTo(id2);
        await().atMost(TIMEOUT)
                .untilAsserted(() -> getTimerWithStatusCode(id, 404, TIMERS_ON_TASK));
        assertJobsAndProcessOnDataIndex(TIMERS_ON_TASK, id, "COMPLETED", "EXECUTED");
    }

    @Test
    public void testDeleteBoundaryTimersOnTask() {
        String id = createTimer(new RequestPayload("PT030S"), TIMERS_ON_TASK);
        String id2 = getTimerById(id, TIMERS_ON_TASK);
        assertThat(id).isEqualTo(id2);
        deleteTimer(id, TIMERS_ON_TASK);
        assertJobsAndProcessOnDataIndex(TIMERS_ON_TASK, id, "ABORTED", "CANCELED");
    }

    private ValidatableResponse getTimerWithStatusCode(String id, int code, String path) {
        return given()
                .accept(ContentType.JSON)
                .when()
                .get("/" + path + "/{id}", id)
                .then()
                .statusCode(code);
    }

    private String createTimer(RequestPayload delay, String path) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(delay)
                .when()
                .post("/" + path)
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    private String getTimerById(String id, String path) {
        return getTimerWithStatusCode(id, 200, path)
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    private Object deleteTimer(String id, String path) {
        return given()
                .accept(ContentType.JSON)
                .when()
                .delete("/" + path + "/{id}", id)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    /**
     * Simple bean class to send as body on the requests
     */
    private class RequestPayload {

        private String delay;

        public RequestPayload(String delay) {
            this.delay = delay;
        }

        public String getDelay() {
            return delay;
        }
    }
}
