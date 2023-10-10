package org.kie.kogito.jobs.service.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(KeycloakQuarkusTestProfile.class)
public class KeycloakPostgreSqlJobResourceTest extends BaseKeycloakJobServiceTest {

}
