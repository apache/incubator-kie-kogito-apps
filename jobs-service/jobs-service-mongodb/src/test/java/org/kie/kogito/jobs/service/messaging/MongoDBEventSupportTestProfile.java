package org.kie.kogito.jobs.service.messaging;

import java.util.ArrayList;
import java.util.List;

import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import io.quarkus.test.junit.QuarkusTestProfile;

public class MongoDBEventSupportTestProfile extends BaseEventsSupportTestProfile {

    @Override
    public List<QuarkusTestProfile.TestResourceEntry> testResources() {
        List<QuarkusTestProfile.TestResourceEntry> resources = new ArrayList<>(super.testResources());
        resources.add(new QuarkusTestProfile.TestResourceEntry(MongoDBQuarkusTestResource.class));
        return resources;
    }
}
