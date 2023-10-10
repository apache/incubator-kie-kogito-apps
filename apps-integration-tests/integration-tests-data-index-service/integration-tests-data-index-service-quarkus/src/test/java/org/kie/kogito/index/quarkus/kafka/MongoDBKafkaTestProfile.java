package org.kie.kogito.index.quarkus.kafka;

import java.util.Arrays;
import java.util.List;

import org.kie.kogito.index.test.quarkus.kafka.DataIndexMongoDBQuarkusKafkaTestResource;

import io.quarkus.test.junit.QuarkusTestProfile;

public class MongoDBKafkaTestProfile implements QuarkusTestProfile {

    @Override
    public List<TestResourceEntry> testResources() {
        return Arrays.asList(new TestResourceEntry(KogitoServiceRandomPortQuarkusKafkaTestResource.class),
                new TestResourceEntry(DataIndexMongoDBQuarkusKafkaTestResource.class));
    }

}
