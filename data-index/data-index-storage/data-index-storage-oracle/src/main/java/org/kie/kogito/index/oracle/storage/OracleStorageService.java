package org.kie.kogito.index.oracle.storage;

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
import static org.kie.kogito.persistence.oracle.Constants.ORACLE_STORAGE;

@AlternativePriority(1)
@ApplicationScoped
@IfBuildProperty(name = PERSISTENCE_TYPE_PROPERTY, stringValue = ORACLE_STORAGE)
public class OracleStorageService implements StorageService {

    @Inject
    ProcessDefinitionEntityStorage definitionStorage;

    @Inject
    ProcessInstanceEntityStorage processStorage;

    @Inject
    JobEntityStorage jobStorage;

    @Inject
    UserTaskInstanceEntityStorage taskStorage;

    @Override
    public Storage<String, String> getCache(String name) {
        throw new UnsupportedOperationException("Generic String cache not available in OracleSQL");
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
        throw new UnsupportedOperationException("Generic custom type cache not available in OracleSQL");
    }
}
