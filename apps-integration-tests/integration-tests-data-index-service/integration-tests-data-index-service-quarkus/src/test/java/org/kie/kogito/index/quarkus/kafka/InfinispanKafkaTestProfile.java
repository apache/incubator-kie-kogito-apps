package org.kie.kogito.index.quarkus.kafka;

import java.util.Arrays;
import java.util.List;

import org.kie.kogito.index.test.quarkus.kafka.DataIndexInfinispanQuarkusKafkaTestResource;

import io.quarkus.test.junit.QuarkusTestProfile;

public class InfinispanKafkaTestProfile implements QuarkusTestProfile {

    @Override
    public List<TestResourceEntry> testResources() {
        return Arrays.asList(new TestResourceEntry(KogitoServiceRandomPortQuarkusKafkaTestResource.class),
                new TestResourceEntry(DataIndexInfinispanQuarkusKafkaTestResource.class));
    }
}
