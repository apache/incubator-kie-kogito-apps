package org.kie.kogito.index.oracle.storage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.oracle.mapper.JobEntityMapper;
import org.kie.kogito.index.oracle.model.AbstractEntity;
import org.kie.kogito.index.oracle.model.JobEntity;
import org.kie.kogito.index.oracle.model.JobEntityRepository;

@ApplicationScoped
public class JobEntityStorage extends AbstractStorage<JobEntity, Job> {

    public JobEntityStorage() {
    }

    @Inject
    public JobEntityStorage(JobEntityRepository repository, JobEntityMapper mapper) {
        super(repository, Job.class, JobEntity.class, mapper::mapToModel, mapper::mapToEntity, AbstractEntity::getId);
    }
}
