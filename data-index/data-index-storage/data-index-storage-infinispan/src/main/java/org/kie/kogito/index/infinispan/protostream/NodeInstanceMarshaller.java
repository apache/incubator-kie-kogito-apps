package org.kie.kogito.index.infinispan.protostream;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;
import org.kie.kogito.index.model.NodeInstance;
import org.kie.kogito.persistence.infinispan.protostream.AbstractMarshaller;

import com.fasterxml.jackson.databind.ObjectMapper;

public class NodeInstanceMarshaller extends AbstractMarshaller implements MessageMarshaller<NodeInstance> {

    public NodeInstanceMarshaller(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public NodeInstance readFrom(ProtoStreamReader reader) throws IOException {
        NodeInstance node = new NodeInstance();
        node.setId(reader.readString("id"));
        node.setName(reader.readString("name"));
        node.setType(reader.readString("type"));
        node.setEnter(dateToZonedDateTime(reader.readDate("enter")));
        node.setExit(dateToZonedDateTime(reader.readDate("exit")));
        node.setDefinitionId(reader.readString("definitionId"));
        node.setNodeId(reader.readString("nodeId"));
        return node;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, NodeInstance node) throws IOException {
        writer.writeString("id", node.getId());
        writer.writeString("name", node.getName());
        writer.writeString("type", node.getType());
        writer.writeDate("enter", zonedDateTimeToDate(node.getEnter()));
        writer.writeDate("exit", zonedDateTimeToDate(node.getExit()));
        writer.writeString("definitionId", node.getDefinitionId());
        writer.writeString("nodeId", node.getNodeId());
    }

    @Override
    public Class<? extends NodeInstance> getJavaClass() {
        return NodeInstance.class;
    }

    @Override
    public String getTypeName() {
        return getJavaClass().getName();
    }
}
