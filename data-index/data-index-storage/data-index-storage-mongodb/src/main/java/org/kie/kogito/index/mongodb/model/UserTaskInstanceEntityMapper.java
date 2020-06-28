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

import com.fasterxml.jackson.databind.JsonNode;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.persistence.mongodb.model.MongoEntityMapper;

import static org.kie.kogito.index.mongodb.model.ModelUtils.documentToJsonNode;
import static org.kie.kogito.index.mongodb.model.ModelUtils.instantToZonedDateTime;
import static org.kie.kogito.index.mongodb.model.ModelUtils.jsonNodeToDocument;
import static org.kie.kogito.index.mongodb.model.ModelUtils.zonedDateTimeToInstant;

public class UserTaskInstanceEntityMapper implements MongoEntityMapper<String, UserTaskInstance, UserTaskInstanceEntity> {

    @Override
    public Class<UserTaskInstanceEntity> getEntityClass() {
        return UserTaskInstanceEntity.class;
    }

    @Override
    public UserTaskInstanceEntity mapToEntity(String key, UserTaskInstance instance) {
        if (instance == null) {
            return null;
        }

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.id = instance.getId();
        entity.description = instance.getDescription();
        entity.name = instance.getName();
        entity.priority = instance.getPriority();
        entity.processInstanceId = instance.getProcessInstanceId();
        entity.state = instance.getState();
        entity.actualOwner = instance.getActualOwner();
        entity.adminGroups = instance.getAdminGroups();
        entity.adminUsers = instance.getAdminUsers();
        entity.completed = zonedDateTimeToInstant(instance.getCompleted());
        entity.started = zonedDateTimeToInstant(instance.getStarted());
        entity.excludedUsers = instance.getExcludedUsers();
        entity.potentialGroups = instance.getPotentialGroups();
        entity.potentialUsers = instance.getPotentialUsers();
        entity.referenceName = instance.getReferenceName();
        entity.lastUpdate = zonedDateTimeToInstant(instance.getLastUpdate());
        entity.processId = instance.getProcessId();
        entity.rootProcessId = instance.getRootProcessId();
        entity.rootProcessInstanceId = instance.getRootProcessInstanceId();
        entity.inputs = jsonNodeToDocument(instance.getInputs());
        entity.outputs = jsonNodeToDocument(instance.getOutputs());
        return entity;
    }

    @Override
    public UserTaskInstance mapToModel(UserTaskInstanceEntity entity) {
        if (entity == null) {
            return null;
        }

        UserTaskInstance instance = new UserTaskInstance();
        instance.setId(entity.id);
        instance.setDescription(entity.description);
        instance.setName(entity.name);
        instance.setPriority(entity.priority);
        instance.setProcessInstanceId(entity.processInstanceId);
        instance.setState(entity.state);
        instance.setActualOwner(entity.actualOwner);
        instance.setAdminGroups(entity.adminGroups);
        instance.setAdminUsers(entity.adminUsers);
        instance.setCompleted(instantToZonedDateTime(entity.completed));
        instance.setStarted(instantToZonedDateTime(entity.started));
        instance.setExcludedUsers(entity.excludedUsers);
        instance.setPotentialGroups(entity.potentialGroups);
        instance.setPotentialUsers(entity.potentialUsers);
        instance.setReferenceName(entity.referenceName);
        instance.setLastUpdate(instantToZonedDateTime(entity.lastUpdate));
        instance.setProcessId(entity.processId);
        instance.setRootProcessId(entity.rootProcessId);
        instance.setRootProcessInstanceId(entity.rootProcessInstanceId);
        instance.setInputs(documentToJsonNode(entity.inputs, JsonNode.class));
        instance.setOutputs(documentToJsonNode(entity.outputs, JsonNode.class));
        return instance;
    }
}
