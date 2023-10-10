package org.kie.kogito.index.test.quarkus.http;

import java.util.HashMap;
import java.util.Map;

public class DataIndexMongoDBHttpQuarkusTestResource extends AbstractDataIndexHttpQuarkusTestResource<DataIndexMongoDBHttpResource> {
    public DataIndexMongoDBHttpQuarkusTestResource() {
        super(new DataIndexMongoDBHttpResource());
    }

    @Override
    protected Map<String, String> getProperties() {
        Map<String, String> properties = new HashMap<>();
        properties.putAll(getDataIndexConnectionProperties());
        properties.putAll(getTestResource().getProperties());
        return properties;
    }

}
