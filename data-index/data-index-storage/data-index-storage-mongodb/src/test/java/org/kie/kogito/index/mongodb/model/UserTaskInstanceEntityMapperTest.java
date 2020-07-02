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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.UserTaskInstance;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.kie.kogito.index.mongodb.model.ModelUtils.MAPPER;
import static org.kie.kogito.index.mongodb.model.ModelUtils.jsonNodeToDocument;
import static org.kie.kogito.index.mongodb.model.ModelUtils.zonedDateTimeToInstant;

class UserTaskInstanceEntityMapperTest {

    UserTaskInstanceEntityMapper userTaskInstanceEntityMapper = new UserTaskInstanceEntityMapper();

    static UserTaskInstance userTaskInstance;

    static UserTaskInstanceEntity userTaskInstanceEntity;

    @BeforeAll
    static void setup() {
        String testId = "testId";
        String description = "testDescription";
        String name = "testName";
        String priority = "10";
        String processInstanceId = "testProcessInstanceId";
        String state = "testState";
        String actualOwner = "testActualOwner";
        Set<String> adminGroups = newHashSet("testAdminGroups");
        Set<String> adminUsers = newHashSet("testAdminUsers");
        ZonedDateTime time = ZonedDateTime.now();
        Set<String> excludedUsers = newHashSet("testExcludedUsers");
        Set<String> potentialGroups = newHashSet("testPotentialGroups");
        Set<String> potentialUsers = newHashSet("testPotentialUsers");
        String referenceName = "testReferenceName";
        String processId = "testProcessId";
        String rootProcessId = "testRootProcessId";
        String rootProcessInstanceId = "testRootProcessInstanceId";
        Map<String, String> object = new HashMap<>();
        object.put("test", "testValue");
        JsonNode inputs = MAPPER.valueToTree(object);
        JsonNode outputs = MAPPER.valueToTree(object);

        userTaskInstance = new UserTaskInstance();
        userTaskInstance.setId(testId);
        userTaskInstance.setDescription(description);
        userTaskInstance.setName(name);
        userTaskInstance.setPriority(priority);
        userTaskInstance.setProcessInstanceId(processInstanceId);
        userTaskInstance.setState(state);
        userTaskInstance.setActualOwner(actualOwner);
        userTaskInstance.setAdminGroups(adminGroups);
        userTaskInstance.setAdminUsers(adminUsers);
        userTaskInstance.setCompleted(time);
        userTaskInstance.setStarted(time);
        userTaskInstance.setExcludedUsers(excludedUsers);
        userTaskInstance.setPotentialGroups(potentialGroups);
        userTaskInstance.setPotentialUsers(potentialUsers);
        userTaskInstance.setReferenceName(referenceName);
        userTaskInstance.setLastUpdate(time);
        userTaskInstance.setProcessId(processId);
        userTaskInstance.setRootProcessId(rootProcessId);
        userTaskInstance.setRootProcessInstanceId(rootProcessInstanceId);
        userTaskInstance.setInputs(inputs);
        userTaskInstance.setOutputs(outputs);

        userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.id = testId;
        userTaskInstanceEntity.description = description;
        userTaskInstanceEntity.name = name;
        userTaskInstanceEntity.priority = priority;
        userTaskInstanceEntity.processInstanceId = processInstanceId;
        userTaskInstanceEntity.state = state;
        userTaskInstanceEntity.actualOwner = actualOwner;
        userTaskInstanceEntity.adminGroups = adminGroups;
        userTaskInstanceEntity.adminUsers = adminUsers;
        userTaskInstanceEntity.completed = zonedDateTimeToInstant(time);
        userTaskInstanceEntity.started = zonedDateTimeToInstant(time);
        userTaskInstanceEntity.excludedUsers = excludedUsers;
        userTaskInstanceEntity.potentialGroups = potentialGroups;
        userTaskInstanceEntity.potentialUsers = potentialUsers;
        userTaskInstanceEntity.referenceName = referenceName;
        userTaskInstanceEntity.lastUpdate = zonedDateTimeToInstant(time);
        userTaskInstanceEntity.processId = processId;
        userTaskInstanceEntity.rootProcessId = rootProcessId;
        userTaskInstanceEntity.rootProcessInstanceId = rootProcessInstanceId;
        userTaskInstanceEntity.inputs = jsonNodeToDocument(inputs);
        userTaskInstanceEntity.outputs = jsonNodeToDocument(outputs);
    }

    @Test
    void testGetEntityClass() {
        assertEquals(UserTaskInstanceEntity.class, userTaskInstanceEntityMapper.getEntityClass());
    }

    @Test
    void testMapToEntity() {
        UserTaskInstanceEntity result = userTaskInstanceEntityMapper.mapToEntity(userTaskInstance.getId(), userTaskInstance);
        assertEquals(userTaskInstanceEntity, result);
    }

    @Test
    void testMapToModel() {
        UserTaskInstance result = userTaskInstanceEntityMapper.mapToModel(userTaskInstanceEntity);
        assertEquals(userTaskInstance, result);
    }
}