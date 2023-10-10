package org.kie.kogito.jobs.service.resource;

import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
public class MongoDBJobResourceTest extends BaseJobResourceTest {

}
