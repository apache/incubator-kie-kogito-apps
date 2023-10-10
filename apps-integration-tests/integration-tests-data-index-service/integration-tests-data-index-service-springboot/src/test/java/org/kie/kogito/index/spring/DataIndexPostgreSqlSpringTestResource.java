package org.kie.kogito.index.spring;

import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.index.test.quarkus.kafka.DataIndexPostgreSqlKafkaResource;
import org.kie.kogito.test.resources.ConditionalSpringBootTestResource;
import org.springframework.context.ConfigurableApplicationContext;

import static org.kie.kogito.index.test.Constants.KOGITO_DATA_INDEX_SERVICE_URL;

public class DataIndexPostgreSqlSpringTestResource extends ConditionalSpringBootTestResource<DataIndexPostgreSqlKafkaResource> {
    public DataIndexPostgreSqlSpringTestResource() {
        super(new DataIndexPostgreSqlKafkaResource());
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        getTestResource().getDataIndex().migrateDB();
        super.initialize(applicationContext);
    }

    @Override
    protected Map<String, String> getProperties() {
        Map<String, String> properties = new HashMap<>();
        properties.put(KOGITO_DATA_INDEX_SERVICE_URL, "http://localhost:" + getTestResource().getMappedPort());
        properties.putAll(getTestResource().getProperties());
        return properties;
    }

}
