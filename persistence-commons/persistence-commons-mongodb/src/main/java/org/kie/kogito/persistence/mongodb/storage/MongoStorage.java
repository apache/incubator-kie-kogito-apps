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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import io.quarkus.mongodb.panache.runtime.MongoOperations;
import org.bson.BsonDocument;
import org.bson.Document;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.query.Query;
import org.kie.kogito.persistence.mongodb.model.MongoEntityMapper;
import org.kie.kogito.persistence.mongodb.query.MongoQuery;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.changestream.FullDocument.UPDATE_LOOKUP;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.kie.kogito.persistence.mongodb.model.ModelUtils.documentToObject;
import static org.kie.kogito.persistence.mongodb.storage.StorageUtils.getReactiveCollection;

public class MongoStorage<V, E> implements Storage<String, V> {

    MongoEntityMapper<V, E> mongoEntityMapper;

    MongoCollection<E> mongoCollection;
    String rootType;

    public MongoStorage(MongoCollection<E> mongoCollection, String rootType, MongoEntityMapper<V, E> mongoEntityMapper) {
        this.mongoCollection = mongoCollection;
        this.rootType = rootType;
        this.mongoEntityMapper = mongoEntityMapper;
    }

    @Override
    public void addObjectCreatedListener(Consumer<V> consumer) {
        com.mongodb.reactivestreams.client.MongoCollection<E> reactiveMongoCollection = getReactiveCollection(this.mongoCollection);
        reactiveMongoCollection.watch(singletonList(match(eq("operationType", "insert"))))
                .fullDocument(UPDATE_LOOKUP).subscribe(new ObjectListenerSubscriber(false, consumer, null));
    }

    @Override
    public void addObjectUpdatedListener(Consumer<V> consumer) {
        com.mongodb.reactivestreams.client.MongoCollection<E> reactiveMongoCollection = getReactiveCollection(this.mongoCollection);
        reactiveMongoCollection.watch(singletonList(match(in("operationType", asList("update", "replace")))))
                .fullDocument(UPDATE_LOOKUP).subscribe(new ObjectListenerSubscriber(false, consumer, null));
    }

    @Override
    public void addObjectRemovedListener(Consumer<String> consumer) {
        com.mongodb.reactivestreams.client.MongoCollection<E> reactiveMongoCollection = getReactiveCollection(this.mongoCollection);
        reactiveMongoCollection.watch(singletonList(match(eq("operationType", "delete"))))
                .fullDocument(UPDATE_LOOKUP).subscribe(new ObjectListenerSubscriber(true, null, consumer));
    }

    @Override
    public Query<V> query() {
        return new MongoQuery<>(this.mongoCollection, this.mongoEntityMapper);
    }

    @Override
    public boolean containsKey(String o) {
        return this.mongoCollection.find(new Document(MongoOperations.ID, o)).iterator().hasNext();
    }

    @Override
    public Set<Map.Entry<String, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get(String o) {
        return Optional.ofNullable(this.mongoCollection.find(new Document(MongoOperations.ID, o)).first()).map(e -> mongoEntityMapper.mapToModel(e)).orElse(null);
    }

    @Override
    public V put(String s, V v) {
        V oldValue = this.get(s);
        Optional.ofNullable(oldValue).ifPresentOrElse(
                o -> Optional.ofNullable(v).map(n -> mongoEntityMapper.mapToEntity(s, n)).ifPresent(
                        e -> this.mongoCollection.replaceOne(
                                new Document(MongoOperations.ID, s),
                                e, new ReplaceOptions().upsert(true))),
                () -> Optional.ofNullable(v).map(n -> mongoEntityMapper.mapToEntity(s, n)).ifPresent(
                        e -> this.mongoCollection.insertOne(e)));
        return Objects.nonNull(v) ? oldValue : null;
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
    public V remove(String o) {
        V oldValue = this.get(o);
        Optional.ofNullable(oldValue).ifPresent(i -> this.mongoCollection.deleteOne(new Document(MongoOperations.ID, o)));
        return oldValue;
    }

    class ObjectListenerSubscriber implements Subscriber<ChangeStreamDocument<Document>> {

        Subscription subscription;
        Consumer<V> consumer;
        Consumer<String> keyConsumer;
        boolean consumeKey;

        ObjectListenerSubscriber(boolean consumeKey, Consumer<V> consumer, Consumer<String> keyConsumer) {
            this.consumeKey = consumeKey;
            this.consumer = consumer;
            this.keyConsumer = keyConsumer;
        }

        @Override
        public void onSubscribe(Subscription subscription) {
            this.subscription = subscription;
            this.subscription.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(ChangeStreamDocument<Document> changeStreamDocument) {
            if (consumeKey) {
                BsonDocument keyDocument = changeStreamDocument.getDocumentKey();
                Optional.ofNullable(keyDocument).ifPresent(key -> keyConsumer.accept(key.getString(MongoOperations.ID).getValue()));
            } else {
                consumer.accept(mongoEntityMapper.mapToModel(documentToObject(changeStreamDocument.getFullDocument(), mongoEntityMapper.getEntityClass())));
            }
        }

        @Override
        public void onError(Throwable throwable) {
            this.onComplete();
            throw new MongoObjectListenerException(throwable);
        }

        @Override
        public void onComplete() {
            if (Objects.nonNull(this.subscription)) {
                this.subscription.cancel();
            }
        }
    }
}
