package org.kie.kogito.persistence.api.proto;

import java.util.List;
import java.util.Objects;

public class DomainDescriptor {

    private String typeName;
    private List<AttributeDescriptor> attributes;

    public List<AttributeDescriptor> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeDescriptor> attributes) {
        this.attributes = attributes;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "DomainDescriptor{" +
                "typeName='" + typeName + '\'' +
                ", attributes=" + attributes +
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
        DomainDescriptor that = (DomainDescriptor) o;
        return Objects.equals(typeName, that.typeName) &&
                Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, attributes);
    }
}
