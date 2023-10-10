package org.kie.kogito.index.service.messaging;

import org.kie.kogito.index.service.test.KafkaMessageTestProfile;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(KafkaMessageTestProfile.class)
class MongoDBMessagingKafkaConsumerIT extends AbstractMessagingKafkaConsumerIT {

}
