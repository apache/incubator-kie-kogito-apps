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

package org.kie.kogito.index.api;

import java.util.List;

import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.kogito.index.api.impl.DataIndexServiceClientFactoryImpl;
import org.kie.kogito.index.api.impl.DataIndexServiceClientImpl;
import org.kie.kogito.index.api.model.NodeInstance;
import org.kie.kogito.index.api.utils.DataIndexWiremock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.kie.kogito.index.api.utils.DataIndexWiremock.DATAINDEX_SERVICE_URL;
import static org.kie.kogito.index.api.utils.DataIndexWiremock.PROCESS_INSTANCE_ID;
import static org.kie.kogito.index.api.utils.DataIndexWiremock.PROCESS_INSTANCE_ID_FAIL;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@QuarkusTest
@QuarkusTestResource(DataIndexWiremock.class)
public class DataIndexApiClientTest {

    private DataIndexServiceClient dataIndexServiceClient = new DataIndexServiceClientImpl(mock(GraphQLServiceClient.class));
    private DataIndexServiceClientFactory dataIndexServiceClientFactory;

    @BeforeEach
    public void setup() {
        String serviceUrl = ConfigProvider.getConfig().getValue(DATAINDEX_SERVICE_URL, String.class);
        DataIndexServiceClientConfig config = DataIndexServiceClientConfig.newBuilder()
                .serviceUrl(serviceUrl)
                .build();
        dataIndexServiceClientFactory = new DataIndexServiceClientFactoryImpl();
        dataIndexServiceClient = dataIndexServiceClientFactory.newClient(config);
    }

    @Test
    public void testAbortGraphqlServiceClient() {
        assertThat(dataIndexServiceClient.abortProcessInstance(PROCESS_INSTANCE_ID)).isEqualTo("{\"id\":\"9e41bc95-81b2-4893-91a9-947a36f36776\",\"it_approval\":true,\"hr_approval\":true}");
        assertThatExceptionOfType(GraphQLServiceException.class).isThrownBy(() -> dataIndexServiceClient.abortProcessInstance(PROCESS_INSTANCE_ID_FAIL));
    }

    @Test
    public void testRetryGraphqlServiceClient() {
        assertThat(dataIndexServiceClient.retryProcessInstance(PROCESS_INSTANCE_ID)).isEqualTo("{\"id\":\"9e41bc95-81b2-4893-91a9-947a36f36776\",\"it_approval\":true,\"hr_approval\":true}");
        assertThatExceptionOfType(GraphQLServiceException.class).isThrownBy(() -> dataIndexServiceClient.retryProcessInstance(PROCESS_INSTANCE_ID_FAIL));
    }

    @Test
    public void testGetProcessInstanceDiagramGraphqlServiceClient() {
        assertThat(dataIndexServiceClient.getProcessInstanceDiagram(PROCESS_INSTANCE_ID)).isEqualTo("svgContent");
        assertThatExceptionOfType(GraphQLServiceException.class).isThrownBy(() -> dataIndexServiceClient.getProcessInstanceDiagram(PROCESS_INSTANCE_ID_FAIL));
    }

    @Test
    public void testGetProcessInstanceNodesGraphqlServiceClient() {
        List<NodeInstance> nodeInstanceList = dataIndexServiceClient.getProcessInstanceNodes(PROCESS_INSTANCE_ID);
        assertThat(nodeInstanceList.size()).isEqualTo(1);
        assertThat(nodeInstanceList.get(0).getId()).isEqualTo("8021ded8-e9bf-4761-92b4-4fcac3a8af85");
        assertThat(nodeInstanceList.get(0).getName()).isEqualTo("HRInterview");
        assertThatExceptionOfType(GraphQLServiceException.class).isThrownBy(() -> dataIndexServiceClient.getProcessInstanceNodes(PROCESS_INSTANCE_ID_FAIL));
    }
}
