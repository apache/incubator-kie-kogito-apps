package org.kie.kogito.trusty.service.common.messaging.outgoing;

import java.net.URI;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.kie.kogito.event.cloudevents.utils.CloudEventUtils;
import org.kie.kogito.explainability.api.BaseExplainabilityRequest;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;

@ApplicationScoped
public class ExplainabilityRequestProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExplainabilityRequestProducer.class);

    private static final URI URI_PRODUCER = URI.create("trustyService/ExplainabilityRequestProducer");

    private final BroadcastProcessor<String> eventSubject = BroadcastProcessor.create();

    public void sendEvent(BaseExplainabilityRequest request) {
        LOGGER.info("Sending explainability request with id {}", request.getExecutionId());
        Optional<String> optPayload = CloudEventUtils
                .build(request.getExecutionId(), URI_PRODUCER, request, BaseExplainabilityRequest.class)
                .flatMap(CloudEventUtils::encode);
        if (optPayload.isPresent()) {
            eventSubject.onNext(optPayload.get());
        } else {
            LOGGER.warn("Ignoring empty CloudEvent");
        }
    }

    @Outgoing("trusty-explainability-request")
    public Publisher<String> getEventPublisher() {
        return eventSubject.toHotStream();
    }
}
