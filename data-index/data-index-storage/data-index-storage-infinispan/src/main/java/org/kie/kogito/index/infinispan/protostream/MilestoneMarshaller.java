package org.kie.kogito.index.infinispan.protostream;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;
import org.kie.kogito.index.model.Milestone;
import org.kie.kogito.persistence.infinispan.protostream.AbstractMarshaller;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MilestoneMarshaller extends AbstractMarshaller implements MessageMarshaller<Milestone> {

    protected static final String ID = "id";
    protected static final String NAME = "name";
    protected static final String STATUS = "status";

    public MilestoneMarshaller(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Milestone readFrom(ProtoStreamReader reader) throws IOException {
        return Milestone.builder()
                .id(reader.readString(ID))
                .name(reader.readString(NAME))
                .status(reader.readString(STATUS))
                .build();
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, Milestone milestone) throws IOException {
        writer.writeString(ID, milestone.getId());
        writer.writeString(NAME, milestone.getName());
        writer.writeString(STATUS, milestone.getStatus());
    }

    @Override
    public Class<? extends Milestone> getJavaClass() {
        return Milestone.class;
    }

    @Override
    public String getTypeName() {
        return getJavaClass().getName();
    }
}
