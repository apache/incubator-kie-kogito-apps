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
package org.kie.kogito.index.api.mp;

import java.io.IOException;

import org.kie.kogito.index.api.GraphQLServiceClient;
import org.kie.kogito.index.api.GraphQLServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.kie.kogito.index.api.util.JsonUtils.OBJECT_MAPPER;

public class GraphQLServiceClientMP implements GraphQLServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphQLServiceClientMP.class);

    private static final String DATA_FIELD = "data";
    private static final String ERRORS_FIELD = "errors";

    private GraphQLServiceClientRest client;

    public GraphQLServiceClientMP(GraphQLServiceClientRest client) {
        this.client = client;
    }

    @Override
    public <T> T executeGraphqlRequest(String opName, String requestStr, Class<T> resultType) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Executing query: {} with type: {}", requestStr, resultType);
        }
        ObjectNode result = client.executeGraphqlRequest(requestStr);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Query result is: {}", result);
        }
        if (result.has(ERRORS_FIELD)) {
            throw new GraphQLServiceException("An error was produced during query execution: " + result.get(ERRORS_FIELD));
        }
        JsonNode data = result.get(DATA_FIELD);
        JsonNode itemsNode = data.get(opName);
        try {
            return OBJECT_MAPPER.treeToValue(itemsNode, resultType);
        } catch (JsonProcessingException e) {
            throw new GraphQLServiceException("An error was produced during query results unmarshalling: " + e.getMessage(), e);
        }
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
