package org.kie.kogito.trusty.service.common.messaging.incoming;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.test.quarkus.QuarkusTestProperty;
import org.kie.kogito.test.quarkus.kafka.KafkaTestClient;
import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;
import org.kie.kogito.trusty.service.common.TrustyService;
import org.kie.kogito.trusty.service.common.TrustyServiceTestUtils;
import org.kie.kogito.trusty.storage.api.model.decision.DMNModelWithMetadata;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@QuarkusTest
@QuarkusTestResource(KafkaQuarkusTestResource.class)
public class ModelEventConsumerIT {

    @QuarkusTestProperty(name = KafkaQuarkusTestResource.KOGITO_KAFKA_PROPERTY)
    String kafkaBootstrapServers;

    @InjectMock
    TrustyService trustyService;

    KafkaTestClient kafkaClient;

    @BeforeEach
    public void setup() {
        kafkaClient = new KafkaTestClient(kafkaBootstrapServers);
    }

    @AfterEach
    public void tearDown() {
        if (kafkaClient != null) {
            kafkaClient.shutdown();
        }
    }

    @Test
    public void eventLoopIsNotStoppedWithException() {
        doThrow(new RuntimeException("Something really bad"))
                .when(trustyService)
                .storeModel(any(DMNModelWithMetadata.class));

        kafkaClient.produce(TrustyServiceTestUtils.buildCloudEventJsonString(TrustyServiceTestUtils.buildCorrectModelEvent()),
                KafkaConstants.KOGITO_TRACING_MODEL_TOPIC);
        kafkaClient.produce(TrustyServiceTestUtils.buildCloudEventJsonString(TrustyServiceTestUtils.buildCorrectModelEvent()),
                KafkaConstants.KOGITO_TRACING_MODEL_TOPIC);

        verify(trustyService, timeout(3000).times(2))
                .storeModel(any(DMNModelWithMetadata.class));
    }
}
