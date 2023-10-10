package org.kie.kogito.it.jobs;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.RestAssured;

@QuarkusIntegrationTest
class MultipleTimerInstancesIT extends BaseMultipleTimerInstancesQuarkusIT {

    @Override
    public String jobServiceUrl() {
        return RestAssured.baseURI;
    }
}
