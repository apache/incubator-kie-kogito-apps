package org.kie.kogito.trusty.storage.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.redis.RedisClientManager;
import org.mockito.Mockito;

import static org.kie.kogito.trusty.storage.common.TrustyStorageService.COUNTERFACTUAL_REQUESTS_STORAGE;
import static org.kie.kogito.trusty.storage.common.TrustyStorageService.COUNTERFACTUAL_RESULTS_STORAGE;
import static org.kie.kogito.trusty.storage.common.TrustyStorageService.DECISIONS_STORAGE;
import static org.kie.kogito.trusty.storage.common.TrustyStorageService.LIME_RESULTS_STORAGE;
import static org.kie.kogito.trusty.storage.common.TrustyStorageService.MODELS_STORAGE;

public class IndexProviderTest {

    @Test
    public void indexesAreCreated() {
        RedisIndexManagerMock redisIndexManager = new RedisIndexManagerMock(Mockito.mock(RedisClientManager.class));
        IndexProvider indexProvider = new IndexProvider(redisIndexManager);

        indexProvider.createIndexes();

        Assertions.assertEquals(5, redisIndexManager.getIndexNames().size());
        Assertions.assertTrue(redisIndexManager.getIndexNames().contains(DECISIONS_STORAGE));
        Assertions.assertTrue(redisIndexManager.getIndexNames().contains(MODELS_STORAGE));
        Assertions.assertTrue(redisIndexManager.getIndexNames().contains(LIME_RESULTS_STORAGE));
        Assertions.assertTrue(redisIndexManager.getIndexNames().contains(COUNTERFACTUAL_REQUESTS_STORAGE));
        Assertions.assertTrue(redisIndexManager.getIndexNames().contains(COUNTERFACTUAL_RESULTS_STORAGE));
    }
}
