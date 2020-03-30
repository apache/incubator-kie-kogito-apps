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

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import org.kie.kogito.index.model.NodeInstance;

import static org.kie.kogito.index.mongodb.utils.ModelUtils.instantToZonedDateTime;
import static org.kie.kogito.index.mongodb.utils.ModelUtils.zonedDateTimeToInstant;

@MongoEntity(collection = "nodeinstances")
public class NodeInstanceEntity extends PanacheMongoEntity {

    public String id;

    public String name;

    public String nodeId;

    public String type;

    public Long enter;

    public Long exit;

    public String definitionId;

    public static NodeInstance toNodeInstance(NodeInstanceEntity entity) {
        if (entity == null) {
            return null;
        }

        NodeInstance instance = new NodeInstance();
        instance.setId(entity.id);
        instance.setName(entity.name);
        instance.setNodeId(entity.nodeId);
        instance.setType(entity.type);
        instance.setEnter(instantToZonedDateTime(entity.enter));
        instance.setExit(instantToZonedDateTime(entity.exit));
        instance.setDefinitionId(entity.definitionId);
        return instance;
    }

    public static NodeInstanceEntity fromNodeInstance(NodeInstance instance) {
        if (instance == null) {
            return null;
        }

        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.id = instance.getId();
        entity.name = instance.getName();
        entity.nodeId = instance.getNodeId();
        entity.type = instance.getType();
        entity.enter = zonedDateTimeToInstant(instance.getEnter());
        entity.exit = zonedDateTimeToInstant(instance.getExit());
        entity.definitionId = instance.getDefinitionId();
        return entity;
    }
}
