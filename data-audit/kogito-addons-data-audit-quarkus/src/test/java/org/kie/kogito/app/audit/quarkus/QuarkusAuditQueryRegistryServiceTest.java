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
package org.kie.kogito.app.audit.quarkus;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.kie.kogito.app.audit.api.SubsystemConstants;
import org.kie.kogito.event.EventPublisher;
import org.kie.kogito.event.job.JobInstanceDataEvent;
import org.kie.kogito.jobs.service.model.JobStatus;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import jakarta.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.kogito.app.audit.quarkus.DataAuditTestUtils.deriveNewState;
import static org.kie.kogito.app.audit.quarkus.DataAuditTestUtils.newJobEvent;
import static org.kie.kogito.app.audit.quarkus.DataAuditTestUtils.wrapQuery;

@QuarkusTest
@TestInstance(Lifecycle.PER_CLASS)
public class QuarkusAuditQueryRegistryServiceTest {

    @Inject
    EventPublisher publisher;

    @BeforeAll
    public void init() throws Exception {

        JobInstanceDataEvent jobEvent;
        jobEvent = newJobEvent("job1", "nodeInstanceId1", 1, "processId1", "processInstanceId1", 100L, 10, "rootProcessId1", "rootProcessInstanceId1", JobStatus.SCHEDULED, 0);
        publisher.publish(jobEvent);

        jobEvent = deriveNewState(jobEvent, 1, JobStatus.EXECUTED);
        publisher.publish(jobEvent);

        jobEvent = newJobEvent("job2", "nodeInstanceId1", 1, "processId1", "processInstanceId2", 100L, 10, "rootProcessId1", "rootProcessInstanceId1", JobStatus.SCHEDULED, 0);
        publisher.publish(jobEvent);

        jobEvent = newJobEvent("job3", "nodeInstanceId1", 1, "processId1", "processInstanceId3", 100L, 10, "rootProcessId1", "rootProcessInstanceId1", JobStatus.SCHEDULED, 0);
        publisher.publish(jobEvent);

        jobEvent = deriveNewState(jobEvent, 1, JobStatus.CANCELED);
        publisher.publish(jobEvent);

        jobEvent = newJobEvent("job4", "nodeInstanceId1", 1, "processId1", "processInstanceId4", 100L, 10, "rootProcessId1", "rootProcessInstanceId1", JobStatus.SCHEDULED, 0);
        publisher.publish(jobEvent);

        jobEvent = deriveNewState(jobEvent, 1, JobStatus.RETRY);
        publisher.publish(jobEvent);

        jobEvent = deriveNewState(jobEvent, 2, JobStatus.EXECUTED);
        publisher.publish(jobEvent);

        jobEvent = newJobEvent("job5", "nodeInstanceId1", 1, "processId1", "processInstanceI51", 100L, 10, "rootProcessId1", "rootProcessInstanceId1", JobStatus.SCHEDULED, 0);
        publisher.publish(jobEvent);

        jobEvent = deriveNewState(jobEvent, 1, JobStatus.ERROR);
        publisher.publish(jobEvent);
    }

    @Test
    public void testRegisterQueryComplexType() {

        String body = "{"
                + "\"identifier\" : \"tests\", "
                + "\"graphQLDefinition\" : \"type EventTest { jobId : String, processInstanceId: String} type Query { tests (pagination: Pagination) : [ EventTest ] } \","
                + "\"query\" : \" SELECT o.job_id, o.process_instance_id FROM job_execution_log o \""
                + "}";

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(SubsystemConstants.DATA_AUDIT_REGISTRY_PATH)
                .then()
                .log()
                .body()
                .assertThat()
                .statusCode(200);

        String query = "{ tests { jobId, processInstanceId } }";
        query = wrapQuery(query);

        List<Map<String, Object>> response = given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post(SubsystemConstants.DATA_AUDIT_QUERY_PATH)
                .then()
                .log()
                .body()
                .assertThat()
                .statusCode(200)
                .and()
                .extract().path("data.tests");

        assertThat(response)
                .hasSize(10);

    }

    @Test
    public void testRegisterQuerySimpleType() {

        String body = "{"
                + "\"identifier\" : \"tests\", "
                + "\"graphQLDefinition\" : \"type Query { tests (pagination: Pagination) : [ String ] } \","
                + "\"query\" : \" SELECT o.job_id FROM job_execution_log o \""
                + "}";

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(SubsystemConstants.DATA_AUDIT_REGISTRY_PATH)
                .then()
                .log()
                .body()
                .assertThat()
                .statusCode(200);

        String query = "{ tests }";
        query = wrapQuery(query);

        List<Map<String, Object>> response = given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post(SubsystemConstants.DATA_AUDIT_QUERY_PATH)
                .then()
                .log()
                .body()
                .assertThat()
                .statusCode(200)
                .and()
                .extract().path("data.tests");

        assertThat(response)
                .hasSize(10);

    }
}
