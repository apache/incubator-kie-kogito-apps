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
package org.kie.kogito.app.audit.quarkus;

import org.kie.kogito.app.audit.api.DataAuditQuery;
import org.kie.kogito.app.audit.api.DataAuditQueryService;
import org.kie.kogito.app.audit.api.DataAuditStoreProxyService;
import org.kie.kogito.app.audit.spi.DataAuditContextFactory;

import io.quarkus.vertx.web.Route;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.graphql.ExecutionInputBuilderWithContext;
import io.vertx.ext.web.handler.graphql.GraphQLHandler;
import io.vertx.ext.web.handler.graphql.GraphQLHandlerOptions;

import static io.quarkus.vertx.web.Route.HttpMethod.GET;
import static io.quarkus.vertx.web.Route.HttpMethod.POST;
import static org.kie.kogito.app.audit.api.SubsystemConstants.DATA_AUDIT_QUERY_PATH;
import static org.kie.kogito.app.audit.api.SubsystemConstants.DATA_AUDIT_REGISTRY_PATH;
import static org.kie.kogito.app.audit.graphql.GraphQLSchemaManager.graphQLSchemaManagerInstance;

import graphql.GraphQL;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class GraphQLJPADataAuditRouter {

    GraphQL graphQL;

    GraphQLHandler graphQLHandler;

    @Inject
    DataAuditContextFactory dataAuditContextFactory;

    private DataAuditQueryService dataAuditQueryService;

    private DataAuditStoreProxyService dataAuditStoreProxyService;

    @PostConstruct
    public void init() {
        graphQLSchemaManagerInstance().rebuildDefinitions(dataAuditContextFactory.newDataAuditContext());
        dataAuditQueryService = DataAuditQueryService.newAuditQuerySerice();
        dataAuditStoreProxyService = DataAuditStoreProxyService.newAuditStoreService();
        graphQLHandler = GraphQLHandler.create(dataAuditQueryService.getGraphQL(), new GraphQLHandlerOptions());
    }

    @Route(path = DATA_AUDIT_QUERY_PATH, type = Route.HandlerType.BLOCKING, order = 2, methods = { GET })
    public void blockingGraphQLHandlerGet(RoutingContext rc) {
        graphQLHandler.beforeExecute(this::beforeExecuteHTTP).handle(rc);
    }

    @Route(path = DATA_AUDIT_QUERY_PATH, type = Route.HandlerType.BLOCKING, order = 2, methods = { POST })
    public void blockingGraphQLHandlerPost(RoutingContext rc) {
        graphQLHandler.beforeExecute(this::beforeExecuteHTTP).handle(rc);
    }

    @Route(path = DATA_AUDIT_REGISTRY_PATH, type = Route.HandlerType.BLOCKING, order = 2, methods = { POST })
    public void blockingRegistryHandlerPost(RoutingContext rc) {
        try {
            JsonObject jsonObject = rc.body().asJsonObject();
            DataAuditQuery dataAuditQuery = new DataAuditQuery();
            dataAuditQuery.setIdentifier(jsonObject.getString("identifier"));
            dataAuditQuery.setGraphQLDefinition(jsonObject.getString("graphQLDefinition"));
            dataAuditQuery.setQuery(jsonObject.getString("query"));
            dataAuditStoreProxyService.storeQuery(dataAuditContextFactory.newDataAuditContext(), dataAuditQuery);
            graphQLHandler = GraphQLHandler.create(dataAuditQueryService.getGraphQL(), new GraphQLHandlerOptions());
            rc.response().setStatusCode(200).end();
        } catch (Exception e) {
            rc.response().setStatusCode(400).end();
        }

    }

    private void beforeExecuteHTTP(ExecutionInputBuilderWithContext<RoutingContext> config) {
        config.builder().localContext(dataAuditContextFactory.newDataAuditContext());
    }

}
