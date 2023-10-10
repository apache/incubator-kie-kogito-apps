package org.kie.kogito.index.test.quarkus.kafka;

import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.test.resources.ConditionalQuarkusTestResource;

import static org.kie.kogito.index.test.Constants.KOGITO_DATA_INDEX_SERVICE_URL;

public class DataIndexOracleQuarkusKafkaTestResource extends ConditionalQuarkusTestResource<DataIndexOracleKafkaResource> {
    public DataIndexOracleQuarkusKafkaTestResource() {
        super(new DataIndexOracleKafkaResource());
    }

    @Override
    protected Map<String, String> getProperties() {
        Map<String, String> properties = new HashMap<>();
        properties.put(KOGITO_DATA_INDEX_SERVICE_URL, "http://localhost:" + getTestResource().getMappedPort());
        properties.putAll(getTestResource().getProperties());
        return properties;
    }

}
