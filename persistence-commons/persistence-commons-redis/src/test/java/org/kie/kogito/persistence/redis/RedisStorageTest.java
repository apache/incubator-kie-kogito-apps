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

package org.kie.kogito.persistence.redis;

import java.util.Map;

import io.redisearch.Schema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.redis.index.RedisCreateIndexEvent;
import org.kie.kogito.persistence.redis.index.RedisIndexManager;
import org.mockito.Mockito;

import static org.kie.kogito.persistence.redis.Constants.INDEX_NAME_FIELD;
import static org.kie.kogito.persistence.redis.Constants.RAW_OBJECT_FIELD;
import static org.kie.kogito.persistence.redis.Person.AGE_PROPERTY;
import static org.kie.kogito.persistence.redis.Person.NAME_PROPERTY;
import static org.kie.kogito.persistence.redis.TestContants.TEST_INDEX_NAME;
import static org.mockito.Mockito.when;

public class RedisStorageTest {

    RedisClientMock redisClientMock;
    RedisStorage<Person> redisStorage;
    RedisClientManager redisClientManagerMock;
    RedisIndexManager redisIndexManager;

    @BeforeEach
    public void setUp() {
        redisClientMock = new RedisClientMock();

        redisClientManagerMock = Mockito.mock(RedisClientManager.class);
        when(redisClientManagerMock.getClient(TEST_INDEX_NAME)).thenReturn(redisClientMock);

        redisIndexManager = new RedisIndexManager(redisClientManagerMock);
        setPersonIndex(redisIndexManager);

        redisStorage = new RedisStorage<>(redisClientMock, redisIndexManager, TEST_INDEX_NAME, Person.class);
    }

    @Test
    public void addObjectCreatedListenerOperationShouldThrowException() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> redisStorage.addObjectCreatedListener((x) -> {
        }));
    }

    @Test
    public void addObjectUpdatedListenerOperationShouldThrowException() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> redisStorage.addObjectUpdatedListener((x) -> {
        }));
    }

    @Test
    public void addObjectRemovedListenerOperationShouldThrowException() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> redisStorage.addObjectRemovedListener((x) -> {
        }));
    }

    @Test
    public void entrySetOperationShouldThrowException() {
        Assertions.assertThrows(UnsupportedOperationException.class, redisStorage::entrySet);
    }

    @Test
    public void indexNameIsPresentInStoredDocument() {
        redisStorage.put("myKey", new Person("pippo", 22));

        Map<String, Map<String, Object>> storage = redisClientMock.getStorage();

        Assertions.assertEquals(1, storage.size());

        Map<String, Object> value = storage.get("myKey");

        Assertions.assertEquals(4, value.size());
        Assertions.assertTrue(value.containsKey(INDEX_NAME_FIELD));
        Assertions.assertTrue(value.containsKey(RAW_OBJECT_FIELD));
        Assertions.assertTrue(value.containsKey(NAME_PROPERTY));
        Assertions.assertTrue(value.containsKey(AGE_PROPERTY));
    }

    @Test
    public void containsKeyOperationTest() {
        String key = "myKey";
        redisStorage.put(key, new Person("pippo", 22));

        Assertions.assertTrue(redisStorage.containsKey(key));
        Assertions.assertFalse(redisStorage.containsKey("a_key_that_does_not_exist"));
    }

    @Test
    public void putAndGetOperationsTest() {
        String key = "myKey";
        Person value = new Person("pippo", 22);
        redisStorage.put(key, value);

        Person retrieved = redisStorage.get(key);
        Assertions.assertEquals("pippo", retrieved.getName());
        Assertions.assertEquals(22, retrieved.getAge());
    }

    @Test
    public void getUnexistingDocumentOperationTest() {
        Assertions.assertNull(redisStorage.get("a_key_that_does_not_exist"));
    }

    @Test
    public void removeOperationTest() {
        String key = "myKey";
        Person value = new Person("pippo", 22);
        redisStorage.put(key, value);

        Assertions.assertNotNull(redisStorage.get(key));

        redisStorage.remove(key);

        Assertions.assertNull(redisStorage.get(key));
    }

    @Test
    public void nullIndexedValuesTest() {
        String key = "myKey";
        Person value = new Person(null, 22);
        redisStorage.put(key, value);

        Assertions.assertNotNull(redisStorage.get(key));
    }

    private void setPersonIndex(RedisIndexManager redisIndexManager) {
        RedisCreateIndexEvent redisCreateIndexEvent = new RedisCreateIndexEvent(TEST_INDEX_NAME);
        redisCreateIndexEvent.withField(new Schema.Field(NAME_PROPERTY, Schema.FieldType.FullText, false));
        redisCreateIndexEvent.withField(new Schema.Field(AGE_PROPERTY, Schema.FieldType.Numeric, false));
        redisIndexManager.createIndex(redisCreateIndexEvent);
    }
}
