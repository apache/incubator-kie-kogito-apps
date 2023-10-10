package org.kie.kogito.testcontainers;

import org.kie.kogito.test.resources.TestResource;
import org.testcontainers.containers.wait.strategy.Wait;

public class JobServiceContainer extends KogitoGenericContainer<JobServiceContainer> implements TestResource {

    public static final String NAME = "jobs-service";
    public static final int PORT = 8080;

    public JobServiceContainer() {
        super(NAME);
        addExposedPort(PORT);
        waitingFor(Wait.forHttp("/q/health/live").forStatusCode(200));
        addEnv("QUARKUS_HTTP_PORT", Integer.toString(PORT));
        addEnv("QUARKUS_LOG_CATEGORY__ORG_KIE_KOGITO_JOBS_SERVICE__LEVEL", "DEBUG");
        withAccessToHost(true);
    }

    @Override
    public int getMappedPort() {
        return getMappedPort(PORT);
    }

    @Override
    public String getResourceName() {
        return NAME;
    }
}
