package org.kie.kogito.index.service.messaging;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.kie.kogito.test.quarkus.kafka.KafkaTestClient;
import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;

import static org.kie.kogito.index.test.TestUtils.readFileContent;

public abstract class AbstractDomainMessagingKafkaConsumerIT extends AbstractDomainMessagingConsumerIT {

    @ConfigProperty(name = KafkaQuarkusTestResource.KOGITO_KAFKA_PROPERTY, defaultValue = "localhost:9092")
    public String kafkaBootstrapServers;

    KafkaTestClient kafkaClient;

    @BeforeEach
    void setup() throws Exception {
        kafkaClient = new KafkaTestClient(kafkaBootstrapServers);
        super.setup();
    }

    @AfterEach
    void close() {
        if (kafkaClient != null) {
            kafkaClient.shutdown();
        }
        super.close();
    }

    protected void sendUserTaskInstanceEvent() throws Exception {
        send("user_task_instance_event.json", "kogito-usertaskinstances-events");
    }

    protected void sendProcessInstanceEvent() throws Exception {
        send("process_instance_event.json", "kogito-processinstances-events");
    }

    private void send(String file, String topic) throws Exception {
        String json = readFileContent(file);
        kafkaClient.produce(json, topic);
    }

    protected abstract String getTestProtobufFileContent() throws Exception;
}
