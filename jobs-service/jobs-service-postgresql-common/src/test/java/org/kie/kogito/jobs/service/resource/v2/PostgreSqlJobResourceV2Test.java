package org.kie.kogito.jobs.service.resource.v2;

import org.kie.kogito.testcontainers.quarkus.PostgreSqlQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(PostgreSqlQuarkusTestResource.class)
class PostgreSqlJobResourceV2Test extends BaseJobResourceV2Test {

}
