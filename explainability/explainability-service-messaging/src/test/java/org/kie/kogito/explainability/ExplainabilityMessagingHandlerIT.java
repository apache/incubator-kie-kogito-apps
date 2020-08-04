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

package org.kie.kogito.explainability;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

import io.cloudevents.v1.CloudEventImpl;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.vertx.kafka.client.producer.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.api.ExplainabilityRequestDto;
import org.kie.kogito.explainability.api.ExplainabilityResultDto;
import org.kie.kogito.explainability.models.ExplainabilityRequest;
import org.kie.kogito.messaging.commons.KafkaTestResource;
import org.kie.kogito.tracing.decision.event.CloudEventUtils;

import static org.kie.kogito.messaging.commons.KafkaUtils.generateProducer;
import static org.kie.kogito.messaging.commons.KafkaUtils.sendToKafkaAndWaitForCompletion;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
@QuarkusTestResource(KafkaTestResource.class)
public class ExplainabilityMessagingHandlerIT {

    private static final String TOPIC = "trusty-explainability-request-test";

    @InjectMock
    IExplanationService explanationService;

    KafkaProducer<String, String> producer;

    @BeforeEach
    public void setup() {
        producer = generateProducer();
    }

    @Test
    public void explainabilityRequestIsProcessed() throws Exception {
        String executionId = "idException";
        ExplainabilityRequestDto request = new ExplainabilityRequestDto(executionId);
        when(explanationService.explainAsync(any(ExplainabilityRequest.class))).thenReturn(CompletableFuture.completedFuture(new ExplainabilityResultDto(executionId)));

        sendToKafkaAndWaitForCompletion(TOPIC, buildCloudEventJsonString(request), producer);

        verify(explanationService, times(1)).explainAsync(any(ExplainabilityRequest.class));
    }

    private CloudEventImpl<ExplainabilityRequestDto> buildCloudEvent(ExplainabilityRequestDto request) {
        return CloudEventUtils.build(
                request.getExecutionId(),
                URI.create("trustyService/test"),
                request,
                ExplainabilityRequestDto.class
        );
    }

    public String buildCloudEventJsonString(ExplainabilityRequestDto request) {
        return CloudEventUtils.encode(buildCloudEvent(request));
    }

}
