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
package org.kie.kogito.app.audit.json;

import java.io.IOException;

import org.kie.kogito.jobs.service.api.event.CreateJobEvent;
import org.kie.kogito.jobs.service.api.event.DeleteJobEvent;
import org.kie.kogito.jobs.service.api.event.JobCloudEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class JsonJobDataEventDeserializer extends StdDeserializer<JobCloudEvent<?>> {


    private static final Logger LOGGER = LoggerFactory.getLogger(JsonJobDataEventDeserializer.class);

    private static final long serialVersionUID = 6152014726577574241L;

    public JsonJobDataEventDeserializer() {
        this(null);
    }

    public JsonJobDataEventDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public JobCloudEvent<?> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        LOGGER.debug("Deserialize process instance data event: {}", node);
        
        
        
        String type = node.get("type").asText();

        switch (type) {
            case CreateJobEvent.TYPE:
                return jp.getCodec().treeToValue(node, CreateJobEvent.class);
            case DeleteJobEvent.TYPE:
                return jp.getCodec().treeToValue(node, DeleteJobEvent.class);
            default:
                return null;
        }
    }
}