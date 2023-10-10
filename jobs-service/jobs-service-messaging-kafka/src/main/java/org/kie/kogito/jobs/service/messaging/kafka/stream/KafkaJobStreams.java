package org.kie.kogito.jobs.service.messaging.kafka.stream;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.stream.AbstractJobStreams;
import org.kie.kogito.jobs.service.stream.AvailableStreams;

import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class KafkaJobStreams extends AbstractJobStreams {

    public static final String PUBLISH_EVENTS_CONFIG_KEY = "kogito.jobs-service.kafka.job-status-change-events";

    @Inject
    public KafkaJobStreams(ObjectMapper objectMapper,
            @ConfigProperty(name = PUBLISH_EVENTS_CONFIG_KEY) Optional<Boolean> config,
            @Channel(AvailableStreams.JOB_STATUS_CHANGE_EVENTS_TOPIC) @OnOverflow(value = OnOverflow.Strategy.LATEST) Emitter<String> emitter,
            @ConfigProperty(name = "kogito.service.url", defaultValue = "http://localhost:8080") String url) {
        super(objectMapper, config.orElse(false), emitter, url);
    }

    @Incoming(AvailableStreams.JOB_STATUS_CHANGE_EVENTS)
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    @Override
    public void jobStatusChange(JobDetails job) {
        super.jobStatusChange(job);
    }
}
