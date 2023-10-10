package org.kie.kogito.persistence.postgresql.reporting.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PostgresPartitionFieldTest {

    static final PostgresPartitionField PARTITION_FIELD = new PostgresPartitionField("field1", "value1");
    static final int PARTITION_FIELD_HASHCODE = PARTITION_FIELD.hashCode();

    @Test
    void testEquality() {
        assertEquals(PARTITION_FIELD,
                PARTITION_FIELD);
        assertNotEquals(PARTITION_FIELD,
                new PostgresPartitionField("different", "value1"));
        assertNotEquals(PARTITION_FIELD,
                new PostgresPartitionField("field1", "different"));
    }

    @Test
    void testHashCode() {
        assertEquals(PARTITION_FIELD_HASHCODE,
                PARTITION_FIELD.hashCode());
        assertNotEquals(PARTITION_FIELD_HASHCODE,
                new PostgresPartitionField("different", "value1").hashCode());
        assertNotEquals(PARTITION_FIELD_HASHCODE,
                new PostgresPartitionField("field1", "different").hashCode());
    }
}
