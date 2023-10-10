package org.kie.kogito.index.service.cache;

import org.kie.kogito.index.service.test.InMemoryMessageTestProfile;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(InMemoryMessageTestProfile.class)
class InfinispanQueryIT extends AbstractQueryIT {

}
