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
package org.kie.kogito.jobs.service.management;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HttpGatekeeperFilterTest {

    private HttpGatekeeperFilter filter;

    @BeforeEach
    void setUp() throws Exception {
        filter = new HttpGatekeeperFilter();
        setField("healthCheckPath", "/q/health");
        setField("protectedPaths", List.of("/jobs", "/v2/jobs"));
        setField("whitelistedPaths", List.of("/management"));
    }

    @Test
    void shouldBlockJobsApiWhenInstanceIsNotLeader() throws Exception {
        RoutingContext routingContext = routingContext("/jobs/my-job");

        filter.masterFilter(routingContext);

        verify(routingContext.response()).setStatusCode(503);
        verify(routingContext.response()).setStatusMessage(HttpGatekeeperFilter.ERROR_MESSAGE);
        verify(routingContext).end();
        verify(routingContext, never()).next();
    }

    @Test
    void shouldAllowManagementApiWhenInstanceIsNotLeader() throws Exception {
        RoutingContext routingContext = routingContext("/management/resign");

        filter.masterFilter(routingContext);

        verify(routingContext).next();
        verify(routingContext, never()).end();
    }

    @Test
    void shouldAllowEmbeddedWorkflowApiWhenInstanceIsNotLeader() throws Exception {
        RoutingContext routingContext = routingContext("/orders/123");

        filter.masterFilter(routingContext);

        verify(routingContext).next();
        verify(routingContext, never()).end();
    }

    @Test
    void shouldAllowAllPathsWhenInstanceIsLeader() throws Exception {
        RoutingContext routingContext = routingContext("/jobs/my-job");
        filter.onMessagingStatusChange(new MessagingChangeEvent(true));

        filter.masterFilter(routingContext);

        verify(routingContext).next();
        verify(routingContext, never()).end();
    }

    @Test
    void shouldSupportStrictModeWithWhitelist() throws Exception {
        setField("protectedPaths", List.of("/"));
        setField("whitelistedPaths", List.of("/orders"));

        RoutingContext jobsRoutingContext = routingContext("/jobs/my-job");
        filter.masterFilter(jobsRoutingContext);
        verify(jobsRoutingContext.response()).setStatusCode(503);
        verify(jobsRoutingContext).end();

        RoutingContext workflowRoutingContext = routingContext("/orders/123");
        filter.masterFilter(workflowRoutingContext);
        verify(workflowRoutingContext).next();

        RoutingContext healthRoutingContext = routingContext("/q/health/ready");
        filter.masterFilter(healthRoutingContext);
        verify(healthRoutingContext).next();
    }

    private RoutingContext routingContext(String path) {
        RoutingContext routingContext = mock(RoutingContext.class);
        HttpServerRequest request = mock(HttpServerRequest.class);
        HttpServerResponse response = mock(HttpServerResponse.class);
        when(routingContext.request()).thenReturn(request);
        when(routingContext.response()).thenReturn(response);
        when(request.path()).thenReturn(path);
        when(response.setStatusCode(503)).thenReturn(response);
        when(response.setStatusMessage(HttpGatekeeperFilter.ERROR_MESSAGE)).thenReturn(response);
        return routingContext;
    }

    private void setField(String name, Object value) throws Exception {
        Field field = HttpGatekeeperFilter.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(filter, value);
    }
}
