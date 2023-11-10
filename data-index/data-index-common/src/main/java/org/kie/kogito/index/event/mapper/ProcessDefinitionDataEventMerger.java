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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        instance.setId(doMerge(data.getId(), instance.getId()));
        instance.setName(doMerge(data.getName(), instance.getName()));
        instance.setVersion(doMerge(data.getVersion(), instance.getVersion()));
        instance.setAddons(doMerge(data.getAddons(), instance.getAddons()));
        instance.setRoles(doMerge(data.getRoles(), instance.getRoles()));
        instance.setType(doMerge(data.getType(), instance.getType()));
        instance.setEndpoint(doMerge(data.getEndpoint(), instance.getEndpoint()));
        instance.setDescription(doMerge(data.getDescription(), instance.getDescription()));
        instance.setAnnotations(doMerge(data.getAnnotations(), instance.getAnnotations()));
        instance.setMetadata(doMerge(toStringMap(data.getMetadata()), instance.getMetadata()));
        instance.setNodes(doMerge(nodeDefinitions(data), instance.getNodes()));
        return instance;
    }

    private List<Node> nodeDefinitions(ProcessDefinitionEventBody data) {
        if (data.getNodes() == null && data.getNodes().isEmpty()) {
            return Collections.emptyList();
        }
        return data.getNodes().stream().map(this::nodeDefinition).collect(toList());
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

    private <T> T doMerge(T incoming, T current) {
        boolean notEmpty = (incoming instanceof Collection) ? !((Collection) incoming).isEmpty() : incoming != null;
        boolean notEquals = !Objects.deepEquals(incoming, current);
        if (notEmpty && notEquals) {
            return incoming;
        }
        return current;
    }

    private static Map<String, String> toStringMap(Map<String, ?> input) {
        return ofNullable(input)
                .map(meta -> meta.entrySet().stream().collect(toMap(Map.Entry::getKey, entry -> valueOf(entry.getValue()))))
                .orElse(null);
    }
}
