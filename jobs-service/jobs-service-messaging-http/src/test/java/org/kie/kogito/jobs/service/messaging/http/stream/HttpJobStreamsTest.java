package org.kie.kogito.jobs.service.messaging.http.stream;

import java.util.Optional;

import javax.ws.rs.core.HttpHeaders;

import org.eclipse.microprofile.reactive.messaging.Message;
import org.kie.kogito.jobs.service.stream.AbstractJobStreamsTest;

import io.cloudevents.jackson.JsonFormat;
import io.quarkus.reactivemessaging.http.runtime.OutgoingHttpMetadata;

import static org.assertj.core.api.Assertions.assertThat;

class HttpJobStreamsTest extends AbstractJobStreamsTest<HttpJobStreams> {

    @Override
    protected HttpJobStreams createJobStreams() {
        return new HttpJobStreams(objectMapper, Optional.of(true), emitter, AbstractJobStreamsTest.URL);
    }

    @Override
    protected void assertExpectedMetadata(Message<String> message) {
        OutgoingHttpMetadata metadata = message.getMetadata(OutgoingHttpMetadata.class).orElse(null);
        assertThat(metadata).isNotNull();
        assertThat(metadata.getHeaders()).hasSize(1);
        assertThat(metadata.getHeaders().get(HttpHeaders.CONTENT_TYPE)).containsExactlyInAnyOrder(JsonFormat.CONTENT_TYPE);
    }
}
