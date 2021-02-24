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

package org.kie.kogito.persistence.mongodb.model;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.mongodb.mock.MockMongoEntityMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.kie.kogito.persistence.mongodb.model.ModelUtils.MAPPER;
import static org.kie.kogito.persistence.mongodb.model.ModelUtils.MONGO_ID;

class ModelUtilsTest {

    @Test
    void testInstantToZonedDateTime() {
        ZonedDateTime time = ZonedDateTime.now();
        assertEquals(time.toInstant().toEpochMilli(), ModelUtils.instantToZonedDateTime(time.toInstant().toEpochMilli()).toInstant().toEpochMilli());
    }

    @Test
    void testZonedDateTimeToInstant() {
        ZonedDateTime time = ZonedDateTime.now();
        assertEquals(time.toInstant().toEpochMilli(), ModelUtils.zonedDateTimeToInstant(time));
    }

    @Test
    void testDocumentToJsonNode() {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("testKey1", "testValue1");
        objectMap.put("testKey2", "testValue2");
        ObjectNode object = MAPPER.valueToTree(objectMap);

        Document document = new Document().append("testKey1", "testValue1")
                .append("testKey2", "testValue2");

        assertEquals(object, ModelUtils.documentToJsonNode(document, ObjectNode.class));
    }

    @Test
    void testJsonNodeToDocument() {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("testKey1", "testValue1");
        objectMap.put("testKey2", "testValue2");
        ObjectNode object = MAPPER.valueToTree(objectMap);

        Document document = new Document().append("testKey1", "testValue1")
                .append("testKey2", "testValue2");

        assertEquals(document, ModelUtils.jsonNodeToDocument(object));
    }

    @Test
    void testDocumentToObject() {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put(ModelUtils.ID, "testId");
        objectMap.put("testKey1", "testValue1");
        objectMap.put("testKey2", "testValue2");

        Document document = new Document()
                .append(MONGO_ID, "testId")
                .append("testKey1", "testValue1")
                .append("testKey2", "testValue2");

        assertEquals(objectMap, ModelUtils.documentToObject(document, HashMap.class, new MockMongoEntityMapper()::convertToModelAttribute));
    }

    @Test
    void testConvertAttributes() {
        Map<String, String> subMap = new HashMap<>();
        subMap.put("subTestKey1", "subTestValue1");
        subMap.put("subTestKey2", "subTestValue2");

        Map objectMap = new HashMap();
        objectMap.put(MONGO_ID, "testId");
        objectMap.put("testKey1", "testValue1");
        objectMap.put("testKey2", "testValue2");
        objectMap.put("subMapKey", subMap);
        ObjectNode object = MAPPER.valueToTree(objectMap);

        Map expectedMap = new HashMap();
        expectedMap.put(ModelUtils.ID, "testId");
        expectedMap.put("testKey1", "testValue1");
        expectedMap.put("testKey2", "testValue2");
        expectedMap.put("subMapKey", subMap);
        ObjectNode expected = MAPPER.valueToTree(expectedMap);

        assertEquals(expected, ModelUtils.convertAttributes(object, Optional.empty(), new MockMongoEntityMapper()::convertToModelAttribute));
    }
}