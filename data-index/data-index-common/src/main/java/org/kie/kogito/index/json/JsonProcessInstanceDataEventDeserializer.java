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

package org.kie.kogito.index.json;

import java.io.IOException;

import org.kie.kogito.event.process.ProcessInstanceDataEvent;
import org.kie.kogito.event.process.ProcessInstanceErrorDataEvent;
import org.kie.kogito.event.process.ProcessInstanceNodeDataEvent;
import org.kie.kogito.event.process.ProcessInstanceSLADataEvent;
import org.kie.kogito.event.process.ProcessInstanceStateDataEvent;
import org.kie.kogito.event.process.ProcessInstanceVariableDataEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class JsonProcessInstanceDataEventDeserializer extends StdDeserializer<ProcessInstanceDataEvent<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonProcessInstanceDataEventDeserializer.class);

    private static final long serialVersionUID = 6152014726577574241L;

    public JsonProcessInstanceDataEventDeserializer() {
        this(null);
    }

    public JsonProcessInstanceDataEventDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ProcessInstanceDataEvent<?> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        LOGGER.debug("Deserialize process instance data event: {}", node);
        String type = node.get("type").asText();

        switch (type) {
            case "ProcessInstanceErrorDataEvent":
                return (ProcessInstanceDataEvent<?>) jp.getCodec().treeToValue(node, ProcessInstanceErrorDataEvent.class);
            case "ProcessInstanceNodeDataEvent":
                return (ProcessInstanceDataEvent<?>) jp.getCodec().treeToValue(node, ProcessInstanceNodeDataEvent.class);
            case "ProcessInstanceSLADataEvent":
                return (ProcessInstanceDataEvent<?>) jp.getCodec().treeToValue(node, ProcessInstanceSLADataEvent.class);
            case "ProcessInstanceStateDataEvent":
                return (ProcessInstanceDataEvent<?>) jp.getCodec().treeToValue(node, ProcessInstanceStateDataEvent.class);
            case "ProcessInstanceVariableDataEvent":
                return (ProcessInstanceDataEvent<?>) jp.getCodec().treeToValue(node, ProcessInstanceVariableDataEvent.class);
            default:
                LOGGER.warn("Unknown type {} in json data {}", type, node);
                return (ProcessInstanceDataEvent<?>) jp.getCodec().treeToValue(node, ProcessInstanceDataEvent.class);

        }
    }
}