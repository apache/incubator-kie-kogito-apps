package org.kie.kogito.index.test.quarkus.http;

import java.util.HashMap;
import java.util.Map;

public class DataIndexPostgreSqlHttpQuarkusTestResource extends AbstractDataIndexHttpQuarkusTestResource<DataIndexPostgreSqlHttpResource> {

    public static final String QUARKUS_DATASOURCE_JDBC_URL = "quarkus.datasource.jdbc.url";
    public static final String QUARKUS_DATASOURCE_USERNAME = "quarkus.datasource.username";
    public static final String QUARKUS_DATASOURCE_PASSWORD = "quarkus.datasource.password";
    public static final String DATA_INDEX_MIGRATE_DB = "kogito.data-index.migrate.db";

    private boolean migrateDb = true;

    public DataIndexPostgreSqlHttpQuarkusTestResource() {
        super(new DataIndexPostgreSqlHttpResource());
    }

    @Override
    public Map<String, String> start() {
        if (migrateDb) {
            getTestResource().getDataIndex().migrateDB();
        }
        return super.start();
    }

    @Override
    public void init(Map<String, String> initArgs) {
        if (initArgs.containsKey(DATA_INDEX_MIGRATE_DB)) {
            migrateDb = Boolean.parseBoolean(initArgs.getOrDefault(DATA_INDEX_MIGRATE_DB, "true"));
        }
    }

    @Override
    protected Map<String, String> getProperties() {
        Map<String, String> properties = new HashMap<>(getDataIndexConnectionProperties());
        properties.put(QUARKUS_DATASOURCE_JDBC_URL, getTestResource().getPostgresql().getJdbcUrl());
        properties.put(QUARKUS_DATASOURCE_USERNAME, getTestResource().getPostgresql().getUsername());
        properties.put(QUARKUS_DATASOURCE_PASSWORD, getTestResource().getPostgresql().getPassword());
        return properties;
    }

}
