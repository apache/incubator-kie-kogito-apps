package org.kie.kogito.index.postgresql.storage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.postgresql.mapper.UserTaskInstanceEntityMapper;
import org.kie.kogito.index.postgresql.model.AbstractEntity;
import org.kie.kogito.index.postgresql.model.UserTaskInstanceEntity;
import org.kie.kogito.index.postgresql.model.UserTaskInstanceEntityRepository;

@ApplicationScoped
public class UserTaskInstanceEntityStorage extends AbstractStorage<UserTaskInstanceEntity, UserTaskInstance> {

    public UserTaskInstanceEntityStorage() {
    }

    @Inject
    public UserTaskInstanceEntityStorage(UserTaskInstanceEntityRepository repository, UserTaskInstanceEntityMapper mapper) {
        super(repository, UserTaskInstance.class, UserTaskInstanceEntity.class, mapper::mapToModel, mapper::mapToEntity, AbstractEntity::getId);
    }
}
