package org.kie.kogito.persistence.api.schema;

import java.util.Objects;

public class SchemaRegisteredEvent {

    SchemaDescriptor schemaDescriptor;

    SchemaType schemaType;

    public SchemaRegisteredEvent(SchemaDescriptor schemaDescriptor, SchemaType type) {
        this.schemaDescriptor = schemaDescriptor;
        this.schemaType = type;
    }

    public SchemaDescriptor getSchemaDescriptor() {
        return schemaDescriptor;
    }

    public SchemaType getSchemaType() {
        return schemaType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SchemaRegisteredEvent that = (SchemaRegisteredEvent) o;
        return Objects.equals(schemaDescriptor, that.schemaDescriptor) &&
                Objects.equals(schemaType, that.schemaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemaDescriptor, schemaType);
    }

    @Override
    public String toString() {
        return "SchemaRegisteredEvent{" +
                "schemaDescriptor=" + schemaDescriptor +
                ", schemaType=" + schemaType +
                '}';
    }
}
