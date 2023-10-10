package org.kie.kogito.addons.quarkus.jobs.service.embedded;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.microprofile.config.spi.ConfigSource;

public class KogitoAddonsQuarkusJobsServiceEmbeddedConfigSource implements ConfigSource {

    private static final String KAFKA_DEV_SERVICES = "quarkus.kafka.devservices.enabled";
    private static final String DATASOURCE_DEV_SERVICES = "quarkus.datasource.devservices.enabled";

    private static final Map<String, String> DEFAULT_CONFIG = new HashMap<>();

    static {
        DEFAULT_CONFIG.put(KAFKA_DEV_SERVICES, "false");
        DEFAULT_CONFIG.put(DATASOURCE_DEV_SERVICES, "false");
    }

    @Override
    public Set<String> getPropertyNames() {
        return DEFAULT_CONFIG.keySet();
    }

    @Override
    public String getValue(String propertyName) {
        return DEFAULT_CONFIG.get(propertyName);
    }

    @Override
    public Map<String, String> getProperties() {
        return DEFAULT_CONFIG;
    }

    @Override
    public String getName() {
        return KogitoAddonsQuarkusJobsServiceEmbeddedConfigSource.class.getSimpleName();
    }

    @Override
    public int getOrdinal() {
        return Integer.MIN_VALUE;
    }
}
