package org.kie.kogito.it.jobs;

import org.kie.kogito.test.resources.JobServiceTestResource;
import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
@QuarkusTestResource(KafkaQuarkusTestResource.class)
@JobServiceTestResource(knativeEventingEnabled = true)
class CallbackStateTimeoutsIT extends BaseCallbackStateTimeoutsIT {
}
