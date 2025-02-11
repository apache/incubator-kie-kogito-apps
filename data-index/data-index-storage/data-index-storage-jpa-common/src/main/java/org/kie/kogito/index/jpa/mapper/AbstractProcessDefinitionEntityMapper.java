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
package org.kie.kogito.index.jpa.mapper;

import java.util.stream.Collectors;

import org.kie.kogito.index.jpa.model.ProcessDefinitionEntity;
import org.kie.kogito.index.model.ProcessDefinition;
import org.mapstruct.factory.Mappers;

public abstract class AbstractProcessDefinitionEntityMapper<T extends ProcessDefinitionEntity> implements ProcessDefinitionEntityMapper<T> {

    @Override
    public T mapToEntity(ProcessDefinition model) {
        if (model == null) {
            return null;
        }
        T entity = create();
        entity.setAddons(model.getAddons());
        entity.setAnnotations(model.getAnnotations());
        entity.setDescription(model.getDescription());
        entity.setEndpoint(model.getEndpoint());
        entity.setId(model.getId());
        entity.setName(model.getName());
        if (model.getNodes() != null) {
            NodeEntityMapper mapper = Mappers.getMapper(NodeEntityMapper.class);
            entity.setNodes(model.getNodes().stream().map(mapper::mapToEntity).collect(Collectors.toList()));
        }
        entity.setRoles(model.getRoles());
        if (model.getSource() != null) {
            entity.setSource(model.getSource().getBytes());
        }
        entity.setType(model.getType());
        entity.setVersion(model.getVersion());
        if (entity.getNodes() != null) {
            entity.getNodes().forEach(n -> n.setProcessDefinition(entity));
        }
        fillEntity(entity, model);
        return entity;
    }

    @Override
    public ProcessDefinition mapToModel(T entity) {
        if (entity == null) {
            return null;
        }
        ProcessDefinition model = new ProcessDefinition();
        model.setAddons(entity.getAddons());
        model.setAnnotations(entity.getAnnotations());
        model.setDescription(entity.getDescription());
        model.setId(entity.getId());
        model.setName(entity.getName());
        if (entity.getNodes() != null) {
            NodeEntityMapper mapper = Mappers.getMapper(NodeEntityMapper.class);
            model.setNodes(entity.getNodes().stream().map(mapper::mapToModel).collect(Collectors.toList()));
        }
        model.setRoles(entity.getRoles());
        if (entity.getSource() != null) {
            model.setSource(new String(entity.getSource()));
        }
        model.setType(entity.getType());
        model.setVersion(entity.getVersion());
        fillModel(model, entity);
        return model;
    }

    protected abstract T create();

    protected abstract void fillModel(ProcessDefinition model, T entity);

    protected abstract void fillEntity(T entity, ProcessDefinition model);

}
