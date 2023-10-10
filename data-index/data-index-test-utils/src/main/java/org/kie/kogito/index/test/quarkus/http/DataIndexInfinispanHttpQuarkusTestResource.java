package org.kie.kogito.index.test.quarkus.http;

import java.util.HashMap;
import java.util.Map;

public class DataIndexInfinispanHttpQuarkusTestResource extends AbstractDataIndexHttpQuarkusTestResource<DataIndexInfinispanHttpResource> {
    public DataIndexInfinispanHttpQuarkusTestResource() {
        super(new DataIndexInfinispanHttpResource());
    }

    @Override
    protected Map<String, String> getProperties() {
        Map<String, String> properties = new HashMap<>();
        properties.putAll(getDataIndexConnectionProperties());
        properties.putAll(getTestResource().getProperties());
        return properties;
    }

}
