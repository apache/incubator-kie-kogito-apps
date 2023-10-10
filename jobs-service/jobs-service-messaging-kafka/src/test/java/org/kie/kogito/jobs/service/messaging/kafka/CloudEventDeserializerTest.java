package org.kie.kogito.jobs.service.messaging.kafka;

import java.net.URI;

import org.junit.jupiter.api.Test;

import io.cloudevents.CloudEvent;
import io.quarkus.test.junit.QuarkusTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.kogito.jobs.service.TestUtils.readFileContent;

@QuarkusTest
class CloudEventDeserializerTest {

    private static final String CLOUD_EVENT_RESOURCE = "org/kie/kogito/jobs/service/messaging/kafka/CloudEvent.json";

    @Test
    void deserialize() throws Exception {
        CloudEventDeserializer deserializer = new CloudEventDeserializer();
        CloudEvent cloudEvent = deserializer.deserialize("topic", readFileContent(CLOUD_EVENT_RESOURCE));

        assertThat(cloudEvent).isNotNull();
        assertThat(cloudEvent.getSpecVersion()).hasToString("1.0");
        assertThat(cloudEvent.getId()).isEqualTo("eventId");
        assertThat(cloudEvent.getSource()).isEqualTo(URI.create("http://event_source"));
        assertThat(cloudEvent.getType()).isEqualTo("eventType");
        assertThat(cloudEvent.getDataSchema()).isEqualTo(URI.create("http://event_data_schema/schema.json"));
        assertThat(cloudEvent.getDataContentType()).isEqualTo("application/json; charset=utf-8");
        assertThat(cloudEvent.getSubject()).isEqualTo("eventSubject");
        assertThat(cloudEvent.getData()).isNotNull();
        assertThat(cloudEvent.getData().toBytes()).isEqualTo("{\"dataField1\":\"eventData1\",\"dataField2\":\"eventData2\"}".getBytes());
        assertThat(cloudEvent.getExtensionNames()).containsExactlyInAnyOrder("extension1", "extension2");
        assertThat(cloudEvent.getExtension("extension1")).isEqualTo("eventExtension1");
        assertThat(cloudEvent.getExtension("extension2")).isEqualTo("eventExtension2");
    }
}
