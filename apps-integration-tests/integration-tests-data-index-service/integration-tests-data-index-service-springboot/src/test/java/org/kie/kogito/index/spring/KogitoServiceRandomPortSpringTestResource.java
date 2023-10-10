package org.kie.kogito.index.spring;

import java.util.Map;

import org.kie.kogito.index.test.quarkus.KogitoServiceRandomPortTestResource;
import org.kie.kogito.test.resources.ConditionalSpringBootTestResource;

import static java.util.Collections.singletonMap;

public class KogitoServiceRandomPortSpringTestResource extends ConditionalSpringBootTestResource<KogitoServiceRandomPortTestResource> {

    public static final String SPRINGBOOT_SERVICE_HTTP_PORT = "server.port";

    public KogitoServiceRandomPortSpringTestResource() {
        super(new KogitoServiceRandomPortTestResource());
    }

    @Override
    protected Map<String, String> getProperties() {
        return singletonMap(SPRINGBOOT_SERVICE_HTTP_PORT, String.valueOf(getTestResource().getMappedPort()));
    }
}
