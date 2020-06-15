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

package org.kie.kogito.index.auth;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
class MultiTenantResolverTest {

    private static final String GRAPH_UI_PATH = System.getProperty("kogito.dataindex.vertx-graphql.ui.path", "/graphiql");
    private static final String GRAPH_UI_TENANT = System.getProperty("kogito.dataindex.vertx-graphql.ui.tenant", "web-app-tenant");

    @Mock
    RoutingContext routingContextMock;

    @Mock
    HttpServerRequest requestMock;

    @InjectMocks
    MultiTenantResolver multiTenantResolver;

    @Before
    public void setup(){
        lenient().when(routingContextMock.request()).thenReturn(requestMock);
    }

    @Test
    void resolveGraphiqlTenantTest() {
        lenient().when(requestMock.path()).thenReturn(GRAPH_UI_PATH);
        assertThat(GRAPH_UI_TENANT.equals(multiTenantResolver.resolve(routingContextMock)));
    }

    @Test
    void resolveGraphqlTenantTest() {
        lenient().when(requestMock.path()).thenReturn("/graphql");
        assertThat(multiTenantResolver.resolve(routingContextMock)).isNull();
    }

    @Test
    void resolveOtherPathTest() {
        lenient().when(requestMock.path()).thenReturn("/other");
        assertThat(multiTenantResolver.resolve(routingContextMock)).isNull();
    }
}