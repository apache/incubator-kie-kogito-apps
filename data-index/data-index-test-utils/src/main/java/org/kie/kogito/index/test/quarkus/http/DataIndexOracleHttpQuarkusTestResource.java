package org.kie.kogito.index.test.quarkus.http;

import java.util.HashMap;
import java.util.Map;

public class DataIndexOracleHttpQuarkusTestResource extends AbstractDataIndexHttpQuarkusTestResource<DataIndexOracleHttpResource> {

    public DataIndexOracleHttpQuarkusTestResource() {
        super(new DataIndexOracleHttpResource());
    }

    @Override
    protected Map<String, String> getProperties() {
        Map<String, String> properties = new HashMap<>();
        properties.putAll(getDataIndexConnectionProperties());
        properties.putAll(getTestResource().getProperties());
        return properties;
    }

}
