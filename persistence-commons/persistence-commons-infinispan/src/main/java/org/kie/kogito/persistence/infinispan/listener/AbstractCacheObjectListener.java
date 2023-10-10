package org.kie.kogito.persistence.infinispan.listener;

import java.util.function.Consumer;

import org.infinispan.client.hotrod.RemoteCache;

public abstract class AbstractCacheObjectListener<K, T> {

    protected RemoteCache<K, T> cache;
    protected Consumer<T> consumer;

    protected AbstractCacheObjectListener(RemoteCache<K, T> cache, Consumer<T> consumer) {
        this.cache = cache;
        this.consumer = consumer;
    }

    protected void handleEvent(K key, long version) {
        cache.getWithMetadataAsync(key).thenAccept(meta -> {
            if (meta.getVersion() == version) {
                consumer.accept(meta.getValue());
            }
        });
    }
}
