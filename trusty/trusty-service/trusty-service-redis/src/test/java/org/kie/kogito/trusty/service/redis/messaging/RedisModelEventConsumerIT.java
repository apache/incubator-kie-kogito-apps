package org.kie.kogito.trusty.service.redis.messaging;

import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;
import org.kie.kogito.testcontainers.quarkus.RedisQuarkusTestResource;
import org.kie.kogito.trusty.service.common.messaging.incoming.AbstractModelEventConsumerIT;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(RedisQuarkusTestResource.class)
@QuarkusTestResource(KafkaQuarkusTestResource.class)
public class RedisModelEventConsumerIT extends AbstractModelEventConsumerIT {

}
