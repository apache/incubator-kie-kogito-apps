package org.kie.kogito.jobs.service.resource;

import java.util.Collections;
import java.util.List;

import org.kie.kogito.testcontainers.quarkus.KeycloakQuarkusTestResource;

import io.quarkus.test.junit.QuarkusTestProfile;

public class KeycloakQuarkusTestProfile implements QuarkusTestProfile {

    @Override
    public String getConfigProfile() {
        return "keycloak";
    }

    @Override
    public List<TestResourceEntry> testResources() {
        return Collections.singletonList(new TestResourceEntry(KeycloakQuarkusTestResource.class));
    }

    @Override
    public boolean disableGlobalTestResources() {
        return true;
    }
}
