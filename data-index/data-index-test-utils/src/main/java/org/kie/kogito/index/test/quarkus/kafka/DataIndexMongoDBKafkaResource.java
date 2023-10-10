package org.kie.kogito.index.test.quarkus.kafka;

import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.index.test.containers.DataIndexMongoDBContainer;
import org.kie.kogito.index.test.containers.KogitoKafkaContainerWithoutBridge;
import org.kie.kogito.test.resources.TestResource;
import org.kie.kogito.testcontainers.KogitoMongoDBContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Network;

public class DataIndexMongoDBKafkaResource implements TestResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataIndexMongoDBKafkaResource.class);

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
        dataIndex.setKafkaURL("kafka:29092");
        dataIndex.addEnv("QUARKUS_PROFILE", "kafka-events-support");
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
