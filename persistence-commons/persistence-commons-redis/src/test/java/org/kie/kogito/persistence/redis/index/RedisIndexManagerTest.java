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

import java.util.List;
import java.util.stream.Collectors;

import io.redisearch.Schema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.redis.RedisClientManager;
import org.kie.kogito.persistence.redis.RedisClientMock;
import org.mockito.Mockito;

import static org.kie.kogito.persistence.redis.Constants.INDEX_NAME_FIELD;
import static org.kie.kogito.persistence.redis.Person.AGE_PROPERTY;
import static org.kie.kogito.persistence.redis.Person.NAME_PROPERTY;
import static org.kie.kogito.persistence.redis.TestContants.TEST_INDEX_NAME;
import static org.mockito.Mockito.when;

public class RedisIndexManagerTest {

    @Test
    public void createSchemaTest() {
        RedisClientMock redisClientMock = new RedisClientMock();
        RedisClientManager redisClientManager = Mockito.mock(RedisClientManager.class);
        when(redisClientManager.getClient(TEST_INDEX_NAME)).thenReturn(redisClientMock);

        RedisIndexManager redisIndexManager = new RedisIndexManager(redisClientManager);

        RedisCreateIndexEvent redisCreateIndexEvent = new RedisCreateIndexEvent(TEST_INDEX_NAME);
        redisCreateIndexEvent.withField(new Schema.Field(NAME_PROPERTY, Schema.FieldType.FullText, false));
        redisCreateIndexEvent.withField(new Schema.Field(AGE_PROPERTY, Schema.FieldType.Numeric, false));
        redisIndexManager.createIndex(redisCreateIndexEvent);

        List<Schema> schemas = redisClientMock.getSchemas();
        Assertions.assertEquals(1, schemas.size());

        Schema personSchema = schemas.get(0);
        Assertions.assertEquals(3, personSchema.fields.size());

        List<String> fieldNames = personSchema.fields.stream().map(x -> x.name).collect(Collectors.toList());
        Assertions.assertTrue(fieldNames.contains(NAME_PROPERTY));
        Assertions.assertTrue(fieldNames.contains(AGE_PROPERTY));
        Assertions.assertTrue(fieldNames.contains(INDEX_NAME_FIELD));
    }
}
