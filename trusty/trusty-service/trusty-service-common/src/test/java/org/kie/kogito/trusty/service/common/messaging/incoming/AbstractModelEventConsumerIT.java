package org.kie.kogito.trusty.service.common.messaging.incoming;

import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.test.quarkus.kafka.KafkaTestClient;
import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;
import org.kie.kogito.trusty.service.common.TrustyService;
import org.kie.kogito.trusty.service.common.TrustyServiceTestUtils;
import org.kie.kogito.trusty.storage.api.model.decision.DMNModelWithMetadata;
import org.kie.kogito.trusty.storage.common.TrustyStorageService;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class AbstractModelEventConsumerIT {

    @Inject
    TrustyService trustyService;

    @Inject
    TrustyStorageService trustyStorageService;

    KafkaTestClient kafkaClient;

    @ConfigProperty(name = KafkaQuarkusTestResource.KOGITO_KAFKA_PROPERTY)
    String kafkaBootstrapServers;

    @BeforeEach
    public void setup() {
        trustyStorageService.getModelStorage(DMNModelWithMetadata.class).clear();
        kafkaClient = new KafkaTestClient(kafkaBootstrapServers);
    }

    @AfterEach
    public void tearDown() {
        if (kafkaClient != null) {
            kafkaClient.shutdown();
        }
    }

    @Test
    void testCorrectCloudEvent() {
        kafkaClient.produce(TrustyServiceTestUtils.buildCloudEventJsonString(TrustyServiceTestUtils.buildCorrectModelEvent()),
                KafkaConstants.KOGITO_TRACING_MODEL_TOPIC);
        await()
                .atMost(5, SECONDS)
                .untilAsserted(() -> assertDoesNotThrow(() -> trustyService.getModelById(TrustyServiceTestUtils.getModelIdentifier(), DMNModelWithMetadata.class)));

        DMNModelWithMetadata storedDefinition =
                trustyService.getModelById(TrustyServiceTestUtils.getModelIdentifier(), DMNModelWithMetadata.class);
        assertNotNull(storedDefinition);
        assertEquals("definition", storedDefinition.getModel());
        assertEquals("name", storedDefinition.getName());
        assertEquals("namespace", storedDefinition.getNamespace());
    }
}
