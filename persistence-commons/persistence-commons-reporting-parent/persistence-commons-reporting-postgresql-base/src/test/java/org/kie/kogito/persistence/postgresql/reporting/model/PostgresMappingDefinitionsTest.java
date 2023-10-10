package org.kie.kogito.persistence.postgresql.reporting.model;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PostgresMappingDefinitionsTest {

    static final PostgresMappingDefinitions DEFINITIONS = new PostgresMappingDefinitions(List.of(PostgresMappingDefinitionTest.DEFINITION));
    static final int DEFINITIONS_HASHCODE = DEFINITIONS.hashCode();

    @Test
    void testEquality() {
        assertEquals(DEFINITIONS,
                DEFINITIONS);
        assertNotEquals(DEFINITIONS,
                new PostgresMappingDefinitions(List.of(new PostgresMappingDefinition("different",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)))));
        assertNotEquals(DEFINITIONS,
                new PostgresMappingDefinitions(List.of(new PostgresMappingDefinition("mappingId",
                        "different",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)))));
        assertNotEquals(DEFINITIONS,
                new PostgresMappingDefinitions(List.of(new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "different",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)))));
        assertNotEquals(DEFINITIONS,
                new PostgresMappingDefinitions(List.of(new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD, PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)))));
        assertNotEquals(DEFINITIONS,
                new PostgresMappingDefinitions(List.of(new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD, PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)))));
        assertNotEquals(DEFINITIONS,
                new PostgresMappingDefinitions(List.of(new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "different",
                        List.of(PostgresMappingTest.MAPPING)))));
        assertNotEquals(DEFINITIONS,
                new PostgresMappingDefinitions(List.of(new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING, PostgresMappingTest.MAPPING)))));
        assertNotEquals(DEFINITIONS,
                new PostgresMappingDefinitions(List.of(PostgresMappingDefinitionTest.DEFINITION,
                        PostgresMappingDefinitionTest.DEFINITION)));
    }

    @Test
    void testHashCode() {
        assertEquals(DEFINITIONS_HASHCODE,
                DEFINITIONS.hashCode());
        assertNotEquals(DEFINITIONS_HASHCODE,
                new PostgresMappingDefinitions(List.of(new PostgresMappingDefinition("different",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)))).hashCode());
        assertNotEquals(DEFINITIONS_HASHCODE,
                new PostgresMappingDefinitions(List.of(new PostgresMappingDefinition("mappingId",
                        "different",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)))).hashCode());
        assertNotEquals(DEFINITIONS_HASHCODE,
                new PostgresMappingDefinitions(List.of(new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "different",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)))).hashCode());
        assertNotEquals(DEFINITIONS_HASHCODE,
                new PostgresMappingDefinitions(List.of(new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD, PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)))).hashCode());
        assertNotEquals(DEFINITIONS_HASHCODE,
                new PostgresMappingDefinitions(List.of(new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD, PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)))).hashCode());
        assertNotEquals(DEFINITIONS_HASHCODE,
                new PostgresMappingDefinitions(List.of(new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "different",
                        List.of(PostgresMappingTest.MAPPING)))).hashCode());
        assertNotEquals(DEFINITIONS_HASHCODE,
                new PostgresMappingDefinitions(List.of(new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING, PostgresMappingTest.MAPPING)))).hashCode());
        assertNotEquals(DEFINITIONS_HASHCODE,
                new PostgresMappingDefinitions(List.of(PostgresMappingDefinitionTest.DEFINITION,
                        PostgresMappingDefinitionTest.DEFINITION)).hashCode());
    }
}
