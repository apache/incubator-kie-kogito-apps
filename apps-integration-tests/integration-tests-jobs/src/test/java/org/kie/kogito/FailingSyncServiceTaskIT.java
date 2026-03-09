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
package org.kie.kogito;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusIntegrationTest
class FailingSyncServiceTaskIT {

    private static final String PROCESS_ID = "FailingSyncServiceTask";
    private static final String USER_TASK_NODE_ID = "_E2424AB3-6F2A-4624-B451-C126EE9EABA1";
    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    static {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private JsonPath executeGraphQLQuery(String query) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(query)
                .when()
                .post("/graphql")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath();
    }

    @Test
    void testFailingSyncServiceTask() {
        String processInstanceId = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/" + PROCESS_ID)
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract()
                .path("id");

        // Check process instance is in ACTIVE state with no error
        String processQuery = String.format(
                "{ \"query\": \"{ ProcessInstances(where: { id: { equal: \\\"%s\\\" } }) { id, processId, state, error { nodeDefinitionId, nodeInstanceId, message } } }\" }",
                processInstanceId);

        JsonPath processResultBefore = executeGraphQLQuery(processQuery);

        List<Map<String, Object>> processesBefore = processResultBefore.get("data.ProcessInstances");
        assertThat(processesBefore).isNotEmpty();

        Map<String, Object> processBefore = processesBefore.get(0);
        assertThat(processBefore.get("id")).isEqualTo(processInstanceId);
        assertThat(processBefore.get("processId")).isEqualTo(PROCESS_ID);
        assertThat(processBefore.get("state")).isEqualTo("ACTIVE");
        assertThat(processBefore.get("error")).isNull();

        // Check nodes - should have 3 nodes total, user task should be active
        String nodesQuery = String.format(
                "{ \"query\": \"{ ProcessInstances(where: { id: { equal: \\\"%s\\\" } }) { nodes { id, nodeId, name, type, enter, exit, definitionId, cancelType, errorMessage } } }\" }",
                processInstanceId);

        JsonPath nodesResultBefore = executeGraphQLQuery(nodesQuery);

        List<Map<String, Object>> nodesBefore = nodesResultBefore.get("data.ProcessInstances[0].nodes");
        assertThat(nodesBefore).hasSize(3);

        // Find user task node
        Map<String, Object> userTaskNodeBefore = nodesBefore.stream()
                .filter(node -> USER_TASK_NODE_ID.equals(node.get("definitionId")))
                .findFirst()
                .orElseThrow(() -> new AssertionError("User task node not found"));

        String userTaskNodeInstanceId = (String) userTaskNodeBefore.get("id");
        assertThat(userTaskNodeBefore.get("type")).isEqualTo("HumanTaskNode");
        assertThat(userTaskNodeBefore.get("enter")).isNotNull();
        assertThat(userTaskNodeBefore.get("exit")).isNull();
        assertThat(userTaskNodeBefore.get("cancelType")).isNull();
        assertThat(userTaskNodeBefore.get("errorMessage")).isNull();

        // Check jobs - should reference the user task nodeInstanceId and not be in error
        String jobsQuery = String.format(
                "{ \"query\": \"{ Jobs(where: { processInstanceId: { equal: \\\"%s\\\" } }) { id, processInstanceId, nodeInstanceId, status, executionCounter } }\" }",
                processInstanceId);

        JsonPath jobsResultBefore = executeGraphQLQuery(jobsQuery);

        List<Map<String, Object>> jobsBefore = jobsResultBefore.get("data.Jobs");
        assertThat(jobsBefore).isNotEmpty();

        Map<String, Object> jobBefore = jobsBefore.get(0);
        assertThat(jobBefore.get("processInstanceId")).isEqualTo(processInstanceId);
        assertThat(jobBefore.get("nodeInstanceId")).isEqualTo(userTaskNodeInstanceId);
        assertThat(jobBefore.get("status")).isIn("SCHEDULED", "RETRY");

        // ===== WAIT FOR TIMER TO FIRE (2 seconds) =====
        // Wait for process to transition to ERROR state
        JsonPath processResultAfter = await()
                .pollDelay(Duration.ofSeconds(3))
                .atMost(TIMEOUT)
                .pollInterval(Duration.ofMillis(500))
                .until(() -> {
                    JsonPath result = executeGraphQLQuery(processQuery);

                    List<Map<String, Object>> processes = result.get("data.ProcessInstances");
                    if (processes != null && !processes.isEmpty() && "ERROR".equals(processes.get(0).get("state"))) {
                        return result;
                    }
                    return null;
                }, Objects::nonNull);

        List<Map<String, Object>> processesAfter = processResultAfter.get("data.ProcessInstances");
        assertThat(processesAfter).isNotEmpty();

        Map<String, Object> processAfter = processesAfter.get(0);
        assertThat(processAfter.get("id")).isEqualTo(processInstanceId);
        assertThat(processAfter.get("state")).isEqualTo("ERROR");
        assertThat(processAfter.get("error")).isNotNull();
        assertThat(processAfter.get("error.nodeDefinitionId")).isEqualTo(USER_TASK_NODE_ID);
        assertThat(processAfter.get("error.nodeInstanceId")).isEqualTo(userTaskNodeInstanceId);
        assertThat(processAfter.get("error.message")).asString().contains("Failed Service Task");

        // Check nodes - should still have 3 nodes, user task should have cancelType and error message
        JsonPath nodesResultAfter = executeGraphQLQuery(nodesQuery);

        List<Map<String, Object>> nodesAfter = nodesResultAfter.get("data.ProcessInstances[0].nodes");
        assertThat(nodesAfter).hasSize(3);

        // Find user task node after error
        Map<String, Object> userTaskNodeAfter = nodesAfter.stream()
                .filter(node -> USER_TASK_NODE_ID.equals(node.get("definitionId")))
                .findFirst()
                .orElseThrow(() -> new AssertionError("User task node not found"));

        assertThat(userTaskNodeAfter.get("id")).isEqualTo(userTaskNodeInstanceId);
        assertThat(userTaskNodeAfter.get("cancelType")).isEqualTo("ERROR");
        assertThat(userTaskNodeAfter.get("errorMessage")).asString().contains("Failed Service Task");

        // Check jobs - should now be in ERROR state
        JsonPath jobsResultAfter = executeGraphQLQuery(jobsQuery);

        List<Map<String, Object>> jobsAfter = jobsResultAfter.get("data.Jobs");
        assertThat(jobsAfter).isNotEmpty();

        Map<String, Object> jobAfter = jobsAfter.get(0);
        assertThat(jobAfter.get("status")).isIn("ERROR");
        assertThat(jobAfter.get("processInstanceId")).isEqualTo(processInstanceId);
        assertThat(jobAfter.get("nodeInstanceId")).isEqualTo(userTaskNodeInstanceId);
    }
}
