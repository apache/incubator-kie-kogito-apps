package org.kie.kogito.index.postgresql.mapper;

import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.postgresql.model.UserTaskInstanceEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface UserTaskInstanceEntityMapper {

    UserTaskInstanceEntity mapToEntity(UserTaskInstance ut);

    @InheritInverseConfiguration
    UserTaskInstance mapToModel(UserTaskInstanceEntity ut);

    @AfterMapping
    default void afterMapping(@MappingTarget UserTaskInstanceEntity entity) {
        entity.getAttachments().forEach(a -> a.setUserTask(entity));
        entity.getComments().forEach(c -> c.setUserTask(entity));
    }
}
