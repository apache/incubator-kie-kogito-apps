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

package org.kie.kogito.trusty.service.common.api;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.kie.kogito.trusty.service.common.TrustyService;
import org.kie.kogito.trusty.service.common.responses.CounterfactualResponse;
import org.kie.kogito.trusty.service.common.responses.SalienciesResponse;
import org.kie.kogito.trusty.storage.api.model.BaseExplainabilityResult;
import org.kie.kogito.trusty.storage.api.model.Counterfactual;
import org.kie.kogito.trusty.storage.api.model.CounterfactualDomainCategorical;
import org.kie.kogito.trusty.storage.api.model.CounterfactualDomainNumerical;
import org.kie.kogito.trusty.storage.api.model.CounterfactualSearchDomain;
import org.kie.kogito.trusty.storage.api.model.ExplainabilityStatus;
import org.kie.kogito.trusty.storage.api.model.FeatureImportanceModel;
import org.kie.kogito.trusty.storage.api.model.LIMEExplainabilityResult;
import org.kie.kogito.trusty.storage.api.model.SaliencyModel;
import org.kie.kogito.trusty.storage.api.model.TypedVariableWithValue;
import org.mockito.ArgumentCaptor;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.org.apache.commons.lang.builder.CompareToBuilder;

import com.fasterxml.jackson.databind.JsonNode;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.kie.kogito.trusty.service.common.TrustyServiceTestUtils.getCounterfactualJsonRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class ExplainabilityApiV1IT {

    private static final String TEST_EXECUTION_ID = "executionId";

    private static final String TEST_COUNTERFACTUAL_ID = "counterfactualId";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @InjectMock
    TrustyService executionService;

    private static BaseExplainabilityResult buildValidExplainabilityResult() {
        return new LIMEExplainabilityResult(
                TEST_EXECUTION_ID,
                ExplainabilityStatus.SUCCEEDED,
                null,
                List.of(
                        new SaliencyModel("O1", "Output1", List.of(
                                new FeatureImportanceModel("Feature1", 0.49384),
                                new FeatureImportanceModel("Feature2", -0.1084))),
                        new SaliencyModel("O2", "Output2", List.of(
                                new FeatureImportanceModel("Feature1", 0.0),
                                new FeatureImportanceModel("Feature2", 0.70293)))));
    }

    private static Counterfactual buildValidCounterfactual() {
        return new Counterfactual(TEST_EXECUTION_ID, TEST_COUNTERFACTUAL_ID);
    }

    @Test
    void testSalienciesWithExplainabilityResult() {
        mockServiceWithExplainabilityResult();

        SalienciesResponse response = given().filter(new ResponseLoggingFilter())
                .when().get("/executions/decisions/" + TEST_EXECUTION_ID + "/explanations/saliencies")
                .as(SalienciesResponse.class);

        assertNotNull(response);
        assertNotNull(response.getSaliencies());
        assertSame(2, response.getSaliencies().size());

        List<SaliencyModel> sortedSaliencies = response.getSaliencies().stream()
                .sorted((s1, s2) -> new CompareToBuilder().append(s1.getOutcomeName(), s2.getOutcomeName()).toComparison())
                .collect(Collectors.toList());

        assertNotNull(sortedSaliencies.get(0));
        assertEquals("Output1", sortedSaliencies.get(0).getOutcomeName());
        assertNotNull(sortedSaliencies.get(0).getFeatureImportance());
        assertSame(2, sortedSaliencies.get(0).getFeatureImportance().size());
        assertEquals("Feature1", sortedSaliencies.get(0).getFeatureImportance().get(0).getFeatureName());
        assertEquals(0.49384, sortedSaliencies.get(0).getFeatureImportance().get(0).getScore());
        assertEquals("Feature2", sortedSaliencies.get(0).getFeatureImportance().get(1).getFeatureName());
        assertEquals(-0.1084, sortedSaliencies.get(0).getFeatureImportance().get(1).getScore());

        assertNotNull(sortedSaliencies.get(1));
        assertEquals("Output2", sortedSaliencies.get(1).getOutcomeName());
        assertNotNull(sortedSaliencies.get(1).getFeatureImportance());
        assertSame(2, sortedSaliencies.get(1).getFeatureImportance().size());
        assertEquals("Feature1", sortedSaliencies.get(1).getFeatureImportance().get(0).getFeatureName());
        assertEquals(0.0, sortedSaliencies.get(1).getFeatureImportance().get(0).getScore());
        assertEquals("Feature2", sortedSaliencies.get(1).getFeatureImportance().get(1).getFeatureName());
        assertEquals(0.70293, sortedSaliencies.get(1).getFeatureImportance().get(1).getScore());
    }

    @Test
    void testSalienciesWithNullExplainabilityResult() {
        mockServiceWithNullExplainabilityResult();

        given().filter(new ResponseLoggingFilter())
                .when().get("/executions/decisions/" + TEST_EXECUTION_ID + "/explanations/saliencies")
                .then().statusCode(400);
    }

    @Test
    void testSalienciesWithoutExplainabilityResult() {
        mockServiceWithoutExplainabilityResult();

        given().filter(new ResponseLoggingFilter())
                .when().get("/executions/decisions/" + TEST_EXECUTION_ID + "/explanations/saliencies")
                .then().statusCode(400);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testCounterfactualRequest() {
        ArgumentCaptor<List<TypedVariableWithValue>> goalsCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<CounterfactualSearchDomain>> searchDomainsCaptor = ArgumentCaptor.forClass(List.class);

        mockServiceWithCounterfactualRequest();

        CounterfactualResponse response = given()
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter())
                .contentType(MediaType.APPLICATION_JSON)
                .body(getCounterfactualJsonRequest())
                .when().post("/executions/decisions/" + TEST_EXECUTION_ID + "/explanations/counterfactuals")
                .as(CounterfactualResponse.class);

        assertNotNull(response);
        assertNotNull(response.getExecutionId());
        assertNotNull(response.getCounterfactualId());
        assertEquals(response.getExecutionId(), TEST_EXECUTION_ID);
        assertEquals(response.getCounterfactualId(), TEST_COUNTERFACTUAL_ID);

        verify(executionService).requestCounterfactuals(eq(TEST_EXECUTION_ID), goalsCaptor.capture(), searchDomainsCaptor.capture());
        List<TypedVariableWithValue> goalsParameter = goalsCaptor.getValue();
        assertNotNull(goalsParameter);
        assertEquals(2, goalsParameter.size());

        TypedVariableWithValue goal1 = goalsParameter.get(0);
        assertEquals("deposit", goal1.getName());
        assertEquals("number", goal1.getTypeRef());
        assertEquals(5000, goal1.getValue().asInt());

        TypedVariableWithValue goal2 = goalsParameter.get(1);
        assertEquals("approved", goal2.getName());
        assertEquals("boolean", goal2.getTypeRef());
        assertEquals(Boolean.TRUE, goal2.getValue().asBoolean());

        List<CounterfactualSearchDomain> searchDomainsParameter = searchDomainsCaptor.getValue();
        assertNotNull(searchDomainsParameter);
        assertEquals(3, searchDomainsParameter.size());

        CounterfactualSearchDomain domain1 = searchDomainsParameter.get(0);
        assertTrue(domain1.isFixed());
        assertEquals("age", domain1.getName());
        assertEquals("number", domain1.getTypeRef());
        assertNull(domain1.getDomain());

        CounterfactualSearchDomain domain2 = searchDomainsParameter.get(1);
        assertFalse(domain2.isFixed());
        assertEquals("income", domain2.getName());
        assertEquals("number", domain2.getTypeRef());
        assertNotNull(domain2.getDomain());
        assertTrue(domain2.getDomain() instanceof CounterfactualDomainNumerical);
        CounterfactualDomainNumerical domain2Def = (CounterfactualDomainNumerical) domain2.getDomain();
        assertEquals(0, domain2Def.getLowerBound());
        assertEquals(1000, domain2Def.getUpperBound());

        CounterfactualSearchDomain domain3 = searchDomainsParameter.get(2);
        assertFalse(domain3.isFixed());
        assertEquals("taxCode", domain3.getName());
        assertEquals("string", domain3.getTypeRef());
        assertNotNull(domain3.getDomain());
        assertTrue(domain3.getDomain() instanceof CounterfactualDomainCategorical);
        CounterfactualDomainCategorical domain3Def = (CounterfactualDomainCategorical) domain3.getDomain();
        assertEquals(3, domain3Def.getCategories().size());
        assertTrue(domain3Def.getCategories().stream().map(JsonNode::asText).collect(Collectors.toList()).containsAll(Arrays.asList("A", "B", "C")));
    }

    private void mockServiceWithExplainabilityResult() {
        when(executionService.getExplainabilityResultById(eq(TEST_EXECUTION_ID)))
                .thenReturn(buildValidExplainabilityResult());
    }

    private void mockServiceWithNullExplainabilityResult() {
        when(executionService.getExplainabilityResultById(anyString()))
                .thenReturn(null);
    }

    private void mockServiceWithoutExplainabilityResult() {
        when(executionService.getExplainabilityResultById(anyString()))
                .thenThrow(new IllegalArgumentException("Explainability result does not exist."));
    }

    private void mockServiceWithCounterfactualRequest() {
        when(executionService.requestCounterfactuals(eq(TEST_EXECUTION_ID), any(), any()))
                .thenReturn(buildValidCounterfactual());
    }
}
