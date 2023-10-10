package org.kie.kogito.persistence.api.schema;

import java.util.Objects;

public class SchemaType {

    String type;

    public SchemaType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SchemaType that = (SchemaType) o;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return "SchemaType{" +
                "type='" + type + '\'' +
                '}';
    }
}
