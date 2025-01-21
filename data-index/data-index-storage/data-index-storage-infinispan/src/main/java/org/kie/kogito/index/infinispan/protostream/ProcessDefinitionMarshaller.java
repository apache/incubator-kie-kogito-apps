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
package org.kie.kogito.index.infinispan.protostream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.infinispan.protostream.MessageMarshaller;
import org.kie.kogito.index.model.Entry;
import org.kie.kogito.index.model.Node;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.persistence.infinispan.protostream.AbstractMarshaller;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ProcessDefinitionMarshaller extends AbstractMarshaller implements MessageMarshaller<ProcessDefinition> {

    protected static final String ID = "id";
    protected static final String VERSION = "version";
    protected static final String NAME = "name";
    protected static final String ANNOTATIONS = "annotations";
    protected static final String DESCRIPTION = "description";
    protected static final String METADATA = "metadata";
    protected static final String ROLES = "roles";
    protected static final String ADDONS = "addons";
    protected static final String TYPE = "type";
    protected static final String ENDPOINT = "endpoint";
    protected static final String SOURCE = "source";
    protected static final String NODES = "nodes";

    public ProcessDefinitionMarshaller(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public ProcessDefinition readFrom(ProtoStreamReader reader) throws IOException {
        ProcessDefinition pd = new ProcessDefinition();
        pd.setId(reader.readString(ID));
        pd.setVersion(reader.readString(VERSION));
        pd.setName(reader.readString(NAME));
        pd.setDescription(reader.readString(DESCRIPTION));
        pd.setAnnotations(reader.readCollection(ANNOTATIONS, new HashSet<>(), String.class));
        pd.setMetadata(buildMetadata(reader));
        pd.setRoles(reader.readCollection(ROLES, new HashSet<>(), String.class));
        pd.setAddons(reader.readCollection(ADDONS, new HashSet<>(), String.class));
        pd.setType(reader.readString(TYPE));
        pd.setEndpoint(reader.readString(ENDPOINT));
        byte[] bytes = reader.readBytes(SOURCE);
        pd.setSource(bytes == null ? null : new String(bytes));
        pd.setNodes(reader.readCollection(NODES, new ArrayList<>(), Node.class));
        return pd;
    }

    private static Map<String, String> buildMetadata(ProtoStreamReader reader) throws IOException {
        return Optional.ofNullable(reader.readCollection(METADATA, new HashSet<>(), Entry.class))
                .map(entries -> entries.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue)))
                .orElse(null);
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, ProcessDefinition pd) throws IOException {
        writer.writeString(ID, pd.getId());
        writer.writeString(VERSION, pd.getVersion());
        writer.writeString(NAME, pd.getName());
        writer.writeString(DESCRIPTION, pd.getDescription());
        writer.writeCollection(ANNOTATIONS, pd.getAnnotations(), String.class);
        writer.writeCollection(METADATA, buildMetadata(pd), Entry.class);
        writer.writeCollection(ROLES, pd.getRoles(), String.class);
        writer.writeCollection(ADDONS, pd.getAddons(), String.class);
        writer.writeString(TYPE, pd.getType());
        writer.writeString(ENDPOINT, pd.getEndpoint());
        writer.writeBytes(SOURCE, pd.getSource() == null ? null : pd.getSource().getBytes());
        writer.writeCollection(NODES, pd.getNodes(), Node.class);
    }

    private static Set<Entry> buildMetadata(ProcessDefinition pd) {
        return Optional.ofNullable(pd.getMetadata())
                .map(Map::entrySet)
                .map(entries -> entries.stream().map(e -> new Entry(e.getKey(), e.getValue())).collect(Collectors.toSet()))
                .orElse(null);
    }

    @Override
    public Class<? extends ProcessDefinition> getJavaClass() {
        return ProcessDefinition.class;
    }

    @Override
    public String getTypeName() {
        return getJavaClass().getName();
    }
}
