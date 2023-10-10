package org.kie.kogito.index.mongodb.storage;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.mongodb.model.DomainEntityMapper;
import org.kie.kogito.index.mongodb.model.JobEntityMapper;
import org.kie.kogito.index.mongodb.model.ProcessDefinitionEntityMapper;
import org.kie.kogito.index.mongodb.model.ProcessIdEntityMapper;
import org.kie.kogito.index.mongodb.model.ProcessInstanceEntityMapper;
import org.kie.kogito.index.mongodb.model.UserTaskInstanceEntityMapper;
import org.kie.kogito.persistence.mongodb.index.IndexCreateOrUpdateEvent;
import org.kie.kogito.persistence.mongodb.model.MongoEntityMapper;
import org.kie.kogito.persistence.mongodb.storage.MongoModelService;

import static org.kie.kogito.index.mongodb.Constants.isDomainCollection;
import static org.kie.kogito.index.storage.Constants.JOBS_STORAGE;
import static org.kie.kogito.index.storage.Constants.PROCESS_DEFINITIONS_STORAGE;
import static org.kie.kogito.index.storage.Constants.PROCESS_ID_MODEL_STORAGE;
import static org.kie.kogito.index.storage.Constants.PROCESS_INSTANCES_STORAGE;
import static org.kie.kogito.index.storage.Constants.USER_TASK_INSTANCES_STORAGE;

@ApplicationScoped
public class MongoModelServiceImpl implements MongoModelService {

    @Inject
    Event<IndexCreateOrUpdateEvent> indexCreateOrUpdateEvent;

    static final Map<String, Supplier<MongoEntityMapper<?, ?>>> ENTITY_MAPPER_MAP = Map.of(
            JOBS_STORAGE, JobEntityMapper::new,
            PROCESS_DEFINITIONS_STORAGE, ProcessDefinitionEntityMapper::new,
            PROCESS_INSTANCES_STORAGE, ProcessInstanceEntityMapper::new,
            USER_TASK_INSTANCES_STORAGE, UserTaskInstanceEntityMapper::new,
            PROCESS_ID_MODEL_STORAGE, ProcessIdEntityMapper::new);

    @PostConstruct
    void init() {
        indexCreateOrUpdateEvent.fire(new IndexCreateOrUpdateEvent(PROCESS_DEFINITIONS_STORAGE, ProcessDefinition.class.getName()));
        indexCreateOrUpdateEvent.fire(new IndexCreateOrUpdateEvent(PROCESS_INSTANCES_STORAGE, ProcessInstance.class.getName()));
        indexCreateOrUpdateEvent.fire(new IndexCreateOrUpdateEvent(USER_TASK_INSTANCES_STORAGE, UserTaskInstance.class.getName()));
        indexCreateOrUpdateEvent.fire(new IndexCreateOrUpdateEvent(JOBS_STORAGE, Job.class.getName()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V, E> MongoEntityMapper<V, E> getEntityMapper(String name) {
        Supplier<MongoEntityMapper<?, ?>> supplier = ENTITY_MAPPER_MAP.get(name);
        if (Objects.nonNull(supplier)) {
            return (MongoEntityMapper<V, E>) supplier.get();
        } else if (isDomainCollection(name)) {
            return (MongoEntityMapper<V, E>) new DomainEntityMapper();
        }
        return null;
    }
}
