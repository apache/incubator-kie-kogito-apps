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

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.trusty.service.common.messaging.incoming.ModelIdentifier;
import org.kie.kogito.trusty.service.common.models.MatchedExecutionHeaders;
import org.kie.kogito.trusty.storage.api.model.CounterfactualDomain;
import org.kie.kogito.trusty.storage.api.model.CounterfactualDomainCategorical;
import org.kie.kogito.trusty.storage.api.model.CounterfactualDomainRange;
import org.kie.kogito.trusty.storage.api.model.CounterfactualRequest;
import org.kie.kogito.trusty.storage.api.model.CounterfactualSearchDomain;
import org.kie.kogito.trusty.storage.api.model.DMNModelWithMetadata;
import org.kie.kogito.trusty.storage.api.model.Decision;
import org.kie.kogito.trusty.storage.api.model.TypedVariableWithValue;
import org.kie.kogito.trusty.storage.common.TrustyStorageService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractTrustyServiceIT {

    @Inject
    TrustyService trustyService;

    @Inject
    TrustyStorageService trustyStorageService;

    @BeforeEach
    public void setup() {
        trustyStorageService.getCounterfactualRequestStorage().clear();
        trustyStorageService.getCounterfactualResultStorage().clear();
        trustyStorageService.getExplainabilityResultStorage().clear();
        trustyStorageService.getDecisionsStorage().clear();
        trustyStorageService.getModelStorage().clear();
    }

    @Test
    public void testStoreAndRetrieveExecution() {
        storeExecution("myExecution", 1591692958000L);

        OffsetDateTime from = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1591692957000L), ZoneOffset.UTC);
        OffsetDateTime to = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1591692959000L), ZoneOffset.UTC);
        MatchedExecutionHeaders result = trustyService.getExecutionHeaders(from, to, 100, 0, "");
        Assertions.assertEquals(1, result.getExecutions().size());
        Assertions.assertEquals("myExecution", result.getExecutions().get(0).getExecutionId());
    }

    @Test
    public void givenTwoExecutionsWhenTheQueryExcludesOneExecutionThenOnlyOneExecutionIsReturned() {
        storeExecution("myExecution", 1591692950000L);
        storeExecution("executionId2", 1591692958000L);

        OffsetDateTime from = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1591692940000L), ZoneOffset.UTC);
        OffsetDateTime to = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1591692955000L), ZoneOffset.UTC);
        MatchedExecutionHeaders result = trustyService.getExecutionHeaders(from, to, 100, 0, "");
        Assertions.assertEquals(1, result.getExecutions().size());
        Assertions.assertEquals("myExecution", result.getExecutions().get(0).getExecutionId());
    }

    @Test
    public void givenTwoExecutionsWhenThePrefixIsUsedThenOnlyOneExecutionIsReturned() {
        storeExecution("myExecution", 1591692950000L);
        storeExecution("executionId2", 1591692958000L);

        OffsetDateTime from = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1591692940000L), ZoneOffset.UTC);
        OffsetDateTime to = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1591692959000L), ZoneOffset.UTC);
        MatchedExecutionHeaders result = trustyService.getExecutionHeaders(from, to, 100, 0, "my");
        Assertions.assertEquals(1, result.getExecutions().size());
        Assertions.assertEquals("myExecution", result.getExecutions().get(0).getExecutionId());
    }

    @Test
    public void givenAnExecutionWhenGetDecisionByIdIsCalledThenTheExecutionIsReturned() {
        String executionId = "myExecution";
        storeExecution(executionId, 1591692950000L);

        Decision result = trustyService.getDecisionById(executionId);
        Assertions.assertEquals(executionId, result.getExecutionId());
    }

    @Test
    public void givenADuplicatedDecisionWhenTheDecisionIsStoredThenAnExceptionIsRaised() {
        String executionId = "myExecution";
        storeExecution(executionId, 1591692950000L);
        Assertions.assertThrows(IllegalArgumentException.class, () -> storeExecution(executionId, 1591692950000L));
    }

    @Test
    public void givenNoExecutionsWhenADecisionIsRetrievedThenAnExceptionIsRaised() {
        String executionId = "myExecution";
        Assertions.assertThrows(IllegalArgumentException.class, () -> trustyService.getDecisionById(executionId));
    }

    @Test
    public void givenAModelWhenGetModelByIdIsCalledThenTheModelIsReturned() {
        String model = "definition";
        String modelId = "name:namespace";
        storeModel(model);

        DMNModelWithMetadata result = getModel();
        Assertions.assertEquals(model, result.getModel());
    }

    @Test
    public void givenADuplicatedModelWhenTheModelIsStoredThenAnExceptionIsRaised() {
        String model = "definition";
        storeModel(model);
        Assertions.assertThrows(IllegalArgumentException.class, () -> storeModel(model));
    }

    @Test
    public void givenNoModelsWhenAModelIsRetrievedThenAnExceptionIsRaised() {
        Assertions.assertThrows(IllegalArgumentException.class, this::getModel);
    }

    @Test
    public void searchExecutionsByPrefixTest() {
        String executionId = "da8ad1e9-a679-4ded-a6d5-53fd019e7002";
        Long executionTimestamp = 1617270053L;
        Instant instant = Instant.ofEpochMilli(executionTimestamp);
        storeExecution(executionId, executionTimestamp);

        MatchedExecutionHeaders executionHeaders = trustyService.getExecutionHeaders(
                OffsetDateTime.ofInstant(instant.minusMillis(1), ZoneOffset.UTC),
                OffsetDateTime.ofInstant(instant.plusMillis(1), ZoneOffset.UTC),
                10,
                0,
                "da8ad1e9-a679");

        Assertions.assertEquals(1, executionHeaders.getExecutions().size());
    }

    @Test
    public void testCounterfactuals_StoreSingleAndRetrieveSingleWithEmptyDefinition() {
        String executionId = "myCFExecution1";
        storeExecution(executionId, 0L);

        CounterfactualRequest request = trustyService.requestCounterfactuals(executionId, Collections.emptyList(), Collections.emptyList());

        assertNotNull(request);
        assertEquals(request.getExecutionId(), executionId);
        assertNotNull(request.getCounterfactualId());

        CounterfactualRequest result = trustyService.getCounterfactualRequest(executionId, request.getCounterfactualId());
        assertNotNull(result);
        assertEquals(request.getExecutionId(), result.getExecutionId());
        assertEquals(request.getCounterfactualId(), result.getCounterfactualId());
    }

    @Test
    public void testCounterfactuals_StoreMultipleAndRetrieveAllWithEmptyDefinition() {
        String executionId = "myCFExecution2";
        storeExecution(executionId, 0L);

        CounterfactualRequest request1 = trustyService.requestCounterfactuals(executionId, Collections.emptyList(), Collections.emptyList());
        CounterfactualRequest request2 = trustyService.requestCounterfactuals(executionId, Collections.emptyList(), Collections.emptyList());

        List<CounterfactualRequest> result = trustyService.getCounterfactualRequests(executionId);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getCounterfactualId().equals(request1.getCounterfactualId())));
        assertTrue(result.stream().anyMatch(c -> c.getCounterfactualId().equals(request2.getCounterfactualId())));
    }

    @Test
    public void testCounterfactuals_StoreMultipleAndRetrieveSingleWithEmptyDefinition() {
        String executionId = "myCFExecution3";
        storeExecution(executionId, 0L);

        CounterfactualRequest request1 = trustyService.requestCounterfactuals(executionId, Collections.emptyList(), Collections.emptyList());
        CounterfactualRequest request2 = trustyService.requestCounterfactuals(executionId, Collections.emptyList(), Collections.emptyList());

        CounterfactualRequest result1 = trustyService.getCounterfactualRequest(executionId, request1.getCounterfactualId());
        assertNotNull(result1);
        assertEquals(request1.getCounterfactualId(), result1.getCounterfactualId());

        CounterfactualRequest result2 = trustyService.getCounterfactualRequest(executionId, request2.getCounterfactualId());
        assertNotNull(result2);
        assertEquals(request2.getCounterfactualId(), result2.getCounterfactualId());
    }

    @Test
    public void testCounterfactuals_StoreMultipleForMultipleExecutionsAndRetrieveSingleWithEmptyDefinition() {
        String executionId1 = "myCFExecution1";
        storeExecution(executionId1, 0L);

        String executionId2 = "myCFExecution2";
        storeExecution(executionId2, 0L);

        CounterfactualRequest request1 = trustyService.requestCounterfactuals(executionId1, Collections.emptyList(), Collections.emptyList());
        assertNotNull(request1);
        assertEquals(request1.getExecutionId(), executionId1);
        assertNotNull(request1.getCounterfactualId());

        CounterfactualRequest request2 = trustyService.requestCounterfactuals(executionId2, Collections.emptyList(), Collections.emptyList());
        assertNotNull(request2);
        assertEquals(request2.getExecutionId(), executionId2);
        assertNotNull(request2.getCounterfactualId());

        CounterfactualRequest result1 = trustyService.getCounterfactualRequest(executionId1, request1.getCounterfactualId());
        assertNotNull(result1);
        assertEquals(request1.getExecutionId(), result1.getExecutionId());
        assertEquals(request1.getCounterfactualId(), result1.getCounterfactualId());
    }

    @Test
    public void testCounterfactuals_StoreSingleAndRetrieveSingleWithGoals() {
        String executionId = "myCFExecution1";
        storeExecution(executionId, 0L);

        TypedVariableWithValue goal1 = TypedVariableWithValue.buildUnit("field1", "typeRef1", new IntNode(25));
        TypedVariableWithValue goal2 = TypedVariableWithValue.buildUnit("field2", "typeRef2", new IntNode(99));

        CounterfactualRequest request = trustyService.requestCounterfactuals(executionId, List.of(goal1, goal2), Collections.emptyList());

        assertNotNull(request);
        assertEquals(request.getExecutionId(), executionId);
        assertNotNull(request.getCounterfactualId());
        assertEquals(2, request.getGoals().size());
        List<TypedVariableWithValue> requestGoals = new ArrayList<>(request.getGoals());
        assertCounterfactualGoal(goal1, requestGoals.get(0));
        assertCounterfactualGoal(goal2, requestGoals.get(1));

        CounterfactualRequest result = trustyService.getCounterfactualRequest(executionId, request.getCounterfactualId());
        assertNotNull(result);
        assertEquals(request.getExecutionId(), result.getExecutionId());
        assertEquals(request.getCounterfactualId(), result.getCounterfactualId());
        assertEquals(2, result.getGoals().size());
        List<TypedVariableWithValue> resultGoals = new ArrayList<>(request.getGoals());
        assertCounterfactualGoal(goal1, resultGoals.get(0));
        assertCounterfactualGoal(goal2, resultGoals.get(1));
    }

    private void assertCounterfactualGoal(TypedVariableWithValue expectedGoal, TypedVariableWithValue actualGoal) {
        assertEquals(expectedGoal.getName(), actualGoal.getName());
        assertEquals(expectedGoal.getTypeRef(), actualGoal.getTypeRef());
        assertEquals(expectedGoal.getValue(), actualGoal.getValue());
    }

    @Test
    public void testCounterfactuals_StoreSingleAndRetrieveSingleWithSearchDomainRange() {
        String executionId = "myCFExecution1";
        storeExecution(executionId, 0L);

        CounterfactualSearchDomain searchDomain = CounterfactualSearchDomain.buildUnit("field1",
                "typeRef1",
                true,
                new CounterfactualDomainRange(new IntNode(1), new IntNode(2)));

        CounterfactualRequest request = trustyService.requestCounterfactuals(executionId, Collections.emptyList(), Collections.singletonList(searchDomain));

        assertNotNull(request);
        assertEquals(request.getExecutionId(), executionId);
        assertNotNull(request.getCounterfactualId());
        assertEquals(1, request.getSearchDomains().size());
        List<CounterfactualSearchDomain> requestSearchDomains = new ArrayList<>(request.getSearchDomains());
        assertCounterfactualSearchDomainRange(searchDomain, requestSearchDomains.get(0));

        CounterfactualRequest result = trustyService.getCounterfactualRequest(executionId, request.getCounterfactualId());
        assertNotNull(result);
        assertEquals(request.getExecutionId(), result.getExecutionId());
        assertEquals(request.getCounterfactualId(), result.getCounterfactualId());
        assertEquals(1, result.getSearchDomains().size());
        List<CounterfactualSearchDomain> resultSearchDomains = new ArrayList<>(result.getSearchDomains());
        assertCounterfactualSearchDomainRange(searchDomain, resultSearchDomains.get(0));
    }

    private void assertCounterfactualSearchDomainRange(CounterfactualSearchDomain expectedSearchDomain, CounterfactualSearchDomain actualSearchDomain) {
        assertEquals(expectedSearchDomain.getName(), actualSearchDomain.getName());
        assertEquals(expectedSearchDomain.getTypeRef(), actualSearchDomain.getTypeRef());
        assertEquals(expectedSearchDomain.isFixed(), actualSearchDomain.isFixed());
        assertCounterfactualDomainRange(expectedSearchDomain.getDomain(), actualSearchDomain.getDomain());
    }

    private void assertCounterfactualDomainRange(CounterfactualDomain expectedDomain, CounterfactualDomain actualDomain) {
        assertTrue(expectedDomain instanceof CounterfactualDomainRange);
        assertTrue(actualDomain instanceof CounterfactualDomainRange);

        CounterfactualDomainRange expectedDomainRange = (CounterfactualDomainRange) expectedDomain;
        CounterfactualDomainRange actualDomainRange = (CounterfactualDomainRange) actualDomain;

        assertEquals(expectedDomainRange.getLowerBound(), actualDomainRange.getLowerBound());
        assertEquals(expectedDomainRange.getUpperBound(), actualDomainRange.getUpperBound());
    }

    @Test
    public void testCounterfactuals_StoreSingleAndRetrieveSingleWithSearchDomainCategorical() {
        String executionId = "myCFExecution1";
        storeExecution(executionId, 0L);

        Collection<JsonNode> categories = List.of(new TextNode("A"), new TextNode("B"));
        CounterfactualSearchDomain searchDomain = CounterfactualSearchDomain.buildUnit("field1", "typeRef1", true, new CounterfactualDomainCategorical(categories));

        CounterfactualRequest request = trustyService.requestCounterfactuals(executionId, Collections.emptyList(), Collections.singletonList(searchDomain));

        assertNotNull(request);
        assertEquals(request.getExecutionId(), executionId);
        assertNotNull(request.getCounterfactualId());
        assertEquals(1, request.getSearchDomains().size());
        List<CounterfactualSearchDomain> requestSearchDomains = new ArrayList<>(request.getSearchDomains());
        assertCounterfactualSearchDomainCategorical(searchDomain, requestSearchDomains.get(0));

        CounterfactualRequest result = trustyService.getCounterfactualRequest(executionId, request.getCounterfactualId());
        assertNotNull(result);
        assertEquals(request.getExecutionId(), result.getExecutionId());
        assertEquals(request.getCounterfactualId(), result.getCounterfactualId());
        assertEquals(1, result.getSearchDomains().size());
        List<CounterfactualSearchDomain> resultSearchDomains = new ArrayList<>(result.getSearchDomains());
        assertCounterfactualSearchDomainCategorical(searchDomain, resultSearchDomains.get(0));
    }

    private void assertCounterfactualSearchDomainCategorical(CounterfactualSearchDomain expectedSearchDomain, CounterfactualSearchDomain actualSearchDomain) {
        assertEquals(expectedSearchDomain.getName(), actualSearchDomain.getName());
        assertEquals(expectedSearchDomain.getTypeRef(), actualSearchDomain.getTypeRef());
        assertEquals(expectedSearchDomain.isFixed(), actualSearchDomain.isFixed());
        assertCounterfactualDomainCategorical(expectedSearchDomain.getDomain(), actualSearchDomain.getDomain());
    }

    private void assertCounterfactualDomainCategorical(CounterfactualDomain expectedDomain, CounterfactualDomain actualDomain) {
        assertTrue(expectedDomain instanceof CounterfactualDomainCategorical);
        assertTrue(actualDomain instanceof CounterfactualDomainCategorical);

        CounterfactualDomainCategorical expectedDomainCategorical = (CounterfactualDomainCategorical) expectedDomain;
        CounterfactualDomainCategorical actualDomainCategorical = (CounterfactualDomainCategorical) actualDomain;

        assertEquals(expectedDomainCategorical.getCategories().size(), actualDomainCategorical.getCategories().size());
        assertTrue(expectedDomainCategorical.getCategories().containsAll(actualDomainCategorical.getCategories()));
    }

    private Decision storeExecution(String executionId, Long timestamp) {
        Decision decision = new Decision();
        decision.setExecutionId(executionId);
        decision.setExecutionTimestamp(timestamp);
        trustyService.storeDecision(decision.getExecutionId(), decision);
        return decision;
    }

    private DMNModelWithMetadata storeModel(String model) {
        DMNModelWithMetadata dmnModelWithMetadata = new DMNModelWithMetadata("groupId", "artifactId", "modelVersion", "dmnVersion", "name", "namespace", model);
        ModelIdentifier identifier = new ModelIdentifier("groupId",
                "artifactId",
                "version",
                "name",
                "namespace");
        trustyService.storeModel(identifier, dmnModelWithMetadata);
        return dmnModelWithMetadata;
    }

    private DMNModelWithMetadata getModel() {
        ModelIdentifier identifier = new ModelIdentifier("groupId",
                "artifactId",
                "version",
                "name",
                "namespace");
        return trustyService.getModelById(identifier);
    }

}
