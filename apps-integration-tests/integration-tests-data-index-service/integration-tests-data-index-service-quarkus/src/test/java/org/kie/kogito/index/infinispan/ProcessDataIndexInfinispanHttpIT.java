package org.kie.kogito.index.infinispan;

import org.kie.kogito.index.quarkus.http.InfinispanHttpTestProfile;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusIntegrationTest
@TestProfile(InfinispanHttpTestProfile.class)
public class ProcessDataIndexInfinispanHttpIT extends AbstractProcessDataIndexInfinispanIT {
}
