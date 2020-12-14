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
package org.kie.kogito.persistence.redis.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.redisearch.Schema;
import io.redisearch.client.Client;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.kogito.persistence.redis.RedisClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.exceptions.JedisDataException;

@ApplicationScoped
public class RedisIndexManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisIndexManager.class);

    private Map<String, List<String>> indexes = new HashMap<>();

    @Inject
    RedisClientManager redisClientManager;

    public void createIndex(RedisCreateIndexEvent event) {
        Client client = redisClientManager.getClient(event.indexName);
        List<String> fields = new ArrayList<>();
        Schema schema = new Schema();
            for (Schema.Field field : event.fields) {
                fields.add(field.name);
                schema.addField(field);
            }
        schema.addField(new Schema.Field("indexName", Schema.FieldType.FullText, false ));

        indexes.put(event.indexName, fields);

        try{
            client.createIndex(schema, Client.IndexOptions.defaultOptions());
        }
        catch (JedisDataException ignored){
            LOGGER.warn("something went wrong.", ignored);
        }
    }

    public List<String> getSchema(String indexName){
        return indexes.get(indexName);
    }
}