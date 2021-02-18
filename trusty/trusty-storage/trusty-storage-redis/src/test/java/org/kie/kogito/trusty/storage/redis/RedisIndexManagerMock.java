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

import java.util.ArrayList;
import java.util.List;

import org.kie.kogito.persistence.redis.RedisClientManager;
import org.kie.kogito.persistence.redis.index.RedisCreateIndexEvent;
import org.kie.kogito.persistence.redis.index.RedisIndexManager;

public class RedisIndexManagerMock extends RedisIndexManager {

    private List<String> indexNames = new ArrayList<>();

    public RedisIndexManagerMock(RedisClientManager redisClientManager) {
        super(redisClientManager);
    }

    @Override
    public void createIndex(RedisCreateIndexEvent event) {
        indexNames.add(event.getIndexName());
    }

    public List<String> getIndexNames() {
        return indexNames;
    }
}
