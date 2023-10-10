package org.kie.kogito.index.service;

import org.kie.kogito.index.service.test.KeycloakTestProfile;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(KeycloakTestProfile.class)
class KeycloakOracleIndexingServiceIT extends AbstractKeycloakIntegrationIndexingServiceIT {

}
