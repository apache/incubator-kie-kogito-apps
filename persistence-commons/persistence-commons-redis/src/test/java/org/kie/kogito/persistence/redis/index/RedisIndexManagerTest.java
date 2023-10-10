package org.kie.kogito.persistence.redis.index;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.persistence.redis.RedisClientManager;
import org.kie.kogito.persistence.redis.RedisClientMock;
import org.mockito.Mockito;

import io.redisearch.Schema;

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
