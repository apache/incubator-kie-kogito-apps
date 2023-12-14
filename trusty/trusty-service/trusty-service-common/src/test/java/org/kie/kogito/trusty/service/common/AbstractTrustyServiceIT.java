/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.trusty.service.common;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.api.CounterfactualDomain;
import org.kie.kogito.explainability.api.CounterfactualDomainCategorical;
import org.kie.kogito.explainability.api.CounterfactualDomainRange;
import org.kie.kogito.explainability.api.CounterfactualExplainabilityRequest;
import org.kie.kogito.explainability.api.CounterfactualExplainabilityResult;
import org.kie.kogito.explainability.api.CounterfactualSearchDomain;
import org.kie.kogito.explainability.api.ExplainabilityStatus;
import org.kie.kogito.explainability.api.FeatureImportanceModel;
import org.kie.kogito.explainability.api.LIMEExplainabilityResult;
import org.kie.kogito.explainability.api.NamedTypedValue;
import org.kie.kogito.explainability.api.SaliencyModel;
import org.kie.kogito.tracing.typedvalue.UnitValue;
import org.kie.kogito.trusty.service.common.models.MatchedExecutionHeaders;
import org.kie.kogito.trusty.storage.api.model.decision.DMNModelMetadata;
import org.kie.kogito.trusty.storage.api.model.decision.DMNModelWithMetadata;
import org.kie.kogito.trusty.storage.api.model.decision.Decision;
import org.kie.kogito.trusty.storage.api.model.decision.DecisionInput;
import org.kie.kogito.trusty.storage.api.model.decision.DecisionOutcome;
import org.kie.kogito.trusty.storage.common.TrustyStorageService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.kie.kogito.trusty.service.common.TypedValueTestUtils.buildGoalUnit;
import static org.kie.kogito.trusty.service.common.TypedValueTestUtils.buildSearchDomainUnit;

public abstract class AbstractTrustyServiceIT {

    @Inject
    TrustyService trustyService;

    @Inject
    TrustyStorageService trustyStorageService;

    @BeforeEach
    public void setup() {
        trustyStorageService.getCounterfactualRequestStorage().clear();
        trustyStorageService.getCounterfactualResultStorage().clear();
        trustyStorageService.getLIMEResultStorage().clear();
        trustyStorageService.getDecisionsStorage().clear();
        trustyStorageService.getModelStorage(DMNModelWithMetadata.class).clear();
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
    public void givenAnExecutionWhenGetDecisionByIdThenTheComponentsInUnitTypesIsNull() {
        String executionId = "myExecution";
        storeExecution(executionId, 1591692950000L);

        Decision result = trustyService.getDecisionById(executionId);
        Assertions.assertTrue(result.getInputs().stream().findFirst().get().getValue().isUnit());
    }

    @Test
    public void givenADuplicatedDecisionWhenTheDecisionIsStoredThenAnExceptionIsRaised() {
        String executionId = "myExecution";
        storeExecution(executionId, 1591692950000L);
        assertThrows(IllegalArgumentException.class, () -> storeExecution(executionId, 1591692950000L));
    }

    @Test
    public void givenNoExecutionsWhenADecisionIsRetrievedThenAnExceptionIsRaised() {
        String executionId = "myExecution";
        assertThrows(IllegalArgumentException.class, () -> trustyService.getDecisionById(executionId));
    }

    @Test
    public void givenAModelWhenGetModelByIdIsCalledThenTheModelIsReturned() {
        String model = "definition";
        storeModel(model);

        DMNModelWithMetadata result = getModel();
        Assertions.assertEquals(model, result.getModel());
    }

    @Test
    public void givenADuplicatedModelWhenTheModelIsStoredThenAnExceptionIsRaised() {
        String model = "definition";
        storeModel(model);
        assertThrows(IllegalArgumentException.class, () -> storeModel(model));
    }

    @Test
    public void givenNoModelsWhenAModelIsRetrievedThenAnExceptionIsRaised() {
        assertThrows(IllegalArgumentException.class, this::getModel);
    }

    @Test
    public void searchExecutionsByPrefixTest() {
        String executionId = "da8ad1e9-a679-4ded-a6d5-53fd019e7002";
        long executionTimestamp = 1617270053L;
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

        // The Goals structures must be comparable to the original decisions outcomes.
        // The Search Domain structures must be identical those of the original decision inputs.
        CounterfactualSearchDomain searchDomain = buildSearchDomainUnit("test",
                "number",
                new CounterfactualDomainRange(new IntNode(1), new IntNode(2)));

        CounterfactualExplainabilityRequest request = trustyService.requestCounterfactuals(executionId, Collections.emptyList(), Collections.singletonList(searchDomain));

        assertNotNull(request);
        assertEquals(request.getExecutionId(), executionId);
        assertNotNull(request.getCounterfactualId());

        CounterfactualExplainabilityRequest result = trustyService.getCounterfactualRequest(executionId, request.getCounterfactualId());
        assertNotNull(result);
        assertEquals(request.getExecutionId(), result.getExecutionId());
        assertEquals(request.getCounterfactualId(), result.getCounterfactualId());
    }

    @Test
    public void testCounterfactuals_StoreMultipleAndRetrieveAllWithEmptyDefinition() {
        String executionId = "myCFExecution2";
        storeExecution(executionId, 0L);

        // The Goals structures must be comparable to the original decisions outcomes.
        // The Search Domain structures must be identical those of the original decision inputs.
        CounterfactualSearchDomain searchDomain = buildSearchDomainUnit("test",
                "number",
                new CounterfactualDomainRange(new IntNode(1), new IntNode(2)));

        CounterfactualExplainabilityRequest request1 = trustyService.requestCounterfactuals(executionId, Collections.emptyList(), Collections.singletonList(searchDomain));
        CounterfactualExplainabilityRequest request2 = trustyService.requestCounterfactuals(executionId, Collections.emptyList(), Collections.singletonList(searchDomain));

        List<CounterfactualExplainabilityRequest> result = trustyService.getCounterfactualRequests(executionId);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getCounterfactualId().equals(request1.getCounterfactualId())));
        assertTrue(result.stream().anyMatch(c -> c.getCounterfactualId().equals(request2.getCounterfactualId())));
    }

    @Test
    public void testCounterfactuals_StoreMultipleAndRetrieveSingleWithEmptyDefinition() {
        String executionId = "myCFExecution3";
        storeExecution(executionId, 0L);

        // The Goals structures must be comparable to the original decisions outcomes.
        // The Search Domain structures must be identical those of the original decision inputs.
        CounterfactualSearchDomain searchDomain = buildSearchDomainUnit("test",
                "number",
                new CounterfactualDomainRange(new IntNode(1), new IntNode(2)));

        CounterfactualExplainabilityRequest request1 = trustyService.requestCounterfactuals(executionId, Collections.emptyList(), Collections.singletonList(searchDomain));
        CounterfactualExplainabilityRequest request2 = trustyService.requestCounterfactuals(executionId, Collections.emptyList(), Collections.singletonList(searchDomain));

        CounterfactualExplainabilityRequest result1 = trustyService.getCounterfactualRequest(executionId, request1.getCounterfactualId());
        assertNotNull(result1);
        assertEquals(request1.getCounterfactualId(), result1.getCounterfactualId());

        CounterfactualExplainabilityRequest result2 = trustyService.getCounterfactualRequest(executionId, request2.getCounterfactualId());
        assertNotNull(result2);
        assertEquals(request2.getCounterfactualId(), result2.getCounterfactualId());
    }

    @Test
    public void testCounterfactuals_StoreMultipleForMultipleExecutionsAndRetrieveSingleWithEmptyDefinition() {
        String executionId1 = "myCFExecution1";
        storeExecution(executionId1, 0L);

        String executionId2 = "myCFExecution2";
        storeExecution(executionId2, 0L);

        // The Goals structures must be comparable to the original decisions outcomes.
        // The Search Domain structures must be identical those of the original decision inputs.
        CounterfactualSearchDomain searchDomain = buildSearchDomainUnit("test",
                "number",
                new CounterfactualDomainRange(new IntNode(1), new IntNode(2)));

        CounterfactualExplainabilityRequest request1 = trustyService.requestCounterfactuals(executionId1, Collections.emptyList(), Collections.singletonList(searchDomain));
        assertNotNull(request1);
        assertEquals(request1.getExecutionId(), executionId1);
        assertNotNull(request1.getCounterfactualId());

        CounterfactualExplainabilityRequest request2 = trustyService.requestCounterfactuals(executionId2, Collections.emptyList(), Collections.singletonList(searchDomain));
        assertNotNull(request2);
        assertEquals(request2.getExecutionId(), executionId2);
        assertNotNull(request2.getCounterfactualId());

        CounterfactualExplainabilityRequest result1 = trustyService.getCounterfactualRequest(executionId1, request1.getCounterfactualId());
        assertNotNull(result1);
        assertEquals(request1.getExecutionId(), result1.getExecutionId());
        assertEquals(request1.getCounterfactualId(), result1.getCounterfactualId());
    }

    @Test
    public void testCounterfactuals_StoreSingleAndRetrieveSingleWithGoals() {
        String executionId = "myCFExecution1";
        storeExecutionWithOutcomes(executionId, 0L);

        // The Goals structures must be comparable to the original decisions outcomes.
        // The Search Domain structures must be identical those of the original decision inputs.
        CounterfactualSearchDomain searchDomain = buildSearchDomainUnit("test",
                "number",
                new CounterfactualDomainRange(new IntNode(1), new IntNode(2)));

        NamedTypedValue goal1 = buildGoalUnit("outcome1", "number", new IntNode(25));
        NamedTypedValue goal2 = buildGoalUnit("outcome2", "string", new TextNode("cheese"));

        CounterfactualExplainabilityRequest request = trustyService.requestCounterfactuals(executionId, List.of(goal1, goal2), Collections.singletonList(searchDomain));

        assertNotNull(request);
        assertEquals(request.getExecutionId(), executionId);
        assertNotNull(request.getCounterfactualId());
        assertEquals(2, request.getGoals().size());
        List<NamedTypedValue> requestGoals = new ArrayList<>(request.getGoals());
        assertCounterfactualGoal(goal1, requestGoals.get(0));
        assertCounterfactualGoal(goal2, requestGoals.get(1));

        CounterfactualExplainabilityRequest result = trustyService.getCounterfactualRequest(executionId, request.getCounterfactualId());
        assertNotNull(result);
        assertEquals(request.getExecutionId(), result.getExecutionId());
        assertEquals(request.getCounterfactualId(), result.getCounterfactualId());
        assertEquals(2, result.getGoals().size());
        List<NamedTypedValue> resultGoals = new ArrayList<>(request.getGoals());
        assertCounterfactualGoal(goal1, resultGoals.get(0));
        assertCounterfactualGoal(goal2, resultGoals.get(1));
    }

    private void assertCounterfactualGoal(NamedTypedValue expectedGoal, NamedTypedValue actualGoal) {
        assertEquals(expectedGoal.getName(), actualGoal.getName());
        assertEquals(expectedGoal.getValue().getType(), actualGoal.getValue().getType());
        assertEquals(expectedGoal.getValue().toUnit().getValue(), actualGoal.getValue().toUnit().getValue());
    }

    @Test
    public void testCounterfactuals_StoreSingleAndRetrieveSingleWithSearchDomainRange() {
        String executionId = "myCFExecution1";
        storeExecution(executionId, 0L);

        // The Goals structures must be comparable to the original decisions outcomes.
        // The Search Domain structures must be identical those of the original decision inputs.
        CounterfactualSearchDomain searchDomain = buildSearchDomainUnit("test",
                "number",
                new CounterfactualDomainRange(new IntNode(1), new IntNode(2)));

        CounterfactualExplainabilityRequest request = trustyService.requestCounterfactuals(executionId, Collections.emptyList(), Collections.singletonList(searchDomain));

        assertNotNull(request);
        assertEquals(request.getExecutionId(), executionId);
        assertNotNull(request.getCounterfactualId());
        assertEquals(1, request.getSearchDomains().size());
        List<CounterfactualSearchDomain> requestSearchDomains = new ArrayList<>(request.getSearchDomains());
        assertCounterfactualSearchDomainRange(searchDomain, requestSearchDomains.get(0));

        CounterfactualExplainabilityRequest result = trustyService.getCounterfactualRequest(executionId, request.getCounterfactualId());
        assertNotNull(result);
        assertEquals(request.getExecutionId(), result.getExecutionId());
        assertEquals(request.getCounterfactualId(), result.getCounterfactualId());
        assertEquals(1, result.getSearchDomains().size());
        List<CounterfactualSearchDomain> resultSearchDomains = new ArrayList<>(result.getSearchDomains());
        assertCounterfactualSearchDomainRange(searchDomain, resultSearchDomains.get(0));
    }

    private void assertCounterfactualSearchDomainRange(CounterfactualSearchDomain expectedSearchDomain, CounterfactualSearchDomain actualSearchDomain) {
        assertEquals(expectedSearchDomain.getName(), actualSearchDomain.getName());
        assertEquals(expectedSearchDomain.getValue().getType(), actualSearchDomain.getValue().getType());
        assertEquals(expectedSearchDomain.getValue().toUnit().isFixed(), actualSearchDomain.getValue().toUnit().isFixed());
        assertCounterfactualDomainRange(expectedSearchDomain.getValue().toUnit().getDomain(), actualSearchDomain.getValue().toUnit().getDomain());
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

        // The Goals structures must be comparable to the original decisions outcomes.
        // The Search Domain structures must be identical those of the original decision inputs.
        Collection<JsonNode> categories = List.of(new TextNode("1"), new TextNode("2"));
        CounterfactualSearchDomain searchDomain = buildSearchDomainUnit("test",
                "number",
                new CounterfactualDomainCategorical(categories));

        CounterfactualExplainabilityRequest request = trustyService.requestCounterfactuals(executionId, Collections.emptyList(), Collections.singletonList(searchDomain));

        assertNotNull(request);
        assertEquals(request.getExecutionId(), executionId);
        assertNotNull(request.getCounterfactualId());
        assertEquals(1, request.getSearchDomains().size());
        List<CounterfactualSearchDomain> requestSearchDomains = new ArrayList<>(request.getSearchDomains());
        assertCounterfactualSearchDomainCategorical(searchDomain, requestSearchDomains.get(0));

        CounterfactualExplainabilityRequest result = trustyService.getCounterfactualRequest(executionId, request.getCounterfactualId());
        assertNotNull(result);
        assertEquals(request.getExecutionId(), result.getExecutionId());
        assertEquals(request.getCounterfactualId(), result.getCounterfactualId());
        assertEquals(1, result.getSearchDomains().size());
        List<CounterfactualSearchDomain> resultSearchDomains = new ArrayList<>(result.getSearchDomains());
        assertCounterfactualSearchDomainCategorical(searchDomain, resultSearchDomains.get(0));
    }

    private void assertCounterfactualSearchDomainCategorical(CounterfactualSearchDomain expectedSearchDomain, CounterfactualSearchDomain actualSearchDomain) {
        assertEquals(expectedSearchDomain.getName(), actualSearchDomain.getName());
        assertEquals(expectedSearchDomain.getValue().getType(), actualSearchDomain.getValue().getType());
        assertEquals(expectedSearchDomain.getValue().toUnit().isFixed(), actualSearchDomain.getValue().toUnit().isFixed());
        assertCounterfactualDomainCategorical(expectedSearchDomain.getValue().toUnit().getDomain(), actualSearchDomain.getValue().toUnit().getDomain());
    }

    private void assertCounterfactualDomainCategorical(CounterfactualDomain expectedDomain, CounterfactualDomain actualDomain) {
        assertTrue(expectedDomain instanceof CounterfactualDomainCategorical);
        assertTrue(actualDomain instanceof CounterfactualDomainCategorical);

        CounterfactualDomainCategorical expectedDomainCategorical = (CounterfactualDomainCategorical) expectedDomain;
        CounterfactualDomainCategorical actualDomainCategorical = (CounterfactualDomainCategorical) actualDomain;

        assertEquals(expectedDomainCategorical.getCategories().size(), actualDomainCategorical.getCategories().size());
        assertTrue(expectedDomainCategorical.getCategories().containsAll(actualDomainCategorical.getCategories()));
    }

    @Test
    public void testCounterfactuals_WithDisparateSearchDomainStructure() {
        String executionId = "myCFExecution1";
        storeExecution(executionId, 0L);

        // The Goals structures must be comparable to the original decisions outcomes.
        // The Search Domain structures must be identical those of the original decision inputs.
        // For this test we're providing an incorrect structure for searchDomains but the correct structure for goals.
        assertThrows(IllegalArgumentException.class,
                () -> trustyService.requestCounterfactuals(executionId, Collections.emptyList(), Collections.emptyList()));
    }

    @Test
    public void testCounterfactuals_WithDisparateGoalsStructure() {
        String executionId = "myCFExecution1";
        storeExecutionWithOutcomes(executionId, 0L);

        // The Goals structures must be comparable to the original decisions outcomes.
        // The Search Domain structures must be identical those of the original decision inputs.
        // For this test we're providing the correct structure for searchDomains but a sub-structure for goals.
        CounterfactualSearchDomain searchDomain = buildSearchDomainUnit("test",
                "number",
                new CounterfactualDomainRange(new IntNode(1), new IntNode(2)));

        CounterfactualExplainabilityRequest request = trustyService.requestCounterfactuals(executionId, Collections.emptyList(), Collections.singletonList(searchDomain));

        assertNotNull(request);
        assertEquals(request.getExecutionId(), executionId);
        assertNotNull(request.getCounterfactualId());
        assertTrue(request.getGoals().isEmpty());

        CounterfactualExplainabilityRequest result = trustyService.getCounterfactualRequest(executionId, request.getCounterfactualId());
        assertNotNull(result);
        assertEquals(request.getExecutionId(), result.getExecutionId());
        assertEquals(request.getCounterfactualId(), result.getCounterfactualId());
        assertTrue(result.getGoals().isEmpty());
    }

    @Test
    public void testStoreExplainabilityResult_LIME() {
        String executionId = "myLIMEExecution1Store";

        trustyService.storeExplainabilityResult(executionId,
                new LIMEExplainabilityResult(executionId,
                        ExplainabilityStatus.SUCCEEDED,
                        "status",
                        List.of(new SaliencyModel("outcomeName",
                                List.of(new FeatureImportanceModel("feature", 1.0))))));

        LIMEExplainabilityResult result = trustyService.getExplainabilityResultById(executionId, LIMEExplainabilityResult.class);
        assertNotNull(result);
    }

    @Test
    public void testStoreExplainabilityResult_Counterfactual() {
        String executionId = "myCFExecution1Store";

        NamedTypedValue input1 = new NamedTypedValue("field1",
                new UnitValue("typeRef1", "typeRef1", new IntNode(25)));
        NamedTypedValue input2 = new NamedTypedValue("field2",
                new UnitValue("typeRef2", "typeRef2", new IntNode(99)));
        NamedTypedValue output1 = new NamedTypedValue("field3",
                new UnitValue("typeRef3", "typeRef3", new IntNode(200)));
        NamedTypedValue output2 = new NamedTypedValue("field4",
                new UnitValue("typeRef4", "typeRef4", new IntNode(1000)));

        trustyService.storeExplainabilityResult(executionId,
                new CounterfactualExplainabilityResult(executionId,
                        "counterfactualId",
                        "solutionId",
                        0L,
                        ExplainabilityStatus.SUCCEEDED,
                        "status",
                        true,
                        CounterfactualExplainabilityResult.Stage.FINAL,
                        List.of(input1, input2),
                        List.of(output1, output2)));

        CounterfactualExplainabilityResult result = trustyService.getExplainabilityResultById(executionId, CounterfactualExplainabilityResult.class);
        assertNotNull(result);
    }

    @Test
    public void testStoreExplainabilityResult_Counterfactual_DuplicateRemoval_FinalThenIntermediate() {
        String executionId = "myCFExecution1Store";
        String counterfactualId = "myCFCounterfactualId";

        NamedTypedValue input1 = new NamedTypedValue("field1",
                new UnitValue("typeRef1", "typeRef1", new IntNode(25)));
        NamedTypedValue input2 = new NamedTypedValue("field2",
                new UnitValue("typeRef2", "typeRef2", new IntNode(99)));
        NamedTypedValue output1 = new NamedTypedValue("field3",
                new UnitValue("typeRef3", "typeRef3", new IntNode(200)));
        NamedTypedValue output2 = new NamedTypedValue("field4",
                new UnitValue("typeRef4", "typeRef4", new IntNode(1000)));

        //First solution is the FINAL (for whatever reason, e.g. messaging delays, the INTERMEDIATE is received afterwards)
        trustyService.storeExplainabilityResult(executionId,
                new CounterfactualExplainabilityResult(executionId,
                        counterfactualId,
                        "solutionId1",
                        0L,
                        ExplainabilityStatus.SUCCEEDED,
                        "status",
                        true,
                        CounterfactualExplainabilityResult.Stage.FINAL,
                        List.of(input1, input2),
                        List.of(output1, output2)));

        List<CounterfactualExplainabilityResult> result1 = trustyService.getCounterfactualResults(executionId, counterfactualId);
        assertNotNull(result1);
        assertEquals(1, result1.size());
        assertEquals("solutionId1", result1.get(0).getSolutionId());
        assertEquals(CounterfactualExplainabilityResult.Stage.FINAL, result1.get(0).getStage());

        trustyService.storeExplainabilityResult(executionId,
                new CounterfactualExplainabilityResult(executionId,
                        counterfactualId,
                        "solutionId2",
                        0L,
                        ExplainabilityStatus.SUCCEEDED,
                        "status",
                        true,
                        CounterfactualExplainabilityResult.Stage.INTERMEDIATE,
                        List.of(input1, input2),
                        List.of(output1, output2)));

        List<CounterfactualExplainabilityResult> result2 = trustyService.getCounterfactualResults(executionId, counterfactualId);
        assertNotNull(result2);
        assertEquals(1, result1.size());
        assertEquals("solutionId1", result1.get(0).getSolutionId());
        assertEquals(CounterfactualExplainabilityResult.Stage.FINAL, result1.get(0).getStage());
    }

    @Test
    public void testStoreExplainabilityResult_Counterfactual_DuplicateRemoval_IntermediateThenFinal() {
        String executionId = "myCFExecution1Store";
        String counterfactualId = "myCFCounterfactualId";

        NamedTypedValue input1 = new NamedTypedValue("field1",
                new UnitValue("typeRef1", "typeRef1", new IntNode(25)));
        NamedTypedValue input2 = new NamedTypedValue("field2",
                new UnitValue("typeRef2", "typeRef2", new IntNode(99)));
        NamedTypedValue output1 = new NamedTypedValue("field3",
                new UnitValue("typeRef3", "typeRef3", new IntNode(200)));
        NamedTypedValue output2 = new NamedTypedValue("field4",
                new UnitValue("typeRef4", "typeRef4", new IntNode(1000)));

        //First solution is the INTERMEDIATE then the FINAL
        trustyService.storeExplainabilityResult(executionId,
                new CounterfactualExplainabilityResult(executionId,
                        counterfactualId,
                        "solutionId1",
                        0L,
                        ExplainabilityStatus.SUCCEEDED,
                        "status",
                        true,
                        CounterfactualExplainabilityResult.Stage.INTERMEDIATE,
                        List.of(input1, input2),
                        List.of(output1, output2)));

        List<CounterfactualExplainabilityResult> result1 = trustyService.getCounterfactualResults(executionId, counterfactualId);
        assertNotNull(result1);
        assertEquals(1, result1.size());
        assertEquals("solutionId1", result1.get(0).getSolutionId());
        assertEquals(CounterfactualExplainabilityResult.Stage.INTERMEDIATE, result1.get(0).getStage());

        trustyService.storeExplainabilityResult(executionId,
                new CounterfactualExplainabilityResult(executionId,
                        counterfactualId,
                        "solutionId2",
                        0L,
                        ExplainabilityStatus.SUCCEEDED,
                        "status",
                        true,
                        CounterfactualExplainabilityResult.Stage.FINAL,
                        List.of(input1, input2),
                        List.of(output1, output2)));

        List<CounterfactualExplainabilityResult> result2 = trustyService.getCounterfactualResults(executionId, counterfactualId);
        assertNotNull(result2);
        assertEquals(1, result2.size());
        assertEquals("solutionId2", result2.get(0).getSolutionId());
        assertEquals(CounterfactualExplainabilityResult.Stage.FINAL, result2.get(0).getStage());
    }

    private void storeExecution(String executionId, Long timestamp) {
        DecisionInput decisionInput = new DecisionInput();
        decisionInput.setId("inputId");
        decisionInput.setName("test");
        decisionInput.setValue(new UnitValue("number", "number", JsonNodeFactory.instance.numberNode(10)));

        Decision decision = new Decision();
        decision.setExecutionId(executionId);
        decision.setExecutionTimestamp(timestamp);
        decision.setServiceUrl("serviceUrl");
        decision.setExecutedModelNamespace("executedModelNamespace");
        decision.setExecutedModelName("executedModelName");
        decision.setInputs(Collections.singletonList(decisionInput));

        trustyService.storeDecision(decision.getExecutionId(), decision);
    }

    private void storeExecutionWithOutcomes(String executionId, Long timestamp) {
        DecisionInput decisionInput = new DecisionInput();
        decisionInput.setId("inputId");
        decisionInput.setName("test");
        decisionInput.setValue(new UnitValue("number", "number", JsonNodeFactory.instance.numberNode(10)));

        DecisionOutcome decisionOutcome1 = new DecisionOutcome();
        decisionOutcome1.setOutcomeId("outcomeId1");
        decisionOutcome1.setOutcomeName("outcome1");
        decisionOutcome1.setOutcomeResult(new UnitValue("number", "number", JsonNodeFactory.instance.numberNode(20)));

        DecisionOutcome decisionOutcome2 = new DecisionOutcome();
        decisionOutcome2.setOutcomeId("outcomeId2");
        decisionOutcome2.setOutcomeName("outcome2");
        decisionOutcome2.setOutcomeResult(new UnitValue("string", "string", JsonNodeFactory.instance.textNode("food")));

        Decision decision = new Decision();
        decision.setExecutionId(executionId);
        decision.setExecutionTimestamp(timestamp);
        decision.setServiceUrl("serviceUrl");
        decision.setExecutedModelNamespace("executedModelNamespace");
        decision.setExecutedModelName("executedModelName");
        decision.setInputs(Collections.singletonList(decisionInput));
        decision.setOutcomes(List.of(decisionOutcome1, decisionOutcome2));

        trustyService.storeDecision(decision.getExecutionId(), decision);
    }

    private void storeModel(String model) {
        DMNModelWithMetadata dmnModelWithMetadata = new DMNModelWithMetadata("groupId", "artifactId", "modelVersion", "dmnVersion", "name", "namespace", model);
        trustyService.storeModel(dmnModelWithMetadata);
    }

    private DMNModelWithMetadata getModel() {
        DMNModelMetadata identifier = new DMNModelMetadata("groupId",
                "artifactId",
                "version",
                "dmnVersion",
                "name",
                "namespace");
        return trustyService.getModelById(identifier, DMNModelWithMetadata.class);
    }
}
