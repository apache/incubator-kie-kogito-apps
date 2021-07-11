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
package org.kie.kogito.index.api.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.kie.kogito.index.api.DataIndexServiceClient;
import org.kie.kogito.index.api.GraphQLServiceClient;
import org.kie.kogito.index.api.model.NodeInstance;

public class DataIndexServiceClientImpl implements DataIndexServiceClient {

    public static final String ABORT_MUTATION_NAME = "ProcessInstanceAbort";
    public static final String RETRY_MUTATION_NAME = "ProcessInstanceRetry";
    public static final String GET_PROCESS_INSTANCE_DIAGRAM_MUTATION_NAME = "GetProcessInstanceDiagram";
    public static final String GET_PROCESS_INSTANCE_NODES_MUTATION_NAME = "GetProcessInstanceNodes";
    public static final String MUTATION_BY_PROCESS_INSTANCE_ID_REQUEST = "{ \"query\" : \"mutation{ %s ( processInstanceId: \\\"%s\\\") %s}\"}";

    private GraphQLServiceClient executeGraphqlRequestService;

    public DataIndexServiceClientImpl(GraphQLServiceClient executeGraphqlRequestService) {
        this.executeGraphqlRequestService = executeGraphqlRequestService;
    }

    @Override
    public String abortProcessInstance(String processInstanceId) {
        String requestStr = String.format(MUTATION_BY_PROCESS_INSTANCE_ID_REQUEST, ABORT_MUTATION_NAME, processInstanceId, "");
        return executeGraphqlRequestService.executeGraphqlRequest(ABORT_MUTATION_NAME, requestStr, String.class);
    }

    @Override
    public String retryProcessInstance(String processInstanceId) {
        String requestStr = String.format(MUTATION_BY_PROCESS_INSTANCE_ID_REQUEST, RETRY_MUTATION_NAME, processInstanceId, "");
        return executeGraphqlRequestService.executeGraphqlRequest(RETRY_MUTATION_NAME, requestStr, String.class);
    }

    @Override
    public String getProcessInstanceDiagram(String processInstanceId) {
        String requestStr = String.format(MUTATION_BY_PROCESS_INSTANCE_ID_REQUEST, GET_PROCESS_INSTANCE_DIAGRAM_MUTATION_NAME, processInstanceId, "");
        return executeGraphqlRequestService.executeGraphqlRequest(GET_PROCESS_INSTANCE_DIAGRAM_MUTATION_NAME, requestStr, String.class);
    }

    @Override
    public List<NodeInstance> getProcessInstanceNodes(String processInstanceId) {
        String requestStr = String.format(MUTATION_BY_PROCESS_INSTANCE_ID_REQUEST, GET_PROCESS_INSTANCE_NODES_MUTATION_NAME, processInstanceId, "{id nodeId name}");
        NodeInstance[] nodeInstances = executeGraphqlRequestService.executeGraphqlRequest(GET_PROCESS_INSTANCE_NODES_MUTATION_NAME, requestStr,
                NodeInstance[].class);
        return Arrays.asList(nodeInstances);
    }

    @Override
    public void close() throws IOException {
        executeGraphqlRequestService.close();
    }
}
