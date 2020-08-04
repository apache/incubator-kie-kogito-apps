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

package org.kie.kogito.trusty.service.messaging;

import javax.inject.Inject;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.kafka.client.producer.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.messaging.commons.KafkaTestResource;
import org.kie.kogito.messaging.commons.KafkaUtils;
import org.kie.kogito.trusty.service.ITrustyService;
import org.kie.kogito.trusty.service.TrustyInfinispanServerTestResource;
import org.kie.kogito.trusty.storage.api.TrustyStorageService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.kie.kogito.messaging.commons.KafkaUtils.generateProducer;
import static org.kie.kogito.messaging.commons.KafkaUtils.sendToKafkaAndWaitForCompletion;
import static org.kie.kogito.trusty.service.TrustyServiceTestUtils.buildCloudEventJsonString;
import static org.kie.kogito.trusty.service.TrustyServiceTestUtils.buildCorrectModelEvent;

@QuarkusTest
@QuarkusTestResource(TrustyInfinispanServerTestResource.class)
@QuarkusTestResource(KafkaTestResource.class)
class ModelEventConsumerInfinispanIT {

    @Inject
    ITrustyService trustyService;

    @Inject
    TrustyStorageService trustyStorageService;

    KafkaProducer<String, String> producer;

    @BeforeEach
    public void setup() {
        trustyStorageService.getDecisionsStorage().clear();
        producer = generateProducer();
    }

    @Test
    void testCorrectCloudEvent() throws Exception {
        sendToKafkaAndWaitForCompletion(KafkaUtils.KOGITO_TRACING_MODEL_TOPIC,
                                        buildCloudEventJsonString(buildCorrectModelEvent()),
                                        producer);
        String storedDefinition = trustyService.getModelById("name:namespace");
        assertNotNull(storedDefinition);
        assertEquals("definition", storedDefinition);
    }
}
