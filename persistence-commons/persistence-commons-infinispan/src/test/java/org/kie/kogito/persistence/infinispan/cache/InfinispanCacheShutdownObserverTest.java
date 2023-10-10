package org.kie.kogito.persistence.infinispan.cache;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.api.StorageService;

import static org.kie.kogito.persistence.infinispan.Constants.INFINISPAN_STORAGE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class InfinispanCacheShutdownObserverTest {

    StorageService nonInfinispanStorageService = mock(StorageService.class);

    InfinispanStorageService infinispanStorageService = mock(InfinispanStorageService.class);

    Optional<String> nonInfinispanStorageType = Optional.of("testStorage");

    InfinispanCacheShutdownObserver observer = new InfinispanCacheShutdownObserver();

    @BeforeEach
    void setup() {
        reset(nonInfinispanStorageService, infinispanStorageService);
    }

    @Test
    void testStop_nonInfinispanStorageType() {
        observer.cacheService = infinispanStorageService;
        observer.storageType = nonInfinispanStorageType;

        observer.stop(null);

        verify(infinispanStorageService, never()).destroy();
    }

    @Test
    void testStop_nonInfinispanStorageService() {
        observer.cacheService = nonInfinispanStorageService;
        observer.storageType = Optional.of(INFINISPAN_STORAGE);

        observer.stop(null);

        verify(infinispanStorageService, never()).destroy();
    }

    @Test
    void testStop_infinispanStorageService() {
        observer.cacheService = infinispanStorageService;
        observer.storageType = Optional.of(INFINISPAN_STORAGE);

        observer.stop(null);

        verify(infinispanStorageService, times(1)).destroy();
    }
}