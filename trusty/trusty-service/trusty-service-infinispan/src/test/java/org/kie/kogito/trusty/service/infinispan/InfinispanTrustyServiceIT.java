package org.kie.kogito.trusty.service.infinispan;

import org.kie.kogito.testcontainers.quarkus.InfinispanQuarkusTestResource;
import org.kie.kogito.trusty.service.common.AbstractTrustyServiceIT;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(InfinispanQuarkusTestResource.class)
public class InfinispanTrustyServiceIT extends AbstractTrustyServiceIT {

}
