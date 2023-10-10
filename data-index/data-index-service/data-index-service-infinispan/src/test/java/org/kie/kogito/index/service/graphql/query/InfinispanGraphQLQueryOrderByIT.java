package org.kie.kogito.index.service.graphql.query;

import org.kie.kogito.index.service.test.InMemoryMessageTestProfile;
import org.kie.kogito.index.test.TestUtils;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(InMemoryMessageTestProfile.class)
class InfinispanGraphQLQueryOrderByIT extends AbstractGraphQLQueryOrderByIT {

    @Override
    protected String getTestProtobufFileContent() throws Exception {
        return TestUtils.getTravelsProtoBufferFile();
    }
}
