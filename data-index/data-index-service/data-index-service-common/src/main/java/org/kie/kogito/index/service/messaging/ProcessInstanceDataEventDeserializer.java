/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.index.service.messaging;

import org.apache.kafka.common.serialization.Deserializer;
import org.kie.kogito.event.process.ProcessInstanceDataEvent;
import org.kie.kogito.index.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ProcessInstanceDataEventDeserializer implements Deserializer<ProcessInstanceDataEvent<?>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessInstanceDataEventDeserializer.class);

    @Override
    public ProcessInstanceDataEvent<?> deserialize(String topic, byte[] data) {
        try {
            return JsonUtils.getObjectMapper().readValue(new String(data), ProcessInstanceDataEvent.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("not possible to deserialize ProcessInstanceDataEvent data {}", new String(data), e);
            throw new IllegalArgumentException("not possible to deserialize data");
        }

    }

}
