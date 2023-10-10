package org.kie.kogito.trusty.service.postgresql.messaging;

import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;
import org.kie.kogito.testcontainers.quarkus.PostgreSqlQuarkusTestResource;
import org.kie.kogito.trusty.service.common.messaging.incoming.AbstractModelEventConsumerIT;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(PostgreSqlQuarkusTestResource.class)
@QuarkusTestResource(KafkaQuarkusTestResource.class)
public class PostgreSQLModelEventConsumerIT extends AbstractModelEventConsumerIT {

}
