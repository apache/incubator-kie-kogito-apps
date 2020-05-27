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

package org.kie.kogito.trusty.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.trusty.storage.mongo.MongoStorageManager;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

public class MongoStorageManagerTest {

    @Test
    public void GivenObjectToStore_WhenCreateIsCalled_ThenTheObjectIsStored() {
        MongoDatabase db = Mockito.mock(MongoDatabase.class);
        MongoCollection collection = Mockito.mock(MongoCollection.class);

        Mockito.when(collection.insertOne(any(Object.class))).thenReturn(null);

        Mockito.when(db.getCollection(any(String.class), any(Class.class))).thenReturn(collection);

        MongoStorageManager storageManager = new MongoStorageManager(db);

        Assertions.assertTrue(storageManager.create("key", new Object(), "index"));
    }
}
