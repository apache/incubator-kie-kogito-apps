package org.kie.kogito.index.oracle.mapper;

import org.kie.kogito.index.model.Milestone;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.oracle.model.MilestoneEntity;
import org.kie.kogito.index.oracle.model.ProcessInstanceEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface ProcessInstanceEntityMapper {

    MilestoneEntity mapMilestoneToEntity(Milestone mi);

    @InheritInverseConfiguration
    Milestone mapMilestoneToModel(MilestoneEntity pi);

    ProcessInstanceEntity mapToEntity(ProcessInstance pi);

    @InheritInverseConfiguration
    ProcessInstance mapToModel(ProcessInstanceEntity pi);

    @AfterMapping
    default void afterMapping(@MappingTarget ProcessInstanceEntity entity) {
        entity.getNodes().forEach(n -> n.setProcessInstance(entity));
        entity.getMilestones().forEach(m -> m.setProcessInstance(entity));
    }
}
