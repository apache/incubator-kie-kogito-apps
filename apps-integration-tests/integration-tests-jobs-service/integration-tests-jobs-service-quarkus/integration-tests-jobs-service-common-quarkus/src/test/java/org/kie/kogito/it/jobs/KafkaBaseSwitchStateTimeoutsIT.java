package org.kie.kogito.it.jobs;

import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.kie.kogito.test.quarkus.kafka.KafkaTestClient;
import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;

import io.restassured.path.json.JsonPath;

import static org.kie.kogito.test.TestUtils.waitForEvent;

public class KafkaBaseSwitchStateTimeoutsIT extends BaseSwitchStateTimeoutsIT {

    private KafkaTestClient kafkaClient;
    private static final String KOGITO_OUTGOING_STREAM_TOPIC = "kogito-sw-out-events";

    @BeforeEach
    void setup() {
        String kafkaBootstrapServers = ConfigProvider.getConfig().getValue(KafkaQuarkusTestResource.KOGITO_KAFKA_PROPERTY, String.class);
        kafkaClient = new KafkaTestClient(kafkaBootstrapServers);
    }

    @AfterEach
    void cleanUp() {
        kafkaClient.shutdown();
    }

    @Override
    protected void verifyNoDecisionEventWasProduced(String processInstanceId) throws Exception {
        JsonPath result = waitForEvent(kafkaClient, KOGITO_OUTGOING_STREAM_TOPIC, 50);
        assertDecisionEvent(result, processInstanceId, PROCESS_RESULT_EVENT_TYPE, DECISION_NO_DECISION);
    }
}
