package org.kie.kogito.index.service;

import org.kie.kogito.index.service.test.InMemoryMessageTestProfile;
import org.kie.kogito.index.test.TestUtils;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(InMemoryMessageTestProfile.class)
class MongoDomainIndexingServiceIT extends AbstractDomainIndexingServiceIT {

    @Override
    protected String getProcessProtobufFileContent() throws Exception {
        return TestUtils.readFileContent("travels-mongo.proto");
    }

    @Override
    protected String getUserTaskProtobufFileContent() throws Exception {
        return TestUtils.readFileContent("deals-mongo.proto");
    }
}
