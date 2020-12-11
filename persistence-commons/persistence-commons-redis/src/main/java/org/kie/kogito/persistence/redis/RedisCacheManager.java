package org.kie.kogito.persistence.redis;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.redisearch.client.Client;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.StorageService;
import org.kie.kogito.persistence.api.factory.StorageQualifier;
import org.kie.kogito.persistence.redis.index.RedisIndexManager;

import static org.kie.kogito.persistence.redis.Constants.REDIS_STORAGE;

@ApplicationScoped
@StorageQualifier(REDIS_STORAGE)
public class RedisCacheManager implements StorageService {

    @Inject
    RedisIndexManager redisIndexManager;

    @Override
    public Storage<String, String> getCache(String name) {
        return new RedisStorage<>(redisIndexManager.getClient(name), redisIndexManager, name, String.class);
    }

    @Override
    public <T> Storage<String, T> getCache(String name, Class<T> type) {
        return new RedisStorage<>(redisIndexManager.getClient(name), redisIndexManager, name, type);
    }

    @Override
    public <T> Storage<String, T> getCacheWithDataFormat(String name, Class<T> type, String rootType) {
        return new RedisStorage<>(redisIndexManager.getClient(name), redisIndexManager, name, type);
    }
}