package org.kie.kogito.persistence.infinispan.listener;

import java.util.function.Consumer;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.annotation.ClientCacheEntryCreated;
import org.infinispan.client.hotrod.annotation.ClientListener;
import org.infinispan.client.hotrod.event.ClientCacheEntryCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ClientListener
public class CacheObjectCreatedListener<K, V> extends AbstractCacheObjectListener<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheObjectCreatedListener.class);

    public CacheObjectCreatedListener(RemoteCache<K, V> cache, Consumer<V> consumer) {
        super(cache, consumer);
    }

    @ClientCacheEntryCreated
    public void handleCreatedEvent(ClientCacheEntryCreatedEvent<K> e) {
        LOGGER.debug("Handle create event for entry with id: {} on cache: {}", e.getKey(), cache.getName());
        handleEvent(e.getKey(), e.getVersion());
    }
}
