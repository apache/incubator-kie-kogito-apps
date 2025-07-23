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

package org.kie.kogito.index.springboot.addon.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.execution.GraphQlSource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import reactor.core.publisher.Mono;

@Component
public class GraphQLQueryExecutor {

    private ObjectMapper objectMapper;
    private GraphQL graphQL;

    @Autowired
    GraphQLQueryExecutor(ObjectMapper objectMapper, GraphQlSource graphQlSource) {
        this.objectMapper = objectMapper;
        this.graphQL = graphQlSource.graphQl();
    }

    public Mono<JsonNode> execute(GraphQLQuery graphQLQuery) {
        ExecutionInput executionInput = builtExecutionInput(graphQLQuery);
        return Mono.fromFuture(graphQL.executeAsync(executionInput))
                .map(this::buildResult);
    }

    private ExecutionInput builtExecutionInput(GraphQLQuery graphQLQuery) {
        ExecutionInput.Builder executionInputBuilder = ExecutionInput.newExecutionInput()
                .query(graphQLQuery.getQuery())
                .operationName(graphQLQuery.getOperationName());

        if (graphQLQuery.getVariables() != null) {
            executionInputBuilder.variables(graphQLQuery.getVariables());
        }

        if (graphQLQuery.getExtensions() != null) {
            executionInputBuilder.variables(graphQLQuery.getExtensions());
        }

        return executionInputBuilder.build();
    }

    private JsonNode buildResult(ExecutionResult executionResult) {
        return objectMapper.valueToTree(executionResult.toSpecification());
    }
}
