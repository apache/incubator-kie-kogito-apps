package org.kie.kogito.index.jpa.mapper;

import org.kie.kogito.index.jpa.model.NodeEntity;
import org.kie.kogito.index.model.Node;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi", suppressTimestampInGenerated = true)
public interface NodeEntityMapper {

    NodeEntity mapToEntity(Node node);

    @InheritInverseConfiguration
    Node mapToModel(NodeEntity nodeEntity);
}
