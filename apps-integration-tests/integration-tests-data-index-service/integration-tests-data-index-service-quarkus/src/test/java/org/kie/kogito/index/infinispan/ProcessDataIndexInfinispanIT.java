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
package org.kie.kogito.index.infinispan;

import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.kogito.index.AbstractProcessDataIndexIT;
import org.kie.kogito.index.api.DataIndexServiceClient;
import org.kie.kogito.index.api.DataIndexServiceClientConfig;
import org.kie.kogito.index.api.DataIndexServiceClientFactory;
import org.kie.kogito.index.api.impl.DataIndexServiceClientFactoryImpl;
import org.kie.kogito.index.api.model.NodeInstance;
import org.kie.kogito.index.quarkus.DataIndexInfinispanQuarkusTestResource;
import org.kie.kogito.index.quarkus.InfinispanTestProfile;
import org.mockito.junit.jupiter.MockitoExtension;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

import static java.lang.String.format;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@QuarkusTest
@TestProfile(InfinispanTestProfile.class)
@ExtendWith(MockitoExtension.class)
public class ProcessDataIndexInfinispanIT extends AbstractProcessDataIndexIT {

    @ConfigProperty(name = DataIndexInfinispanQuarkusTestResource.KOGITO_DATA_INDEX_SERVICE_URL)
    String dataIndex;

    @Override
    public String getDataIndexURL() {
        return dataIndex;
    }

    @Test
    public void testDataIndexAPIClient() throws Exception {
        DataIndexServiceClient dataIndexServiceClient;
        DataIndexServiceClientFactory dataIndexServiceClientFactory;
        DataIndexServiceClientConfig config = DataIndexServiceClientConfig.newBuilder()
                .serviceUrl(dataIndex)
                .build();
        dataIndexServiceClientFactory = new DataIndexServiceClientFactoryImpl();
        dataIndexServiceClient = dataIndexServiceClientFactory.newClient(config);

        String pId = super.createTestProcessInstance();
        getProcessInstanceById(pId, "ACTIVE");
        List<NodeInstance> nodeInstanceList = dataIndexServiceClient.getProcessInstanceNodes(pId);
        assertThat(nodeInstanceList.size()).isEqualTo(1);
        assertThat(nodeInstanceList.get(0).getName()).isEqualTo("firstLineApproval");

        assertThat(String.valueOf(dataIndexServiceClient.getProcessInstanceDiagram(pId))).isEqualTo(readFileContent("approvals-expected.svg"));

        assertThat(dataIndexServiceClient.abortProcessInstance(pId)).isEqualTo(format(
                "{\"id\":\"%s\",\"approver\":null,\"firstLineApproval\":null,\"secondLineApproval\":null,\"traveller\":{\"firstName\":\"Darth\",\"lastName\":\"Vader\",\"email\":\"darth.vader@deathstar.com\",\"nationality\":\"Tatooine\",\"address\":null,\"processed\":false}}",
                pId));

        getProcessInstanceById(pId, "ABORTED");
    }
}
