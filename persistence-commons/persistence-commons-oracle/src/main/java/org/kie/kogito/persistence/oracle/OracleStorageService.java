package org.kie.kogito.persistence.oracle;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.StorageService;
import org.kie.kogito.persistence.oracle.model.CacheEntityRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.arc.properties.IfBuildProperty;

import static org.kie.kogito.persistence.api.factory.Constants.PERSISTENCE_TYPE_PROPERTY;
import static org.kie.kogito.persistence.oracle.Constants.ORACLE_STORAGE;

@ApplicationScoped
@IfBuildProperty(name = PERSISTENCE_TYPE_PROPERTY, stringValue = ORACLE_STORAGE)
public class OracleStorageService implements StorageService {

    @Inject
    CacheEntityRepository repository;

    @Inject
    ObjectMapper mapper;

    @Override
    public Storage<String, String> getCache(String name) {
        return new OracleStorage<>(name, repository, mapper, String.class);
    }

    @Override
    public <T> Storage<String, T> getCache(String name, Class<T> type) {
        return new OracleStorage<>(name, repository, mapper, type);
    }

    @Override
    public <T> Storage<String, T> getCache(String name, Class<T> type, String rootType) {
        return new OracleStorage<>(name, repository, mapper, type, rootType);
    }
}
