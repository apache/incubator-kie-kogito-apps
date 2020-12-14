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

import io.redisearch.client.Client;
import org.eclipse.microprofile.config.inject.ConfigProperty;

public class RedisClientManager {
    @ConfigProperty(name = "kogito.persistence.redis.host", defaultValue = "localhost")
    private String host;
    @ConfigProperty(name = "kogito.persistence.redis.port", defaultValue = "6379")
    private int port;

    public Client getClient(String indexName){
        return new Client(indexName, host, port);
    }
}
