package org.kie.kogito.persistence.postgresql.reporting.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostgresFieldTest {

    static final PostgresField FIELD = new PostgresField("field1");
    static final int FIELD_HASHCODE = FIELD.hashCode();

    @Test
    void testEquality() {
        assertEquals(FIELD,
                FIELD);
    }

    @Test
    void testHashCode() {
        assertEquals(FIELD_HASHCODE,
                FIELD.hashCode());
    }
}
