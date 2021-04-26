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

package org.kie.kogito.trusty.service.common;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.api.BaseExplainabilityRequestDto;
import org.kie.kogito.explainability.api.CounterfactualDomainRangeDto;
import org.kie.kogito.explainability.api.CounterfactualExplainabilityRequestDto;
import org.kie.kogito.explainability.api.CounterfactualSearchDomainDto;
import org.kie.kogito.explainability.api.CounterfactualSearchDomainUnitDto;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.query.Query;
import org.kie.kogito.tracing.typedvalue.TypedValue;
import org.kie.kogito.trusty.service.common.messaging.incoming.ModelIdentifier;
import org.kie.kogito.trusty.service.common.messaging.outgoing.ExplainabilityRequestProducer;
import org.kie.kogito.trusty.service.common.mocks.StorageImplMock;
import org.kie.kogito.trusty.service.common.models.MatchedExecutionHeaders;
import org.kie.kogito.trusty.storage.api.model.BaseExplainabilityResult;
import org.kie.kogito.trusty.storage.api.model.CounterfactualDomainRange;
import org.kie.kogito.trusty.storage.api.model.CounterfactualRequest;
import org.kie.kogito.trusty.storage.api.model.CounterfactualSearchDomain;
import org.kie.kogito.trusty.storage.api.model.DMNModelWithMetadata;
import org.kie.kogito.trusty.storage.api.model.Decision;
import org.kie.kogito.trusty.storage.api.model.DecisionInput;
import org.kie.kogito.trusty.storage.api.model.DecisionOutcome;
import org.kie.kogito.trusty.storage.api.model.ExplainabilityStatus;
import org.kie.kogito.trusty.storage.api.model.LIMEExplainabilityResult;
import org.kie.kogito.trusty.storage.api.model.TypedVariableWithValue;
import org.kie.kogito.trusty.storage.common.TrustyStorageService;
import org.mockito.ArgumentCaptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TrustyServiceTest {

    private static final String TEST_EXECUTION_ID = "executionId";
    private static final String TEST_MODEL = "definition";
    private static final String TEST_SOURCE_URL = "http://localhost:8080/model/service";
    private static final String TEST_SERVICE_URL = "http://localhost:8080/model";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ExplainabilityRequestProducer explainabilityRequestProducerMock;
    private TrustyStorageService trustyStorageServiceMock;
    private TrustyServiceImpl trustyService;

    private static JsonNode toJsonNode(String jsonString) throws JsonProcessingException {
        return MAPPER.reader().readTree(jsonString);
    }

    @BeforeEach
    void setup() {
        explainabilityRequestProducerMock = mock(ExplainabilityRequestProducer.class);
        trustyStorageServiceMock = mock(TrustyStorageService.class);
        trustyService = new TrustyServiceImpl(false, explainabilityRequestProducerMock, trustyStorageServiceMock);
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenADecisionWhenStoreDecisionIsCalledThenNoExceptionsAreThrown() {
        Decision decision = new Decision();
        Storage storageMock = mock(Storage.class);
        when(storageMock.put(any(Object.class), any(Object.class))).thenReturn(decision);
        when(trustyStorageServiceMock.getDecisionsStorage()).thenReturn(storageMock);

        Assertions.assertDoesNotThrow(() -> trustyService.storeDecision("test", decision));
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenADecisionWhenADecisionIsStoredAndRetrievedThenTheOriginalObjectIsReturned() {
        Decision decision = new Decision();
        decision.setExecutionId(TEST_EXECUTION_ID);

        Query queryMock = mock(Query.class);
        when(queryMock.filter(any(List.class))).thenReturn(queryMock);
        when(queryMock.offset(any(Integer.class))).thenReturn(queryMock);
        when(queryMock.sort(any(List.class))).thenReturn(queryMock);
        when(queryMock.execute()).thenReturn(List.of(decision));

        Storage storageMock = mock(Storage.class);
        when(storageMock.put(eq(TEST_EXECUTION_ID), any(Object.class))).thenReturn(decision);
        when(storageMock.containsKey(eq(TEST_EXECUTION_ID))).thenReturn(false);
        when(storageMock.query()).thenReturn(queryMock);

        when(trustyStorageServiceMock.getDecisionsStorage()).thenReturn(storageMock);

        trustyService.storeDecision(TEST_EXECUTION_ID, decision);

        MatchedExecutionHeaders result = trustyService.getExecutionHeaders(OffsetDateTime.now().minusDays(1), OffsetDateTime.now(), 100, 0, "");

        assertEquals(1, result.getExecutions().size());
        assertEquals(decision.getExecutionId(), result.getExecutions().get(0).getExecutionId());
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenManyExecutionsThenPaginationWorksProperly() {
        List<Decision> decisions = new ArrayList<>();
        IntStream.range(0, 10).forEach(x -> {
            Decision d = new Decision();
            d.setExecutionId(String.valueOf(x));
            decisions.add(d);
        });

        Query queryMock = mock(Query.class);
        when(queryMock.filter(any(List.class))).thenReturn(queryMock);
        when(queryMock.sort(any(List.class))).thenReturn(queryMock);
        when(queryMock.execute()).thenReturn(decisions);

        Storage storageMock = mock(Storage.class);
        decisions.forEach(x -> {
            when(storageMock.put(eq(x.getExecutionId()), any(Object.class))).thenReturn(x);
            when(storageMock.containsKey(eq(x.getExecutionId()))).thenReturn(false);
        });
        when(storageMock.query()).thenReturn(queryMock);

        when(trustyStorageServiceMock.getDecisionsStorage()).thenReturn(storageMock);

        decisions.forEach(x -> trustyService.storeDecision(x.getExecutionId(), x));

        MatchedExecutionHeaders result = trustyService.getExecutionHeaders(OffsetDateTime.now().minusDays(1), OffsetDateTime.now(), 3, 5, "");

        assertEquals(3, result.getExecutions().size());
        assertEquals(decisions.size(), result.getAvailableResults());

        result = trustyService.getExecutionHeaders(OffsetDateTime.now().minusDays(1), OffsetDateTime.now(), 100, 5, "");

        assertEquals(5, result.getExecutions().size());
        assertEquals(decisions.size(), result.getAvailableResults());
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenNoExecutionsNoExceptionsAreRaised() {
        Query queryMock = mock(Query.class);
        when(queryMock.filter(any(List.class))).thenReturn(queryMock);
        when(queryMock.sort(any(List.class))).thenReturn(queryMock);
        when(queryMock.execute()).thenReturn(new ArrayList<>());

        Storage storageMock = mock(Storage.class);
        when(storageMock.query()).thenReturn(queryMock);

        when(trustyStorageServiceMock.getDecisionsStorage()).thenReturn(storageMock);

        MatchedExecutionHeaders result = trustyService.getExecutionHeaders(OffsetDateTime.now().minusDays(1), OffsetDateTime.now(), 100, 0, "");

        assertEquals(0, result.getExecutions().size());
        assertEquals(0, result.getAvailableResults());
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenADecisionWhenADecisionIsStoredAndRetrievedByIdThenTheOriginalObjectIsReturned() {
        Decision decision = new Decision();
        decision.setExecutionId(TEST_EXECUTION_ID);

        @SuppressWarnings("unchecked")
        Storage storageMock = new StorageImplMock(Decision.class);

        when(trustyStorageServiceMock.getDecisionsStorage()).thenReturn(storageMock);

        trustyService.storeDecision(TEST_EXECUTION_ID, decision);

        Decision result = trustyService.getDecisionById(TEST_EXECUTION_ID);

        assertEquals(TEST_EXECUTION_ID, result.getExecutionId());
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenADecisionToProcessWhenExplainabilityIsEnabledThenRequestIsSent() throws JsonProcessingException {
        trustyService.enableExplainability();

        Decision decision = new Decision(
                TEST_EXECUTION_ID, TEST_SOURCE_URL, TEST_SERVICE_URL, 1591692950000L, true,
                null, "model", "modelNamespace",
                List.of(
                        new DecisionInput("1", "Input1", TypedVariableWithValue.buildCollection(
                                "testList", "string", List.of(
                                        TypedVariableWithValue.buildUnit(null, "string", toJsonNode("\"ONE\"")),
                                        TypedVariableWithValue.buildUnit(null, "string", toJsonNode("\"TWO\""))))),
                        new DecisionInput("2", "Input2", TypedVariableWithValue.buildStructure(
                                "author", "Person", List.of(
                                        TypedVariableWithValue.buildUnit("Name", "string", toJsonNode("\"George Orwell\"")),
                                        TypedVariableWithValue.buildUnit("Age", "number", toJsonNode("45")))))),
                List.of(
                        new DecisionOutcome(
                                "OUT1", "Result", "SUCCEEDED",
                                TypedVariableWithValue.buildUnit("Result", "string", toJsonNode("\"YES\"")),
                                Collections.emptyList(), Collections.emptyList())));

        Storage<String, Decision> decisionStorageMock = mock(Storage.class);
        when(decisionStorageMock.containsKey(eq(TEST_EXECUTION_ID))).thenReturn(false);

        when(trustyStorageServiceMock.getDecisionsStorage()).thenReturn(decisionStorageMock);

        trustyService.processDecision(TEST_EXECUTION_ID, decision);

        verify(explainabilityRequestProducerMock).sendEvent(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenADecisionToProcessThatAlreadyExistsWhenExplainabilityIsEnabledThenExceptionIsThrown() {
        trustyService.enableExplainability();

        Decision decision = new Decision();
        decision.setExecutionId(TEST_EXECUTION_ID);

        Storage<String, Decision> decisionStorageMock = mock(Storage.class);
        when(decisionStorageMock.containsKey(eq(TEST_EXECUTION_ID))).thenReturn(true);

        when(trustyStorageServiceMock.getDecisionsStorage()).thenReturn(decisionStorageMock);

        assertThrows(IllegalArgumentException.class, () -> trustyService.processDecision(TEST_EXECUTION_ID, decision));

        verify(explainabilityRequestProducerMock, never()).sendEvent(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenAModelWhenStoreModelIsCalledThenNoExceptionsAreThrown() {
        String model = TEST_MODEL;
        Storage storageMock = mock(Storage.class);

        when(storageMock.put(any(Object.class), any(Object.class))).thenReturn(model);
        when(trustyStorageServiceMock.getModelStorage()).thenReturn(storageMock);

        Assertions.assertDoesNotThrow(() -> trustyService.storeModel(buildDmnModelIdentifier(), buildDmnModel(model)));
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenAModelWhenStoreModelIsCalledMoreThanOnceForSameModelThenExceptionIsThrown() {
        ModelIdentifier modelIdentifier = buildDmnModelIdentifier();
        String model = TEST_MODEL;
        Storage storageMock = mock(Storage.class);

        when(storageMock.containsKey(modelIdentifier.getIdentifier())).thenReturn(true);
        when(storageMock.put(any(Object.class), any(Object.class))).thenReturn(model);
        when(trustyStorageServiceMock.getModelStorage()).thenReturn(storageMock);

        assertThrows(IllegalArgumentException.class, () -> trustyService.storeModel(modelIdentifier, buildDmnModel(model)));
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenAModelWhenAModelIsStoredAndRetrievedByIdThenTheOriginalObjectIsReturned() {
        ModelIdentifier modelIdentifier = buildDmnModelIdentifier();
        String model = TEST_MODEL;
        Storage storageMock = new StorageImplMock(String.class);

        when(trustyStorageServiceMock.getModelStorage()).thenReturn(storageMock);

        trustyService.storeModel(modelIdentifier, buildDmnModel(model));

        DMNModelWithMetadata result = trustyService.getModelById(modelIdentifier);

        assertEquals(model, result.getModel());
    }

    @Test
    @SuppressWarnings("unchecked")
    void whenAModelIsNotStoredAndRetrievedByIdThenExceptionIsThrown() {
        ModelIdentifier modelIdentifier = buildDmnModelIdentifier();
        Storage storageMock = mock(Storage.class);

        when(storageMock.containsKey(modelIdentifier.getIdentifier())).thenReturn(false);
        when(trustyStorageServiceMock.getModelStorage()).thenReturn(storageMock);

        assertThrows(IllegalArgumentException.class, () -> trustyService.getModelById(modelIdentifier));
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenAnExplainabilityResultWhenStoreModelIsCalledThenNoExceptionsAreThrown() {
        LIMEExplainabilityResult result = new LIMEExplainabilityResult(TEST_EXECUTION_ID, ExplainabilityStatus.SUCCEEDED, null, Collections.emptyList());
        Storage<String, BaseExplainabilityResult> storageMock = mock(Storage.class);

        when(storageMock.put(eq(TEST_EXECUTION_ID), any(BaseExplainabilityResult.class))).thenReturn(result);
        when(trustyStorageServiceMock.getExplainabilityResultStorage()).thenReturn(storageMock);

        Assertions.assertDoesNotThrow(() -> trustyService.storeExplainabilityResult(TEST_EXECUTION_ID, result));
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenAnExplainabilityResultWhenStoreModelIsCalledMoreThanOnceForSameModelThenExceptionIsThrown() {
        LIMEExplainabilityResult result = new LIMEExplainabilityResult(TEST_EXECUTION_ID, ExplainabilityStatus.SUCCEEDED, null, Collections.emptyList());
        Storage<String, BaseExplainabilityResult> storageMock = mock(Storage.class);

        when(storageMock.containsKey(eq(TEST_EXECUTION_ID))).thenReturn(true);
        when(storageMock.put(eq(TEST_EXECUTION_ID), any(BaseExplainabilityResult.class))).thenReturn(result);
        when(trustyStorageServiceMock.getExplainabilityResultStorage()).thenReturn(storageMock);

        assertThrows(IllegalArgumentException.class, () -> trustyService.storeExplainabilityResult(TEST_EXECUTION_ID, result));
    }

    @Test
    void givenAnExplainabilityResultWhenAnExplainabilityResultIsStoredAndRetrievedByIdThenTheOriginalObjectIsReturned() {
        LIMEExplainabilityResult result = new LIMEExplainabilityResult(TEST_EXECUTION_ID, ExplainabilityStatus.SUCCEEDED, null, Collections.emptyList());
        Storage<String, BaseExplainabilityResult> storageMock = new StorageImplMock<>(String.class);

        when(trustyStorageServiceMock.getExplainabilityResultStorage()).thenReturn(storageMock);

        trustyService.storeExplainabilityResult(TEST_EXECUTION_ID, result);

        assertEquals(result, trustyService.getExplainabilityResultById(TEST_EXECUTION_ID));
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenAnExplainabilityResultNotStoredWhenRetrievedByIdThenExceptionIsThrown() {
        Storage<String, BaseExplainabilityResult> storageMock = mock(Storage.class);

        when(storageMock.containsKey(eq(TEST_EXECUTION_ID))).thenReturn(false);
        when(trustyStorageServiceMock.getExplainabilityResultStorage()).thenReturn(storageMock);

        assertThrows(IllegalArgumentException.class, () -> trustyService.getExplainabilityResultById(TEST_EXECUTION_ID));
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenNoStoredExecutionWhenCounterfactualRequestIsMadeThenExceptionIsThrown() {
        Storage<String, Decision> decisionStorage = mock(Storage.class);

        when(decisionStorage.containsKey(eq(TEST_EXECUTION_ID))).thenReturn(false);
        when(trustyStorageServiceMock.getDecisionsStorage()).thenReturn(decisionStorage);

        assertThrows(IllegalArgumentException.class, () -> trustyService.requestCounterfactuals(TEST_EXECUTION_ID, Collections.emptyList(), Collections.emptyList()));
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenStoredExecutionWhenCounterfactualRequestIsMadeThenRequestIsStored() {
        Storage<String, Decision> decisionStorage = mock(Storage.class);
        Storage<String, CounterfactualRequest> counterfactualStorage = mock(Storage.class);
        ArgumentCaptor<CounterfactualRequest> counterfactualArgumentCaptor = ArgumentCaptor.forClass(CounterfactualRequest.class);

        when(decisionStorage.containsKey(eq(TEST_EXECUTION_ID))).thenReturn(true);
        when(trustyStorageServiceMock.getDecisionsStorage()).thenReturn(decisionStorage);
        when(trustyStorageServiceMock.getCounterfactualRequestStorage()).thenReturn(counterfactualStorage);
        when(decisionStorage.get(eq(TEST_EXECUTION_ID))).thenReturn(TrustyServiceTestUtils.buildCorrectDecision(TEST_EXECUTION_ID));

        trustyService.requestCounterfactuals(TEST_EXECUTION_ID, Collections.emptyList(), Collections.emptyList());

        verify(counterfactualStorage).put(anyString(), counterfactualArgumentCaptor.capture());
        CounterfactualRequest counterfactual = counterfactualArgumentCaptor.getValue();
        assertNotNull(counterfactual);
        assertEquals(TEST_EXECUTION_ID, counterfactual.getExecutionId());
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenStoredExecutionWhenCounterfactualRequestIsMadeThenExplainabilityEventIsEmitted() {
        Storage<String, Decision> decisionStorage = mock(Storage.class);
        Storage<String, CounterfactualRequest> counterfactualStorage = mock(Storage.class);
        ArgumentCaptor<BaseExplainabilityRequestDto> explainabilityEventArgumentCaptor = ArgumentCaptor.forClass(BaseExplainabilityRequestDto.class);

        when(decisionStorage.containsKey(eq(TEST_EXECUTION_ID))).thenReturn(true);
        when(trustyStorageServiceMock.getDecisionsStorage()).thenReturn(decisionStorage);
        when(trustyStorageServiceMock.getCounterfactualRequestStorage()).thenReturn(counterfactualStorage);
        when(decisionStorage.get(eq(TEST_EXECUTION_ID))).thenReturn(TrustyServiceTestUtils.buildCorrectDecision(TEST_EXECUTION_ID));

        trustyService.requestCounterfactuals(TEST_EXECUTION_ID, Collections.emptyList(), Collections.emptyList());

        verify(explainabilityRequestProducerMock).sendEvent(explainabilityEventArgumentCaptor.capture());
        BaseExplainabilityRequestDto event = explainabilityEventArgumentCaptor.getValue();
        assertNotNull(event);
        assertTrue(event instanceof CounterfactualExplainabilityRequestDto);
        CounterfactualExplainabilityRequestDto request = (CounterfactualExplainabilityRequestDto) event;
        assertEquals(TEST_EXECUTION_ID, request.getExecutionId());
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenStoredExecutionWhenCounterfactualRequestIsMadeThenExplainabilityEventHasCorrectPaytload() {
        Storage<String, Decision> decisionStorage = mock(Storage.class);
        Storage<String, CounterfactualRequest> counterfactualStorage = mock(Storage.class);
        ArgumentCaptor<BaseExplainabilityRequestDto> explainabilityEventArgumentCaptor = ArgumentCaptor.forClass(BaseExplainabilityRequestDto.class);

        Decision decision = new Decision(
                TEST_EXECUTION_ID, TEST_SOURCE_URL, TEST_SERVICE_URL, 0L, true,
                null, "model", "modelNamespace",
                List.of(
                        new DecisionInput("IN1", "yearsOfService", TypedVariableWithValue.buildUnit(
                                "yearsOfService", "integer", new IntNode(10)))),
                List.of(
                        new DecisionOutcome(
                                "OUT1", "salary", "SUCCEEDED",
                                TypedVariableWithValue.buildUnit("salary", "integer", new IntNode(1000)),
                                Collections.emptyList(), Collections.emptyList())));

        when(decisionStorage.containsKey(eq(TEST_EXECUTION_ID))).thenReturn(true);
        when(trustyStorageServiceMock.getDecisionsStorage()).thenReturn(decisionStorage);
        when(trustyStorageServiceMock.getCounterfactualRequestStorage()).thenReturn(counterfactualStorage);
        when(decisionStorage.get(eq(TEST_EXECUTION_ID))).thenReturn(decision);

        trustyService.requestCounterfactuals(TEST_EXECUTION_ID,
                List.of(new TypedVariableWithValue(TypedValue.Kind.UNIT, "salary", "integer", new IntNode(2000), null)),
                List.of(new CounterfactualSearchDomain(TypedValue.Kind.UNIT,
                        "yearsOfService",
                        "integer",
                        Collections.emptyList(),
                        Boolean.FALSE,
                        new CounterfactualDomainRange(new IntNode(10), new IntNode(30)))));

        verify(explainabilityRequestProducerMock).sendEvent(explainabilityEventArgumentCaptor.capture());
        BaseExplainabilityRequestDto event = explainabilityEventArgumentCaptor.getValue();
        CounterfactualExplainabilityRequestDto request = (CounterfactualExplainabilityRequestDto) event;
        assertEquals(TEST_EXECUTION_ID, request.getExecutionId());
        assertEquals(TEST_SERVICE_URL, request.getServiceUrl());

        //Check original input value has been copied into CF request
        assertEquals(1, request.getInputs().size());
        assertTrue(request.getInputs().containsKey("yearsOfService"));
        assertEquals(decision.getInputs().iterator().next().getValue().getValue().asInt(),
                request.getInputs().get("yearsOfService").toUnit().getValue().asInt());

        //Check CF goals have been copied into CF request
        assertEquals(1, request.getOutputs().size());
        assertTrue(request.getOutputs().containsKey("salary"));
        assertEquals(2000,
                request.getOutputs().get("salary").toUnit().getValue().asInt());

        //Check CF search domains have been copied into CF request
        assertEquals(1, request.getSearchDomains().size());
        assertTrue(request.getSearchDomains().containsKey("yearsOfService"));
        CounterfactualSearchDomainDto searchDomain = request.getSearchDomains().get("yearsOfService");
        assertTrue(searchDomain instanceof CounterfactualSearchDomainUnitDto);

        CounterfactualSearchDomainUnitDto unit = (CounterfactualSearchDomainUnitDto) searchDomain;
        assertFalse(unit.isFixed());
        assertNotNull(unit.getDomain());
        assertTrue(unit.getDomain() instanceof CounterfactualDomainRangeDto);

        CounterfactualDomainRangeDto range = (CounterfactualDomainRangeDto) unit.getDomain();
        assertEquals(10, range.getLowerBound().asInt());
        assertEquals(30, range.getUpperBound().asInt());
    }

    private DMNModelWithMetadata buildDmnModel(String model) {
        return new DMNModelWithMetadata("groupId", "artifactId", "modelVersion", "dmnVersion", "name", "namespace", model);
    }

    private ModelIdentifier buildDmnModelIdentifier() {
        return new ModelIdentifier("groupId", "artifactId", "version", "name", "namespace");
    }

}
