package org.kie.kogito.jobs.service.messaging;

import java.util.ArrayList;
import java.util.List;

import org.kie.kogito.testcontainers.quarkus.PostgreSqlQuarkusTestResource;

public class PostgreSqlEventSupportTestProfile extends BaseEventsSupportTestProfile {

    @Override
    public List<TestResourceEntry> testResources() {
        List<TestResourceEntry> resources = new ArrayList<>(super.testResources());
        resources.add(new TestResourceEntry(PostgreSqlQuarkusTestResource.class));
        return resources;
    }
}
