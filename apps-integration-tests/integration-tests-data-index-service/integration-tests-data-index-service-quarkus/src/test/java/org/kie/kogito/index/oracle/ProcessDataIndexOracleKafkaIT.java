package org.kie.kogito.index.oracle;

import org.kie.kogito.index.quarkus.kafka.OracleKafkaTestProfile;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusIntegrationTest
@TestProfile(OracleKafkaTestProfile.class)
public class ProcessDataIndexOracleKafkaIT extends AbstractProcessDataIndexOracleIT {
}
