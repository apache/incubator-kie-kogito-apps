package org.kie.kogito.index.postgresql.storage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.postgresql.mapper.ProcessInstanceEntityMapper;
import org.kie.kogito.index.postgresql.model.AbstractEntity;
import org.kie.kogito.index.postgresql.model.ProcessInstanceEntity;
import org.kie.kogito.index.postgresql.model.ProcessInstanceEntityRepository;

@ApplicationScoped
public class ProcessInstanceEntityStorage extends AbstractStorage<ProcessInstanceEntity, ProcessInstance> {

    public ProcessInstanceEntityStorage() {
    }

    @Inject
    public ProcessInstanceEntityStorage(ProcessInstanceEntityRepository repository, ProcessInstanceEntityMapper mapper) {
        super(repository, ProcessInstance.class, ProcessInstanceEntity.class, mapper::mapToModel, mapper::mapToEntity, AbstractEntity::getId);
    }
}
