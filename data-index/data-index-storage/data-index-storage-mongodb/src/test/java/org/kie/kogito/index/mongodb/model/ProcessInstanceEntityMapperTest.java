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

package org.kie.kogito.index.mongodb.model;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import io.quarkus.mongodb.panache.runtime.MongoOperations;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.NodeInstance;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.ProcessInstanceError;
import org.kie.kogito.persistence.mongodb.model.MongoEntityMapper;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.kie.kogito.persistence.mongodb.model.ModelUtils.MAPPER;
import static org.kie.kogito.persistence.mongodb.model.ModelUtils.jsonNodeToDocument;
import static org.kie.kogito.persistence.mongodb.model.ModelUtils.zonedDateTimeToInstant;

class ProcessInstanceEntityMapperTest {

    ProcessInstanceEntityMapper processInstanceEntityMapper = new ProcessInstanceEntityMapper();

    static ProcessInstance processInstance;

    static ProcessInstanceEntity processInstanceEntity;

    @BeforeAll
    static void setup() {
        String nodeInstanceId = "testNodeInstanceId";
        String nodeInstanceName = "testNodeInstanceName";
        String nodeInstanceNodeId = "testNodeInstanceNodeId";
        String nodeInstanceType = "testNodeInstanceType";
        String nodeInstanceDefinitionId = "testNodeInstanceDefinitionId";

        String processInstanceErrorMessage = "testProcessInstanceErrorMessage";
        String processInstanceErrorNodeDefinitionId = "testProcessInstanceErrorNodeDefinitionId";

        String testId = "testId";
        String processId = "testProcessId";
        Set<String> roles = newHashSet("testRoles");
        Map<String, String> object = new HashMap<>();
        object.put("test", "testValue");
        JsonNode variables = MAPPER.valueToTree(object);
        String endpoint = "testEndpoint";
        Integer state = 2;
        ZonedDateTime time = ZonedDateTime.now();
        String rootProcessId = "testRootProcessId";
        String rootProcessInstanceId = "testRootProcessInstanceId";
        String parentProcessInstanceId = "testParentProcessInstanceId";
        String processName = "testProcessName";
        Set<String> addons = newHashSet("testAddons");
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

        processInstance = new ProcessInstance();
        processInstance.setId(testId);
        processInstance.setProcessId(processId);
        processInstance.setRoles(roles);
        processInstance.setVariables(variables);
        processInstance.setEndpoint(endpoint);
        processInstance.setNodes(newArrayList(nodeInstance));
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

        ProcessInstanceEntity.NodeInstanceEntity nodeInstanceEntity = new ProcessInstanceEntity.NodeInstanceEntity();
        nodeInstanceEntity.id = nodeInstanceId;
        nodeInstanceEntity.definitionId = nodeInstanceDefinitionId;
        nodeInstanceEntity.enter = zonedDateTimeToInstant(time);
        nodeInstanceEntity.exit = zonedDateTimeToInstant(time);
        nodeInstanceEntity.name = nodeInstanceName;
        nodeInstanceEntity.nodeId = nodeInstanceNodeId;
        nodeInstanceEntity.type = nodeInstanceType;

        ProcessInstanceEntity.ProcessInstanceErrorEntity processInstanceErrorEntity = new ProcessInstanceEntity.ProcessInstanceErrorEntity();
        processInstanceErrorEntity.message = processInstanceErrorMessage;
        processInstanceErrorEntity.nodeDefinitionId = processInstanceErrorNodeDefinitionId;

        processInstanceEntity = new ProcessInstanceEntity();
        processInstanceEntity.id = testId;
        processInstanceEntity.processId = processId;
        processInstanceEntity.roles = roles;
        processInstanceEntity.variables = jsonNodeToDocument(variables);
        processInstanceEntity.endpoint = endpoint;
        processInstanceEntity.nodes = newArrayList(nodeInstanceEntity);
        processInstanceEntity.state = state;
        processInstanceEntity.start = zonedDateTimeToInstant(time);
        processInstanceEntity.end = zonedDateTimeToInstant(time);
        processInstanceEntity.rootProcessId = rootProcessId;
        processInstanceEntity.rootProcessInstanceId = rootProcessInstanceId;
        processInstanceEntity.parentProcessInstanceId = parentProcessInstanceId;
        processInstanceEntity.processName = processName;
        processInstanceEntity.error = processInstanceErrorEntity;
        processInstanceEntity.addons = addons;
        processInstanceEntity.lastUpdate = zonedDateTimeToInstant(time);
        processInstanceEntity.businessKey = businessKey;
    }

    @Test
    void testGetEntityClass() {
        assertEquals(ProcessInstanceEntity.class, processInstanceEntityMapper.getEntityClass());
    }

    @Test
    void testMapToEntity() {
        ProcessInstanceEntity result = processInstanceEntityMapper.mapToEntity(processInstance.getId(), processInstance);
        assertEquals(processInstanceEntity, result);
    }

    @Test
    void testMapToModel() {
        ProcessInstance result = processInstanceEntityMapper.mapToModel(processInstanceEntity);
        assertEquals(processInstance, result);
    }

    @Test
    void testConvertAttribute() {
        assertEquals(MongoOperations.ID, processInstanceEntityMapper.convertAttribute(MongoEntityMapper.ID));

        assertEquals(ProcessInstanceEntityMapper.MONGO_NODES_ID_ATTRIBUTE,
                     processInstanceEntityMapper.convertAttribute(ProcessInstanceEntityMapper.NODES_ID_ATTRIBUTE));

        String testAttribute = "testAttribute";
        assertEquals(testAttribute, processInstanceEntityMapper.convertAttribute(testAttribute));
    }
}