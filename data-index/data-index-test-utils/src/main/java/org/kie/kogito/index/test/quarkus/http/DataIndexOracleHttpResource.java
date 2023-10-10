package org.kie.kogito.index.test.quarkus.http;

import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.index.test.containers.DataIndexOracleContainer;
import org.kie.kogito.test.resources.TestResource;
import org.kie.kogito.testcontainers.KogitoOracleSqlContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Network;

public class DataIndexOracleHttpResource implements TestResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataIndexOracleHttpResource.class);

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

        dataIndex.addProtoFileFolder();
        dataIndex.withNetwork(network);
        dataIndex.setDatabaseURL("jdbc:oracle:thin:@oracle:1521/" + oracle.getDatabaseName(),
                oracle.getUsername(), oracle.getPassword());
        dataIndex.addEnv("QUARKUS_PROFILE", "http-events-support");
        dataIndex.start();
        LOGGER.debug("Oracle Quarkus test resource started");
    }

    @Override
    public void stop() {
        dataIndex.stop();
        oracle.stop();
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
