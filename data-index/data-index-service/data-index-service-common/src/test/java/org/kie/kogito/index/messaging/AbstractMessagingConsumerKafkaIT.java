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

package org.kie.kogito.index.messaging;

import java.time.Duration;

import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.DataIndexStorageService;
import org.kie.kogito.kafka.KafkaClient;
import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;
import static org.kie.kogito.index.TestUtils.readFileContent;

public abstract class AbstractMessagingConsumerKafkaIT {

    Duration timeout = Duration.ofSeconds(5);

    @ConfigProperty(name = KafkaQuarkusTestResource.KOGITO_KAFKA_PROPERTY, defaultValue = "localhost:9092")
    public String kafkaBootstrapServers;

    @Inject
    public DataIndexStorageService cacheService;

    KafkaClient kafkaClient;

    @BeforeEach
    void setup() {
        kafkaClient = new KafkaClient(kafkaBootstrapServers);
        cacheService.getJobsCache().clear();
        cacheService.getProcessInstancesCache().clear();
        cacheService.getUserTaskInstancesCache().clear();
    }

    @AfterEach
    void close() {
        if (kafkaClient != null) {
            kafkaClient.shutdown();
        }
        cacheService.getJobsCache().clear();
        cacheService.getProcessInstancesCache().clear();
        cacheService.getUserTaskInstancesCache().clear();
    }

    @Test
    void testProcessInstanceEvent() throws Exception {
        sendProcessInstanceEvent();

        String processInstanceId = "c2fa5c5e-3002-44c7-aef7-bce82297e3fe";

        await()
                .atMost(timeout)
                .untilAsserted(
                        () -> given().contentType(ContentType.JSON).body("{ \"query\" : \"{ ProcessInstances { id } }\" }")
                                .when().post("/graphql")
                                .then().log().ifValidationFails().statusCode(200)
                                .body("data.ProcessInstances.size()", is(1))
                                .body("data.ProcessInstances[0].id", is(processInstanceId)));

    }

    @Test
    void testUserTaskInstanceEvent() throws Exception {
        sendUserTaskInstanceEvent();

        String taskId = "228d5922-5e88-4bfa-8329-7116a5cbe58b";

        await()
                .atMost(timeout)
                .untilAsserted(
                        () -> given().contentType(ContentType.JSON).body("{ \"query\" : \"{ UserTaskInstances { id } }\" }")
                                .when().post("/graphql")
                                .then().log().ifValidationFails().statusCode(200)
                                .body("data.UserTaskInstances.size()", is(1))
                                .body("data.UserTaskInstances[0].id", is(taskId)));
    }

    @Test
    void testJobEvent() throws Exception {
        sendJobEvent();

        String jobId = "8350b8b6-c5d9-432d-a339-a9fc85f642d4_0";

        await()
                .atMost(timeout)
                .untilAsserted(() -> given().contentType(ContentType.JSON).body("{ \"query\" : \"{ Jobs { id } }\" }")
                        .when().post("/graphql")
                        .then().log().ifValidationFails().statusCode(200)
                        .body("data.Jobs.size()", is(1))
                        .body("data.Jobs[0].id", is(jobId)));
    }

    private void sendUserTaskInstanceEvent() throws Exception {
        send("user_task_instance_event.json", "kogito-usertaskinstances-events");
    }

    private void sendProcessInstanceEvent() throws Exception {
        send("process_instance_event.json", "kogito-processinstances-events");
    }

    private void sendJobEvent() throws Exception {
        send("job_event.json", "kogito-jobs-events");
    }

    private void send(String file, String topic) throws Exception {
        String json = readFileContent(file);
        kafkaClient.produce(json, topic);
    }

}
