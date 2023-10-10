package org.kie.kogito.index.test.quarkus.http;

import org.kie.kogito.index.test.containers.DataIndexPostgreSqlContainer;
import org.kie.kogito.test.resources.TestResource;
import org.kie.kogito.testcontainers.KogitoPostgreSqlContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;

public class DataIndexPostgreSqlHttpResource implements TestResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataIndexPostgreSqlHttpResource.class);

    private KogitoPostgreSqlContainer postgresql = new KogitoPostgreSqlContainer();
    private DataIndexPostgreSqlContainer dataIndex = new DataIndexPostgreSqlContainer();

    @Override
    public String getResourceName() {
        return dataIndex.getResourceName();
    }

    @Override
    public void start() {
        LOGGER.debug("Starting PostgreSQL Quarkus test resource");
        Network network = Network.newNetwork();
        postgresql.withNetwork(network);
        postgresql.withNetworkAliases("postgresql");
        postgresql.waitingFor(Wait.forListeningPort());
        postgresql.start();

        dataIndex.addProtoFileFolder();
        dataIndex.withNetwork(network);
        dataIndex
                .setPostgreSqlURL("jdbc:postgresql://postgresql:5432/" + postgresql.getDatabaseName(), postgresql.getUsername(),
                        postgresql.getPassword());
        dataIndex.addEnv("QUARKUS_PROFILE", "http-events-support");
        dataIndex.start();
        LOGGER.debug("PostgreSQL Quarkus test resource started");
    }

    @Override
    public void stop() {
        dataIndex.stop();
        postgresql.stop();
        LOGGER.debug("PostgreSQL Quarkus test resource stopped");
    }

    @Override
    public int getMappedPort() {
        return dataIndex.getMappedPort();
    }

    public KogitoPostgreSqlContainer getPostgresql() {
        return postgresql;
    }

    public DataIndexPostgreSqlContainer getDataIndex() {
        return dataIndex;
    }
}
