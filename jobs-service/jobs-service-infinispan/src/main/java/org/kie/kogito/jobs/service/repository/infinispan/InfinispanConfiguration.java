package org.kie.kogito.jobs.service.repository.infinispan;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.interceptor.Interceptor;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.Readiness;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.kie.kogito.infinispan.health.InfinispanHealthCheck;

import io.quarkus.runtime.StartupEvent;

import static org.kie.kogito.jobs.service.repository.infinispan.InfinispanConfiguration.Caches.JOB_DETAILS;

@ApplicationScoped
public class InfinispanConfiguration {

    private AtomicBoolean initialized = new AtomicBoolean(Boolean.FALSE);

    /**
     * Constants for Caches
     */
    public static class Caches {

        private Caches() {

        }

        public static final String JOB_DETAILS = "JOB_DETAILS_V2";
    }

    @Produces
    @Readiness
    public HealthCheck infinispanHealthCheck(Instance<RemoteCacheManager> cacheManagerInstance) {
        return new InfinispanHealthCheck(cacheManagerInstance);
    }

    void initializeCaches(@Observes @Priority(Interceptor.Priority.PLATFORM_BEFORE) StartupEvent startupEvent,
            Instance<RemoteCacheManager> remoteCacheManager,
            Event<InfinispanInitialized> initializedEvent) {
        Optional.ofNullable(remoteCacheManager.get().getCache(JOB_DETAILS))
                .ifPresent(c -> {
                    initializedEvent.fire(new InfinispanInitialized());
                    initialized.set(Boolean.TRUE);
                });
    }

    protected Boolean isInitialized() {
        return initialized.get();
    }

}
