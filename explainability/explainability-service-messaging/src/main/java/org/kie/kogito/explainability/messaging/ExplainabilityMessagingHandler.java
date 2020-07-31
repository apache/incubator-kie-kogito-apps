/*
 *  Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.kie.kogito.explainability.messaging;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.core.type.TypeReference;
import io.cloudevents.v1.AttributesImpl;
import io.cloudevents.v1.CloudEventImpl;
import io.reactivex.BackpressureStrategy;
import io.reactivex.subjects.PublishSubject;
import io.smallrye.reactive.messaging.annotations.Blocking;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.kie.kogito.explainability.IExplanationService;
import org.kie.kogito.explainability.api.ExplainabilityRequestDto;
import org.kie.kogito.explainability.api.ExplainabilityResultDto;
import org.kie.kogito.explainability.models.ExplainabilityRequest;
import org.kie.kogito.tracing.decision.event.CloudEventUtils;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ExplainabilityMessagingHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExplainabilityMessagingHandler.class);

    private static final URI URI_PRODUCER = URI.create("explainabilityService/ExplainabilityMessagingHandler");
    private static final TypeReference<CloudEventImpl<ExplainabilityRequestDto>> CLOUD_EVENT_TYPE = new TypeReference<>() {
    };
    private final PublishSubject<String> eventSubject = PublishSubject.create();
    private final IExplanationService service;

    @Inject
    ManagedExecutor executor;

    @Inject
    public ExplainabilityMessagingHandler(IExplanationService service) {
        this.service = service;
    }

    // Incoming
    @Incoming("trusty-explainability-request")
    public CompletionStage<Void> handleMessage(Message<String> message) {
        Optional<CloudEventImpl<ExplainabilityRequestDto>> cloudEventOpt = decodeCloudEvent(message.getPayload());
        if (!cloudEventOpt.isPresent()) {
            return message.ack();
        }

        CloudEventImpl<ExplainabilityRequestDto> cloudEvent = cloudEventOpt.get();
        return CompletableFuture
                .supplyAsync(() -> handleCloudEvent(cloudEvent), executor)
                .thenAccept(x -> message.ack());
    }

    private Optional<CloudEventImpl<ExplainabilityRequestDto>> decodeCloudEvent(String payload) {
        try {
            return Optional.of(CloudEventUtils.decode(payload, CLOUD_EVENT_TYPE));
        } catch (IllegalStateException e) {
            LOGGER.error(String.format("Can't decode message to CloudEvent: %s", payload), e);
            return Optional.empty();
        }
    }

    private CompletableFuture<Void> handleCloudEvent(CloudEventImpl<ExplainabilityRequestDto> cloudEvent) {
        AttributesImpl attributes = cloudEvent.getAttributes();
        Optional<ExplainabilityRequestDto> optData = cloudEvent.getData();

        if (!optData.isPresent()) {
            LOGGER.error("Received CloudEvent with id {} from {} with empty data", attributes.getId(), attributes.getSource());
            return CompletableFuture.completedFuture(null);
        }

        LOGGER.info("Received CloudEvent with id {} from {}", attributes.getId(), attributes.getSource());

        ExplainabilityRequestDto explainabilityResult = optData.get();

        return service
                .explainAsync(ExplainabilityRequest.from(explainabilityResult))
                .thenAcceptAsync(this::sendEvent, executor);
    }

    // Outgoing
    public CompletableFuture<Void> sendEvent(ExplainabilityResultDto result) {
        LOGGER.info("Explainability service emits explainability for execution with ID " + result.getExecutionId());
        String payload = CloudEventUtils.encode(
                CloudEventUtils.build(result.getExecutionId(),
                                      URI_PRODUCER,
                                      result,
                                      ExplainabilityResultDto.class)
        );
        eventSubject.onNext(payload);
        return CompletableFuture.completedFuture(null);
    }

    @Outgoing("trusty-explainability-result")
    public Publisher<String> getEventPublisher() {
        return eventSubject.toFlowable(BackpressureStrategy.BUFFER);
    }
}