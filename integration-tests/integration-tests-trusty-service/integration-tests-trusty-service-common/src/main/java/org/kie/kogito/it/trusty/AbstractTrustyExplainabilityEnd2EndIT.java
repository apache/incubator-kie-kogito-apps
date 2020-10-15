/*
 *  Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.kie.kogito.it.trusty;

import java.util.List;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;
import org.kie.kogito.testcontainers.ExplainabilityServiceMessagingContainer;
import org.kie.kogito.testcontainers.InfinispanContainer;
import org.kie.kogito.testcontainers.KogitoServiceContainer;
import org.kie.kogito.testcontainers.TrustyServiceContainer;
import org.kie.kogito.trusty.service.responses.ExecutionsResponse;
import org.kie.kogito.trusty.service.responses.SalienciesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractTrustyExplainabilityEnd2EndIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTrustyExplainabilityEnd2EndIT.class);

    private static final String EXPL_SERVICE_ALIAS = "expl-service";
    private static final int EXPL_SERVICE_SAMPLES = 10;

    private static final String INFINISPAN_ALIAS = "infinispan";
    private static final String INFINISPAN_SERVER_LIST = INFINISPAN_ALIAS + ":11222";

    private static final String KAFKA_ALIAS = "kafka";
    private static final String KAFKA_BOOTSTRAP_SERVERS = KAFKA_ALIAS + ":9092";

    private static final String KOGITO_SERVICE_ALIAS = "kogito-service";
    private static final String KOGITO_SERVICE_URL = "http://" + KOGITO_SERVICE_ALIAS + ":8080";

    private static final String TRUSTY_SERVICE_ALIAS = "trusty-service";

    private final BiFunction<String, String, KogitoServiceContainer> kogitoServiceContainerProducer;

    protected AbstractTrustyExplainabilityEnd2EndIT(BiFunction<String, String, KogitoServiceContainer> kogitoServiceContainerProducer) {
        this.kogitoServiceContainerProducer = kogitoServiceContainerProducer;
    }

    @Test
    public void doTest() throws Exception {
        try (
                final Network network = Network.newNetwork();

                final InfinispanContainer infinispan = new InfinispanContainer()
                        .withNetwork(network)
                        .withNetworkAliases(INFINISPAN_ALIAS);

                final KafkaContainer kafka = new KafkaContainer()
                        .withNetwork(network)
                        .withNetworkAliases(KAFKA_ALIAS);

                final ExplainabilityServiceMessagingContainer explService = new ExplainabilityServiceMessagingContainer(KAFKA_BOOTSTRAP_SERVERS, EXPL_SERVICE_SAMPLES)
                        .withLogConsumer(new Slf4jLogConsumer(LOGGER))
                        .withNetwork(network)
                        .withNetworkAliases(EXPL_SERVICE_ALIAS);

                final TrustyServiceContainer trustyService = new TrustyServiceContainer(INFINISPAN_SERVER_LIST, KAFKA_BOOTSTRAP_SERVERS, true)
                        .withLogConsumer(new Slf4jLogConsumer(LOGGER))
                        .withNetwork(network)
                        .withNetworkAliases(TRUSTY_SERVICE_ALIAS);

                final KogitoServiceContainer kogitoService = kogitoServiceContainerProducer.apply(KAFKA_BOOTSTRAP_SERVERS, KOGITO_SERVICE_URL)
                        .withLogConsumer(new Slf4jLogConsumer(LOGGER))
                        .withNetwork(network)
                        .withNetworkAliases(KOGITO_SERVICE_ALIAS)
        ) {
            infinispan.start();
            assertTrue(infinispan.isRunning());

            kafka.start();
            assertTrue(kafka.isRunning());

            explService.start();
            assertTrue(explService.isRunning());

            trustyService.start();
            assertTrue(trustyService.isRunning());

            kogitoService.start();
            assertTrue(kogitoService.isRunning());

            final List<String> jsonList = List.of(
                    "{\"Driver\":{\"Age\":25,\"Points\":13},\"Violation\":{\"Type\":\"speed\",\"Actual Speed\":105,\"Speed Limit\":100}}",
                    "{\"Driver\":{\"Age\":37,\"Points\":20},\"Violation\":{\"Type\":\"speed\",\"Actual Speed\":135,\"Speed Limit\":100}}",
                    "{\"Driver\":{\"Age\":18,\"Points\": 0},\"Violation\":{\"Type\":\"speed\",\"Actual Speed\": 85,\"Speed Limit\": 70}}",
                    "{\"Driver\":{\"Age\":56,\"Points\":13},\"Violation\":{\"Type\":\"speed\",\"Actual Speed\": 35,\"Speed Limit\": 25}}",
                    "{\"Driver\":{\"Age\":40,\"Points\":13},\"Violation\":{\"Type\":\"speed\",\"Actual Speed\":215,\"Speed Limit\":120}}"
            );
            final int expectedExecutions = jsonList.size();

            jsonList.forEach(json ->
                    given()
                            .port(kogitoService.getFirstMappedPort())
                            .contentType("application/json")
                            .body(json)
                            .when().post("/Traffic Violation")
                            .then().statusCode(200)
            );

            await()
                    .atLeast(5, SECONDS)
                    .atMost(30, SECONDS)
                    .with().pollInterval(5, SECONDS)
                    .untilAsserted(() -> {
                        ExecutionsResponse executionsResponse = given()
                                .port(trustyService.getFirstMappedPort())
                                .when().get(String.format("/executions?limit=%d", expectedExecutions))
                                .then().statusCode(200)
                                .extract().as(ExecutionsResponse.class);

                        assertSame(expectedExecutions, executionsResponse.getHeaders().size());

                        executionsResponse.getHeaders().forEach(execution -> {
                            String executionId = execution.getExecutionId();

                            assertNotNull(executionId);

                            SalienciesResponse salienciesResponse = given()
                                    .port(trustyService.getFirstMappedPort())
                                    .when().get("/executions/decisions/" + executionId + "/explanations/saliencies")
                                    .then().statusCode(200)
                                    .extract().as(SalienciesResponse.class);

                            assertEquals("SUCCEEDED", salienciesResponse.getStatus());
                        });
                    });
        }
    }
}
