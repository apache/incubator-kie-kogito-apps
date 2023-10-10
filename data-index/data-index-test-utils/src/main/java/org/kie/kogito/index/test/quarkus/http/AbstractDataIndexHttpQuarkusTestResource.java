package org.kie.kogito.index.test.quarkus.http;

import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.test.resources.ConditionHolder;
import org.kie.kogito.test.resources.ConditionalQuarkusTestResource;
import org.kie.kogito.test.resources.TestResource;

import static org.kie.kogito.index.test.Constants.KOGITO_DATA_INDEX_SERVICE_URL;

public abstract class AbstractDataIndexHttpQuarkusTestResource<T extends TestResource> extends ConditionalQuarkusTestResource<T> {

    public AbstractDataIndexHttpQuarkusTestResource(T testResource) {
        super(testResource);
    }

    public AbstractDataIndexHttpQuarkusTestResource(T testResource, ConditionHolder condition) {
        super(testResource, condition);
    }

    protected Map<String, String> getDataIndexConnectionProperties() {
        Map<String, String> properties = new HashMap<>();
        String dataIndexUrl = "http://localhost:" + getTestResource().getMappedPort();
        properties.put(KOGITO_DATA_INDEX_SERVICE_URL, dataIndexUrl);
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
