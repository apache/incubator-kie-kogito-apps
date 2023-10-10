package org.kie.kogito.jobs.service.messaging.kafka;

import io.cloudevents.CloudEvent;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class CloudEventDeserializer extends ObjectMapperDeserializer<CloudEvent> {

    public CloudEventDeserializer() {
        super(CloudEvent.class);
    }
}
