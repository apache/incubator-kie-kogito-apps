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

package org.kie.kogito.trusty.service.common.messaging.incoming;

import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.kafka.KafkaClient;
import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;
import org.kie.kogito.trusty.service.common.TrustyService;
import org.kie.kogito.trusty.service.common.TrustyServiceTestUtils;
import org.kie.kogito.trusty.storage.api.TrustyStorageService;
import org.kie.kogito.trusty.storage.api.model.Decision;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class AbstractTraceEventConsumerIT {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @ConfigProperty(name = KafkaQuarkusTestResource.KOGITO_KAFKA_PROPERTY)
    String kafkaBootstrapServers;

    @Inject
    TrustyService trustyService;

    @Inject
    TrustyStorageService trustyStorageService;

    KafkaClient kafkaClient;

    @BeforeEach
    public void setup() {
        trustyStorageService.getDecisionsStorage().clear();
        kafkaClient = new KafkaClient(kafkaBootstrapServers);
    }

    @Test
    void testCorrectCloudEvent() throws JsonProcessingException, JSONException {
        kafkaClient.produce(TrustyServiceTestUtils.buildCloudEventJsonString(TrustyServiceTestUtils.buildCorrectTraceEvent(TrustyServiceTestUtils.CORRECT_CLOUDEVENT_ID)),
                            KafkaConstants.KOGITO_TRACING_TOPIC);

        await()
                .atMost(5, SECONDS)
                .untilAsserted(() -> assertDoesNotThrow(() -> trustyService.getDecisionById(TrustyServiceTestUtils.CORRECT_CLOUDEVENT_ID)));

        Decision storedDecision = trustyService.getDecisionById(TrustyServiceTestUtils.CORRECT_CLOUDEVENT_ID);
        assertNotNull(storedDecision);
        JSONAssert.assertEquals(MAPPER.writeValueAsString(TrustyServiceTestUtils.buildCorrectDecision(TrustyServiceTestUtils.CORRECT_CLOUDEVENT_ID)), MAPPER.writeValueAsString(storedDecision), true);
    }

    @Test
    void testCloudEventWithErrors() throws JsonProcessingException, JSONException {
        kafkaClient.produce(TrustyServiceTestUtils.buildCloudEventJsonString(TrustyServiceTestUtils.buildTraceEventWithErrors()),
                            KafkaConstants.KOGITO_TRACING_TOPIC);

        await()
                .atMost(5, SECONDS)
                .untilAsserted(() -> assertDoesNotThrow(() -> trustyService.getDecisionById(TrustyServiceTestUtils.CLOUDEVENT_WITH_ERRORS_ID)));

        Decision storedDecision = trustyService.getDecisionById(TrustyServiceTestUtils.CLOUDEVENT_WITH_ERRORS_ID);
        assertNotNull(storedDecision);
        JSONAssert.assertEquals(MAPPER.writeValueAsString(TrustyServiceTestUtils.buildDecisionWithErrors()), MAPPER.writeValueAsString(storedDecision), true);
    }
}
