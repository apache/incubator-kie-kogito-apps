package org.kie.kogito.index.jdbc;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.kie.kogito.index.jpa.mapper.AbstractProcessDefinitionEntityMapper;
import org.kie.kogito.index.model.ProcessDefinition;

public class JPAProcessDefinitionEntityMapper extends AbstractProcessDefinitionEntityMapper<JPAProcessDefinitionEntity> {

    @Override
    protected JPAProcessDefinitionEntity create() {
        return new JPAProcessDefinitionEntity();
    }

    @Override
    protected void fillModel(ProcessDefinition model, JPAProcessDefinitionEntity entity) {
        if (entity.getMetadata() != null) {
            model.setMetadata(Collections.<String, Object> unmodifiableMap(entity.getMetadata()));
        }

    }

    @Override
    protected void fillEntity(JPAProcessDefinitionEntity entity, ProcessDefinition model) {
        if (model.getMetadata() != null) {
            model.setMetadata(model.getMetadata().entrySet().stream().filter(e -> e.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString())));
        }
    }
}
