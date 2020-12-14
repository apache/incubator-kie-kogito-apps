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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.redisearch.Document;
import io.redisearch.client.Client;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.query.Query;
import org.kie.kogito.persistence.redis.index.RedisIndexManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisStorage<V> implements Storage<String, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisStorage.class);

    private Client redisClient;
    private RedisIndexManager redisIndexManager;
    private String indexName;
    private Class<V> type;

    public RedisStorage(Client redisClient, RedisIndexManager redisIndexManager, String indexName, Class<V> type) {
        LOGGER.info(indexName);
        this.redisClient = redisClient;
        this.redisIndexManager = redisIndexManager;
        this.indexName = indexName;
        this.type = type;
    }

    @Override
    public void addObjectCreatedListener(Consumer<V> consumer) {
    }

    @Override
    public void addObjectUpdatedListener(Consumer<V> consumer) {

    }

    @Override
    public void addObjectRemovedListener(Consumer<String> consumer) {
    }

    @Override
    public Query<V> query() {
        return new RedisQuery<>(redisClient, indexName, type);
    }

    @Override
    public V get(String key) {
        LOGGER.info("FETCHING KEY: " + key + " in storage: " + indexName);
        Document document = redisClient.getDocument(key);
        LOGGER.info(String.valueOf(document == null));
        if (document == null) {
            return null;
        }
        try {
            return JsonUtils.getMapper().readValue((String) document.get("rawObject"), type);
        } catch (JsonProcessingException e) {
            LOGGER.warn("SHIT", e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public V put(String key, V value) {
        Map<String, Object> myMap = new HashMap<>();
        LOGGER.info("PUTTING KEY: " + key + " in storage: " + indexName + " for declared type: " + type.getName() + " for given class: " + value.getClass());
        List<String> fields = redisIndexManager.getSchema(indexName);
        if (fields.size() != 0) { // Add into the payload only the indexed fields, if there is any
            Map<String, Object> tmp = JsonUtils.getMapper().convertValue(value, Map.class);
            for (String mapKey : fields) {
                if (tmp.get(mapKey) != null) {
                    myMap.put(mapKey, tmp.get(mapKey));
                }
            }
        }

        myMap.put("indexName", indexName);

        try {
            LOGGER.info(JsonUtils.getMapper().writeValueAsString(value));
            myMap.put("rawObject", JsonUtils.getMapper().writeValueAsString(value));
        } catch (JsonProcessingException e) {
            LOGGER.warn("VERY BAD", e);
            e.printStackTrace();
        }
        redisClient.addDocument(key, myMap);
        return value;
    }

    @Override
    public V remove(String key) {
        V value = get(key);
        redisClient.deleteDocument(key);
        return value;
    }

    @Override
    public boolean containsKey(String key) {
        try {
            Document document = redisClient.getDocument(key);
            return key.equals(document.getId());
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public Set<Map.Entry<String, V>> entrySet() {
        throw new UnsupportedOperationException("entrySet operation not supported for Redis persistence.");
    }

    @Override
    public void clear() {
        redisClient.dropIndex();
    }

    @Override
    public String getRootType() {
        return type.getSimpleName();
    }
}
