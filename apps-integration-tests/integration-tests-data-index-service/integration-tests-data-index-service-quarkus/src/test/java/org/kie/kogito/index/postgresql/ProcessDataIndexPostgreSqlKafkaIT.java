package org.kie.kogito.index.postgresql;

import org.kie.kogito.index.quarkus.kafka.PostgreSqlKafkaTestProfile;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusIntegrationTest
@TestProfile(PostgreSqlKafkaTestProfile.class)
public class ProcessDataIndexPostgreSqlKafkaIT extends AbstractProcessDataIndexPostgreSqlIT {
}
