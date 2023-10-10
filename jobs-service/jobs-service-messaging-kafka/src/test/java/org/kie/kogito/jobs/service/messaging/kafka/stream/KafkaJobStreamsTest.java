package org.kie.kogito.jobs.service.messaging.kafka.stream;

import java.util.Optional;

import org.kie.kogito.jobs.service.stream.AbstractJobStreamsTest;

class KafkaJobStreamsTest extends AbstractJobStreamsTest<KafkaJobStreams> {

    @Override
    protected KafkaJobStreams createJobStreams() {
        return new KafkaJobStreams(objectMapper, Optional.of(true), emitter, URL);
    }
}
