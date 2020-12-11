package org.kie.kogito.persistence.redis.index;

import java.util.ArrayList;
import java.util.List;

import io.redisearch.Schema;

public class RedisCreateIndexEvent {

    public String indexName;
    public List<Schema.Field> fields = new ArrayList<>();
    public Boolean isPrimitiveType = false;

    public void setIsPrimitiveType(Boolean isPrimitiveType){
        this.isPrimitiveType = isPrimitiveType;
    }

    public RedisCreateIndexEvent withField(Schema.Field field){
        this.fields.add(field);
        return this;
    }
}
