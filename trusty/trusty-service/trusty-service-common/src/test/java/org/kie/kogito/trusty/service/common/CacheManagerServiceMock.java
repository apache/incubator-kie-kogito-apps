package org.kie.kogito.trusty.service.common;

import javax.enterprise.context.ApplicationScoped;

import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.StorageService;

import io.quarkus.arc.properties.IfBuildProperty;

import static org.kie.kogito.persistence.api.factory.Constants.PERSISTENCE_TYPE_PROPERTY;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unchecked")
@ApplicationScoped
@IfBuildProperty(name = PERSISTENCE_TYPE_PROPERTY, stringValue = "mock")
public class CacheManagerServiceMock implements StorageService {

    @Override
    public Storage<String, String> getCache(String name) {
        return mock(Storage.class);
    }

    @Override
    public <T> Storage<String, T> getCache(String name, Class<T> type) {
        return mock(Storage.class);
    }

    @Override
    public <T> Storage<String, T> getCache(String name, Class<T> type, String rootType) {
        return mock(Storage.class);
    }
}
