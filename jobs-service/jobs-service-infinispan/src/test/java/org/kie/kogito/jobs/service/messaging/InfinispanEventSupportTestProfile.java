package org.kie.kogito.jobs.service.messaging;

import java.util.ArrayList;
import java.util.List;

public class InfinispanEventSupportTestProfile extends BaseEventsSupportTestProfile {

    @Override
    public List<TestResourceEntry> testResources() {
        List<TestResourceEntry> resources = new ArrayList<>(super.testResources());
        return resources;
    }
}
