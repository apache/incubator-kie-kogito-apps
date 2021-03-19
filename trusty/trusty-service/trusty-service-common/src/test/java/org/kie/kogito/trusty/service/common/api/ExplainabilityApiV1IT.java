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

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.trusty.service.common.TrustyService;
import org.kie.kogito.trusty.service.common.requests.CounterfactualSearchDomain;
import org.kie.kogito.trusty.service.common.requests.DomainCategorical;
import org.kie.kogito.trusty.service.common.requests.DomainNumerical;
import org.kie.kogito.trusty.service.common.responses.CounterfactualResponse;
import org.kie.kogito.trusty.service.common.responses.SalienciesResponse;
import org.kie.kogito.trusty.service.common.responses.SaliencyResponse;
import org.kie.kogito.trusty.service.common.shared.TypedVariableWithValue;
import org.kie.kogito.trusty.storage.api.model.CounterfactualResult;
import org.kie.kogito.trusty.storage.api.model.ExplainabilityResult;
import org.kie.kogito.trusty.storage.api.model.ExplainabilityStatus;
import org.kie.kogito.trusty.storage.api.model.FeatureImportance;
import org.kie.kogito.trusty.storage.api.model.Saliency;
import org.mockito.ArgumentCaptor;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class ExplainabilityApiV1IT {

    private static final String TEST_EXECUTION_ID = "executionId";

    private static final String TEST_COUNTERFACTUAL_ID = "counterfactualId";

    @InjectMock
    TrustyService executionService;

    private static ExplainabilityResult buildValidExplainabilityResult() {
        return new ExplainabilityResult(
                TEST_EXECUTION_ID,
                ExplainabilityStatus.SUCCEEDED,
                null,
                List.of(
                        new Saliency("O1", "Output1", List.of(
                                new FeatureImportance("Feature1", 0.49384),
                                new FeatureImportance("Feature2", -0.1084))),
                        new Saliency("O2", "Output2", List.of(
                                new FeatureImportance("Feature1", 0.0),
                                new FeatureImportance("Feature2", 0.70293)))));
    }

    private static CounterfactualResult buildValidCounterfactualRequest() {
        return new CounterfactualResult(TEST_EXECUTION_ID, TEST_COUNTERFACTUAL_ID);
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

        List<SaliencyResponse> sortedSaliencies = response.getSaliencies().stream()
                .sorted((s1, s2) -> new CompareToBuilder().append(s1.getOutcomeName(), s2.getOutcomeName()).toComparison())
                .collect(Collectors.toList());

        assertNotNull(sortedSaliencies.get(0));
        assertEquals("Output1", sortedSaliencies.get(0).getOutcomeName());
        assertNotNull(sortedSaliencies.get(0).getFeatureImportance());
        assertSame(2, sortedSaliencies.get(0).getFeatureImportance().size());
        assertEquals("Feature1", sortedSaliencies.get(0).getFeatureImportance().get(0).getFeatureName());
        assertEquals(0.49384, sortedSaliencies.get(0).getFeatureImportance().get(0).getFeatureScore());
        assertEquals("Feature2", sortedSaliencies.get(0).getFeatureImportance().get(1).getFeatureName());
        assertEquals(-0.1084, sortedSaliencies.get(0).getFeatureImportance().get(1).getFeatureScore());

        assertNotNull(sortedSaliencies.get(1));
        assertEquals("Output2", sortedSaliencies.get(1).getOutcomeName());
        assertNotNull(sortedSaliencies.get(1).getFeatureImportance());
        assertSame(2, sortedSaliencies.get(1).getFeatureImportance().size());
        assertEquals("Feature1", sortedSaliencies.get(1).getFeatureImportance().get(0).getFeatureName());
        assertEquals(0.0, sortedSaliencies.get(1).getFeatureImportance().get(0).getFeatureScore());
        assertEquals("Feature2", sortedSaliencies.get(1).getFeatureImportance().get(1).getFeatureName());
        assertEquals(0.70293, sortedSaliencies.get(1).getFeatureImportance().get(1).getFeatureScore());
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
    void testConverterMethodsNotThrowingWithNullModelValues() {
        Assertions.assertDoesNotThrow(() -> ExplainabilityApiV1.explainabilityResultModelToResponse(null));
        Assertions.assertDoesNotThrow(() -> ExplainabilityApiV1.featureImportanceModelToResponse(null));
        Assertions.assertDoesNotThrow(() -> ExplainabilityApiV1.saliencyModelToResponse(null));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testCounterfactualRequest() {
        ArgumentCaptor<List<TypedVariableWithValue>> goalsCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<CounterfactualSearchDomain>> searchDomainsCaptor = ArgumentCaptor.forClass(List.class);

        mockServiceWithCounterfactualRequest();

        JsonArray goals = Json
                .createArrayBuilder()
                .add(Json
                        .createObjectBuilder()
                        .add("name", "deposit")
                        .add("typeRef", "number")
                        .add("value", 5000))
                .add(Json
                        .createObjectBuilder()
                        .add("name", "approved")
                        .add("typeRef", "boolean")
                        .add("value", true))
                .build();
        JsonArray searchDomains = Json
                .createArrayBuilder()
                .add(Json
                        .createObjectBuilder()
                        .add("isFixed", true)
                        .add("name", "age")
                        .add("typeRef", "number"))
                .add(Json
                        .createObjectBuilder()
                        .add("isFixed", false)
                        .add("name", "income")
                        .add("typeRef", "number")
                        .add("domain", Json
                                .createObjectBuilder()
                                .add("type", "numerical")
                                .add("lowerBound", 0)
                                .add("upperBound", 1000)))
                .add(Json
                        .createObjectBuilder()
                        .add("isFixed", false)
                        .add("name", "taxCode")
                        .add("typeRef", "string")
                        .add("domain", Json
                                .createObjectBuilder()
                                .add("type", "categorical")
                                .add("categories", Json.createArrayBuilder().add("A").add("B").add("C"))))
                .build();
        JsonObject params = Json.createObjectBuilder().add("goals", goals).add("searchDomains", searchDomains).build();

        CounterfactualResponse response = given()
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter())
                .contentType(MediaType.APPLICATION_JSON)
                .body(params.toString())
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
        assertTrue(domain2.getDomain() instanceof DomainNumerical);
        DomainNumerical domain2Def = (DomainNumerical) domain2.getDomain();
        assertEquals(0, domain2Def.getLowerBound());
        assertEquals(1000, domain2Def.getUpperBound());

        CounterfactualSearchDomain domain3 = searchDomainsParameter.get(2);
        assertFalse(domain3.isFixed());
        assertEquals("taxCode", domain3.getName());
        assertEquals("string", domain3.getTypeRef());
        assertNotNull(domain3.getDomain());
        assertTrue(domain3.getDomain() instanceof DomainCategorical);
        DomainCategorical domain3Def = (DomainCategorical) domain3.getDomain();
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
                .thenReturn(buildValidCounterfactualRequest());
    }
}
