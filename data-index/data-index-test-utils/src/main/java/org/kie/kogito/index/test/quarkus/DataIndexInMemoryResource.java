package org.kie.kogito.index.test.quarkus;

import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.index.test.containers.DataIndexInMemoryContainer;
import org.kie.kogito.test.resources.TestResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Network;

public class DataIndexInMemoryResource implements TestResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataIndexInMemoryResource.class);

    DataIndexInMemoryContainer dataIndex = new DataIndexInMemoryContainer();
    Map<String, String> properties = new HashMap<>();

    @Override
    public String getResourceName() {
        return dataIndex.getResourceName();
    }

    @Override
    public void start() {
        LOGGER.debug("Starting InMemory Quarkus test resource");
        properties.clear();
        Network network = Network.newNetwork();
        dataIndex.addProtoFileFolder();
        dataIndex.withNetwork(network);
        dataIndex.start();
        LOGGER.debug("InMemory Quarkus test resource started");
    }

    @Override
    public void stop() {
        dataIndex.stop();
        LOGGER.debug("InMemory Quarkus test resource stopped");
    }

    @Override
    public int getMappedPort() {
        return dataIndex.getMappedPort();
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
