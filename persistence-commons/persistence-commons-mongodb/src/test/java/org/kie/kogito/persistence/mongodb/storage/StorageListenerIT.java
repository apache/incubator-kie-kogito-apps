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

import javax.inject.Inject;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.StorageService;
import org.kie.kogito.persistence.api.factory.StorageQualifier;
import org.kie.kogito.persistence.mongodb.MongoServerTestResource;
import org.kie.kogito.persistence.mongodb.client.MongoClientManager;
import org.kie.kogito.persistence.mongodb.storage.StorageUtilsIT.TestListener;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.kie.kogito.persistence.mongodb.Constants.MONGODB_STORAGE;

@QuarkusTest
@QuarkusTestResource(MongoServerTestResource.class)
class StorageListenerIT {

    @Inject
    MongoClientManager mongoClientManager;

    @Inject
    @StorageQualifier(MONGODB_STORAGE)
    StorageService storageService;

    @BeforeEach
    void setup() {
        mongoClientManager.getCollection("test", Document.class).insertOne(new Document("test", "test"));
    }

    @AfterEach
    void tearDown() {
        mongoClientManager.getCollection("test", Document.class).drop();
    }

    @Test
    void testObjectCreatedListener() throws Exception {
        TestListener testListener = new TestListener(3);
        Storage<String, String> storage = storageService.getCache("test");
        storage.addObjectCreatedListener(testListener::add);
        storage.put("testKey_insert_1", "testValue1");
        storage.put("testKey_insert_2", "testValue2");
        storage.put("testKey_insert_3", "testValue3");

        testListener.await();
        assertEquals(3, testListener.items.size(), "values: " + testListener.items.keySet());
        assertTrue(testListener.items.keySet().containsAll(asList("testValue1", "testValue2", "testValue3")));
    }

    @Test
    void testObjectUpdatedListener() throws Exception {
        TestListener testListener = new TestListener(2);
        Storage<String, String> cache = storageService.getCache("test");
        cache.addObjectUpdatedListener(testListener::add);
        cache.put("testKey_update_1", "testValue1");
        cache.put("testKey_update_1", "testValue2");
        cache.put("testKey_update_2", "testValue3");
        cache.put("testKey_update_2", "testValue4");

        testListener.await();
        assertEquals(2, testListener.items.size());
        assertTrue(testListener.items.keySet().containsAll(asList("testValue2", "testValue4")));
    }

    @Test
    void testObjectRemovedListener() throws Exception {
        TestListener testListener = new TestListener(2);
        Storage<String, String> cache = storageService.getCache("test");
        cache.addObjectRemovedListener(testListener::add);
        cache.put("testKey_remove_1", "testValue1");
        cache.put("testKey_remove_2", "testValue2");
        cache.remove("testKey_remove_1");
        cache.remove("testKey_remove_2");

        testListener.await();
        assertEquals(2, testListener.items.size());
        assertTrue(testListener.items.keySet().containsAll(asList("testKey_remove_1", "testKey_remove_2")));
    }
}
