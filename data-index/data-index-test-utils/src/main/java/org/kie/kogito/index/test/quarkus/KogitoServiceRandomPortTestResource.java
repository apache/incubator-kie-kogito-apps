package org.kie.kogito.index.test.quarkus;

import org.kie.kogito.test.resources.TestResource;
import org.kie.kogito.test.utils.SocketUtils;
import org.testcontainers.Testcontainers;

public class KogitoServiceRandomPortTestResource implements TestResource {

    public static final String NAME = "kogito-service";

    public static final String KOGITO_SERVICE_URL = "kogito.service.url";

    private int httpPort;

    @Override
    public String getResourceName() {
        return NAME;
    }

    @Override
    public void start() {
        httpPort = SocketUtils.findAvailablePort();
        Testcontainers.exposeHostPorts(httpPort);
        //the hostname for the container to access the host is "host.testcontainers.internal"
        //https://www.testcontainers.org/features/networking/#exposing-host-ports-to-the-container
        System.setProperty(KOGITO_SERVICE_URL, "http://host.testcontainers.internal:" + httpPort);
    }

    @Override
    public void stop() {

    }

    @Override
    public int getMappedPort() {
        return httpPort;
    }
}
