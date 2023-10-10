package org.kie.kogito.index.test.quarkus.kafka;

import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.index.test.containers.DataIndexOracleContainer;
import org.kie.kogito.index.test.containers.KogitoKafkaContainerWithoutBridge;
import org.kie.kogito.test.resources.TestResource;
import org.kie.kogito.testcontainers.KogitoOracleSqlContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;

public class DataIndexOracleKafkaResource implements TestResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataIndexOracleKafkaResource.class);

    KogitoKafkaContainerWithoutBridge kafka = new KogitoKafkaContainerWithoutBridge();
    KogitoOracleSqlContainer oracle = new KogitoOracleSqlContainer();
    DataIndexOracleContainer dataIndex = new DataIndexOracleContainer();
    Map<String, String> properties = new HashMap<>();

    @Override
    public String getResourceName() {
        return dataIndex.getResourceName();
    }

    @Override
    public void start() {
        LOGGER.debug("Starting Oracle Quarkus test resource");
        properties.clear();
        Network network = Network.newNetwork();
        oracle.withNetwork(network);
        oracle.withNetworkAliases("oracle");
        oracle.withUsername("kogito");
        oracle.withPassword("kogito");
        oracle.start();
        kafka.withNetwork(network);
        kafka.withNetworkAliases("kafka");
        kafka.waitingFor(Wait.forListeningPort());
        kafka.start();
        String kafkaURL = kafka.getBootstrapServers();
        properties.put("kafka.bootstrap.servers", kafkaURL);
        properties.put("spring.kafka.bootstrap-servers", kafkaURL);

        dataIndex.addProtoFileFolder();
        dataIndex.withNetwork(network);
        dataIndex.setDatabaseURL("jdbc:oracle:thin:@oracle:1521/" + oracle.getDatabaseName(),
                oracle.getUsername(), oracle.getPassword());
        dataIndex.setKafkaURL("kafka:29092");
        dataIndex.addEnv("QUARKUS_PROFILE", "kafka-events-support");
        dataIndex.start();
        LOGGER.debug("Oracle Quarkus test resource started");
    }

    @Override
    public void stop() {
        dataIndex.stop();
        oracle.stop();
        kafka.stop();
        LOGGER.debug("Oracle Quarkus test resource stopped");
    }

    @Override
    public int getMappedPort() {
        return dataIndex.getMappedPort();
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
