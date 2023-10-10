package org.kie.kogito.index.test.containers;

/**
 * This container wraps Data Index Service container
 */
public class DataIndexOracleContainer extends AbstractDataIndexContainer {
    public static final String NAME = "data-index-service-oracle";

    public DataIndexOracleContainer() {
        super(NAME);
    }

    public void setDatabaseURL(String oracleURL, String username, String password) {
        addEnv("QUARKUS_DATASOURCE_JDBC_URL", oracleURL);
        addEnv("QUARKUS_DATASOURCE_USERNAME", username);
        addEnv("QUARKUS_DATASOURCE_PASSWORD", password);
        addEnv("QUARKUS_FLYWAY_MIGRATE_AT_START", "true");
        addEnv("QUARKUS_FLYWAY_BASELINE_ON_MIGRATE", "true");
    }

    @Override
    public String getResourceName() {
        return NAME;
    }

}
