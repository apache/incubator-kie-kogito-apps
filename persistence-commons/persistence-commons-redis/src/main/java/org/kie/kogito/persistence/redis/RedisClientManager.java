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

import java.net.URL;

import javax.enterprise.context.ApplicationScoped;

import io.redisearch.Client;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class RedisClientManager {

    @ConfigProperty(name = "kogito.persistence.redis.url", defaultValue = "http://localhost:6379")
    private URL url;

    public Client getClient(String indexName) {
        return new io.redisearch.client.Client(indexName, url.getHost(), url.getPort());
    }
}
