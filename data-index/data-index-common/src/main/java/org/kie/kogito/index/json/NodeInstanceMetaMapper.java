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
package org.kie.kogito.index.json;

import java.util.function.Function;

import org.kie.kogito.event.process.NodeInstanceDataEvent;

import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.kie.kogito.index.json.JsonUtils.getObjectMapper;
import static org.kie.kogito.index.storage.Constants.ID;
import static org.kie.kogito.index.storage.Constants.KOGITO_DOMAIN_ATTRIBUTE;
import static org.kie.kogito.index.storage.Constants.LAST_UPDATE;
import static org.kie.kogito.index.storage.Constants.NODE_INSTANCES_DOMAIN_ATTRIBUTE;
import static org.kie.kogito.index.storage.Constants.PROCESS_ID;

public class NodeInstanceMetaMapper implements Function<NodeInstanceDataEvent, ObjectNode> {

    @Override
    public ObjectNode apply(NodeInstanceDataEvent event) {
        if (event == null) {
            return null;
        } else {
            ObjectNode json = getObjectMapper().createObjectNode();
            json.put(ID, event.getData().getId());
            json.put(PROCESS_ID, event.getKogitoProcessId());
            ObjectNode kogito = getObjectMapper().createObjectNode();
            kogito.put(LAST_UPDATE, event.getTime().toInstant().toEpochMilli());
            kogito.withArray(NODE_INSTANCES_DOMAIN_ATTRIBUTE).add(getNodeJson(event));
            json.set(KOGITO_DOMAIN_ATTRIBUTE, kogito);
            return json;
        }
    }

    private ObjectNode getNodeJson(NodeInstanceDataEvent event) {
        ObjectNode json = getObjectMapper().createObjectNode();
        json.put(ID, event.getData().getId());
        json.put(PROCESS_ID, event.getKogitoProcessId());
        json.put("processInstanceId", event.getKogitoProcessInstanceId());
        json.put("nodeDefinitionId", event.getData().getNodeDefinitionId());
        json.put("nodeName", event.getData().getNodeName());
        json.put("nodeType", event.getData().getNodeType());
        return json;
    }
}
