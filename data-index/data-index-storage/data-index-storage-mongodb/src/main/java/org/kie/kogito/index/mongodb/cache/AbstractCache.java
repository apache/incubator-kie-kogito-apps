/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.index.mongodb.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import org.kie.kogito.index.cache.Cache;

public abstract class AbstractCache<K, V> implements Cache<K, V> {

    Optional<Consumer<V>> objectCreatedListener = Optional.empty();
    Optional<Consumer<V>> objectUpdatedListener = Optional.empty();
    Optional<Consumer<K>> objectRemovedListener = Optional.empty();

    @Override
    public void addObjectCreatedListener(Consumer<V> consumer) {
        this.objectCreatedListener = Optional.ofNullable(consumer);
    }

    @Override
    public void addObjectUpdatedListener(Consumer<V> consumer) {
        this.objectUpdatedListener = Optional.ofNullable(consumer);
    }

    @Override
    public void addObjectRemovedListener(Consumer<K> consumer) {
        this.objectRemovedListener = Optional.ofNullable(consumer);
    }

    @Override
    public boolean containsKey(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsValue(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V putIfAbsent(K k, V v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o, Object o1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean replace(K k, V v, V v1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V replace(K k, V v) {
        throw new UnsupportedOperationException();
    }
}
