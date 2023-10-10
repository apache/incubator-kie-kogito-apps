package org.kie.kogito.persistence.infinispan.cache;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.kogito.persistence.api.StorageService;

import io.quarkus.runtime.ShutdownEvent;

import static org.kie.kogito.persistence.api.factory.Constants.PERSISTENCE_TYPE_PROPERTY;
import static org.kie.kogito.persistence.infinispan.Constants.INFINISPAN_STORAGE;

@ApplicationScoped
public class InfinispanCacheShutdownObserver {

    @ConfigProperty(name = PERSISTENCE_TYPE_PROPERTY)
    Optional<String> storageType;

    @Inject
    StorageService cacheService;

    public void stop(@Observes ShutdownEvent event) {
        if (storageType.isPresent() && INFINISPAN_STORAGE.equals(storageType.get()) && cacheService instanceof InfinispanStorageService) {
            ((InfinispanStorageService) cacheService).destroy();
        }
    }
}
