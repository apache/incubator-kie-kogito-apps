package org.kie.kogito.persistence.postgresql.reporting.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PostgresMappingTest {

    static final PostgresMapping MAPPING = new PostgresMapping("sourceJsonPath", PostgresJsonFieldTest.FIELD);
    static final int MAPPING_HASHCODE = MAPPING.hashCode();

    @Test
    void testEquality() {
        assertEquals(MAPPING,
                MAPPING);
        assertNotEquals(MAPPING,
                new PostgresMapping("different",
                        new PostgresJsonField("field1", JsonType.STRING)));
        assertNotEquals(MAPPING,
                new PostgresMapping("sourceJsonPath",
                        new PostgresJsonField("field1", JsonType.NUMBER)));
        assertNotEquals(MAPPING,
                new PostgresMapping("sourceJsonPath",
                        new PostgresJsonField("field2", JsonType.STRING)));
    }

    @Test
    void testHashCode() {
        assertEquals(MAPPING_HASHCODE,
                MAPPING.hashCode());
        assertNotEquals(MAPPING_HASHCODE,
                new PostgresMapping("different",
                        new PostgresJsonField("field1", JsonType.STRING)).hashCode());
        assertNotEquals(MAPPING_HASHCODE,
                new PostgresMapping("sourceJsonPath",
                        new PostgresJsonField("field1", JsonType.NUMBER)).hashCode());
        assertNotEquals(MAPPING_HASHCODE,
                new PostgresMapping("sourceJsonPath",
                        new PostgresJsonField("field2", JsonType.STRING)).hashCode());
    }
}
