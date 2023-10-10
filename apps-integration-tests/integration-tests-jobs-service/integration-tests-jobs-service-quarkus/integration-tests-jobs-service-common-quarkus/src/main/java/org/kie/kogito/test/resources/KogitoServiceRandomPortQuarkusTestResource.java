package org.kie.kogito.test.resources;

import java.util.HashMap;
import java.util.Map;

public class KogitoServiceRandomPortQuarkusTestResource extends ConditionalQuarkusTestResource<KogitoServiceRandomPortTestResource> {

    public static final String QUARKUS_SERVICE_HTTP_PORT = "quarkus.http.port";

    public KogitoServiceRandomPortQuarkusTestResource() {
        super(new KogitoServiceRandomPortTestResource());
    }

    /**
     * The Kogito Service must be run first to make the port available in the rest of services.
     */
    @Override
    public int order() {
        return -1;
    }

    @Override
    protected Map<String, String> getProperties() {
        Map<String, String> properties = new HashMap<>();
        properties.put(QUARKUS_SERVICE_HTTP_PORT, String.valueOf(getTestResource().getMappedPort()));
        properties.put(KogitoServiceRandomPortTestResource.KOGITO_SERVICE_URL, getTestResource().getKogitoServiceURL());
        return properties;
    }
}
