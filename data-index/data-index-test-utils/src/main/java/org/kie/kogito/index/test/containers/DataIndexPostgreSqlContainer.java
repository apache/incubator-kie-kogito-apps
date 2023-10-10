package org.kie.kogito.index.test.containers;

/**
 * This container wraps Data Index Service container
 */
public class DataIndexPostgreSqlContainer extends AbstractDataIndexContainer {
    public static final String NAME = "data-index-service-postgresql";

    public DataIndexPostgreSqlContainer() {
        super(NAME);
    }

    public void setPostgreSqlURL(String postgreSqlURL, String username, String password) {
        addEnv("QUARKUS_DATASOURCE_JDBC_URL", postgreSqlURL);
        addEnv("QUARKUS_DATASOURCE_USERNAME", username);
        addEnv("QUARKUS_DATASOURCE_PASSWORD", password);
    }

    public void migrateDB() {
        addEnv("QUARKUS_FLYWAY_MIGRATE_AT_START", "true");
        addEnv("QUARKUS_FLYWAY_BASELINE_ON_MIGRATE", "true");
        addEnv("QUARKUS_FLYWAY_TABLE", "data-index-flyway");
    }

    @Override
    public String getResourceName() {
        return NAME;
    }

}
