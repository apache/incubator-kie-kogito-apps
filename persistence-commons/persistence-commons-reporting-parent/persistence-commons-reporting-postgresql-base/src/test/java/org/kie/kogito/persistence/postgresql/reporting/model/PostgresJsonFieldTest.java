package org.kie.kogito.persistence.postgresql.reporting.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PostgresJsonFieldTest {

    static final PostgresJsonField FIELD = new PostgresJsonField("field1", JsonType.STRING);
    static final int FIELD_HASHCODE = FIELD.hashCode();

    @Test
    void testEquality() {
        assertEquals(FIELD,
                FIELD);
        assertNotEquals(FIELD,
                new PostgresJsonField("field1", JsonType.NUMBER));
        assertNotEquals(FIELD,
                new PostgresJsonField("field2", JsonType.STRING));
    }

    @Test
    void testHashCode() {
        assertEquals(FIELD_HASHCODE,
                FIELD.hashCode());
        assertNotEquals(FIELD_HASHCODE,
                new PostgresJsonField("field1", JsonType.NUMBER).hashCode());
        assertNotEquals(FIELD_HASHCODE,
                new PostgresJsonField("field2", JsonType.STRING).hashCode());
    }
}
