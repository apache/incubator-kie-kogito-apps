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

package org.kie.kogito.persistence.mongodb;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.microprofile.config.spi.ConfigSource;

public class MongoConfigSource implements ConfigSource {

    private static Map<String, String> mongoDBProperties = new ConcurrentHashMap<>();

    static void addProperty(String name, String value) {
        mongoDBProperties.put(name, value);
    }

    @Override
    public Map<String, String> getProperties() {
        return mongoDBProperties;
    }

    @Override
    public String getValue(String propertyName) {
        return mongoDBProperties.get(propertyName);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
