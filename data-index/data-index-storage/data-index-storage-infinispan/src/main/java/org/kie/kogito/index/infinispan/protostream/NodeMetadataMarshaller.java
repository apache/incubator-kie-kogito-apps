package org.kie.kogito.index.infinispan.protostream;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;
import org.kie.kogito.index.model.NodeMetadata;
import org.kie.kogito.persistence.infinispan.protostream.AbstractMarshaller;

import com.fasterxml.jackson.databind.ObjectMapper;

public class NodeMetadataMarshaller extends AbstractMarshaller implements MessageMarshaller<NodeMetadata> {

    public static final String ACTION = "action";
    public static final String BRANCH = "branch";
    public static final String STATE = "state";
    public static final String UNIQUE_ID = "UniqueId";

    public NodeMetadataMarshaller(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public NodeMetadata readFrom(ProtoStreamReader reader) throws IOException {
        NodeMetadata metadata = new NodeMetadata();
        metadata.setUniqueId(reader.readString(UNIQUE_ID));
        metadata.setState(reader.readString(STATE));
        metadata.setBranch(reader.readString(BRANCH));
        metadata.setAction(reader.readString(ACTION));
        return metadata;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, NodeMetadata node) throws IOException {
        writer.writeString(UNIQUE_ID, node.getUniqueId());
        writer.writeString(STATE, node.getState());
        writer.writeString(BRANCH, node.getBranch());
        writer.writeString(ACTION, node.getAction());
    }

    @Override
    public Class<? extends NodeMetadata> getJavaClass() {
        return NodeMetadata.class;
    }

    @Override
    public String getTypeName() {
        return getJavaClass().getName();
    }
}
