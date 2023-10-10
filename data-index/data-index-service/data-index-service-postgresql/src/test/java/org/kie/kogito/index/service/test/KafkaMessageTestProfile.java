package org.kie.kogito.index.service.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;
import org.kie.kogito.testcontainers.quarkus.PostgreSqlQuarkusTestResource;

import io.quarkus.test.junit.QuarkusTestProfile;

public class KafkaMessageTestProfile implements QuarkusTestProfile {

    @Override
    public List<TestResourceEntry> testResources() {
        return Arrays.asList(
                new TestResourceEntry(PostgreSqlQuarkusTestResource.class, Collections.emptyMap(), true),
                new TestResourceEntry(KafkaQuarkusTestResource.class, Collections.emptyMap(), true));
    }

    @Override
    public String getConfigProfile() {
        return "kafka-events-support";
    }
}
