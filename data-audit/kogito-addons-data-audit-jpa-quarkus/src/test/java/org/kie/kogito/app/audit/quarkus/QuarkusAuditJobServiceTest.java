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

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.kie.kogito.app.audit.api.DataAuditEventPublisher;
import org.kie.kogito.app.audit.api.SubsystemConstants;
import org.kie.kogito.jobs.service.api.Job;
import org.kie.kogito.jobs.service.api.Job.State;
import org.kie.kogito.jobs.service.api.TemporalUnit;
import org.kie.kogito.jobs.service.api.event.JobCloudEvent;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.kie.kogito.app.audit.quarkus.DataAuditTestUtils.deriveNewState;
import static org.kie.kogito.app.audit.quarkus.DataAuditTestUtils.newJobEvent;
import static org.kie.kogito.app.audit.quarkus.DataAuditTestUtils.wrapQuery;

@QuarkusTest
@TestInstance(Lifecycle.PER_CLASS)
public class QuarkusAuditJobServiceTest {

    @Inject
    DataAuditEventPublisher publisher;

    @BeforeAll
    public void init() {

        JobCloudEvent<Job> jobEvent;
        jobEvent = newJobEvent("job1", "correlation1", State.SCHEDULED, 10L, TemporalUnit.MILLIS, null, null);
        publisher.publish(jobEvent);

        jobEvent = deriveNewState(jobEvent, State.EXECUTED);
        publisher.publish(jobEvent);

        jobEvent = newJobEvent("job2", "correlation2", State.SCHEDULED, 10L, TemporalUnit.MILLIS, null, null);
        publisher.publish(jobEvent);

        jobEvent = newJobEvent("job3", "correlation3", State.SCHEDULED, 10L, TemporalUnit.MILLIS, null, null);
        publisher.publish(jobEvent);

        jobEvent = deriveNewState(jobEvent, State.CANCELED);
        publisher.publish(jobEvent);

        jobEvent = newJobEvent("job4", "correlation4", State.SCHEDULED, 10L, TemporalUnit.MILLIS, null, null);
        publisher.publish(jobEvent);

        jobEvent = deriveNewState(jobEvent, State.RETRY);
        publisher.publish(jobEvent);

        jobEvent = deriveNewState(jobEvent, State.EXECUTED);
        publisher.publish(jobEvent);

        jobEvent = newJobEvent("job5", "correlation5", State.SCHEDULED, 10L, TemporalUnit.MILLIS, null, null);
        publisher.publish(jobEvent);

        jobEvent = deriveNewState(jobEvent, State.ERROR);
        publisher.publish(jobEvent);
    }

    @Test
    public void testGetAllScheduledJobs() {
        String query = "{ GetAllScheduledJobs { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp } }";
        query = wrapQuery(query);

        List<Map<String, Object>> response = given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post(SubsystemConstants.DATA_AUDIT_PATH)
                .then()
                .log()
                .body()
                .assertThat()
                .statusCode(200)
                .and()
                .extract().path("data.GetAllScheduledJobs");

        assertThat(response)
                .hasSize(1).first()
                .hasFieldOrPropertyWithValue("jobId", equalTo("job2"))
                .hasFieldOrPropertyWithValue("correlationId", equalTo("correlation2"))
                .hasFieldOrPropertyWithValue("state", equalTo("SCHEDULED"))
                .hasFieldOrPropertyWithValue("executionTimeout", equalTo(10))
                .hasFieldOrPropertyWithValue("executionTimeoutUnit", equalTo("MILLIS"));

    }

    @Test
    public void testGetJobById() {
        String query = "{ GetJobById ( jobId : \\\"job1\\\") { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }";
        query = wrapQuery(query);
        List<Map<String, Object>> response = given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post(SubsystemConstants.DATA_AUDIT_PATH)
                .then()
                .log()
                .body()
                .assertThat()
                .statusCode(200)
                .and()
                .extract().path("data.GetJobById");

        assertThat(response)
                .hasSize(1);
    }

    @Test
    public void testGetJobHistoryById() {
        String query = "{ GetJobHistoryById ( jobId : \\\"job4\\\") { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }";
        query = wrapQuery(query);
        List<Map<String, Object>> response = given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post(SubsystemConstants.DATA_AUDIT_PATH)
                .then()
                .log()
                .body()
                .assertThat()
                .statusCode(200)
                .and()
                .extract().path("data.GetJobHistoryById");

        assertThat(response)
                .hasSize(3)
                .allMatch(e -> "job4".equals(e.get("jobId")))
                .extracting(e -> e.get("state"))
                .containsExactlyInAnyOrder("SCHEDULED", "RETRY", "EXECUTED");

    }

    @Test
    public void testGetJobHistoryByCorrelationId() {
        String query = "{ GetJobHistoryByCorrelationId ( correlationId : \\\"correlation4\\\") { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }";
        query = wrapQuery(query);
        List<Map<String, Object>> data = given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post(SubsystemConstants.DATA_AUDIT_PATH)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .extract().path("data.GetJobHistoryByCorrelationId");

        assertThat(data)
                .hasSize(3)
                .allMatch(e -> "job4".equals(e.get("jobId")))
                .allMatch(e -> "correlation4".equals(e.get("correlationId")))
                .extracting(e -> e.get("state"))
                .containsExactlyInAnyOrder("SCHEDULED", "RETRY", "EXECUTED");
    }

    @Test
    public void testGetAllPendingJobs() {
        String query = "{ GetAllPendingJobs { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }";
        query = wrapQuery(query);
        List<Map<String, Object>> data = given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post(SubsystemConstants.DATA_AUDIT_PATH)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .extract().path("data.GetAllPendingJobs");

        assertThat(data).hasSize(1);
    }

    @Test
    public void testGetAllEligibleJobsForExecution() {
        String query = "{ GetAllEligibleJobsForExecution { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }";
        query = wrapQuery(query);
        List<Map<String, Object>> data = given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post(SubsystemConstants.DATA_AUDIT_PATH)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .extract().path("data.GetAllEligibleJobsForExecution");

        assertThat(data).hasSize(1);

    }

    @Test
    public void testGetAllEligibleJobsForRetry() {
        String query = "{ GetAllEligibleJobsForRetry { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }";
        query = wrapQuery(query);
        List<Map<String, Object>> data = given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post(SubsystemConstants.DATA_AUDIT_PATH)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .extract().path("data.GetAllEligibleJobsForRetry");

        assertThat(data).hasSize(1);
    }

    @Test
    public void testGetAllJobs() {
        String query = "{ GetAllJobs { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }";
        query = wrapQuery(query);
        List<Map<String, Object>> data = given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post(SubsystemConstants.DATA_AUDIT_PATH)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .extract().path("data.GetAllJobs");

        assertThat(data)
                .hasSize(5)
                .extracting(e -> e.get("jobId"))
                .containsExactlyInAnyOrder("job1", "job2", "job3", "job4", "job5");
    }

    @Test
    public void testGetAllCompletedJobs() {
        String query = "{ GetAllCompletedJobs { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }";
        query = wrapQuery(query);
        List<Map<String, Object>> data = given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post(SubsystemConstants.DATA_AUDIT_PATH)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .extract().path("data.GetAllCompletedJobs");

        assertThat(data)
                .hasSize(2)
                .allMatch(e -> "EXECUTED".equals(e.get("state")))
                .extracting(e -> e.get("jobId"))
                .containsExactlyInAnyOrder("job1", "job4");
    }

    @Test
    public void testGetAllInErrorJobs() {

        String query = "{ GetAllInErrorJobs { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }";
        query = wrapQuery(query);
        List<Map<String, Object>> data = given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post(SubsystemConstants.DATA_AUDIT_PATH)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .extract().path("data.GetAllInErrorJobs");

        assertThat(data)
                .hasSize(1)
                .allMatch(e -> "ERROR".equals(e.get("state")))
                .extracting(e -> e.get("jobId"))
                .containsExactlyInAnyOrder("job5");
    }

    @Test
    public void testGetAllJobsByState() {

        String query = "{ GetAllJobsByState (state : \\\"EXECUTED\\\") { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }";
        query = wrapQuery(query);
        List<Map<String, Object>> data = given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post(SubsystemConstants.DATA_AUDIT_PATH)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .extract().path("data.GetAllJobsByState");

        assertThat(data)
                .allMatch(e -> "EXECUTED".equals(e.get("state")))
                .extracting(e -> e.get("jobId"))
                .containsExactlyInAnyOrder("job1", "job4");
    }

    @Test
    public void testGetJobByCorrelationId() {

        String query = "{ GetJobByCorrelationId (correlationId : \\\"correlation1\\\") { jobId, correlationId, state, schedule, retry, executionTimeout, executionTimeoutUnit, timestamp} }";
        query = wrapQuery(query);
        List<Map<String, Object>> data = given()
                .contentType(ContentType.JSON)
                .body(query)
                .when()
                .post(SubsystemConstants.DATA_AUDIT_PATH)
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .extract().path("data.GetJobByCorrelationId");

        assertThat(data).first()
                .hasFieldOrPropertyWithValue("correlationId", "correlation1");

    }

}
