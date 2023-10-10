package org.kie.kogito.index.test.quarkus.http;

import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.index.test.containers.DataIndexInfinispanContainer;
import org.kie.kogito.test.resources.TestResource;
import org.kie.kogito.testcontainers.KogitoInfinispanContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Network;

public class DataIndexInfinispanHttpResource implements TestResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataIndexInfinispanHttpResource.class);

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
        dataIndex.addProtoFileFolder();
        dataIndex.withNetwork(network);
        dataIndex.setInfinispanURL("infinispan:11222");
        dataIndex.addEnv("QUARKUS_PROFILE", "http-events-support");
        dataIndex.start();
        LOGGER.debug("Data Index Service started");
    }

    @Override
    public void stop() {
        dataIndex.stop();
        infinispan.stop();
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
