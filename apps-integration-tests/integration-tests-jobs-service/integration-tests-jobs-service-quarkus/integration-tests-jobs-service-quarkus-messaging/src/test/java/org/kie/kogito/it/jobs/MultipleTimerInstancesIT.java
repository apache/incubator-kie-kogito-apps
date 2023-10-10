package org.kie.kogito.it.jobs;

import org.kie.kogito.test.resources.JobServiceTestResource;

import io.quarkus.test.junit.QuarkusIntegrationTest;

import static org.kie.kogito.test.resources.JobServiceCompositeQuarkusTestResource.JOBS_SERVICE_URL;

@QuarkusIntegrationTest
@JobServiceTestResource(kafkaEnabled = true)
class MultipleTimerInstancesIT extends BaseMultipleTimerInstancesQuarkusIT {

    @Override
    public String jobServiceUrl() {
        return System.getProperty(JOBS_SERVICE_URL);
    }
}
