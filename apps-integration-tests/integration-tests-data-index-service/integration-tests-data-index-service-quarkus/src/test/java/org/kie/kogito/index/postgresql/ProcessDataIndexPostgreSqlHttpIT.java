package org.kie.kogito.index.postgresql;

import org.kie.kogito.index.quarkus.http.PostgreSqlHttpTestProfile;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusIntegrationTest
@TestProfile(PostgreSqlHttpTestProfile.class)
public class ProcessDataIndexPostgreSqlHttpIT extends AbstractProcessDataIndexPostgreSqlIT {
}
