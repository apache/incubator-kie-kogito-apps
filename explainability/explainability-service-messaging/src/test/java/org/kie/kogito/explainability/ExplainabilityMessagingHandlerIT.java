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
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.api.ExplainabilityRequestDto;
import org.kie.kogito.explainability.api.ExplainabilityResultDto;
import org.kie.kogito.explainability.models.ExplainabilityRequest;
import org.kie.kogito.kafka.KafkaClient;
import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;
import org.kie.kogito.tracing.decision.event.CloudEventUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
@QuarkusTestResource(KafkaQuarkusTestResource.class)
public class ExplainabilityMessagingHandlerIT {

    private static final String TOPIC = "trusty-explainability-request-test";

    @ConfigProperty(name = KafkaQuarkusTestResource.KOGITO_KAFKA_PROPERTY)
    private String kafkaBootstrapServers;

    @InjectMock
    IExplanationService explanationService;

    @Test
    public void explainabilityRequestIsProcessed() throws Exception {
        KafkaClient kafkaClient = new KafkaClient(kafkaBootstrapServers);

        String executionId = "idException";
        ExplainabilityRequestDto request = new ExplainabilityRequestDto(executionId);
        when(explanationService.explainAsync(any(ExplainabilityRequest.class))).thenReturn(CompletableFuture.completedFuture(new ExplainabilityResultDto(executionId)));

        kafkaClient.produce(buildCloudEventJsonString(request), TOPIC);

        verify(explanationService, timeout(1000).times(1)).explainAsync(any(ExplainabilityRequest.class));
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
