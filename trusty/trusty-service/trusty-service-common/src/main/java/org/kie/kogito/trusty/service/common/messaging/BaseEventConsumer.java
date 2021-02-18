/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.trusty.service.common.messaging;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.kie.kogito.cloudevents.CloudEventUtils;
import org.kie.kogito.trusty.service.common.TrustyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseEventConsumer<E> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseEventConsumer.class);

    protected final TrustyService service;
    private final ObjectMapper mapper;

    protected BaseEventConsumer() {
        this(null, null);
    }

    public BaseEventConsumer(final TrustyService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    protected CompletionStage<Void> handleMessage(final Message<String> message) {
        try {
            CloudEventUtils.decode(message.getPayload()).ifPresent(this::handleCloudEvent);
        } catch (Exception e) {
            LOG.error("Something unexpected happened during the processing of an Event. The event is discarded.", e);
        }
        return message.ack();
    }

    protected void handleCloudEvent(final CloudEvent cloudEvent) {
        final E payload;
        try {
            payload = mapper.readValue(cloudEvent.getData(), getEventType());
        } catch (IOException e) {
            LOG.error("Unable to deserialize CloudEvent data as " + getEventType().getType().getTypeName(), e);
            return;
        }
        if (payload == null) {
            LOG.error("Received CloudEvent with id {} from {} with empty data", cloudEvent.getId(), cloudEvent.getSource());
            return;
        }
        LOG.debug("Received CloudEvent with id {} from {}", cloudEvent.getId(), cloudEvent.getSource());
        internalHandleCloudEvent(cloudEvent, payload);
    }

    protected abstract TypeReference<E> getEventType();

    protected abstract void internalHandleCloudEvent(final CloudEvent cloudEvent, final E payload);
}
