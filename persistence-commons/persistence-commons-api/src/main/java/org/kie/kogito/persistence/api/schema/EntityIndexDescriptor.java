package org.kie.kogito.persistence.api.schema;

import java.util.List;
import java.util.Objects;

public class EntityIndexDescriptor {

    String name;

    List<IndexDescriptor> indexDescriptors;

    List<AttributeDescriptor> attributeDescriptors;

    public EntityIndexDescriptor(String name, List<IndexDescriptor> indexDescriptors, List<AttributeDescriptor> attributeDescriptors) {
        this.name = name;
        this.indexDescriptors = indexDescriptors;
        this.attributeDescriptors = attributeDescriptors;
    }

    public String getName() {
        return name;
    }

    public List<IndexDescriptor> getIndexDescriptors() {
        return indexDescriptors;
    }

    public List<AttributeDescriptor> getAttributeDescriptors() {
        return attributeDescriptors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EntityIndexDescriptor that = (EntityIndexDescriptor) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(indexDescriptors, that.indexDescriptors) &&
                Objects.equals(attributeDescriptors, that.attributeDescriptors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, indexDescriptors, attributeDescriptors);
    }

    @Override
    public String toString() {
        return "EntityIndexDescriptor{" +
                "name='" + name + '\'' +
                ", indexDescriptors=" + indexDescriptors +
                ", attributeDescriptors=" + attributeDescriptors +
                '}';
    }
}
