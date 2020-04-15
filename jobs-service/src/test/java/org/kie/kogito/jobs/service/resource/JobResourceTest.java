/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.jobs.service.resource;

import java.io.IOException;
import java.time.temporal.ChronoUnit;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.vertx.core.Vertx;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.kie.kogito.jobs.api.Job;
import org.kie.kogito.jobs.api.JobBuilder;
import org.kie.kogito.jobs.service.model.JobStatus;
import org.kie.kogito.jobs.service.model.ScheduledJob;
import org.kie.kogito.jobs.service.utils.DateUtil;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@QuarkusTestResource(InfinispanServerTestResource.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JobResourceTest {

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private Vertx vertx;

    @Test
    void create() throws Exception {
        final Job job = getJob("1");
        final ScheduledJob response = create(jobToJson(job))
                .extract()
                .as(ScheduledJob.class);
        assertEquals(job, response);
    }

    private ValidatableResponse create(String body) throws IOException {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(JobResource.JOBS_PATH)
                .then()
                .statusCode(200);
    }

    private String jobToJson(Job job) throws JsonProcessingException {
        return objectMapper.writeValueAsString(job);
    }

    private Job getJob(String id) {
        return JobBuilder
                .builder()
                .id(id)
                .expirationTime(DateUtil.now().plusSeconds(10))
                .callbackEndpoint("http://localhost:8081/callback")
                .priority(1)
                .build();
    }

    @Test
    void deleteAfterCreate() throws Exception {
        final String id = "2";
        final Job job = getJob(id);
        create(jobToJson(job));
        final ScheduledJob response = given().pathParam("id", id)
                .when()
                .delete(JobResource.JOBS_PATH + "/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .as(ScheduledJob.class);
        assertEquals(job, response);
    }

    @Test
    void getAfterCreate() throws Exception {
        final String id = "3";
        final Job job = getJob(id);
        create(jobToJson(job));
        final ScheduledJob scheduledJob = given()
                .pathParam("id", id)
                .when()
                .get(JobResource.JOBS_PATH + "/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .assertThat()
                .extract()
                .as(ScheduledJob.class);
        assertEquals(scheduledJob.getId(), job.getId());
    }

    @Test
    void executeTest() throws Exception {
        final String id = "4";
        final Job job = getJob(id);
        create(jobToJson(job));
        final ScheduledJob scheduledJob = given()
                .pathParam("id", id)
                .when()
                .get(JobResource.JOBS_PATH + "/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .assertThat()
                .extract()
                .as(ScheduledJob.class);
        assertEquals(scheduledJob.getId(), job.getId());
        assertEquals(0, scheduledJob.getRetries());
        assertEquals(JobStatus.SCHEDULED, scheduledJob.getStatus());
        assertNotNull(scheduledJob.getScheduledId());
    }

    @Test
    void cancelRunningPeriodicJobTest() throws Exception {
        final String id = "1000";
        int timeMillis = 200;
        final Job job = JobBuilder
                .builder()
                .id(id)
                .expirationTime(DateUtil.now().plus(timeMillis, ChronoUnit.MILLIS))
                .repeatLimit(10)
                .repeatInterval(100l)
                .callbackEndpoint("http://localhost:8081/callback")
                .priority(1)
                .build();
        create(jobToJson(job));

        //check the job was created
        ScheduledJob scheduledJob = given()
                .pathParam("id", id)
                .when()
                .get(JobResource.JOBS_PATH + "/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .assertThat()
                .extract()
                .as(ScheduledJob.class);
        assertThat(scheduledJob.getId()).isEqualTo(job.getId());
        assertThat(scheduledJob.getStatus()).isEqualTo(JobStatus.SCHEDULED);
        assertThat(scheduledJob.getScheduledId()).isNotBlank();
        assertThat(scheduledJob.getExecutionCounter()).isEqualTo(0);

        //guarantee the job is running
        Thread.sleep(timeMillis + 1);

        //canceled the running job
        scheduledJob = given()
                .pathParam("id", id)
                .when()
                .delete(JobResource.JOBS_PATH + "/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .assertThat()
                .extract()
                .as(ScheduledJob.class);

        assertEquals(scheduledJob.getId(), job.getId());
        assertThat(scheduledJob.getExecutionCounter()).isGreaterThan(0);
        assertEquals(JobStatus.CANCELED, scheduledJob.getStatus());
        assertThat(scheduledJob.getScheduledId()).isNotBlank();

        //assert the job was deleted from the api perspective
        given()
                .pathParam("id", id)
                .when()
                .get(JobResource.JOBS_PATH + "/{id}")
                .then()
                .statusCode(404);

        //ensure the job was indeed canceled on vertx
        Long scheduledId = Long.valueOf(scheduledJob.getScheduledId());
        boolean timerCanceled = vertx.cancelTimer(scheduledId);
        assertThat(timerCanceled).isFalse();
    }
}

