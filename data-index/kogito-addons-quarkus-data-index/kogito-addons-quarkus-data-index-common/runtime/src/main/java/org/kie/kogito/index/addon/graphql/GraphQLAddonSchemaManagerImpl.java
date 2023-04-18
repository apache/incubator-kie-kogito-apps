/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.index.addon.graphql;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;

import org.kie.kogito.dashboard.model.CustomDashboardInfo;
import org.kie.kogito.index.graphql.AbstractGraphQLSchemaManager;
import org.kie.kogito.index.model.ProcessInstanceState;

import graphql.scalars.ExtendedScalars;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.TypeDefinitionRegistry;

@ApplicationScoped
public class GraphQLAddonSchemaManagerImpl extends AbstractGraphQLSchemaManager {

    public GraphQLSchema createSchema() {
        TypeDefinitionRegistry typeDefinitionRegistry = new TypeDefinitionRegistry();
        typeDefinitionRegistry.merge(loadSchemaDefinitionFile("basic.schema.graphqls"));

        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .type("Query", builder -> {
                    builder.dataFetcher("ProcessInstances", this::getProcessInstancesValues);
                    builder.dataFetcher("UserTaskInstances", this::getUserTaskInstancesValues);

                    builder.dataFetcher("CustomDashboards", this::getCustomDashboards);
                    builder.dataFetcher("CustomDashboardCount", this::getCustomDashboardCount);
                    builder.dataFetcher("CustomDashboardContent", this::getCustomDashboardContent);
                    return builder;
                })
                .type("ProcessInstance", builder -> {
                    builder.dataFetcher("parentProcessInstance", this::getParentProcessInstanceValue);
                    builder.dataFetcher("childProcessInstances", this::getChildProcessInstancesValues);
                    builder.dataFetcher("serviceUrl", this::getProcessInstanceServiceUrl);
                    builder.dataFetcher("diagram", this::getProcessInstanceDiagram);
                    builder.dataFetcher("source", this::getProcessInstanceSourceFileContent);
                    builder.dataFetcher("nodeDefinitions", this::getProcessNodes);
                    return builder;
                })
                .type("ProcessInstanceState", builder -> {
                    builder.enumValues(name -> ProcessInstanceState.valueOf(name).ordinal());
                    return builder;
                })
                .scalar(getDateTimeScalarType())
                .scalar(ExtendedScalars.GraphQLBigDecimal)
                .scalar(ExtendedScalars.GraphQLLong)
                .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
    }

    protected CompletableFuture<Integer> getCustomDashboardCount(DataFetchingEnvironment env) {
        String serverUrl = env.getArgument("serverUrl");
        CompletableFuture<Map> result = new CompletableFuture<>();
        return getDataIndexApiExecutor().getCustomDashboardCount(serverUrl);
    }

    protected CompletableFuture<List<CustomDashboardInfo>> getCustomDashboards(DataFetchingEnvironment env) {
        String serverUrl = env.getArgument("serverUrl");
        String names = env.getArgument("names");
        return getDataIndexApiExecutor().getCustomDashboards(serverUrl, names);
    }

    protected CompletableFuture<String> getCustomDashboardContent(DataFetchingEnvironment env) {
        String serverUrl = env.getArgument("serverUrl");
        String name = env.getArgument("name");
        return getDataIndexApiExecutor().getCustomDashboardContent(serverUrl, name);
    }
}
