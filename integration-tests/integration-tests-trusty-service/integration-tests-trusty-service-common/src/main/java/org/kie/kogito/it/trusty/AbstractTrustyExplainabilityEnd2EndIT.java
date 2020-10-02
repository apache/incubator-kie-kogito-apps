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

import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;
import org.kie.kogito.testcontainers.ExplainabilityServiceMessagingContainer;
import org.kie.kogito.testcontainers.InfinispanContainer;
import org.kie.kogito.testcontainers.KogitoServiceContainer;
import org.kie.kogito.testcontainers.TrustyServiceContainer;
import org.kie.kogito.trusty.service.responses.ExecutionHeaderResponse;
import org.kie.kogito.trusty.service.responses.ExecutionsResponse;
import org.kie.kogito.trusty.service.responses.SalienciesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractTrustyExplainabilityEnd2EndIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTrustyExplainabilityEnd2EndIT.class);

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
                        .withNetworkAliases("infinispan");

                final KafkaContainer kafka = new KafkaContainer()
                        .withNetwork(network)
                        .withNetworkAliases("kafka");

                final ExplainabilityServiceMessagingContainer explService = new ExplainabilityServiceMessagingContainer("kafka:9092", 10)
                        .withLogConsumer(new Slf4jLogConsumer(LOGGER))
                        .withNetwork(network)
                        .withNetworkAliases("expl-service");

                final TrustyServiceContainer trustyService = new TrustyServiceContainer("infinispan:11222", "kafka:9092", true)
                        .withLogConsumer(new Slf4jLogConsumer(LOGGER))
                        .withNetwork(network)
                        .withNetworkAliases("trusty-service");

                final KogitoServiceContainer kogitoService = kogitoServiceContainerProducer.apply("kafka:9092", "http://kogito-service:8080")
                        .withLogConsumer(new Slf4jLogConsumer(LOGGER))
                        .withNetwork(network)
                        .withNetworkAliases("kogito-service")
        ) {
            infinispan.start();
            assertTrue(infinispan.isRunning());

            kafka.start();
            assertTrue(kafka.isRunning());

            Testcontainers.exposeHostPorts(18080);

            explService.start();
            assertTrue(explService.isRunning());

            trustyService.start();
            assertTrue(trustyService.isRunning());

            kogitoService.start();
            assertTrue(kogitoService.isRunning());

            String json = "{\"Driver\":{\"Age\":25,\"Points\":13},\"Violation\":{\"Type\":\"speed\",\"Actual Speed\":115,\"Speed Limit\":100}}";

            given()
                    .port(kogitoService.getFirstMappedPort())
                    .contentType("application/json")
                    .body(json)
                    .when().post("/Traffic Violation")
                    .then().statusCode(200);

            // wait a reasonable amount of time for the loop to complete
            Thread.sleep(5000);

            ExecutionsResponse executionsResponse = given()
                    .port(trustyService.getFirstMappedPort())
                    .when().get("/executions?limit=1")
                    .then().statusCode(200)
                    .extract().as(ExecutionsResponse.class);

            assertSame(1, executionsResponse.getHeaders().size());

            String executionId = executionsResponse.getHeaders().stream()
                    .findFirst()
                    .map(ExecutionHeaderResponse::getExecutionId)
                    .orElseThrow(IllegalStateException::new);

            assertNotNull(executionId);

            SalienciesResponse salienciesResponse = given()
                    .port(trustyService.getFirstMappedPort())
                    .when().get("/executions/decisions/" + executionId + "/explanations/saliencies")
                    .then().statusCode(200)
                    .extract().as(SalienciesResponse.class);

            assertEquals("SUCCEEDED", salienciesResponse.getStatus());
        }
    }

}
