package org.kie.kogito.index.test.quarkus.kafka;

import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.index.test.containers.DataIndexInfinispanContainer;
import org.kie.kogito.index.test.containers.KogitoKafkaContainerWithoutBridge;
import org.kie.kogito.test.resources.TestResource;
import org.kie.kogito.testcontainers.KogitoInfinispanContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Network;

public class DataIndexInfinispanKafkaResource implements TestResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataIndexInfinispanKafkaResource.class);

    KogitoKafkaContainerWithoutBridge kafka = new KogitoKafkaContainerWithoutBridge();
    KogitoInfinispanContainer infinispan = new KogitoInfinispanContainer();
    DataIndexInfinispanContainer dataIndex = new DataIndexInfinispanContainer();
    Map<String, String> properties = new HashMap<>();

    @Override
    public String getResourceName() {
        return dataIndex.getResourceName();
    }

    @Override
    public void start() {
        LOGGER.debug("Start Infinispan Quarkus test resource");
        properties.clear();
        Network network = Network.newNetwork();
        infinispan.withNetwork(network);
        infinispan.withNetworkAliases("infinispan");
        infinispan.start();
        String infinispanURL = "localhost:" + infinispan.getMappedPort();
        properties.put("quarkus.infinispan-client.hosts", infinispanURL);
        kafka.withNetwork(network);
        kafka.withNetworkAliases("kafka");
        kafka.start();
        String kafkaURL = kafka.getBootstrapServers();
        properties.put("kafka.bootstrap.servers", kafkaURL);
        properties.put("spring.kafka.bootstrap-servers", kafkaURL);
        dataIndex.addProtoFileFolder();
        dataIndex.withNetwork(network);
        dataIndex.setInfinispanURL("infinispan:11222");
        dataIndex.setKafkaURL("kafka:29092");
        dataIndex.addEnv("QUARKUS_PROFILE", "kafka-events-support");
        dataIndex.start();
        LOGGER.debug("Data Index Service started");
    }

    @Override
    public void stop() {
        dataIndex.stop();
        infinispan.stop();
        kafka.stop();
        LOGGER.debug("Stop Infinispan Quarkus test resource");
    }

    @Override
    public int getMappedPort() {
        return dataIndex.getMappedPort();
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
