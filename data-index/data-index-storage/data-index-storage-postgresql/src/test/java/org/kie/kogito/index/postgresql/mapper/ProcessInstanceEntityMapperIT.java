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

package org.kie.kogito.index.postgresql.mapper;

import java.time.ZonedDateTime;
import java.util.Set;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.Milestone;
import org.kie.kogito.index.model.NodeInstance;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.ProcessInstanceError;
import org.kie.kogito.index.postgresql.model.MilestoneEntity;
import org.kie.kogito.index.postgresql.model.NodeInstanceEntity;
import org.kie.kogito.index.postgresql.model.ProcessInstanceEntity;
import org.kie.kogito.index.postgresql.model.ProcessInstanceErrorEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.quarkus.test.junit.QuarkusTest;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class ProcessInstanceEntityMapperIT {

    ObjectMapper jsonMapper = new ObjectMapper();
    ProcessInstance processInstance = new ProcessInstance();
    ProcessInstanceEntity processInstanceEntity = new ProcessInstanceEntity();

    @Inject
    ProcessInstanceEntityMapper mapper;

    @BeforeEach
    void setup() {
        String nodeInstanceId = "testNodeInstanceId";
        String nodeInstanceName = "testNodeInstanceName";
        String nodeInstanceNodeId = "testNodeInstanceNodeId";
        String nodeInstanceType = "testNodeInstanceType";
        String nodeInstanceDefinitionId = "testNodeInstanceDefinitionId";

        String processInstanceErrorMessage = "testProcessInstanceErrorMessage";
        String processInstanceErrorNodeDefinitionId = "testProcessInstanceErrorNodeDefinitionId";

        String milestoneId = "testMilestone";
        String milestoneName = "testMilestoneName";
        String milestoneStatus = "testMilestoneStatus";

        String testId = "testId";
        String processId = "testProcessId";
        Set<String> roles = singleton("testRoles");
        ObjectNode variables = jsonMapper.createObjectNode();
        variables.put("test", "testValue");
        String endpoint = "testEndpoint";
        Integer state = 2;
        ZonedDateTime time = ZonedDateTime.now();
        String rootProcessId = "testRootProcessId";
        String rootProcessInstanceId = "testRootProcessInstanceId";
        String parentProcessInstanceId = "testParentProcessInstanceId";
        String processName = "testProcessName";
        Set<String> addons = singleton("testAddons");
        String businessKey = "testBusinessKey";

        NodeInstance nodeInstance = new NodeInstance();
        nodeInstance.setId(nodeInstanceId);
        nodeInstance.setDefinitionId(nodeInstanceDefinitionId);
        nodeInstance.setExit(time);
        nodeInstance.setEnter(time);
        nodeInstance.setType(nodeInstanceType);
        nodeInstance.setNodeId(nodeInstanceNodeId);
        nodeInstance.setName(nodeInstanceName);

        ProcessInstanceError processInstanceError = new ProcessInstanceError();
        processInstanceError.setMessage(processInstanceErrorMessage);
        processInstanceError.setNodeDefinitionId(processInstanceErrorNodeDefinitionId);

        Milestone milestone = new Milestone();
        milestone.setId(milestoneId);
        milestone.setName(milestoneName);
        milestone.setStatus(milestoneStatus);

        processInstance.setId(testId);
        processInstance.setProcessId(processId);
        processInstance.setRoles(roles);
        processInstance.setVariables(variables);
        processInstance.setEndpoint(endpoint);
        processInstance.setNodes(singletonList(nodeInstance));
        processInstance.setState(state);
        processInstance.setStart(time);
        processInstance.setEnd(time);
        processInstance.setRootProcessId(rootProcessId);
        processInstance.setRootProcessInstanceId(rootProcessInstanceId);
        processInstance.setParentProcessInstanceId(parentProcessInstanceId);
        processInstance.setProcessName(processName);
        processInstance.setError(processInstanceError);
        processInstance.setAddons(addons);
        processInstance.setLastUpdate(time);
        processInstance.setBusinessKey(businessKey);
        processInstance.setMilestones(singletonList(milestone));

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(nodeInstanceId);
        nodeInstanceEntity.setDefinitionId(nodeInstanceDefinitionId);
        nodeInstanceEntity.setEnter(time);
        nodeInstanceEntity.setExit(time);
        nodeInstanceEntity.setName(nodeInstanceName);
        nodeInstanceEntity.setNodeId(nodeInstanceNodeId);
        nodeInstanceEntity.setType(nodeInstanceType);

        ProcessInstanceErrorEntity processInstanceErrorEntity = new ProcessInstanceErrorEntity();
        processInstanceErrorEntity.setMessage(processInstanceErrorMessage);
        processInstanceErrorEntity.setNodeDefinitionId(processInstanceErrorNodeDefinitionId);

        MilestoneEntity milestoneEntity = new MilestoneEntity();
        milestoneEntity.setId(milestoneId);
        milestoneEntity.setName(milestoneName);
        milestoneEntity.setStatus(milestoneStatus);

        processInstanceEntity.setId(testId);
        processInstanceEntity.setProcessId(processId);
        processInstanceEntity.setRoles(roles);
        processInstanceEntity.setVariables(variables);
        processInstanceEntity.setEndpoint(endpoint);
        processInstanceEntity.setNodes(singletonList(nodeInstanceEntity));
        processInstanceEntity.setState(state);
        processInstanceEntity.setStart(time);
        processInstanceEntity.setEnd(time);
        processInstanceEntity.setRootProcessId(rootProcessId);
        processInstanceEntity.setRootProcessInstanceId(rootProcessInstanceId);
        processInstanceEntity.setParentProcessInstanceId(parentProcessInstanceId);
        processInstanceEntity.setProcessName(processName);
        processInstanceEntity.setError(processInstanceErrorEntity);
        processInstanceEntity.setAddons(addons);
        processInstanceEntity.setLastUpdate(time);
        processInstanceEntity.setBusinessKey(businessKey);
        processInstanceEntity.setMilestones(singletonList(milestoneEntity));
    }

    @Test
    void testMapToEntity() {
        ProcessInstanceEntity result = mapper.mapToEntity(processInstance);
        assertThat(result).isEqualToIgnoringGivenFields(processInstanceEntity, "$$_hibernate_tracker");
    }

    @Test
    void testMapToModel() {
        ProcessInstance result = mapper.mapToModel(processInstanceEntity);
        assertThat(result).isEqualToComparingFieldByField(processInstance);
    }

}
