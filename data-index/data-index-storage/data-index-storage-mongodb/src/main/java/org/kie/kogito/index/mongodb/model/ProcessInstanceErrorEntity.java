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
import org.kie.kogito.index.model.ProcessInstanceError;

@MongoEntity(collection = "processinstanceerrors")
public class ProcessInstanceErrorEntity extends PanacheMongoEntity {

    public String nodeDefinitionId;

    public String message;

    public static ProcessInstanceError toProcessInstanceError(ProcessInstanceErrorEntity entity) {
        if (entity == null) {
            return null;
        }

        ProcessInstanceError error = new ProcessInstanceError();
        error.setNodeDefinitionId(entity.nodeDefinitionId);
        error.setMessage(entity.message);
        return error;
    }

    public static ProcessInstanceErrorEntity fromProcessInstanceError(ProcessInstanceError error) {
        if (error == null) {
            return null;
        }

        ProcessInstanceErrorEntity entity = new ProcessInstanceErrorEntity();
        entity.nodeDefinitionId = error.getNodeDefinitionId();
        entity.message = error.getMessage();
        return entity;
    }
}
