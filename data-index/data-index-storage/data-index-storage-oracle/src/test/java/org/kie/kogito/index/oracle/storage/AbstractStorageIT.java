package org.kie.kogito.index.oracle.storage;

import org.kie.kogito.index.oracle.model.AbstractEntity;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.StorageService;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractStorageIT<E extends AbstractEntity, T> {

    Class<T> type;

    public AbstractStorageIT(Class<T> type) {
        this.type = type;
    }

    abstract StorageService getStorage();

    abstract PanacheRepositoryBase<E, String> getRepository();

    void testStorage(String key, T value1, T value2) {
        Storage<String, T> cache = getStorage().getCache("cache", type);
        assertThat(cache.get(key)).isNull();
        assertThat(cache.containsKey(key)).isFalse();
        assertThat(getRepository().count()).isZero();

        cache.put(key, value1);
        assertThat(cache.get(key)).isEqualTo(value1);
        assertThat(cache.containsKey(key)).isTrue();
        assertThat(getRepository().count()).isOne();

        cache.put(key, value2);
        assertThat(cache.get(key)).isEqualTo(value2);
        assertThat(cache.containsKey(key)).isTrue();
        assertThat(getRepository().count()).isOne();

        cache.remove(key);
        assertThat(cache.get(key)).isNull();
        assertThat(cache.containsKey(key)).isFalse();
        assertThat(getRepository().count()).isZero();
    }

}
