package org.kie.kogito.persistence.redis.index;

import java.util.ArrayList;
import java.util.List;

import io.redisearch.Schema;

public class RedisCreateIndexEvent {

    private final String indexName;
    private final List<Schema.Field> fields = new ArrayList<>();

    public RedisCreateIndexEvent(String indexName) {
        this.indexName = indexName;
    }

    public RedisCreateIndexEvent withField(Schema.Field field) {
        this.fields.add(field);
        return this;
    }

    public String getIndexName() {
        return indexName;
    }

    public List<Schema.Field> getFields() {
        return fields;
    }
}
