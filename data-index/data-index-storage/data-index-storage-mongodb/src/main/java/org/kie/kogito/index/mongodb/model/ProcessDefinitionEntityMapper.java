package org.kie.kogito.index.mongodb.model;

import java.util.function.Function;

import org.kie.kogito.index.model.Node;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.persistence.mongodb.model.MongoEntityMapper;

import static java.util.stream.Collectors.toList;

public class ProcessDefinitionEntityMapper implements MongoEntityMapper<ProcessDefinition, ProcessDefinitionEntity> {

    @Override
    public Class<ProcessDefinitionEntity> getEntityClass() {
        return ProcessDefinitionEntity.class;
    }

    @Override
    public ProcessDefinitionEntity mapToEntity(String key, ProcessDefinition pd) {
        if (pd == null) {
            return null;
        }

        ProcessDefinitionEntity entity = new ProcessDefinitionEntity();
        entity.setKey(pd.getKey());
        entity.setId(pd.getId());
        entity.setVersion(pd.getVersion());
        entity.setName(pd.getName());
        entity.setRoles(pd.getRoles());
        entity.setAddons(pd.getAddons());
        entity.setType(pd.getType());
        entity.setEndpoint(pd.getEndpoint());
        entity.setSource(pd.getSource() == null ? null : pd.getSource().getBytes());
        entity.setNodes(pd.getNodes() == null ? null : pd.getNodes().stream().map(fromNode()).collect(toList()));
        return entity;
    }

    @Override
    public ProcessDefinition mapToModel(ProcessDefinitionEntity entity) {
        if (entity == null) {
            return null;
        }

        ProcessDefinition pd = new ProcessDefinition();
        pd.setId(entity.getId());
        pd.setVersion(entity.getVersion());
        pd.setName(entity.getName());
        pd.setRoles(entity.getRoles());
        pd.setAddons(entity.getAddons());
        pd.setType(entity.getType());
        pd.setEndpoint(entity.getEndpoint());
        pd.setSource(entity.getSource() == null ? null : new String(entity.getSource()));
        pd.setNodes(entity.getNodes() == null ? null : entity.getNodes().stream().map(toNode()).collect(toList()));
        return pd;
    }

    @Override
    public String convertToMongoAttribute(String attribute) {
        return attribute;
    }

    @Override
    public String convertToModelAttribute(String attribute) {
        return attribute;
    }

    Function<ProcessDefinitionEntity.NodeEntity, Node> toNode() {
        return entity -> {
            if (entity == null) {
                return null;
            }

            Node node = new Node();
            node.setId(entity.getId());
            node.setName(entity.getName());
            node.setType(entity.getType());
            node.setUniqueId(entity.getUniqueId());
            node.setMetadata(entity.getMetadata());
            return node;
        };
    }

    Function<Node, ProcessDefinitionEntity.NodeEntity> fromNode() {
        return node -> {
            if (node == null) {
                return null;
            }

            ProcessDefinitionEntity.NodeEntity entity = new ProcessDefinitionEntity.NodeEntity();
            entity.setId(node.getId());
            entity.setName(node.getName());
            entity.setType(node.getType());
            entity.setUniqueId(node.getUniqueId());
            entity.setMetadata(node.getMetadata());
            return entity;
        };
    }
}
