package org.kie.kogito.index.service.test;

import java.util.Arrays;
import java.util.List;

import io.quarkus.test.junit.QuarkusTestProfile;

public class InMemoryMessageTestProfile implements QuarkusTestProfile {

    @Override
    public List<TestResourceEntry> testResources() {
        return Arrays.asList(new TestResourceEntry(InMemoryMessagingTestResource.class));
    }
}
