/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.index.postgresql.storage;

import org.kie.kogito.index.postgresql.model.AbstractEntity;
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
