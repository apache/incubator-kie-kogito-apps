package org.kie.kogito.persistence.mongodb.model;

import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.mongodb.mock.MockMongoEntityMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.kie.kogito.persistence.mongodb.model.ModelUtils.MONGO_ID;

class MongoEntityMapperTest {

    MockMongoEntityMapper mockMongoEntityMapper = new MockMongoEntityMapper();

    @Test
    void testConvertToMongoAttribute() {
        String testAttribute = "test";
        assertEquals(testAttribute, mockMongoEntityMapper.convertToMongoAttribute(testAttribute));

        String idAttribute = ModelUtils.ID;
        assertEquals(MONGO_ID, mockMongoEntityMapper.convertToMongoAttribute(idAttribute));
    }

    @Test
    void testConvertToModelAttribute() {
        String idAttribute = MONGO_ID;
        assertEquals(ModelUtils.ID, mockMongoEntityMapper.convertToModelAttribute(idAttribute));

        String testAttribute = "nodes";
        assertEquals(testAttribute, mockMongoEntityMapper.convertToModelAttribute(testAttribute));

        String testSubAttribute = "nodes.name.go";
        assertEquals("go", mockMongoEntityMapper.convertToModelAttribute(testSubAttribute));
    }
}