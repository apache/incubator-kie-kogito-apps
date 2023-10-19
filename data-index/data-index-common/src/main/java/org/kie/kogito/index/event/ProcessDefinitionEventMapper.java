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
package org.kie.kogito.index.event;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.kie.kogito.event.process.NodeDefinition;
import org.kie.kogito.event.process.ProcessDefinitionDataEvent;
import org.kie.kogito.event.process.ProcessDefinitionEventBody;
import org.kie.kogito.index.model.Node;
import org.kie.kogito.index.model.ProcessDefinition;

import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class ProcessDefinitionEventMapper implements Function<ProcessDefinitionDataEvent, ProcessDefinition> {

    private static final AtomicReference<ProcessDefinitionEventMapper> INSTANCE = new AtomicReference<>();

    @Override
    public ProcessDefinition apply(ProcessDefinitionDataEvent event) {
        ProcessDefinitionEventBody data = event.getData();
        if (event == null || data == null) {
            return null;
        }
        ProcessDefinition pd = new ProcessDefinition();
        pd.setId(data.getId());
        pd.setName(data.getName());
        pd.setVersion(data.getVersion());
        pd.setAddons(data.getAddons());
        pd.setRoles(data.getRoles());
        pd.setType(event.getKogitoProcessType());
        pd.setEndpoint(data.getEndpoint());
        pd.setDescription(data.getDescription());
        pd.setAnnotations(data.getAnnotations());
        pd.setMetadata(toStringMap(data.getMetadata()));
        pd.setNodes(ofNullable(data.getNodes()).map(nodes -> nodes.stream().map(this::nodeDefinition).collect(toList())).orElse(null));
        return pd;
    }

    private Node nodeDefinition(NodeDefinition definition) {
        Node node = new Node();
        node.setId(definition.getId());
        node.setName(definition.getName());
        node.setUniqueId(definition.getUniqueId());
        node.setType(definition.getType());
        node.setMetadata(toStringMap(definition.getMetadata()));
        return node;
    }

    private static Map<String, String> toStringMap(Map<String, ?> input) {
        return ofNullable(input)
                .map(meta -> meta.entrySet().stream().collect(toMap(Map.Entry::getKey, entry -> valueOf(entry.getValue()))))
                .orElse(null);
    }

    public static ProcessDefinitionEventMapper get() {
        INSTANCE.compareAndSet(null, new ProcessDefinitionEventMapper());
        return INSTANCE.get();
    }
}
