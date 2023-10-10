package org.kie.kogito.index.mongodb.model;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.persistence.mongodb.model.ModelUtils;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.kie.kogito.persistence.mongodb.model.ModelUtils.MONGO_ID;

class ProcessDefinitionEntityMapperTest {

    ProcessDefinitionEntityMapper mapper = new ProcessDefinitionEntityMapper();

    ProcessDefinition pd = new ProcessDefinition();

    ProcessDefinitionEntity entity = new ProcessDefinitionEntity();

    @BeforeEach
    void setup() {
        String version = "1.0";
        String processId = "testProcessId";
        Set<String> roles = singleton("testRoles");
        String type = "testType";
        Set<String> addons = singleton("testAddons");

        pd.setId(processId);
        pd.setVersion(version);
        pd.setRoles(roles);
        pd.setAddons(addons);
        pd.setType(type);

        entity.setId(processId);
        entity.setVersion(version);
        entity.setRoles(roles);
        entity.setAddons(addons);
        entity.setType(type);
    }

    @Test
    void testGetEntityClass() {
        assertEquals(ProcessDefinitionEntity.class, mapper.getEntityClass());
    }

    @Test
    void testMapToEntity() {
        ProcessDefinitionEntity result = mapper.mapToEntity(pd.getId(), pd);
        assertEquals(entity, result);
    }

    @Test
    void testMapToModel() {
        ProcessDefinition result = mapper.mapToModel(entity);
        assertEquals(pd, result);
    }

    @Test
    void testConvertToMongoAttribute() {
        assertEquals(MONGO_ID, mapper.convertToMongoAttribute(MONGO_ID));
        String testAttribute = "testAttribute";
        assertEquals(testAttribute, mapper.convertToMongoAttribute(testAttribute));
    }

    @Test
    void testConvertToModelAttribute() {
        assertEquals(ModelUtils.ID, mapper.convertToModelAttribute(ModelUtils.ID));
        String testAttribute = "test.attribute.go";
        assertEquals(testAttribute, mapper.convertToModelAttribute(testAttribute));
    }
}
