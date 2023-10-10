package org.kie.kogito.persistence.postgresql;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.StorageService;
import org.kie.kogito.persistence.postgresql.model.CacheEntityRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.arc.properties.IfBuildProperty;

import static org.kie.kogito.persistence.api.factory.Constants.PERSISTENCE_TYPE_PROPERTY;
import static org.kie.kogito.persistence.postgresql.Constants.POSTGRESQL_STORAGE;

@ApplicationScoped
@IfBuildProperty(name = PERSISTENCE_TYPE_PROPERTY, stringValue = POSTGRESQL_STORAGE)
public class PostgresStorageService implements StorageService {

    @Inject
    protected CacheEntityRepository repository;

    @Inject
    protected ObjectMapper mapper;

    @Override
    public Storage<String, String> getCache(String name) {
        return new PostgresStorage<>(name, repository, mapper, String.class);
    }

    @Override
    public <T> Storage<String, T> getCache(String name, Class<T> type) {
        return new PostgresStorage<>(name, repository, mapper, type);
    }

    @Override
    public <T> Storage<String, T> getCache(String name, Class<T> type, String rootType) {
        return new PostgresStorage<>(name, repository, mapper, type, rootType);
    }
}
