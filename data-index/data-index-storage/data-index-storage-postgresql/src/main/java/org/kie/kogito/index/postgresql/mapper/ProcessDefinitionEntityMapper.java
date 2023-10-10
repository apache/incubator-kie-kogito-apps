package org.kie.kogito.index.postgresql.mapper;

import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.postgresql.model.ProcessDefinitionEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface ProcessDefinitionEntityMapper {

    ProcessDefinitionEntity mapToEntity(ProcessDefinition pd);

    @InheritInverseConfiguration
    ProcessDefinition mapToModel(ProcessDefinitionEntity pd);

    default byte[] map(String value) {
        return value == null ? null : value.getBytes();
    }

    default String map(byte[] value) {
        return value == null ? null : new String(value);
    }

    @AfterMapping
    default void afterMapping(@MappingTarget ProcessDefinitionEntity entity) {
        if (entity.getNodes() != null) {
            entity.getNodes().forEach(n -> n.setProcessDefinition(entity));
        }
    }

}
