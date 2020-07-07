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

package org.kie.kogito.trusty.service.tracing;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;

import com.fasterxml.jackson.core.type.TypeReference;
import io.cloudevents.json.Json;
import io.cloudevents.v1.CloudEventImpl;
import org.junit.jupiter.api.Test;
import org.kie.kogito.tracing.decision.event.trace.TraceEvent;
import org.kie.kogito.trusty.storage.api.model.Decision;
import org.kie.kogito.trusty.storage.api.model.DecisionOutcome;
import org.kie.kogito.trusty.storage.api.model.Message;
import org.kie.kogito.trusty.storage.api.model.MessageExceptionField;
import org.kie.kogito.trusty.storage.api.model.TypedValue;
import org.testcontainers.shaded.org.apache.commons.lang.builder.CompareToBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

public class TraceEventConverterTest {

    @Test
    public void testCorrectTraceEvent() {
        doTest("/TraceEventTest_correct_CloudEvent.json", "/TraceEventTest_correct_Decision.json");
    }

    @Test
    public void testTraceEventWithError() {
        doTest("/TraceEventTest_error_CloudEvent.json", "/TraceEventTest_error_Decision.json");
    }

    @Test
    public void testTraceEventWithNullFields() {
        doTest("/TraceEventTest_nullFields_CloudEvent.json", "/TraceEventTest_nullFields_Decision.json");
    }

    private void doTest(String cloudEventResource, String decisionResource) {
        TraceEvent traceEvent = readCloudEventFromResource(cloudEventResource)
                .getData()
                .orElseThrow(IllegalStateException::new);
        Decision expectedDecision = readDecisionFromResource(decisionResource);

        TraceEventConverter converter = new TraceEventConverter();
        Decision actualDecision = converter.toDecision(traceEvent);

        assertDecision(expectedDecision, actualDecision);
    }

    private void assertDecision(Decision expected, Decision actual) {
        assertEquals(expected.getExecutionId(), actual.getExecutionId());
        assertSame(expected.getExecutionType(), actual.getExecutionType());
        assertEquals(expected.getExecutionTimestamp(), actual.getExecutionTimestamp());
        assertEquals(expected.getExecutedModelName(), actual.getExecutedModelName());
        assertEquals(expected.getExecutorName(), actual.getExecutorName());
        assertList(expected.getInputs(), actual.getInputs(), this::assertTypedValue, this::compareTypedValue);
        assertList(expected.getOutcomes(), actual.getOutcomes(), this::assertDecisionOutcome, this::compareDecisionOutcome);
    }

    private void assertDecisionOutcome(DecisionOutcome expected, DecisionOutcome actual) {
        assertEquals(expected.getOutcomeId(), actual.getOutcomeId());
        assertEquals(expected.getOutcomeName(), actual.getOutcomeName());
        assertTypedValue(expected.getOutcomeResult(), actual.getOutcomeResult());
        assertEquals(expected.getEvaluationStatus(), actual.getEvaluationStatus());
        assertList(expected.getOutcomeInputs(), actual.getOutcomeInputs(), this::assertTypedValue, this::compareTypedValue);
        assertList(expected.getMessages(), actual.getMessages(), this::assertMessage, this::compareMessage);
    }

    private <T> void assertList(List<T> expected, List<T> actual, BiConsumer<T, T> itemAssertor, Comparator<? super T> comparator) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || actual == null) {
            fail();
        }
        assertSame(expected.size(), actual.size());

        expected.sort(comparator);
        actual.sort(comparator);

        for (int i = 0; i < expected.size(); i++) {
            itemAssertor.accept(expected.get(0), actual.get(0));
        }
    }

    private void assertMessage(Message expected, Message actual) {
        assertSame(expected.getLevel(), actual.getLevel());
        assertEquals(expected.getCategory(), actual.getCategory());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getSourceId(), actual.getSourceId());
        assertEquals(expected.getText(), actual.getText());
        assertMessageExceptionField(expected.getException(), actual.getException());
    }

    private void assertMessageExceptionField(MessageExceptionField expected, MessageExceptionField actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || actual == null) {
            fail();
        }
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getClassName(), actual.getClassName());
        assertMessageExceptionField(expected.getCause(), actual.getCause());
    }

    private void assertTypedValue(TypedValue expected, TypedValue actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getTypeRef(), actual.getTypeRef());
        assertEquals(expected.getValue(), actual.getValue());
    }

    private int compareDecisionOutcome(DecisionOutcome expected, DecisionOutcome actual) {
        return new CompareToBuilder()
                .append(expected.getOutcomeId(), actual.getOutcomeId())
                .append(expected.getOutcomeName(), actual.getOutcomeName())
                .append(expected.getEvaluationStatus(), actual.getEvaluationStatus())
                .toComparison();
    }

    private int compareMessage(Message expected, Message actual) {
        return new CompareToBuilder()
                .append(expected.getLevel(), actual.getLevel())
                .append(expected.getCategory(), actual.getCategory())
                .append(expected.getType(), actual.getType())
                .append(expected.getText(), actual.getText())
                .toComparison();
    }

    private int compareTypedValue(TypedValue expected, TypedValue actual) {
        return new CompareToBuilder()
                .append(expected.getTypeRef(), actual.getTypeRef())
                .append(expected.getName(), actual.getName())
                .toComparison();
    }

    private CloudEventImpl<TraceEvent> readCloudEventFromResource(String resourceName) {
        return Json.fromInputStream(TraceEventConverterTest.class.getResourceAsStream(resourceName), new TypeReference<>() {
        });
    }

    private Decision readDecisionFromResource(String resourceName) {
        return Json.fromInputStream(TraceEventConverterTest.class.getResourceAsStream(resourceName), Decision.class);
    }
}
