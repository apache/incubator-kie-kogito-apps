package org.kie.kogito.trusty.service.infinispan.messaging;

import org.kie.kogito.testcontainers.quarkus.InfinispanQuarkusTestResource;
import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;
import org.kie.kogito.trusty.service.common.messaging.incoming.AbstractTraceEventConsumerIT;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(InfinispanQuarkusTestResource.class)
@QuarkusTestResource(KafkaQuarkusTestResource.class)
public class InfinispanTraceEventConsumerIT extends AbstractTraceEventConsumerIT {

}
