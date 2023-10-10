package org.kie.kogito.it.jobs;

import org.kie.kogito.test.resources.JobServiceTestResource;

import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
@JobServiceTestResource(kafkaEnabled = true)
class WorkflowTimeoutsIT extends BaseWorkflowTimeoutsIT {
}
