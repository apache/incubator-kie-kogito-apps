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

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import io.quarkus.mongodb.panache.runtime.MongoOperations;

import static org.eclipse.microprofile.config.ConfigProvider.getConfig;

public class StorageUtils {

    private StorageUtils() {
    }

    private static final String MONGODB_CONNECTION_PROPERTY = "quarkus.mongodb.connection-string";

    public static <E> MongoCollection<E> getCollection(String collection, Class<E> type) {
        return MongoOperations.mongoDatabase(type).getCollection(collection, type);
    }

    public static <E> com.mongodb.reactivestreams.client.MongoCollection<E> getReactiveCollection(MongoCollection<E> collection) {
        String connection = getConfig().getValue(MONGODB_CONNECTION_PROPERTY, String.class);
        MongoClient mongoClient = MongoClients.create(new ConnectionString(connection));
        return mongoClient.getDatabase(collection.getNamespace().getDatabaseName())
                .getCollection(collection.getNamespace().getCollectionName(), collection.getDocumentClass());
    }
}
