package org.kie.kogito.index.service.messaging;

import org.junit.jupiter.api.Disabled;
import org.kie.kogito.index.service.test.KafkaMessageTestProfile;
import org.kie.kogito.index.test.TestUtils;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(KafkaMessageTestProfile.class)
@Disabled
class InfinispanMessagingLoadKafkaIT extends AbstractMessagingLoadKafkaIT {

    @Override
    protected String getTestProtobufFileContent() throws Exception {
        return TestUtils.getTravelsProtoBufferFile();
    }
}
