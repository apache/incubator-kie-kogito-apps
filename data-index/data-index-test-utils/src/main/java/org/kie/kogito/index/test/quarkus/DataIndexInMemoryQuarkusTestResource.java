package org.kie.kogito.index.test.quarkus;

import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.test.resources.ConditionalQuarkusTestResource;

import static org.kie.kogito.index.test.Constants.KOGITO_DATA_INDEX_SERVICE_URL;

public class DataIndexInMemoryQuarkusTestResource extends ConditionalQuarkusTestResource<DataIndexInMemoryResource> {

    public DataIndexInMemoryQuarkusTestResource() {
        super(new DataIndexInMemoryResource());
    }

    @Override
    protected Map<String, String> getProperties() {
        Map<String, String> properties = new HashMap<>();
        String dataIndexUrl = "http://localhost:" + getTestResource().getMappedPort();
        properties.put(KOGITO_DATA_INDEX_SERVICE_URL, dataIndexUrl);
        properties.putAll(getTestResource().getProperties());
        properties.put("mp.messaging.outgoing.kogito-processinstances-events.connector", "quarkus-http");
        properties.put("mp.messaging.outgoing.kogito-processinstances-events.url", dataIndexUrl + "/processes");
        properties.put("mp.messaging.outgoing.kogito-usertaskinstances-events.connector", "quarkus-http");
        properties.put("mp.messaging.outgoing.kogito-usertaskinstances-events.url", dataIndexUrl + "/tasks");
        properties.put("mp.messaging.outgoing.kogito-variables-events.connector", "quarkus-http");
        properties.put("mp.messaging.outgoing.kogito-variables-events.url", dataIndexUrl);
        properties.put("mp.messaging.outgoing.kogito-jobs-events.connector", "quarkus-http");
        properties.put("mp.messaging.outgoing.kogito-jobs-events.url", dataIndexUrl + "/jobs");
        properties.put("kogito.events.variables.enabled", "false");
        return properties;
    }

}
