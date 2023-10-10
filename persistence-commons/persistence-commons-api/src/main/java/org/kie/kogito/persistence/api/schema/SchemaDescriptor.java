package org.kie.kogito.persistence.api.schema;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class SchemaDescriptor {

    String name;

    String schemaContent;

    Map<String, EntityIndexDescriptor> entityIndexDescriptors;

    ProcessDescriptor processDescriptor;

    public SchemaDescriptor(String name, String schemaContent, Map<String, EntityIndexDescriptor> entityIndexDescriptors, ProcessDescriptor processDescriptor) {
        this.name = name;
        this.schemaContent = schemaContent;
        this.entityIndexDescriptors = entityIndexDescriptors;
        this.processDescriptor = processDescriptor;
    }

    public String getName() {
        return name;
    }

    public String getSchemaContent() {
        return schemaContent;
    }

    public Map<String, EntityIndexDescriptor> getEntityIndexDescriptors() {
        return entityIndexDescriptors;
    }

    public Optional<ProcessDescriptor> getProcessDescriptor() {
        return Optional.ofNullable(processDescriptor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SchemaDescriptor that = (SchemaDescriptor) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(schemaContent, that.schemaContent) &&
                Objects.equals(entityIndexDescriptors, that.entityIndexDescriptors) &&
                Objects.equals(processDescriptor, that.processDescriptor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, schemaContent, entityIndexDescriptors, processDescriptor);
    }

    @Override
    public String toString() {
        return "SchemaDescriptor{" +
                "name='" + name + '\'' +
                ", schemaContent='" + schemaContent + '\'' +
                ", entityIndexDescriptors=" + entityIndexDescriptors +
                ", processDescriptor=" + processDescriptor +
                '}';
    }
}
