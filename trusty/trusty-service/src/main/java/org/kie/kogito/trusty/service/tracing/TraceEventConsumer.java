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

package org.kie.kogito.trusty.service.tracing;

import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;

import io.cloudevents.v1.CloudEventImpl;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.kie.kogito.tracing.decision.event.CloudEventUtils;
import org.kie.kogito.tracing.decision.event.trace.TraceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class TraceEventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(TraceEventConsumer.class);

    @Incoming("kogito-tracing-decision")
    public CompletionStage<Void> handleTraceEventMessage(Message<String> message) {
        try {
            CloudEventImpl<TraceEvent> cloudEvent = CloudEventUtils.decode(message.getPayload());
            LOG.info("New CloudEvent with id {} from {}", cloudEvent.getAttributes().getId(), cloudEvent.getAttributes().getSource());
        } catch (IllegalStateException e) {
            LOG.error("Catched IllegalStateException while decoding TraceEvent", e);
        }
        return message.ack();
    }

}
