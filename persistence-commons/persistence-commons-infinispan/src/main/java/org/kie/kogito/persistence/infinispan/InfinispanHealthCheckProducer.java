package org.kie.kogito.persistence.infinispan;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;

import org.eclipse.microprofile.health.Readiness;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.kie.kogito.infinispan.health.InfinispanHealthCheck;

@ApplicationScoped
public class InfinispanHealthCheckProducer {

    @Produces
    @Readiness
    public InfinispanHealthCheck infinispanHealthCheck(Instance<RemoteCacheManager> cacheManagerInstance) {
        return new InfinispanHealthCheck(cacheManagerInstance);
    }
}
