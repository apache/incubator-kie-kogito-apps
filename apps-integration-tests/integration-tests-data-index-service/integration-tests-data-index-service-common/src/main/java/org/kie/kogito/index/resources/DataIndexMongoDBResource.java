/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.index.resources;

import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.index.testcontainers.DataIndexMongoDBContainer;
import org.kie.kogito.index.testcontainers.KogitoKafkaContainerWithoutBridge;
import org.kie.kogito.resources.TestResource;
import org.kie.kogito.testcontainers.KogitoMongoDBContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Network;

public class DataIndexMongoDBResource implements TestResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataIndexMongoDBResource.class);

    KogitoKafkaContainerWithoutBridge kafka = new KogitoKafkaContainerWithoutBridge();
    KogitoMongoDBContainer mongodb = new KogitoMongoDBContainer();
    DataIndexMongoDBContainer dataIndex = new DataIndexMongoDBContainer();
    Map<String, String> properties = new HashMap<>();

    @Override
    public String getResourceName() {
        return dataIndex.getResourceName();
    }

    @Override
    public void start() {
        LOGGER.debug("Start MongoDB Quarkus test resource");
        properties.clear();
        Network network = Network.newNetwork();
        mongodb.withNetwork(network);
        mongodb.withNetworkAliases("mongo");
        mongodb.start();
        properties.put("quarkus.mongodb.connection-string", mongodb.getReplicaSetUrl());
        kafka.withNetwork(network);
        kafka.withNetworkAliases("kafka");
        kafka.start();
        String kafkaURL = kafka.getBootstrapServers();
        properties.put("kafka.bootstrap.servers", kafkaURL);
        properties.put("spring.kafka.bootstrap-servers", kafkaURL);
        dataIndex.addProtoFileFolder();
        dataIndex.withNetwork(network);
        dataIndex.setMongoDBURL("mongodb://mongo:27017/test");
        dataIndex.setKafkaURL("kafka:9092");
        dataIndex.start();
        LOGGER.debug("Data Index Service started");
    }

    @Override
    public void stop() {
        dataIndex.stop();
        mongodb.stop();
        kafka.stop();
        LOGGER.debug("Stop MongoDB Quarkus test resource");
    }

    @Override
    public int getMappedPort() {
        return dataIndex.getMappedPort();
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
