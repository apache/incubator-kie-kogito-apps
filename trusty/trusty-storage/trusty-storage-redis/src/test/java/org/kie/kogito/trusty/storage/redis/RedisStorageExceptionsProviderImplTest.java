package org.kie.kogito.trusty.storage.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisExhaustedPoolException;

public class RedisStorageExceptionsProviderImplTest {
    @Test
    public void testConnectionExceptions() {
        RedisStorageExceptionsProviderImpl redisStorageExceptionsProvider = new RedisStorageExceptionsProviderImpl();
        Assertions.assertTrue(redisStorageExceptionsProvider.isConnectionException(new JedisConnectionException("I'm a connection exception")));
        Assertions.assertTrue(redisStorageExceptionsProvider.isConnectionException(new JedisExhaustedPoolException("I'm a connection exception")));
        Assertions.assertFalse(redisStorageExceptionsProvider.isConnectionException(new RuntimeException("I'm not a connection exception")));
    }
}
