package org.kie.kogito.index.postgresql.mapper;

import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.postgresql.model.JobEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface JobEntityMapper {

    JobEntity mapToEntity(Job job);

    @InheritInverseConfiguration
    Job mapToModel(JobEntity job);
}
