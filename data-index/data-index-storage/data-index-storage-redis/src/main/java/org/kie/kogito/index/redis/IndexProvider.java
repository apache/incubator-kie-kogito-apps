package org.kie.kogito.index.redis;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.quarkus.runtime.Startup;
import io.redisearch.Schema;
import org.kie.kogito.persistence.redis.index.RedisCreateIndexEvent;
import org.kie.kogito.persistence.redis.index.RedisIndexManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.kie.kogito.index.Constants.JOBS_STORAGE;
import static org.kie.kogito.index.Constants.PROCESS_ID_MODEL_STORAGE;
import static org.kie.kogito.index.Constants.PROCESS_INSTANCES_STORAGE;
import static org.kie.kogito.index.Constants.USER_TASK_INSTANCES_STORAGE;

@Singleton
@Startup
public class IndexProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexProvider.class);

    @Inject
    RedisIndexManager indexManager;

    @PostConstruct
    public void createIndexes(){
        LOGGER.info("Producing create index event");
        RedisCreateIndexEvent jobIndexEvent = new RedisCreateIndexEvent();
        setJobSchema(jobIndexEvent);
        indexManager.createIndex(jobIndexEvent);

        RedisCreateIndexEvent processInstanceIndexEvent = new RedisCreateIndexEvent();
        setProcessInstanceSchema(processInstanceIndexEvent);
        indexManager.createIndex(processInstanceIndexEvent);

        RedisCreateIndexEvent userTaskInstanceIndexEvent = new RedisCreateIndexEvent();
        setUserTaskInstanceSchema(userTaskInstanceIndexEvent);
        indexManager.createIndex(userTaskInstanceIndexEvent);

        RedisCreateIndexEvent processIdIndexEvent = new RedisCreateIndexEvent();
        setProcessIdSchema(processIdIndexEvent);
        indexManager.createIndex(processIdIndexEvent);

        LOGGER.info("Producing create index event done");
    }

    private void setJobSchema(RedisCreateIndexEvent redisCreateIndexEvent){
        redisCreateIndexEvent.indexName = JOBS_STORAGE;
        redisCreateIndexEvent.withField(new Schema.Field("id", Schema.FieldType.FullText, false));
        redisCreateIndexEvent.withField(new Schema.Field("processId", Schema.FieldType.FullText, false));
        redisCreateIndexEvent.withField(new Schema.Field("processInstanceId", Schema.FieldType.FullText, false));
        redisCreateIndexEvent.withField(new Schema.Field("nodeInstanceId", Schema.FieldType.FullText, false));
        redisCreateIndexEvent.withField(new Schema.Field("rootProcessId", Schema.FieldType.FullText, false));
        redisCreateIndexEvent.withField(new Schema.Field("rootProcessInstanceId", Schema.FieldType.FullText, false));
        //redisCreateIndexEvent.withField(new Schema.Field("expirationTime", Schema.FieldType.FullText, false));
        redisCreateIndexEvent.withField(new Schema.Field("priority", Schema.FieldType.Numeric, false));
        redisCreateIndexEvent.withField(new Schema.Field("callbackEndpoint", Schema.FieldType.FullText, false));
        redisCreateIndexEvent.withField(new Schema.Field("repeatInterval", Schema.FieldType.Numeric, false));
        redisCreateIndexEvent.withField(new Schema.Field("repeatLimit", Schema.FieldType.Numeric, false));
        redisCreateIndexEvent.withField(new Schema.Field("scheduledId", Schema.FieldType.FullText, false));
        redisCreateIndexEvent.withField(new Schema.Field("retries", Schema.FieldType.Numeric, false));
        redisCreateIndexEvent.withField(new Schema.Field("status", Schema.FieldType.FullText, false));
//        redisCreateIndexEvent.withField(new Schema.Field("lastUpdate", Schema.FieldType.FullText, false));
        redisCreateIndexEvent.withField(new Schema.Field("executionCounter", Schema.FieldType.Numeric, false));
        redisCreateIndexEvent.withField(new Schema.Field("endpoint", Schema.FieldType.FullText, false));
    }

    private void setProcessInstanceSchema(RedisCreateIndexEvent redisCreateIndexEvent){
        redisCreateIndexEvent.indexName = PROCESS_INSTANCES_STORAGE;
        redisCreateIndexEvent.setIsPrimitiveType(true);
    }

    private void setUserTaskInstanceSchema(RedisCreateIndexEvent redisCreateIndexEvent){
        redisCreateIndexEvent.indexName = USER_TASK_INSTANCES_STORAGE;
        redisCreateIndexEvent.withField(new Schema.Field("processId", Schema.FieldType.FullText, false));
        redisCreateIndexEvent.withField(new Schema.Field("rootProcessId", Schema.FieldType.FullText, false));
        redisCreateIndexEvent.withField(new Schema.Field("rootProcessInstanceId", Schema.FieldType.FullText, false));
        redisCreateIndexEvent.withField(new Schema.Field("endpoint", Schema.FieldType.FullText, false));
    }

    private void setProcessIdSchema(RedisCreateIndexEvent redisCreateIndexEvent){
        redisCreateIndexEvent.indexName = PROCESS_ID_MODEL_STORAGE;
        redisCreateIndexEvent.setIsPrimitiveType(true);
    }
}
