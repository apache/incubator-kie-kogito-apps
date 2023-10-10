package org.kie.kogito.trusty.storage.infinispan;

import org.infinispan.client.hotrod.exceptions.HotRodClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InfinispanStorageExceptionsProviderImplTest {
    @Test
    public void testConnectionExceptions() {
        InfinispanStorageExceptionsProviderImpl redisStorageExceptionsProvider = new InfinispanStorageExceptionsProviderImpl();
        Assertions.assertTrue(redisStorageExceptionsProvider.isConnectionException(new HotRodClientException("I'm a connection exception")));
        Assertions.assertFalse(redisStorageExceptionsProvider.isConnectionException(new RuntimeException("I'm not a connection exception")));
    }
}
