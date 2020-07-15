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

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.trusty.service.TrustyInfinispanServerTestResource;
import org.kie.kogito.trusty.service.TrustyKafkaTestResource;
import org.kie.kogito.trusty.service.TrustyService;
import org.kie.kogito.trusty.storage.api.TrustyStorageService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@QuarkusTestResource(TrustyInfinispanServerTestResource.class)
@QuarkusTestResource(TrustyKafkaTestResource.class)
class TraceEventConsumerIT {

    @Inject
    TraceEventConsumer traceEventConsumer;
    @Inject
    TrustyService trustyService;
    @Inject
    TrustyStorageService trustyStorageService;

    KafkaProducer<String, String> producer;

    @BeforeEach
    public void setup() {
        trustyStorageService.getDecisionsStorage().clear();

        System.err.println("kafka.bootstrap.servers: " + System.getProperty(TrustyKafkaTestResource.KAFKA_BOOTSTRAP_SERVERS, "localhost:9092"));

        producer = KafkaProducer.create(Vertx.vertx(), Map.of(
                "bootstrap.servers", System.getProperty(TrustyKafkaTestResource.KAFKA_BOOTSTRAP_SERVERS, "localhost:9092"),
                "key.serializer", "org.apache.kafka.common.serialization.StringSerializer",
                "value.serializer", "org.apache.kafka.common.serialization.StringSerializer",
                "acks", "all"
        ));
    }

    @Test
    void testCorrectCloudEvent() throws Exception {
        sendToKafkaAndRun(IOUtils.resourceToString("/TraceEventTest_correct_CloudEvent.json", StandardCharsets.UTF_8), () ->
                assertNotNull(trustyService.getDecisionById("82639415-ceb1-411a-b3c8-4832e6a82905"))
        );
    }

    @Test
    void testCloudEventWithErrors() throws Exception {
        sendToKafkaAndRun(IOUtils.resourceToString("/TraceEventTest_error_CloudEvent.json", StandardCharsets.UTF_8), () ->
                assertNotNull(trustyService.getDecisionById("6f8f5a8b-5477-464c-b5d3-1e3ed399e0da"))
        );
    }

    private CompletableFuture<Void> sendToKafka(String payload) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        producer.write(KafkaProducerRecord.create("trusty-service-test", payload), event -> {
            if (event.succeeded()) {
                future.complete(null);
            } else {
                future.completeExceptionally(event.cause());
            }
        });
        return future;
    }

    private void sendToKafkaAndRun(String payload, Runnable runnable) throws Exception {
        sendToKafka(payload)
                .thenRunAsync(runnable, CompletableFuture.delayedExecutor(2L, TimeUnit.SECONDS))
                .get(15L, TimeUnit.SECONDS);
    }

}
