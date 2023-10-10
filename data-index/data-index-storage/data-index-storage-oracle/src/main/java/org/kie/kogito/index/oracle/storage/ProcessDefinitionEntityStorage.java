package org.kie.kogito.index.oracle.storage;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.oracle.mapper.ProcessDefinitionEntityMapper;
import org.kie.kogito.index.oracle.model.ProcessDefinitionEntity;
import org.kie.kogito.index.oracle.model.ProcessDefinitionEntityId;
import org.kie.kogito.index.oracle.model.ProcessDefinitionEntityRepository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class ProcessDefinitionEntityStorage extends AbstractStorage<ProcessDefinitionEntity, ProcessDefinition> {

    public ProcessDefinitionEntityStorage() {
    }

    @Inject
    public ProcessDefinitionEntityStorage(ProcessDefinitionEntityRepository repository, ProcessDefinitionEntityMapper mapper) {
        super(new RepositoryAdapter(repository), ProcessDefinition.class, ProcessDefinitionEntity.class, mapper::mapToModel, mapper::mapToEntity, e -> new ProcessDefinitionEntityId(e.getId(),
                e.getVersion()).getKey());
    }

    @Override
    public boolean containsKey(String key) {
        ProcessDefinitionEntityId id = new ProcessDefinitionEntityId(key);
        return getRepository().count("id = ?1 and version = ?2", id.getId(), id.getVersion()) == 1;
    }

    static class RepositoryAdapter implements PanacheRepositoryBase<ProcessDefinitionEntity, String> {

        ProcessDefinitionEntityRepository repository;

        public RepositoryAdapter(ProcessDefinitionEntityRepository repository) {
            this.repository = repository;
        }

        @Override
        public boolean deleteById(String key) {
            return repository.deleteById(new ProcessDefinitionEntityId(key));
        }

        @Override
        public Optional<ProcessDefinitionEntity> findByIdOptional(String key) {
            return repository.findByIdOptional(new ProcessDefinitionEntityId(key));
        }

        @Override
        public void persist(ProcessDefinitionEntity entity) {
            repository.persist(entity);
        }

        @Override
        public EntityManager getEntityManager() {
            return repository.getEntityManager();
        }
    }
}
