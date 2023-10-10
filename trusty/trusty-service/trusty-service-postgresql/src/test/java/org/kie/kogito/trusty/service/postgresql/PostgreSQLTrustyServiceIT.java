package org.kie.kogito.trusty.service.postgresql;

import org.kie.kogito.testcontainers.quarkus.PostgreSqlQuarkusTestResource;
import org.kie.kogito.trusty.service.common.AbstractTrustyServiceIT;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(PostgreSqlQuarkusTestResource.class)
public class PostgreSQLTrustyServiceIT extends AbstractTrustyServiceIT {

}
