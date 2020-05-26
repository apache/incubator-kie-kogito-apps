package org.kie.kogito.trusty.storage.infinispan;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.kie.kogito.trusty.storage.api.IStorageManager;
import org.kie.kogito.trusty.storage.api.TrustyStorageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class InfinispanStorageManager implements IStorageManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfinispanStorageManager.class);

    @Inject
    RemoteCacheManager manager;

    @PostConstruct
    void setup() {
        manager.start();
    }

    @Override
    public <T> boolean create(String key, T request, String index) {
        RemoteCache<String, T> remoteCache = manager.administration().getOrCreateCache(index, new TrustyCacheDefaultConfig(index));
        remoteCache.put(key, request);
        return true;
    }

    @Override
    public <T> List<T> search(TrustyStorageQuery query, String index, Class<T> type) {
        QueryFactory queryFactory = Search.getQueryFactory(manager.getCache(index));
        String qq = InfinispanQueryFactory.build(query, type.getName());
        Query q = queryFactory.create(qq);
        return q.list();
    }
}