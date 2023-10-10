package org.kie.kogito.index.service.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import io.quarkus.test.junit.QuarkusTestProfile;

public class InMemoryMessageTestProfile implements QuarkusTestProfile {

    @Override
    public List<TestResourceEntry> testResources() {
        return Arrays.asList(
                new TestResourceEntry(InMemoryMessagingTestResource.class, Collections.emptyMap(), true),
                new TestResourceEntry(MongoDBQuarkusTestResource.class, Collections.emptyMap(), true));
    }
}
