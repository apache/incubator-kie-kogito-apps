package org.kie.kogito.persistence.api.proto;

import java.util.Objects;

public class AttributeDescriptor {

    private String name;
    private String typeName;
    private String label;

    public AttributeDescriptor() {
    }

    public AttributeDescriptor(String name, String typeName) {
        this(name, typeName, null);
    }

    public AttributeDescriptor(String name, String typeName, String label) {
        this.name = name;
        this.typeName = typeName;
        this.label = label;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "AttributeDescriptor{" +
                "name='" + name + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
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
        return Objects.equals(name, that.name) &&
                Objects.equals(typeName, that.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, typeName);
    }
}
