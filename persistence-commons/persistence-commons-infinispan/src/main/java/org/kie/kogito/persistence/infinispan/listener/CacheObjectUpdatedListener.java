package org.kie.kogito.persistence.infinispan.listener;

import java.util.function.Consumer;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryModified;
import org.infinispan.client.hotrod.annotation.ClientListener;
import org.infinispan.client.hotrod.event.ClientCacheEntryModifiedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ClientListener
public class CacheObjectUpdatedListener<K, V> extends AbstractCacheObjectListener<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheObjectUpdatedListener.class);

    public CacheObjectUpdatedListener(RemoteCache<K, V> cache, Consumer<V> consumer) {
        super(cache, consumer);
    }

    @ClientCacheEntryModified
    public void handleModifiedEvent(ClientCacheEntryModifiedEvent<K> e) {
        LOGGER.debug("Handle modified event for entry with id: {} on cache: {}", e.getKey(), cache.getName());
        handleEvent(e.getKey(), e.getVersion());
    }
}
