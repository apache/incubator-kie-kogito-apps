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
package org.kie.kogito.trusty.storage.redis;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.quarkus.runtime.Startup;
import io.redisearch.Schema;
import org.kie.kogito.persistence.redis.index.RedisCreateIndexEvent;
import org.kie.kogito.persistence.redis.index.RedisIndexManager;
import org.kie.kogito.trusty.storage.api.model.Execution;
import org.kie.kogito.trusty.storage.api.model.ExplainabilityResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.kie.kogito.trusty.storage.api.TrustyStorageServiceImpl.DECISIONS_STORAGE;
import static org.kie.kogito.trusty.storage.api.TrustyStorageServiceImpl.MODELS_STORAGE;

@Singleton
@Startup
public class IndexProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexProvider.class);

    @Inject
    RedisIndexManager indexManager;

    @PostConstruct
    public void createIndexes(){
        LOGGER.info("Producing create index event");
        RedisCreateIndexEvent decisionIndexEvent = new RedisCreateIndexEvent();
        decisionIndexEvent.indexName = DECISIONS_STORAGE;
        decisionIndexEvent.withField(new Schema.Field(Execution.EXECUTION_ID_FIELD, Schema.FieldType.FullText, false));
        decisionIndexEvent.withField(new Schema.Field(Execution.EXECUTION_TIMESTAMP_FIELD, Schema.FieldType.Numeric, true));
        indexManager.createIndex(decisionIndexEvent);
        LOGGER.info("Producing create index event done");

        RedisCreateIndexEvent modelIndexEvent = new RedisCreateIndexEvent();
        modelIndexEvent.indexName = MODELS_STORAGE;
        indexManager.createIndex(modelIndexEvent);

        RedisCreateIndexEvent explainabilityIndexEvent = new RedisCreateIndexEvent();
        explainabilityIndexEvent.indexName = MODELS_STORAGE;
        explainabilityIndexEvent.withField(new Schema.Field(ExplainabilityResult.EXECUTION_ID_FIELD, Schema.FieldType.FullText, false));
        indexManager.createIndex(explainabilityIndexEvent);
    }
}
