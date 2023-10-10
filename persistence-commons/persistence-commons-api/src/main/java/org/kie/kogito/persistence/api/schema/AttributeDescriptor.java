package org.kie.kogito.persistence.api.schema;

import java.util.Objects;

public class AttributeDescriptor {

    String name;

    String typeName;

    boolean primitiveType;

    public AttributeDescriptor(String name, String typeName, boolean primitiveType) {
        this.name = name;
        this.typeName = typeName;
        this.primitiveType = primitiveType;
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return typeName;
    }

    public boolean isPrimitiveType() {
        return primitiveType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttributeDescriptor that = (AttributeDescriptor) o;
        return primitiveType == that.primitiveType &&
                Objects.equals(name, that.name) &&
                Objects.equals(typeName, that.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, typeName, primitiveType);
    }
}
