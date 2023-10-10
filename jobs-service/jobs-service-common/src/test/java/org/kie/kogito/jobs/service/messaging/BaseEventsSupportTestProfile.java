package org.kie.kogito.jobs.service.messaging;

import java.util.Collections;
import java.util.List;

import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;

import io.quarkus.test.junit.QuarkusTestProfile;

public abstract class BaseEventsSupportTestProfile implements QuarkusTestProfile {

    @Override
    public String getConfigProfile() {
        return "kafka-events-support";
    }

    @Override
    public List<TestResourceEntry> testResources() {
        return Collections.singletonList(new TestResourceEntry(KafkaQuarkusTestResource.class));
    }
}
