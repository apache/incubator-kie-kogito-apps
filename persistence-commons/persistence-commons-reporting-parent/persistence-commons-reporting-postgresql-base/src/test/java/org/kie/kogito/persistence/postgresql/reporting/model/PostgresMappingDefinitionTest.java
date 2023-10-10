package org.kie.kogito.persistence.postgresql.reporting.model;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PostgresMappingDefinitionTest {

    static final PostgresMappingDefinition DEFINITION = new PostgresMappingDefinition("mappingId",
            "sourceTableName",
            "sourceTableJsonFieldName",
            List.of(PostgresFieldTest.FIELD),
            List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
            "targetTableName",
            List.of(PostgresMappingTest.MAPPING));
    static final int DEFINITION_HASHCODE = DEFINITION.hashCode();

    @Test
    void testEquality() {
        assertEquals(DEFINITION,
                DEFINITION);
        assertNotEquals(DEFINITION,
                new PostgresMappingDefinition("different",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)));
        assertNotEquals(DEFINITION,
                new PostgresMappingDefinition("mappingId",
                        "different",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)));
        assertNotEquals(DEFINITION,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "different",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)));
        assertNotEquals(DEFINITION,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD, PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)));
        assertNotEquals(DEFINITION,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD, PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)));
        assertNotEquals(DEFINITION,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "different",
                        List.of(PostgresMappingTest.MAPPING)));
        assertNotEquals(DEFINITION,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING, PostgresMappingTest.MAPPING)));
    }

    @Test
    void testHashCode() {
        assertEquals(DEFINITION_HASHCODE,
                DEFINITION.hashCode());
        assertNotEquals(DEFINITION_HASHCODE,
                new PostgresMappingDefinition("different",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)).hashCode());
        assertNotEquals(DEFINITION_HASHCODE,
                new PostgresMappingDefinition("mappingId",
                        "different",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)).hashCode());
        assertNotEquals(DEFINITION_HASHCODE,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "different",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)).hashCode());
        assertNotEquals(DEFINITION_HASHCODE,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD, PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)).hashCode());
        assertNotEquals(DEFINITION_HASHCODE,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD, PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING)).hashCode());
        assertNotEquals(DEFINITION_HASHCODE,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "different",
                        List.of(PostgresMappingTest.MAPPING)).hashCode());
        assertNotEquals(DEFINITION_HASHCODE,
                new PostgresMappingDefinition("mappingId",
                        "sourceTableName",
                        "sourceTableJsonFieldName",
                        List.of(PostgresFieldTest.FIELD),
                        List.of(PostgresPartitionFieldTest.PARTITION_FIELD),
                        "targetTableName",
                        List.of(PostgresMappingTest.MAPPING, PostgresMappingTest.MAPPING)).hashCode());
    }
}
