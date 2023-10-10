package org.kie.kogito.it.jobs;

import org.kie.kogito.test.resources.JobServiceTestResource;

import io.quarkus.test.junit.QuarkusIntegrationTest;

import static org.kie.kogito.test.resources.JobServiceCompositeQuarkusTestResource.DATA_INDEX_SERVICE_URL;
import static org.kie.kogito.test.resources.JobServiceCompositeQuarkusTestResource.JOBS_SERVICE_URL;

@QuarkusIntegrationTest
@JobServiceTestResource(kafkaEnabled = true, dataIndexEnabled = true)
class ProcessTimerIT extends BaseProcessTimerIT {
    @Override
    public String jobServiceUrl() {
        return System.getProperty(JOBS_SERVICE_URL);
    }

    @Override
    public String dataIndexUrl() {
        String url = System.getProperty(DATA_INDEX_SERVICE_URL);
        return url;
    }
}
