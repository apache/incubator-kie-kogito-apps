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

package org.kie.kogito.it.trusty;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.keycloak.representations.AccessTokenResponse;
import org.kie.kogito.testcontainers.ExplainabilityServiceMessagingContainer;
import org.kie.kogito.testcontainers.InfinispanTrustyServiceContainer;
import org.kie.kogito.testcontainers.KogitoInfinispanContainer;
import org.kie.kogito.testcontainers.KogitoKafkaContainer;
import org.kie.kogito.testcontainers.KogitoKeycloakContainer;
import org.kie.kogito.testcontainers.KogitoServiceContainer;
import org.kie.kogito.trusty.service.common.requests.CounterfactualRequest;
import org.kie.kogito.trusty.service.common.responses.CounterfactualRequestResponse;
import org.kie.kogito.trusty.service.common.responses.DecisionOutcomesResponse;
import org.kie.kogito.trusty.service.common.responses.DecisionStructuredInputsResponse;
import org.kie.kogito.trusty.service.common.responses.ExecutionsResponse;
import org.kie.kogito.trusty.service.common.responses.SalienciesResponse;
import org.kie.kogito.trusty.storage.api.model.CounterfactualSearchDomain;
import org.kie.kogito.trusty.storage.api.model.DecisionOutcome;
import org.kie.kogito.trusty.storage.api.model.TypedVariableWithValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.restassured.http.ContentType;

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

    private static final String KEYCLOAK_ALIAS = "keycloak";
    private static final String KEYCLOAK_DB_VENDOR_VARIABLE = "DB_VENDOR";
    private static final String KEYCLOAK_DB_VENDOR_VALUE = "h2";
    private static final String KEYCLOAK_ACCESS_TOKEN_PATH = "/auth/realms/kogito/protocol/openid-connect/token";
    private static final String KEYCLOAK_GRANT_TYPE_PARAM_NAME = "grant_type";
    private static final String KEYCLOAK_GRANT_TYPE_PARAM_VALUE = "password";
    private static final String KEYCLOAK_USERNAME_PARAM_NAME = "username";
    private static final String KEYCLOAK_USERNAME_PARAM_VALUE = "jdoe";
    private static final String KEYCLOAK_PASSWORD_PARAM_NAME = "password";
    private static final String KEYCLOAK_PASSWORD_PARAM_VALUE = "jdoe";
    private static final String KEYCLOAK_CLIENT_ID_PARAM_NAME = "client_id";
    private static final String KEYCLOAK_CLIENT_ID_PARAM_VALUE = KogitoKeycloakContainer.CLIENT_ID;
    private static final String KEYCLOAK_CLIENT_SECRET_PARAM_NAME = "client_secret";
    private static final String KEYCLOAK_CLIENT_SECRET_PARAM_VALUE = KogitoKeycloakContainer.CLIENT_SECRET;

    private static final String KOGITO_SERVICE_ALIAS = "kogito-service";
    private static final String KOGITO_SERVICE_URL = "http://" + KOGITO_SERVICE_ALIAS + ":8080";
    private static final List<String> KOGITO_SERVICE_PAYLOADS = List.of(
            "{\"Driver\":{\"Age\":25,\"Points\":13},\"Violation\":{\"Type\":\"speed\",\"Actual Speed\":105,\"Speed Limit\":100}}",
            "{\"Driver\":{\"Age\":37,\"Points\":20},\"Violation\":{\"Type\":\"speed\",\"Actual Speed\":135,\"Speed Limit\":100}}",
            "{\"Driver\":{\"Age\":18,\"Points\": 0},\"Violation\":{\"Type\":\"speed\",\"Actual Speed\": 85,\"Speed Limit\": 70}}",
            "{\"Driver\":{\"Age\":56,\"Points\":13},\"Violation\":{\"Type\":\"speed\",\"Actual Speed\": 35,\"Speed Limit\": 25}}",
            "{\"Driver\":{\"Age\":40,\"Points\":13},\"Violation\":{\"Type\":\"speed\",\"Actual Speed\":215,\"Speed Limit\":120}}");

    private static final String TRUSTY_SERVICE_ALIAS = "trusty-service";
    private static final String TRUSTY_SERVICE_OIDC_AUTH_SERVER_URL_VARIABLE = "QUARKUS_OIDC_AUTH_SERVER_URL";
    private static final String TRUSTY_SERVICE_OIDC_AUTH_SERVER_URL_VALUE = "http://" + KEYCLOAK_ALIAS + ":8080/auth/realms/kogito";
    private static final String TRUSTY_SERVICE_OIDC_CLIENT_ID_VARIABLE = "QUARKUS_OIDC_CLIENT_ID";
    private static final String TRUSTY_SERVICE_OIDC_CLIENT_ID_VALUE = "kogito-trusty-service";

    private final BiFunction<String, String, KogitoServiceContainer> kogitoServiceContainerProducer;

    protected AbstractTrustyExplainabilityEnd2EndIT(BiFunction<String, String, KogitoServiceContainer> kogitoServiceContainerProducer) {
        this.kogitoServiceContainerProducer = kogitoServiceContainerProducer;
    }

    @Test
    public void doTest() {
        try (
                final Network network = Network.newNetwork();

                final KogitoInfinispanContainer infinispan = new KogitoInfinispanContainer()
                        .withNetwork(network)
                        .withNetworkAliases(INFINISPAN_ALIAS);

                final KafkaContainer kafka = new KogitoKafkaContainer()
                        .withNetwork(network)
                        .withNetworkAliases(KAFKA_ALIAS);

                final KogitoKeycloakContainer keycloak = new KogitoKeycloakContainer()
                        .withEnv(KEYCLOAK_DB_VENDOR_VARIABLE, KEYCLOAK_DB_VENDOR_VALUE)
                        .withNetwork(network)
                        .withNetworkAliases(KEYCLOAK_ALIAS);

                final ExplainabilityServiceMessagingContainer explService = new ExplainabilityServiceMessagingContainer(KAFKA_BOOTSTRAP_SERVERS, EXPL_SERVICE_SAMPLES)
                        .withLogConsumer(new Slf4jLogConsumer(LOGGER))
                        .withNetwork(network)
                        .withNetworkAliases(EXPL_SERVICE_ALIAS);

                final InfinispanTrustyServiceContainer trustyService = new InfinispanTrustyServiceContainer(INFINISPAN_SERVER_LIST, KAFKA_BOOTSTRAP_SERVERS, true)
                        .withEnv(TRUSTY_SERVICE_OIDC_AUTH_SERVER_URL_VARIABLE, TRUSTY_SERVICE_OIDC_AUTH_SERVER_URL_VALUE)
                        .withEnv(TRUSTY_SERVICE_OIDC_CLIENT_ID_VARIABLE, TRUSTY_SERVICE_OIDC_CLIENT_ID_VALUE)
                        .withEnv("INFINISPAN_USE_AUTH", "FALSE")
                        .withLogConsumer(new Slf4jLogConsumer(LOGGER))
                        .withNetwork(network)
                        .withNetworkAliases(TRUSTY_SERVICE_ALIAS);

                final KogitoServiceContainer kogitoService = kogitoServiceContainerProducer.apply(KAFKA_BOOTSTRAP_SERVERS, KOGITO_SERVICE_URL)
                        .withLogConsumer(new Slf4jLogConsumer(LOGGER))
                        .withNetwork(network)
                        .withNetworkAliases(KOGITO_SERVICE_ALIAS)) {
            infinispan.start();
            assertTrue(infinispan.isRunning());

            kafka.start();
            assertTrue(kafka.isRunning());

            keycloak.start();
            assertTrue(keycloak.isRunning());

            explService.start();
            assertTrue(explService.isRunning());

            trustyService.start();
            assertTrue(trustyService.isRunning());

            kogitoService.start();
            assertTrue(kogitoService.isRunning());

            final String accessToken = given()
                    .port(keycloak.getFirstMappedPort())
                    .param(KEYCLOAK_GRANT_TYPE_PARAM_NAME, KEYCLOAK_GRANT_TYPE_PARAM_VALUE)
                    .param(KEYCLOAK_USERNAME_PARAM_NAME, KEYCLOAK_USERNAME_PARAM_VALUE)
                    .param(KEYCLOAK_PASSWORD_PARAM_NAME, KEYCLOAK_PASSWORD_PARAM_VALUE)
                    .param(KEYCLOAK_CLIENT_ID_PARAM_NAME, KEYCLOAK_CLIENT_ID_PARAM_VALUE)
                    .param(KEYCLOAK_CLIENT_SECRET_PARAM_NAME, KEYCLOAK_CLIENT_SECRET_PARAM_VALUE)
                    .when()
                    .post(KEYCLOAK_ACCESS_TOKEN_PATH)
                    .as(AccessTokenResponse.class).getToken();

            assertNotNull(accessToken);

            final int expectedExecutions = KOGITO_SERVICE_PAYLOADS.size();

            KOGITO_SERVICE_PAYLOADS.forEach(json -> given()
                    .port(kogitoService.getFirstMappedPort())
                    .contentType("application/json")
                    .body(json)
                    .when().post("/Traffic Violation")
                    .then().statusCode(200));

            await()
                    .atLeast(5, SECONDS)
                    .atMost(30, SECONDS)
                    .with().pollInterval(5, SECONDS)
                    .untilAsserted(() -> {
                        ExecutionsResponse executionsResponse = given()
                                .port(trustyService.getFirstMappedPort())
                                .auth().oauth2(accessToken)
                                .when().get(String.format("/executions?limit=%d", expectedExecutions))
                                .then().statusCode(200)
                                .extract().as(ExecutionsResponse.class);

                        assertSame(expectedExecutions, executionsResponse.getHeaders().size());

                        executionsResponse.getHeaders().forEach(execution -> {
                            String executionId = execution.getExecutionId();

                            assertNotNull(executionId);

                            SalienciesResponse salienciesResponse = given()
                                    .port(trustyService.getFirstMappedPort())
                                    .auth().oauth2(accessToken)
                                    .when().get("/executions/decisions/" + executionId + "/explanations/saliencies")
                                    .then().statusCode(200)
                                    .extract().as(SalienciesResponse.class);

                            assertEquals("SUCCEEDED", salienciesResponse.getStatus());

                            doCounterfactuals(trustyService, accessToken, executionId);
                        });
                    });
        }
    }

    private void doCounterfactuals(final InfinispanTrustyServiceContainer trustyService,
            final String accessToken,
            final String executionId) {
        //Decision's Inputs
        DecisionStructuredInputsResponse inputs = given()
                .port(trustyService.getFirstMappedPort())
                .auth().oauth2(accessToken)
                .when().get("/executions/decisions/" + executionId + "/structuredInputs")
                .then().statusCode(200)
                .extract().as(DecisionStructuredInputsResponse.class);

        //Decision's Outcomes
        DecisionOutcomesResponse outcomes = given()
                .port(trustyService.getFirstMappedPort())
                .auth().oauth2(accessToken)
                .when().get("/executions/decisions/" + executionId + "/outcomes")
                .then().statusCode(200)
                .extract().as(DecisionOutcomesResponse.class);

        //Debugging output (handy to keep).
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        StringBuilder sb = new StringBuilder();
        sb.append("== INPUTS ==>\n");
        inputs.getInputs().forEach(i -> {
            try {
                sb.append(writer.writeValueAsString(i)).append("\n");
            } catch (JsonProcessingException jpe) {
                //Swallow
            }
        });
        sb.append("== OUTPUTS ==>\n");
        outcomes.getOutcomes().forEach(o -> {
            try {
                sb.append(writer.writeValueAsString(o.getOutcomeResult())).append("\n");
            } catch (JsonProcessingException jpe) {
                //Swallow
            }
        });
        LOGGER.info(sb.toString());

        // The Goals and Search Domain structures must match those of the original decision
        // See https://issues.redhat.com/browse/FAI-486
        CounterfactualRequestResponse counterfactualRequestResponse = given()
                .port(trustyService.getFirstMappedPort())
                .auth().oauth2(accessToken)
                .when()
                .contentType(ContentType.JSON)
                .body(new CounterfactualRequest(
                        outcomes.getOutcomes().stream().map(DecisionOutcome::getOutcomeResult).collect(Collectors.toList()),
                        inputs.getInputs().stream().map(AbstractTrustyExplainabilityEnd2EndIT::toCounterfactualSearchDomain).collect(Collectors.toList())))
                .post("/executions/decisions/" + executionId + "/explanations/counterfactuals")
                .then().statusCode(200)
                .extract().as(CounterfactualRequestResponse.class);

        assertEquals(executionId, counterfactualRequestResponse.getExecutionId());
        assertNotNull(counterfactualRequestResponse.getCounterfactualId());
    }

    private static CounterfactualSearchDomain toCounterfactualSearchDomain(TypedVariableWithValue input) {
        switch (input.getKind()) {
            case COLLECTION:
                return CounterfactualSearchDomain.buildCollection(input.getName(),
                        input.getTypeRef(),
                        input.getComponents().stream().map(AbstractTrustyExplainabilityEnd2EndIT::toCounterfactualSearchDomain).collect(Collectors.toList()),
                        true,
                        null);
            case STRUCTURE:
                return CounterfactualSearchDomain.buildStructure(input.getName(),
                        input.getTypeRef(),
                        input.getComponents().stream().map(AbstractTrustyExplainabilityEnd2EndIT::toCounterfactualSearchDomain).collect(Collectors.toList()));
            case UNIT:
                return CounterfactualSearchDomain.buildFixedUnit(input.getName(),
                        input.getTypeRef());
        }
        throw new IllegalArgumentException("An unexpected TypedVariableWithValue.Kind detected. Unable to process.");
    }
}
