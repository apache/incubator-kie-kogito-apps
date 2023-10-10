package org.kie.kogito.index.infinispan;

import org.kie.kogito.index.quarkus.kafka.InfinispanKafkaTestProfile;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusIntegrationTest
@TestProfile(InfinispanKafkaTestProfile.class)
public class ProcessDataIndexInfinispanKafkaIT extends AbstractProcessDataIndexInfinispanIT {
}
