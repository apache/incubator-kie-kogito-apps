package org.kie.kogito.jobs.service.messaging.http.stream;

import java.util.Optional;
import java.util.function.Supplier;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.stream.AbstractJobStreams;
import org.kie.kogito.jobs.service.stream.AvailableStreams;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cloudevents.jackson.JsonFormat;
import io.quarkus.reactivemessaging.http.runtime.OutgoingHttpMetadata;

@ApplicationScoped
public class HttpJobStreams extends AbstractJobStreams {

    public static final String PUBLISH_EVENTS_CONFIG_KEY = "kogito.jobs-service.http.job-status-change-events";
    public static final String JOB_STATUS_CHANGE_EVENTS_HTTP = "kogito-job-service-job-status-events-http";

    /**
     * Metadata to include the content-type for structured CloudEvents messages
     */
    public static final Supplier<OutgoingHttpMetadata> OUTGOING_HTTP_METADATA = () -> new OutgoingHttpMetadata.Builder()
            .addHeader(HttpHeaders.CONTENT_TYPE, JsonFormat.CONTENT_TYPE)
            .build();

    @Inject
    public HttpJobStreams(ObjectMapper objectMapper,
            @ConfigProperty(name = PUBLISH_EVENTS_CONFIG_KEY) Optional<Boolean> config,
            @Channel(JOB_STATUS_CHANGE_EVENTS_HTTP) @OnOverflow(value = OnOverflow.Strategy.LATEST) Emitter<String> emitter,
            @ConfigProperty(name = "kogito.service.url", defaultValue = "http://localhost:8080") String url) {
        super(objectMapper, config.orElse(false), emitter, url);
    }

    @Incoming(AvailableStreams.JOB_STATUS_CHANGE_EVENTS)
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    @Override
    public void jobStatusChange(JobDetails job) {
        super.jobStatusChange(job);
    }

    @Override
    protected Message<String> decorate(Message<String> message) {
        return message.addMetadata(OUTGOING_HTTP_METADATA.get());
    }
}
