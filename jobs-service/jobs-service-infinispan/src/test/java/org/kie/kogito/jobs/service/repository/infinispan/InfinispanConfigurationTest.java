package org.kie.kogito.jobs.service.repository.infinispan;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.quarkus.runtime.StartupEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InfinispanConfigurationTest {

    private InfinispanConfiguration tested;

    @BeforeEach
    public void setUp() {
        tested = new InfinispanConfiguration();
    }

    @Test
    void initializeCaches(@Mock Event<InfinispanInitialized> initializedEvent,
            @Mock RemoteCacheManager remoteCacheManager,
            @Mock Instance<RemoteCacheManager> instance,
            @Mock RemoteCache<Object, Object> cache) {
        when(instance.get()).thenReturn(remoteCacheManager);
        when(remoteCacheManager.getCache(InfinispanConfiguration.Caches.JOB_DETAILS)).thenReturn(cache);

        assertThat(tested.isInitialized()).isFalse();
        tested.initializeCaches(new StartupEvent(), instance, initializedEvent);
        verify(remoteCacheManager).getCache(eq(InfinispanConfiguration.Caches.JOB_DETAILS));

        verify(initializedEvent).fire(any(InfinispanInitialized.class));
        assertThat(tested.isInitialized()).isTrue();

    }
}
