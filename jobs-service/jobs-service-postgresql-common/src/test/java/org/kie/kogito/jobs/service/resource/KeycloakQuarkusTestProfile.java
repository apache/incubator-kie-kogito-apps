package org.kie.kogito.jobs.service.resource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.kie.kogito.testcontainers.quarkus.KeycloakQuarkusTestResource;
import org.kie.kogito.testcontainers.quarkus.PostgreSqlQuarkusTestResource;

import io.quarkus.test.junit.QuarkusTestProfile;

public class KeycloakQuarkusTestProfile implements QuarkusTestProfile {

    @Override
    public String getConfigProfile() {
        return "keycloak";
    }

    @Override
    public List<TestResourceEntry> testResources() {
        return Arrays.asList(
                new TestResourceEntry(KeycloakQuarkusTestResource.class, Collections.emptyMap(), true),
                new TestResourceEntry(PostgreSqlQuarkusTestResource.class, Collections.emptyMap(), true));
    }

    @Override
    public boolean disableGlobalTestResources() {
        return true;
    }
}
