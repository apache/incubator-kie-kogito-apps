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

import io.quarkus.mongodb.panache.runtime.MongoOperations;
import org.kie.kogito.persistence.mongodb.model.MongoEntityMapper;

public class ProcessIdEntityMapper implements MongoEntityMapper<String, ProcessIdEntity> {

    static final String PROCESS_ID_ATTRIBUTE = "processId";

    @Override
    public Class<ProcessIdEntity> getEntityClass() {
        return ProcessIdEntity.class;
    }

    @Override
    public ProcessIdEntity mapToEntity(String key, String value) {
        ProcessIdEntity processIdEntity = new ProcessIdEntity();
        processIdEntity.processId = key;
        processIdEntity.fullTypeName = value;
        return processIdEntity;
    }

    @Override
    public String mapToModel(ProcessIdEntity entity) {
        return entity.fullTypeName;
    }

    @Override
    public String convertToMongoAttribute(String attribute) {
        return PROCESS_ID_ATTRIBUTE.equals(attribute) ? MongoOperations.ID : MongoEntityMapper.super.convertToMongoAttribute(attribute);
    }

    @Override
    public String convertToModelAttribute(String attribute) {
        return MongoOperations.ID.equals(attribute) ? PROCESS_ID_ATTRIBUTE : MongoEntityMapper.super.convertToModelAttribute(attribute);
    }
}
