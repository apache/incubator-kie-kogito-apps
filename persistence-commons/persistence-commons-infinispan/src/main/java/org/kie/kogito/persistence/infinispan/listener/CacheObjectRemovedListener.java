package org.kie.kogito.persistence.infinispan.listener;

import java.util.function.Consumer;

import org.infinispan.client.hotrod.annotation.ClientCacheEntryRemoved;
import org.infinispan.client.hotrod.annotation.ClientListener;
import org.infinispan.client.hotrod.event.ClientCacheEntryRemovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ClientListener
public class CacheObjectRemovedListener<K> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheObjectRemovedListener.class);

    private Consumer<K> consumer;

    public CacheObjectRemovedListener(Consumer<K> consumer) {
        this.consumer = consumer;
    }

    @ClientCacheEntryRemoved
    public void handleRemovedEvent(ClientCacheEntryRemovedEvent<K> e) {
        LOGGER.debug("Handle remove event for entry with id: {}", e.getKey());
        consumer.accept(e.getKey());
    }
}
