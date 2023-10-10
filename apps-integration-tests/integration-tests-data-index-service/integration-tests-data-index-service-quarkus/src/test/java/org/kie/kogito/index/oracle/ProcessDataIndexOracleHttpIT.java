package org.kie.kogito.index.oracle;

import org.kie.kogito.index.quarkus.http.OracleHttpTestProfile;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusIntegrationTest
@TestProfile(OracleHttpTestProfile.class)
public class ProcessDataIndexOracleHttpIT extends AbstractProcessDataIndexOracleIT {
}
