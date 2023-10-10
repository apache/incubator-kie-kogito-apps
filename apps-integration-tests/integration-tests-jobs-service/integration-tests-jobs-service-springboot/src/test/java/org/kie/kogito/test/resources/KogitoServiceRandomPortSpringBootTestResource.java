package org.kie.kogito.test.resources;

import java.util.Map;

import static java.util.Collections.singletonMap;

public class KogitoServiceRandomPortSpringBootTestResource extends ConditionalSpringBootTestResource<KogitoServiceRandomPortTestResource> {

    public static final String SPRINGBOOT_SERVICE_HTTP_PORT = "server.port";

    public KogitoServiceRandomPortSpringBootTestResource() {
        super(new KogitoServiceRandomPortTestResource());
    }

    @Override
    protected Map<String, String> getProperties() {
        return singletonMap(SPRINGBOOT_SERVICE_HTTP_PORT, String.valueOf(getTestResource().getMappedPort()));
    }

}
