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

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import io.quarkus.mongodb.panache.runtime.MongoOperations;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.kie.kogito.persistence.mongodb.model.MongoEntityMapper;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.changestream.FullDocument.UPDATE_LOOKUP;
import static java.util.Collections.singletonList;
import static org.eclipse.microprofile.config.ConfigProvider.getConfig;
import static org.kie.kogito.persistence.mongodb.model.ModelUtils.documentToObject;

public class StorageUtils {

    private StorageUtils() {
    }

    private static MongoClient mongoClient;

    static {
        String mongoConnectionProperty = "quarkus.mongodb.connection-string";
        String connection = getConfig().getValue(mongoConnectionProperty, String.class);
        mongoClient = MongoClients.create(new ConnectionString(connection));
    }

    public static <E> MongoCollection<E> getCollection(String collection, Class<E> type) {
        return MongoOperations.mongoDatabase(type).getCollection(collection, type);
    }

    public static <V, E> void watchCollection(MongoCollection<E> collection, Bson operationType,
                                              BiConsumer<String, V> consumer, MongoEntityMapper<V, E> mongoEntityMapper) {
        com.mongodb.reactivestreams.client.MongoCollection<E> reactiveMongoCollection = mongoClient.getDatabase(collection.getNamespace().getDatabaseName())
                .getCollection(collection.getNamespace().getCollectionName(), collection.getDocumentClass());
        reactiveMongoCollection.watch(singletonList(match(operationType)))
                .fullDocument(UPDATE_LOOKUP).subscribe(new ObjectListenerSubscriber<>(consumer, mongoEntityMapper));
    }

    private static class ObjectListenerSubscriber<V, E> implements Subscriber<ChangeStreamDocument<Document>> {

        Subscription subscription;
        BiConsumer<String, V> consumer;
        MongoEntityMapper<V, E> mongoEntityMapper;

        ObjectListenerSubscriber(BiConsumer<String, V> consumer, MongoEntityMapper<V, E> mongoEntityMapper) {
            this.consumer = consumer;
            this.mongoEntityMapper = mongoEntityMapper;
        }

        @Override
        public void onSubscribe(Subscription subscription) {
            this.subscription = subscription;
            this.subscription.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(ChangeStreamDocument<Document> changeStreamDocument) {
            BsonDocument keyDocument = changeStreamDocument.getDocumentKey();
            Document document = changeStreamDocument.getFullDocument();
            consumer.accept(Optional.ofNullable(keyDocument).map(key -> key.getString(MongoOperations.ID).getValue()).orElse(null),
                            Optional.ofNullable(document).map(doc -> mongoEntityMapper.mapToModel(documentToObject(doc, mongoEntityMapper.getEntityClass(), mongoEntityMapper::convertToModelAttribute))).orElse(null));
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
