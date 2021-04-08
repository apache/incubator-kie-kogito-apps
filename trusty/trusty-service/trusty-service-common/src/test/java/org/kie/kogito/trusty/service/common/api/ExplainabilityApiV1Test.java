/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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

import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.trusty.service.common.TrustyService;
import org.kie.kogito.trusty.service.common.requests.CounterfactualRequest;
import org.kie.kogito.trusty.service.common.responses.CounterfactualResponse;
import org.kie.kogito.trusty.storage.api.model.Counterfactual;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExplainabilityApiV1Test {

    private static final String EXECUTION_ID = "executionId";
    private static final String COUNTERFACTUAL_ID = "counterfactualId";

    private static final TrustyService trustyService = mock(TrustyService.class);

    private static final ExplainabilityApiV1 explainabilityEndpoint = new ExplainabilityApiV1();

    @BeforeAll
    public static void initialise() {
        explainabilityEndpoint.trustyService = trustyService;
    }

    @BeforeEach
    public void reset() {
        Mockito.reset(trustyService);
    }

    @Test
    public void testRequestCounterfactualsWhenExecutionDoesNotExist() {
        when(trustyService.requestCounterfactuals(anyString(), any(), any())).thenThrow(new IllegalArgumentException());

        CounterfactualRequest request = new CounterfactualRequest(Collections.emptyList(), Collections.emptyList());

        Response response = explainabilityEndpoint.requestCounterfactuals(EXECUTION_ID, request);
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void testRequestCounterfactualsWhenExecutionDoesExist() {
        when(trustyService.requestCounterfactuals(anyString(), any(), any())).thenReturn(new Counterfactual(EXECUTION_ID, COUNTERFACTUAL_ID));

        CounterfactualRequest request = new CounterfactualRequest(Collections.emptyList(), Collections.emptyList());

        Response response = explainabilityEndpoint.requestCounterfactuals(EXECUTION_ID, request);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Object entity = response.getEntity();
        assertNotNull(entity);
        assertTrue(entity instanceof CounterfactualResponse);
        CounterfactualResponse counterfactualResponse = (CounterfactualResponse) entity;
        assertEquals(EXECUTION_ID, counterfactualResponse.getExecutionId());
        assertEquals(COUNTERFACTUAL_ID, counterfactualResponse.getCounterfactualId());
    }

    @Test
    public void testGetAllCounterfactualsWhenExecutionDoesNotExist() {
        when(trustyService.getCounterfactuals(anyString())).thenThrow(new IllegalArgumentException());

        Response response = explainabilityEndpoint.getAllCounterfactuals(EXECUTION_ID);
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    @SuppressWarnings({ "unchecked" })
    public void testGetAllCounterfactualsWhenExecutionDoesExist() {
        when(trustyService.getCounterfactuals(anyString())).thenReturn(List.of(new Counterfactual(EXECUTION_ID, COUNTERFACTUAL_ID)));

        Response response = explainabilityEndpoint.getAllCounterfactuals(EXECUTION_ID);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Object entity = response.getEntity();
        assertNotNull(entity);
        assertTrue(entity instanceof List);
        List<CounterfactualResponse> counterfactualResponse = (List) entity;
        assertEquals(1, counterfactualResponse.size());

        CounterfactualResponse counterfactual = counterfactualResponse.get(0);
        assertEquals(EXECUTION_ID, counterfactual.getExecutionId());
        assertEquals(COUNTERFACTUAL_ID, counterfactual.getCounterfactualId());
    }

    @Test
    public void testGetCounterfactualWhenExecutionDoesNotExist() {
        when(trustyService.getCounterfactual(anyString(), anyString())).thenThrow(new IllegalArgumentException());

        Response response = explainabilityEndpoint.getCounterfactual(EXECUTION_ID, COUNTERFACTUAL_ID);
        assertNotNull(response);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void testGetCounterfactualWhenExecutionDoesExist() {
        when(trustyService.getCounterfactual(anyString(), anyString())).thenReturn(new Counterfactual(EXECUTION_ID, COUNTERFACTUAL_ID));

        Response response = explainabilityEndpoint.getCounterfactual(EXECUTION_ID, COUNTERFACTUAL_ID);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Object entity = response.getEntity();
        assertNotNull(entity);
        assertTrue(entity instanceof CounterfactualResponse);
        CounterfactualResponse counterfactualResponse = (CounterfactualResponse) entity;
        assertEquals(EXECUTION_ID, counterfactualResponse.getExecutionId());
        assertEquals(COUNTERFACTUAL_ID, counterfactualResponse.getCounterfactualId());
    }
}
