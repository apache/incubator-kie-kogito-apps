package org.kie.kogito.index.service.test;

import java.util.Arrays;
import java.util.List;

import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;
import org.kie.kogito.testcontainers.quarkus.OracleSqlQuarkusTestResource;

import io.quarkus.test.junit.QuarkusTestProfile;

public class KafkaMessageTestProfile implements QuarkusTestProfile {

    @Override
    public List<TestResourceEntry> testResources() {
        return Arrays.asList(
                new TestResourceEntry(OracleSqlQuarkusTestResource.class),
                new TestResourceEntry(KafkaQuarkusTestResource.class));
    }

    @Override
    public String getConfigProfile() {
        return "kafka-events-support";
    }
}
