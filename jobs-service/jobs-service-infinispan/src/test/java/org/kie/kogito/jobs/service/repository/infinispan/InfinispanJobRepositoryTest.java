package org.kie.kogito.jobs.service.repository.infinispan;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.junit.jupiter.api.BeforeEach;
import org.kie.kogito.jobs.service.repository.ReactiveJobRepository;
import org.kie.kogito.jobs.service.repository.impl.BaseJobRepositoryTest;

import io.quarkus.test.junit.QuarkusTest;

import static org.awaitility.Awaitility.await;

@QuarkusTest
class InfinispanJobRepositoryTest extends BaseJobRepositoryTest {

    @Inject
    InfinispanJobRepository tested;

    @Inject
    InfinispanConfiguration infinispanConfiguration;

    @Inject
    RemoteCacheManager remoteCacheManager;

    @BeforeEach
    public void setUp() throws Exception {
        await().atMost(2, TimeUnit.SECONDS).until(() -> infinispanConfiguration.isInitialized());
        remoteCacheManager.getCache(InfinispanConfiguration.Caches.JOB_DETAILS).clear();
        super.setUp();
    }

    @Override
    public ReactiveJobRepository tested() {
        return tested;
    }
}
