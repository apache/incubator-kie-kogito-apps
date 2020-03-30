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

import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.BasicDBObject;
import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import org.bson.codecs.pojo.annotations.BsonId;
import org.kie.kogito.index.model.UserTaskInstance;

import static org.kie.kogito.index.mongodb.utils.ModelUtils.dbObjectToJsonNode;
import static org.kie.kogito.index.mongodb.utils.ModelUtils.instantToZonedDateTime;
import static org.kie.kogito.index.mongodb.utils.ModelUtils.jsonNodeToDBObject;
import static org.kie.kogito.index.mongodb.utils.ModelUtils.zonedDateTimeToInstant;

@MongoEntity(collection = "usertaskinstances")
public class UserTaskInstanceEntity extends PanacheMongoEntityBase {

    @BsonId
    public String id;

    public String description;

    public String name;

    public String priority;

    public String processInstanceId;

    public String state;

    public String actualOwner;

    public Set<String> adminGroups;

    public Set<String> adminUsers;

    public Long completed;

    public Long started;

    public Set<String> excludedUsers;

    public Set<String> potentialGroups;

    public Set<String> potentialUsers;

    public String referenceName;

    public Long lastUpdate;

    public String processId;

    public String rootProcessId;

    public String rootProcessInstanceId;

    public BasicDBObject inputs;

    public BasicDBObject outputs;

    public static UserTaskInstance toUserTaskInstance(UserTaskInstanceEntity entity) {
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
        instance.setInputs(dbObjectToJsonNode(entity.inputs, JsonNode.class));
        instance.setOutputs(dbObjectToJsonNode(entity.outputs, JsonNode.class));
        return instance;
    }

    public static UserTaskInstanceEntity fromUserTaskInstance(UserTaskInstance instance) {
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
        entity.inputs = jsonNodeToDBObject(instance.getInputs());
        entity.outputs = jsonNodeToDBObject(instance.getOutputs());
        return entity;
    }
}
