package org.kie.kogito.index.postgresql.storage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.StorageService;

import io.quarkus.arc.AlternativePriority;
import io.quarkus.arc.properties.IfBuildProperty;

import static java.lang.String.format;
import static org.kie.kogito.persistence.api.factory.Constants.PERSISTENCE_TYPE_PROPERTY;
import static org.kie.kogito.persistence.postgresql.Constants.POSTGRESQL_STORAGE;

@AlternativePriority(1)
@ApplicationScoped
@IfBuildProperty(name = PERSISTENCE_TYPE_PROPERTY, stringValue = POSTGRESQL_STORAGE)
public class PostgreSqlStorageService implements StorageService {

    private ProcessDefinitionEntityStorage definitionStorage;
    private ProcessInstanceEntityStorage processStorage;
    private JobEntityStorage jobStorage;
    private UserTaskInstanceEntityStorage taskStorage;

    protected PostgreSqlStorageService() {
        //CDI proxy
    }

    @Inject
    public PostgreSqlStorageService(final ProcessDefinitionEntityStorage definitionStorage,
            final ProcessInstanceEntityStorage processStorage,
            final JobEntityStorage jobStorage,
            final UserTaskInstanceEntityStorage taskStorage) {
        this.definitionStorage = definitionStorage;
        this.processStorage = processStorage;
        this.jobStorage = jobStorage;
        this.taskStorage = taskStorage;
    }

    @Override
    public Storage<String, String> getCache(String name) {
        throw new UnsupportedOperationException("Generic String cache not available in PostgresSQL");
    }

    @Override
    public <T> Storage<String, T> getCache(String name, Class<T> type) {
        if (type == ProcessInstance.class) {
            return (Storage<String, T>) processStorage;
        }
        if (type == ProcessDefinition.class) {
            return (Storage<String, T>) definitionStorage;
        }
        if (type == Job.class) {
            return (Storage<String, T>) jobStorage;
        }
        if (type == UserTaskInstance.class) {
            return (Storage<String, T>) taskStorage;
        }
        throw new UnsupportedOperationException(format("Unknown class type: %s, cache not available", type.getCanonicalName()));
    }

    @Override
    public <T> Storage<String, T> getCache(String name, Class<T> type, String rootType) {
        throw new UnsupportedOperationException("Generic custom type cache not available in PostgresSQL");
    }
}
