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

package org.kie.kogito.index.springboot.addon.routes;

import java.util.Map;

import org.kie.kogito.index.springboot.addon.graphql.GraphQLQuery;
import org.kie.kogito.index.springboot.addon.graphql.GraphQLQueryExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Controller
@ResponseBody
public class DataIndexGraphQLRoutes {

    private static Logger LOG = LoggerFactory.getLogger(DataIndexGraphQLRoutes.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GraphQLQueryExecutor graphQLQueryExecutor;

    @PostMapping(path = "/graphql", consumes = MediaType.APPLICATION_JSON_VALUE, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_GRAPHQL_RESPONSE_VALUE })
    public Mono<JsonNode> postGraphQL(@RequestBody GraphQLQuery graphQLQuery) {
        return graphQLQueryExecutor.execute(graphQLQuery);
    }

    @GetMapping(value = "/graphql", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_GRAPHQL_RESPONSE_VALUE })
    public ResponseEntity<Mono<JsonNode>> getGraphQL(@RequestParam("query") String query, @RequestParam(name = "variables", required = false) String variables,
            @RequestParam(name = "extensions", required = false) String extensions,
            @RequestParam(name = "operationName", required = false) String operationName) {
        try {
            GraphQLQuery graphQLQuery = new GraphQLQuery();
            graphQLQuery.setQuery(query);
            graphQLQuery.setOperationName(operationName);
            if (variables != null) {
                graphQLQuery.setVariables(objectMapper.readValue(variables, Map.class));
            }
            if (extensions != null) {
                graphQLQuery.setVariables(objectMapper.readValue(extensions, Map.class));
            }
            return ResponseEntity.ok(graphQLQueryExecutor.execute(graphQLQuery));
        } catch (Exception e) {
            LOG.error("Error parsing graphQL query", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
