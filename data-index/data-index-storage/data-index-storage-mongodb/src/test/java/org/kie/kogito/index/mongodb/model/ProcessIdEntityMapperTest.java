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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.kie.kogito.index.mongodb.model.ProcessIdEntityMapper.PROCESS_ID_ATTRIBUTE;

class ProcessIdEntityMapperTest {

    ProcessIdEntityMapper processIdEntityMapper = new ProcessIdEntityMapper();

    @Test
    void testGetEntityClass() {
        assertEquals(ProcessIdEntity.class, processIdEntityMapper.getEntityClass());
    }

    @Test
    void testMapToEntity() {
        String testId = "testProcessId";
        String testValue = "testProcessType";
        ProcessIdEntity result = processIdEntityMapper.mapToEntity(testId, testValue);

        ProcessIdEntity processIdEntity = new ProcessIdEntity();
        processIdEntity.processId = testId;
        processIdEntity.fullTypeName = testValue;

        assertEquals(processIdEntity, result);
    }

    @Test
    void testMapToModel() {
        String testId = "testProcessId";
        String testValue = "testProcessType";

        ProcessIdEntity processIdEntity = new ProcessIdEntity();
        processIdEntity.processId = testId;
        processIdEntity.fullTypeName = testValue;

        String result = processIdEntityMapper.mapToModel(processIdEntity);

        assertEquals(testValue, result);
    }

    @Test
    void testConvertToMongoAttribute() {
        assertEquals(MongoOperations.ID, processIdEntityMapper.convertToMongoAttribute(PROCESS_ID_ATTRIBUTE));

        String testAttribute = "testAttribute";
        assertEquals(testAttribute, processIdEntityMapper.convertToMongoAttribute(testAttribute));
    }

    @Test
    void testConvertToModelAttribute() {
        assertEquals(PROCESS_ID_ATTRIBUTE, processIdEntityMapper.convertToModelAttribute(MongoOperations.ID));

        String testAttribute = "test.attribute.name";
        assertEquals("name", processIdEntityMapper.convertToModelAttribute(testAttribute));
    }
}