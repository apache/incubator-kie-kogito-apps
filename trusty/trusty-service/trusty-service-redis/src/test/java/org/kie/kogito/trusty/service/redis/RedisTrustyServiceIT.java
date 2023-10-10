package org.kie.kogito.trusty.service.redis;

import org.kie.kogito.testcontainers.quarkus.RedisQuarkusTestResource;
import org.kie.kogito.trusty.service.common.AbstractTrustyServiceIT;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(RedisQuarkusTestResource.class)
public class RedisTrustyServiceIT extends AbstractTrustyServiceIT {

}
