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

package org.kie.kogito.persistence.mongodb.storage;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import io.quarkus.mongodb.panache.runtime.MongoOperations;
import org.bson.Document;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.query.Query;
import org.kie.kogito.persistence.mongodb.model.MongoEntityMapper;
import org.kie.kogito.persistence.mongodb.query.MongoQuery;

public class MongoStorage<K, V, E> implements Storage<K, V> {

    Consumer<V> objectCreatedListener;
    Consumer<V> objectUpdatedListener;
    Consumer<K> objectRemovedListener;

    MongoEntityMapper<K, V, E> mongoEntityMapper;

    MongoCollection<E> mongoCollection;
    String rootType;

    public MongoStorage(MongoCollection<E> mongoCollection, String rootType, MongoEntityMapper<K, V, E> mongoEntityMapper) {
        this.mongoCollection = mongoCollection;
        this.rootType = rootType;
        this.mongoEntityMapper = mongoEntityMapper;
    }

    @Override
    public void addObjectCreatedListener(Consumer<V> consumer) {
        this.objectCreatedListener = consumer;
    }

    @Override
    public void addObjectUpdatedListener(Consumer<V> consumer) {
        this.objectUpdatedListener = consumer;
    }

    @Override
    public void addObjectRemovedListener(Consumer<K> consumer) {
        this.objectRemovedListener = consumer;
    }

    @Override
    public Query<V> query() {
        return new MongoQuery<>(this.mongoCollection, this.mongoEntityMapper);
    }

    @Override
    public boolean containsKey(K o) {
        return this.mongoCollection.find(new Document(MongoOperations.ID, o)).iterator().hasNext();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get(K o) {
        return Optional.ofNullable(this.mongoCollection.find(new Document(MongoOperations.ID, o)).first()).map(e -> mongoEntityMapper.mapToModel(e)).orElse(null);
    }

    @Override
    public V put(K s, V v) {
        V oldValue = this.get(s);
        Optional.ofNullable(v).map(n -> mongoEntityMapper.mapToEntity(s, n)).ifPresent(
                e -> this.mongoCollection.replaceOne(
                        new Document(MongoOperations.ID, s.toString()),
                        e, new ReplaceOptions().upsert(true)));
        Optional.ofNullable(oldValue).ifPresentOrElse(o -> Optional.ofNullable(this.objectUpdatedListener).ifPresent(l -> l.accept(v)),
                                                      () -> Optional.ofNullable(this.objectCreatedListener).ifPresent(l -> l.accept(v)));
        return oldValue;
    }

    @Override
    public void clear() {
        this.mongoCollection.deleteMany(new Document());
    }

    @Override
    public String getRootType() {
        return this.rootType;
    }

    @Override
    public V remove(K o) {
        V oldValue = this.get(o);
        Optional.ofNullable(oldValue).ifPresent(i -> this.mongoCollection.deleteOne(new Document(MongoOperations.ID, o)));
        Optional.ofNullable(oldValue).map(i -> this.objectRemovedListener).ifPresent(l -> l.accept(o));
        return oldValue;
    }
}
