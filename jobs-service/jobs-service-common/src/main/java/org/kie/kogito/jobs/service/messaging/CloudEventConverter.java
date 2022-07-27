/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.jobs.service.messaging;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.HttpHeaders;

import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.cloudevents.CloudEvent;
import io.cloudevents.SpecVersion;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.quarkus.reactivemessaging.http.runtime.IncomingHttpMetadata;
import io.smallrye.reactive.messaging.MessageConverter;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

import static io.cloudevents.core.v1.CloudEventV1.DATACONTENTTYPE;
import static io.cloudevents.core.v1.CloudEventV1.DATASCHEMA;
import static io.cloudevents.core.v1.CloudEventV1.ID;
import static io.cloudevents.core.v1.CloudEventV1.SOURCE;
import static io.cloudevents.core.v1.CloudEventV1.SPECVERSION;
import static io.cloudevents.core.v1.CloudEventV1.SUBJECT;
import static io.cloudevents.core.v1.CloudEventV1.TIME;
import static io.cloudevents.core.v1.CloudEventV1.TYPE;

/**
 * Converts the message payload into a io.cloudevents.CloudEvent object. The conversion takes into account that the
 * message can be coded in the structured or binary format.
 */
@ApplicationScoped
public class CloudEventConverter implements MessageConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudEventConverter.class);

    private static final String CE_PREFIX = "ce-";

    private static final String APPLICATION_CLOUDEVENTS_JSON = "application/cloudevents+json";

    private static final String DATA = "data";

    private static final Predicate<String> IS_CE_ATTRIBUTE_NAME = name -> Objects.equals(ID, name) ||
            Objects.equals(SOURCE, name) ||
            Objects.equals(TYPE, name) ||
            Objects.equals(SPECVERSION, name) ||
            Objects.equals(TIME, name) ||
            Objects.equals(SUBJECT, name) ||
            Objects.equals(DATASCHEMA, name) ||
            Objects.equals(DATACONTENTTYPE, name) ||
            Objects.equals(DATA, name);

    private interface CloudEventPropertyAccessor {
        Set<String> getPropertyNames();

        boolean containsProperty(String propertyName);

        String getPropertyValue(String propertyName);

        Buffer getDataBuffer();
    }

    @Override
    public boolean canConvert(Message<?> message, Type type) {
        return message.getMetadata(IncomingHttpMetadata.class).isPresent() &&
                message.getPayload() instanceof Buffer && type == CloudEvent.class;
    }

    @Override
    public Message<?> convert(Message<?> message, Type type) {
        LOGGER.debug("convert: message, {}, type: {}, metadata: {}, payload: {}", message, type,
                message.getMetadata(), message.getPayload());

        IncomingHttpMetadata httpMetadata = message.getMetadata(IncomingHttpMetadata.class)
                .orElseThrow(() -> new IllegalStateException("No http metadata"));
        MultiMap httpHeaders = httpMetadata.getHeaders();
        LOGGER.debug("httpHeaders: {}", httpHeaders);
        if (httpHeaders == null) {
            throw new IllegalStateException("No http headers");
        }
        if (isStructuredFormat(httpHeaders)) {
            return doConvertFromStructured(message);
        } else {
            return doConvertFromBinary(httpHeaders, message);
        }
    }

    private Message<?> doConvertFromStructured(Message<?> message) {
        final Buffer buffer = (Buffer) message.getPayload();
        final JsonObject jsonObject = new JsonObject(buffer);
        final CloudEventPropertyAccessor propertyAccessor = new CloudEventPropertyAccessor() {
            @Override
            public Set<String> getPropertyNames() {
                return jsonObject.fieldNames();
            }

            @Override
            public boolean containsProperty(String propertyName) {
                return jsonObject.containsKey(propertyName);
            }

            @Override
            public String getPropertyValue(String propertyName) {
                return jsonObject.getString(propertyName);
            }

            @Override
            public Buffer getDataBuffer() {
                return jsonObject.getJsonObject(DATA).toBuffer();
            }
        };
        return doConvert(message, propertyAccessor);
    }

    private Message<?> doConvertFromBinary(MultiMap headers, Message<?> message) {
        final Map<String, String> ceHeaders = new HashMap<>();
        headers.forEach(entry -> {
            if (isCeHeader(entry.getKey())) {
                ceHeaders.put(ceHeaderName(entry.getKey()), entry.getValue());
            }
        });

        final CloudEventPropertyAccessor propertyAccessor = new CloudEventPropertyAccessor() {
            @Override
            public Set<String> getPropertyNames() {
                return ceHeaders.keySet();
            }

            @Override
            public String getPropertyValue(String propertyName) {
                return ceHeaders.get(propertyName);
            }

            @Override
            public Buffer getDataBuffer() {
                return (Buffer) message.getPayload();
            }

            @Override
            public boolean containsProperty(String propertyName) {
                return ceHeaders.containsKey(propertyName);
            }
        };
        return doConvert(message, propertyAccessor);
    }

    private Message<?> doConvert(Message<?> message, CloudEventPropertyAccessor propertyAccessor) {
        SpecVersion version = SpecVersion.parse(propertyAccessor.getPropertyValue(SPECVERSION));
        if (version != SpecVersion.V1) {
            throw new IllegalArgumentException("CloudEvent specversion: " + version + " is not supported by this converter.");
        }
        // mandatory spec fields
        CloudEventBuilder ceBuilder = CloudEventBuilder.fromSpecVersion(SpecVersion.parse(propertyAccessor.getPropertyValue(SPECVERSION)))
                .withId(propertyAccessor.getPropertyValue(ID))
                .withSource(newUri(propertyAccessor.getPropertyValue(SOURCE)))
                .withType(propertyAccessor.getPropertyValue(TYPE));

        // optional spec fields
        if (propertyAccessor.containsProperty(TIME)) {
            ceBuilder.withTime(OffsetDateTime.parse(propertyAccessor.getPropertyValue(TIME)));
        }
        if (propertyAccessor.containsProperty(DATASCHEMA)) {
            ceBuilder.withDataSchema(newUri(propertyAccessor.getPropertyValue(DATASCHEMA)));
        }
        if (propertyAccessor.containsProperty(DATACONTENTTYPE)) {
            ceBuilder.withDataContentType(propertyAccessor.getPropertyValue(DATACONTENTTYPE));
        }
        if (propertyAccessor.containsProperty(SUBJECT)) {
            ceBuilder.withSubject(propertyAccessor.getPropertyValue(SUBJECT));
        }

        // any other non spec related field is an extension
        propertyAccessor.getPropertyNames().stream()
                .filter(fieldName -> IS_CE_ATTRIBUTE_NAME.negate().test(fieldName))
                .forEach(fieldName -> ceBuilder.withExtension(fieldName, propertyAccessor.getPropertyValue(fieldName)));

        Buffer dataBuffer = propertyAccessor.getDataBuffer();
        LOGGER.debug("dataBuffer.toString(): {}", dataBuffer);
        ceBuilder.withData(dataBuffer.getBytes());
        return message.withPayload(ceBuilder.build());
    }

    private static boolean isStructuredFormat(MultiMap headers) {
        return headers.getAll(HttpHeaders.CONTENT_TYPE).contains(APPLICATION_CLOUDEVENTS_JSON);
    }

    private static boolean isCeHeader(String name) {
        return name.length() > 3 && name.substring(0, CE_PREFIX.length()).toLowerCase().startsWith(CE_PREFIX);
    }

    private static String ceHeaderName(String ceHeader) {
        return ceHeader.substring(CE_PREFIX.length()).toLowerCase();
    }

    private static URI newUri(String value) {
        try {
            return new URI(value);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid value was provided for creating an URI: " + value, e);
        }
    }
}
