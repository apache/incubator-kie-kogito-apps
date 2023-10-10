package org.kie.kogito.testcontainers;

public class KogitoServiceContainer extends KogitoGenericContainer<KogitoServiceContainer> {

    public KogitoServiceContainer(String kogitoServiceUrl) {
        super("kogito-service");
        addEnv("KOGITO_SERVICE_URL", kogitoServiceUrl);
        addExposedPort(8080);
    }
}
