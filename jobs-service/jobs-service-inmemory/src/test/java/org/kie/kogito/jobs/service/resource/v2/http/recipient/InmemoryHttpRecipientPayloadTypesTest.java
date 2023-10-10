package org.kie.kogito.jobs.service.resource.v2.http.recipient;

import org.kie.kogito.jobs.service.resource.v2.ExternalResourcesMock;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(ExternalResourcesMock.class)
class InmemoryHttpRecipientPayloadTypesTest extends BaseHttpRecipientPayloadTypesTest {
}
