package org.kie.kogito.trusty.storage.redis;

import javax.enterprise.context.ApplicationScoped;

import org.kie.kogito.trusty.storage.api.StorageExceptionsProvider;

import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisExhaustedPoolException;

@ApplicationScoped
public class RedisStorageExceptionsProviderImpl implements StorageExceptionsProvider {
    public boolean isConnectionException(Throwable e) {
        return e instanceof JedisConnectionException || e instanceof JedisExhaustedPoolException;
    }
}
