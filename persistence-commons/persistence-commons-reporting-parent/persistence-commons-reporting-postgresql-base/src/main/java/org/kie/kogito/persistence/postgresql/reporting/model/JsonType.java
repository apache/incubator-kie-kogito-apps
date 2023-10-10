package org.kie.kogito.persistence.postgresql.reporting.model;

// See https://www.postgresql.org/docs/13/datatype-json.html
public enum JsonType {

    STRING("text"),
    NUMBER("numeric"),
    BOOLEAN("boolean"),
    OBJECT("jsonb");

    private final String postgresType;

    JsonType(final String postgresType) {
        this.postgresType = postgresType;
    }

    public String getPostgresType() {
        return postgresType;
    }
}
