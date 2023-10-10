package org.kie.kogito.index.mongodb.model;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.kie.kogito.index.mongodb.model.DomainEntityMapper.ID;
import static org.kie.kogito.persistence.mongodb.model.ModelUtils.MAPPER;
import static org.kie.kogito.persistence.mongodb.model.ModelUtils.MONGO_ID;

class DomainEntityMapperTest {

    DomainEntityMapper domainEntityMapper = new DomainEntityMapper();

    @Test
    void testGetEntityClass() {
        assertEquals(Document.class, domainEntityMapper.getEntityClass());
    }

    @Test
    void testMapToEntity() {
        String testId = "testId";

        Map<String, String> objectMap = new HashMap<>();
        objectMap.put(ID, testId);
        objectMap.put("testKey", "testValue");
        ObjectNode object = MAPPER.valueToTree(objectMap);

        Document document = new Document(MONGO_ID, testId).append("testKey", "testValue");

        Document result = domainEntityMapper.mapToEntity(testId, object);
        assertEquals(document, result);
    }

    @Test
    void testMapToModel() {
        String testId = "testId";

        Map<String, String> objectMap = new HashMap<>();
        objectMap.put(ID, testId);
        objectMap.put("testKey", "testValue");
        ObjectNode object = MAPPER.valueToTree(objectMap);

        Document document = new Document(MONGO_ID, testId).append("testKey", "testValue");

        ObjectNode result = domainEntityMapper.mapToModel(document);
        assertEquals(object, result);
    }
}
