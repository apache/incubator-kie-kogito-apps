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
package org.kie.kogito.index.event.mapper;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.kie.kogito.event.process.NodeDefinition;
import org.kie.kogito.event.process.ProcessDefinitionDataEvent;
import org.kie.kogito.event.process.ProcessDefinitionEventBody;
import org.kie.kogito.index.model.Node;
import org.kie.kogito.index.model.ProcessDefinition;

import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@ApplicationScoped
public class ProcessDefinitionDataEventMerger implements ProcessDefinitionEventMerger {
    @Override
    public boolean accept(Object input) {
        return input != null && input instanceof ProcessDefinitionDataEvent;
    }

    @Override
    public ProcessDefinition merge(ProcessDefinition instance, ProcessDefinitionDataEvent event) {
        ProcessDefinitionEventBody data = event.getData();
        if (event == null || data == null) {
            return instance;
        }
        if (instance == null) {
            instance = new ProcessDefinition();
        }
        instance.setId(data.getId());
        instance.setName(data.getName());
        instance.setVersion(data.getVersion());
        instance.setAddons(data.getAddons());
        instance.setRoles(data.getRoles());
        instance.setType(event.getKogitoProcessType());
        instance.setEndpoint(data.getEndpoint());
        instance.setDescription(data.getDescription());
        instance.setAnnotations(data.getAnnotations());
        instance.setMetadata(toStringMap(data.getMetadata()));
        instance.setNodes(ofNullable(data.getNodes()).map(nodes -> nodes.stream().map(this::nodeDefinition).collect(toList())).orElse(null));
        return instance;
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
}
