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

package org.kie.kogito.index.mongodb;

import java.util.Collections;
import java.util.Map;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

public class MongoDBServerTestResource implements QuarkusTestResourceLifecycleManager {

    private static final String MONGODB_VERSION = System.getProperty("mongodb.version");
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBServerTestResource.class);
    private GenericContainer mongoDB;

    @Override
    public Map<String, String> start() {
        if (MONGODB_VERSION == null) {
            throw new RuntimeException("Please define a valid MongoDB image version in system property mongodb.version");
        }
        LOGGER.info("Using MongoDB image version: {}", MONGODB_VERSION);
        mongoDB = new FixedHostPortGenericContainer("mongo:" + MONGODB_VERSION)
                .withFixedExposedPort(27017, 27017)
                .withLogConsumer(new Slf4jLogConsumer(LOGGER))
                .waitingFor(Wait.forLogMessage(".*build index done.*", 1));
        mongoDB.start();
        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        mongoDB.stop();
    }
}
