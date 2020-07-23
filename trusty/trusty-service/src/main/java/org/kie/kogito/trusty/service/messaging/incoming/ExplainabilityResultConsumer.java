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

package org.kie.kogito.trusty.service.messaging.incoming;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.core.type.TypeReference;
import io.cloudevents.v1.AttributesImpl;
import io.cloudevents.v1.CloudEventImpl;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.kie.kogito.trusty.api.CloudEventUtils;
import org.kie.kogito.trusty.api.ExplainabilityResultDto;
import org.kie.kogito.trusty.service.ITrustyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ExplainabilityResultConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExplainabilityResultConsumer.class);

    private static final TypeReference<CloudEventImpl<ExplainabilityResultDto>> CLOUD_EVENT_TYPE =  new TypeReference<>() {};

    private final ITrustyService service;

    @Inject
    public ExplainabilityResultConsumer(ITrustyService service) {
        this.service = service;
    }

    @Incoming("trusty-explainability-result")
    public CompletionStage<Void> handleMessage(Message<String> message) {
        decodeCloudEvent(message.getPayload()).ifPresent(this::handleCloudEvent);
        return message.ack();
    }

    private Optional<CloudEventImpl<ExplainabilityResultDto>> decodeCloudEvent(String payload) {
        try {
            return Optional.of(CloudEventUtils.decode(payload, CLOUD_EVENT_TYPE));
        } catch (IllegalStateException e) {
            LOGGER.error(String.format("Can't decode message to CloudEvent: %s", payload), e);
            return Optional.empty();
        }
    }

    private void handleCloudEvent(CloudEventImpl<ExplainabilityResultDto> cloudEvent) {
        AttributesImpl attributes = cloudEvent.getAttributes();
        Optional<ExplainabilityResultDto> optData = cloudEvent.getData();

        if (!optData.isPresent()) {
            LOGGER.error("Received CloudEvent with id {} from {} with empty data", attributes.getId(), attributes.getSource());
            return;
        }

        LOGGER.info("Received CloudEvent with id {} from {}", attributes.getId(), attributes.getSource());

        ExplainabilityResultDto explainabilityResult = optData.get();

        service.storeExplainability(attributes.getId(), ExplainabilityResultConverter.toResult(explainabilityResult));
    }
}

