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

package org.kie.kogito.trusty.service.messaging.incoming;

import java.net.URI;
import java.util.List;

import io.cloudevents.v1.CloudEventImpl;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.api.ExplainabilityResultDto;
import org.kie.kogito.explainability.api.FeatureImportanceDto;
import org.kie.kogito.explainability.api.SaliencyDto;
import org.kie.kogito.tracing.decision.event.CloudEventUtils;
import org.kie.kogito.trusty.service.TrustyService;
import org.kie.kogito.trusty.storage.api.model.Decision;
import org.kie.kogito.trusty.storage.api.model.DecisionInput;
import org.kie.kogito.trusty.storage.api.model.DecisionOutcome;
import org.kie.kogito.trusty.storage.api.model.ExplainabilityResult;
import org.kie.kogito.trusty.storage.api.model.FeatureImportance;
import org.kie.kogito.trusty.storage.api.model.Saliency;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// TODO: improve this test with decision values
public class ExplainabilityResultConsumerTest {

    private static final String TEST_EXECUTION_ID = "test";

    private static final FeatureImportanceDto TEST_FEATURE_IMPORTANCE_DTO_1 = new FeatureImportanceDto("feature1", 1d);
    private static final FeatureImportanceDto TEST_FEATURE_IMPORTANCE_DTO_2 = new FeatureImportanceDto("feature2", 1d);
    private static final SaliencyDto TEST_SALIENCY_DTO = new SaliencyDto(asList(TEST_FEATURE_IMPORTANCE_DTO_1, TEST_FEATURE_IMPORTANCE_DTO_2));
    private static final ExplainabilityResultDto TEST_RESULT_DTO = new ExplainabilityResultDto("executionId", true, singletonMap("saliency", TEST_SALIENCY_DTO));
    private static final Decision TEST_DECISION = new Decision(
            TEST_EXECUTION_ID, null, null, true, null, null, null,
            List.of(
                    new DecisionInput("F1-ID", TEST_FEATURE_IMPORTANCE_DTO_1.getFeatureName(), null),
                    new DecisionInput("F2-ID", TEST_FEATURE_IMPORTANCE_DTO_2.getFeatureName(), null)
            ),
            List.of(
                    new DecisionOutcome("O1-ID", "saliency", null, null, null, null)
            )
    );

    private TrustyService trustyService;
    private ExplainabilityResultConsumer consumer;

    @BeforeEach
    void setup() {
        trustyService = mock(TrustyService.class);
        consumer = new ExplainabilityResultConsumer(trustyService);
    }

    @Test
    void testCorrectCloudEvent() {
        Message<String> message = mockMessage(buildCloudEventJsonString(new ExplainabilityResultDto(TEST_EXECUTION_ID, true, emptyMap())));
        doNothing().when(trustyService).storeExplainabilityResult(any(String.class), any(ExplainabilityResult.class));

        testNumberOfInvocations(message, 1);
    }

    @Test
    void testInvalidPayload() {
        Message<String> message = mockMessage("Not a cloud event");
        testNumberOfInvocations(message, 0);
    }

    @Test
    void testExceptionsAreCatched() {
        Message<String> message = mockMessage(buildCloudEventJsonString(new ExplainabilityResultDto(TEST_EXECUTION_ID, true, emptyMap())));

        doThrow(new RuntimeException("Something really bad")).when(trustyService).storeExplainabilityResult(any(String.class), any(ExplainabilityResult.class));
        Assertions.assertDoesNotThrow(() -> consumer.handleMessage(message));
    }

    @Test
    public void explainabilityResultFrom() {
        Assertions.assertNull(ExplainabilityResultConsumer.explainabilityResultFrom(null, null));

        ExplainabilityResult explainabilityResult = ExplainabilityResultConsumer.explainabilityResultFrom(TEST_RESULT_DTO, TEST_DECISION);

        Assertions.assertNotNull(explainabilityResult);
        Assertions.assertEquals(TEST_RESULT_DTO.getExecutionId(), explainabilityResult.getExecutionId());
        Assertions.assertEquals(TEST_RESULT_DTO.getSaliencies().size(), explainabilityResult.getSaliencies().size());
        Assertions.assertTrue(TEST_RESULT_DTO.getSaliencies().containsKey("saliency"));
        // Assertions.assertEquals(TEST_RESULT_DTO.getSaliencies().get("saliency").getFeatureImportance().size(),
        // explainabilityResult.getSaliencies().get("saliency").getFeatureImportance().size());
    }

    @Test
    public void featureImportanceFrom() {
        Assertions.assertNull(ExplainabilityResultConsumer.featureImportanceFrom(null));

        FeatureImportance featureImportance = ExplainabilityResultConsumer.featureImportanceFrom(TEST_FEATURE_IMPORTANCE_DTO_1);

        Assertions.assertNotNull(featureImportance);
        Assertions.assertEquals(TEST_FEATURE_IMPORTANCE_DTO_1.getFeatureName(), featureImportance.getFeatureName());
        Assertions.assertEquals(TEST_FEATURE_IMPORTANCE_DTO_1.getScore(), featureImportance.getScore());
    }

    @Test
    public void saliencyFrom() {
        Assertions.assertNull(ExplainabilityResultConsumer.saliencyFrom(null, null, null));

        Saliency saliency = ExplainabilityResultConsumer.saliencyFrom("O1-ID", "saliency", TEST_SALIENCY_DTO);

        Assertions.assertNotNull(saliency);
        Assertions.assertEquals(TEST_SALIENCY_DTO.getFeatureImportance().size(), saliency.getFeatureImportance().size());
        Assertions.assertEquals(TEST_SALIENCY_DTO.getFeatureImportance().get(0).getFeatureName(),
                saliency.getFeatureImportance().get(0).getFeatureName());
        Assertions.assertEquals(TEST_SALIENCY_DTO.getFeatureImportance().get(0).getScore(),
                saliency.getFeatureImportance().get(0).getScore(), 0.1);
    }

    private Message<String> mockMessage(String payload) {
        Message<String> message = mock(Message.class);
        when(message.getPayload()).thenReturn(payload);
        return message;
    }

    private void testNumberOfInvocations(Message<String> message, int wantedNumberOfServiceInvocations) {
        consumer.handleMessage(message);
        verify(trustyService, times(wantedNumberOfServiceInvocations)).storeExplainabilityResult(any(), any());
        verify(message, times(1)).ack();
    }

    public static CloudEventImpl<ExplainabilityResultDto> buildExplainabilityCloudEvent(ExplainabilityResultDto resultDto) {
        return CloudEventUtils.build(
                resultDto.getExecutionId(),
                URI.create("explainabilityResult/test"),
                resultDto,
                ExplainabilityResultDto.class
        );
    }

    public static String buildCloudEventJsonString(ExplainabilityResultDto resultDto) {
        return CloudEventUtils.encode(buildExplainabilityCloudEvent(resultDto));
    }
}
