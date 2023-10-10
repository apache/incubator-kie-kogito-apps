package org.kie.kogito.index.mongodb;

import org.kie.kogito.index.quarkus.kafka.MongoDBKafkaTestProfile;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusIntegrationTest
@TestProfile(MongoDBKafkaTestProfile.class)
public class ProcessDataIndexMongoDBKafkaIT extends AbstractProcessDataIndexMongoDBIT {
}
