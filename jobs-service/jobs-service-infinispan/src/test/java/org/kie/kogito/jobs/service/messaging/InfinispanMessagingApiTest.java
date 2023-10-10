package org.kie.kogito.jobs.service.messaging;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(InfinispanEventSupportTestProfile.class)
public class InfinispanMessagingApiTest extends BaseMessagingApiTest {

}
