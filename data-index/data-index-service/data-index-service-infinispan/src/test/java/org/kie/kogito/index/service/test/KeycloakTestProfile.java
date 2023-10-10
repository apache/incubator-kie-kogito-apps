package org.kie.kogito.index.service.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.kogito.testcontainers.quarkus.InfinispanQuarkusTestResource;
import org.kie.kogito.testcontainers.quarkus.KeycloakQuarkusTestResource;

import io.quarkus.test.junit.QuarkusTestProfile;

import static java.util.Collections.singletonMap;
import static org.kie.kogito.testcontainers.quarkus.KeycloakQuarkusTestResource.KOGITO_OIDC_TENANTS;

public class KeycloakTestProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        Map<String, String> config = new HashMap<>();
        config.put("quarkus.http.auth.policy.role-policy1.roles-allowed", "confidential");
        config.put("quarkus.http.auth.permission.roles1.paths", "/*");
        config.put("quarkus.http.auth.permission.roles1.policy", "role-policy1");
        return config;
    }

    @Override
    public String getConfigProfile() {
        return "keycloak-test";
    }

    @Override
    public List<TestResourceEntry> testResources() {
        Map<String, String> args = singletonMap(KOGITO_OIDC_TENANTS, "web-app-tenant");
        return Arrays.asList(
                new TestResourceEntry(InfinispanQuarkusTestResource.class, Collections.emptyMap(), true),
                new TestResourceEntry(InMemoryMessagingTestResource.class, Collections.emptyMap(), true),
                new TestResourceEntry(KeycloakQuarkusTestResource.class, args, true));
    }

}
