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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.StorageService;
import org.kie.kogito.persistence.api.factory.StorageQualifier;
import org.kie.kogito.persistence.mongodb.MongoDBServerTestResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.kie.kogito.persistence.mongodb.Constants.MONGODB_STORAGE;

@QuarkusTest
@QuarkusTestResource(MongoDBServerTestResource.class)
public class CacheListenerTest {

    @Inject
    @StorageQualifier(MONGODB_STORAGE)
    StorageService storageService;

    @Test
    void testObjectCreatedListener() throws Exception {
        CompletableFuture<String> cf = new CompletableFuture<>();
        Storage<String, String> storage = storageService.getCache("test");
        storage.addObjectCreatedListener(cf::complete);
        storage.put("testKey", "testValue");

        String value = cf.get(1, TimeUnit.MINUTES);
        assertEquals("testValue", value);
    }

    @Test
    void testObjectUpdatedListener() throws Exception {
        CompletableFuture<String> cf = new CompletableFuture<>();
        Storage<String, String> cache = storageService.getCache("test");
        cache.addObjectUpdatedListener(cf::complete);
        cache.put("testKey", "testValue2");
        cache.put("testKey", "testValue2");

        String value = cf.get(1, TimeUnit.MINUTES);
        assertEquals("testValue2", value);
    }

    @Test
    void testObjectRemovedListener() throws Exception {
        CompletableFuture<String> cf = new CompletableFuture<>();
        Storage<String, String> cache = storageService.getCache("test");
        cache.addObjectRemovedListener(cf::complete);
        cache.put("testKey", "testValue");
        cache.remove("testKey");

        String value = cf.get(1, TimeUnit.MINUTES);
        assertEquals("testKey", value);
    }
}
