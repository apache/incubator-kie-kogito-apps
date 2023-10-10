package org.kie.kogito.trusty.storage.redis;

import java.util.ArrayList;
import java.util.List;

import org.kie.kogito.persistence.redis.RedisClientManager;
import org.kie.kogito.persistence.redis.index.RedisCreateIndexEvent;
import org.kie.kogito.persistence.redis.index.RedisIndexManager;

public class RedisIndexManagerMock extends RedisIndexManager {

    private List<String> indexNames = new ArrayList<>();

    public RedisIndexManagerMock(RedisClientManager redisClientManager) {
        super(redisClientManager);
    }

    @Override
    public void createIndex(RedisCreateIndexEvent event) {
        indexNames.add(event.getIndexName());
    }

    public List<String> getIndexNames() {
        return indexNames;
    }
}
