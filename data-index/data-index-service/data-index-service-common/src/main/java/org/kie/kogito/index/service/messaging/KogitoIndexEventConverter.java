/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.index.service.messaging;

import java.io.IOException;
import java.lang.reflect.Type;

import org.eclipse.microprofile.reactive.messaging.Message;
import org.kie.kogito.event.AbstractDataEvent;
import org.kie.kogito.event.process.ProcessDefinitionDataEvent;
import org.kie.kogito.event.process.ProcessDefinitionEventBody;
import org.kie.kogito.index.event.KogitoJobCloudEvent;
import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.service.DataIndexServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.message.MessageReader;
import io.cloudevents.http.vertx.VertxMessageFactory;
import io.quarkus.reactivemessaging.http.runtime.IncomingHttpMetadata;
import io.smallrye.reactive.messaging.MessageConverter;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Converts the message payload into an indexable object. The conversion takes into account that the
 * message can be coded in the structured or binary format.
 */
@ApplicationScoped
public class KogitoIndexEventConverter implements MessageConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(KogitoIndexEventConverter.class);

    ObjectMapper objectMapper;

    @Override
    public boolean canConvert(Message<?> message, Type type) {
        return isIndexable(type) &&
                (message.getPayload() instanceof Buffer);
    }

    private boolean isIndexable(Type type) {
        return type == ProcessDefinitionDataEvent.class
                || type == KogitoJobCloudEvent.class;
    }

    @Override
    public Message<?> convert(Message<?> message, Type type) {
        try {
            // quarkus-http connector case, let Vertx manage binary and structured mode.
            IncomingHttpMetadata httpMetadata = message.getMetadata(IncomingHttpMetadata.class)
                    .orElseThrow(() -> new IllegalStateException("No IncomingHttpMetadata metadata was found current message."));
            CloudEvent cloudEvent;
            MultiMap httpHeaders = httpMetadata.getHeaders();
            Buffer buffer = (Buffer) message.getPayload();
            MessageReader messageReader = VertxMessageFactory.createReader(httpHeaders, buffer);
            cloudEvent = messageReader.toEvent();

            if (type.getTypeName().equals(KogitoJobCloudEvent.class.getTypeName())) {
                return message.withPayload(buildKogitoJobCloudEvent(cloudEvent));
            } else if (type.getTypeName().equals(ProcessDefinitionDataEvent.class.getTypeName())) {
                return message.withPayload(buildProcessDefinitionEvent(cloudEvent));
            }
            // never happens, see isIndexable.
            throw new IllegalArgumentException("Unknown event type: " + type);
        } catch (IOException e) {
            LOGGER.error("Error converting message payload to " + type.getTypeName(), e);
            throw new DataIndexServiceException("Error converting message payload:\n" + message.getPayload() + " \n to" + type.getTypeName(), e);
        }
    }

    private ProcessDefinitionDataEvent buildProcessDefinitionEvent(CloudEvent cloudEvent) throws IOException {
        ProcessDefinitionDataEvent event = new ProcessDefinitionDataEvent();
        applyCloudEventAttributes(cloudEvent, event);
        if (cloudEvent.getData() != null) {
            event.setData(objectMapper.readValue(cloudEvent.getData().toBytes(), ProcessDefinitionEventBody.class));
        }
        return event;
    }

    @Inject
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private KogitoJobCloudEvent buildKogitoJobCloudEvent(CloudEvent cloudEvent) throws IOException {
        KogitoJobCloudEvent jobCloudEvent = new KogitoJobCloudEvent();
        jobCloudEvent.setId(cloudEvent.getId());
        jobCloudEvent.setType(cloudEvent.getType());
        jobCloudEvent.setSource(cloudEvent.getSource());
        jobCloudEvent.setContentType(cloudEvent.getDataContentType());
        jobCloudEvent.setSchemaURL(cloudEvent.getDataSchema());
        jobCloudEvent.setSubject(cloudEvent.getSubject());
        jobCloudEvent.setTime(cloudEvent.getTime() != null ? cloudEvent.getTime().toZonedDateTime() : null);
        if (cloudEvent.getData() != null) {
            jobCloudEvent.setData(objectMapper.readValue(cloudEvent.getData().toBytes(), Job.class));
        }
        return jobCloudEvent;
    }

    private static void applyCloudEventAttributes(CloudEvent cloudEvent, AbstractDataEvent<?> dataEvent) {
        dataEvent.setSpecVersion(cloudEvent.getSpecVersion());
        dataEvent.setId(cloudEvent.getId());
        dataEvent.setType(cloudEvent.getType());
        dataEvent.setSource(cloudEvent.getSource());
        dataEvent.setDataContentType(cloudEvent.getDataContentType());
        dataEvent.setDataSchema(cloudEvent.getDataSchema());
        dataEvent.setSubject(cloudEvent.getSubject());
        dataEvent.setTime(cloudEvent.getTime());
    }
}
