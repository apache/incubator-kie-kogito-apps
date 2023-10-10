package org.kie.kogito.it.jobs;

import org.kie.kogito.test.resources.JobServiceTestResource;
import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;

import static org.kie.kogito.test.resources.JobServiceCompositeQuarkusTestResource.JOBS_SERVICE_URL;

@QuarkusIntegrationTest
@QuarkusTestResource(KafkaQuarkusTestResource.class)
@JobServiceTestResource
class ProcessTimerIT extends BaseProcessTimerIT {

    @Override
    public String jobServiceUrl() {
        return System.getProperty(JOBS_SERVICE_URL);
    }
}
