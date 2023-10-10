package org.kie.kogito.persistence.redis;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.StorageService;
import org.kie.kogito.persistence.redis.index.RedisIndexManager;

import io.quarkus.arc.properties.IfBuildProperty;

import static org.kie.kogito.persistence.api.factory.Constants.PERSISTENCE_TYPE_PROPERTY;
import static org.kie.kogito.persistence.redis.Constants.REDIS_STORAGE;

@ApplicationScoped
@IfBuildProperty(name = PERSISTENCE_TYPE_PROPERTY, stringValue = REDIS_STORAGE)
public class RedisCacheManager implements StorageService {

    @Inject
    RedisClientManager redisClientManager;

    @Inject
    RedisIndexManager redisIndexManager;

    @Override
    public Storage<String, String> getCache(String name) {
        return new RedisStorage<>(redisClientManager.getClient(name), redisIndexManager, name, String.class);
    }

    @Override
    public <T> Storage<String, T> getCache(String name, Class<T> type) {
        return new RedisStorage<>(redisClientManager.getClient(name), redisIndexManager, name, type);
    }

    @Override
    public <T> Storage<String, T> getCache(String name, Class<T> type, String rootType) {
        return new RedisStorage<>(redisClientManager.getClient(name), redisIndexManager, name, type);
    }
}