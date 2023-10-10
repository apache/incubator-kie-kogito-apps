package org.kie.kogito.index.infinispan.protostream;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;
import org.kie.kogito.index.model.ProcessInstanceError;
import org.kie.kogito.persistence.infinispan.protostream.AbstractMarshaller;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ProcessInstanceErrorMarshaller extends AbstractMarshaller implements MessageMarshaller<ProcessInstanceError> {

    public ProcessInstanceErrorMarshaller(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public ProcessInstanceError readFrom(ProtoStreamReader reader) throws IOException {
        ProcessInstanceError error = new ProcessInstanceError();
        error.setNodeDefinitionId(reader.readString("nodeDefinitionId"));
        error.setMessage(reader.readString("message"));
        return error;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, ProcessInstanceError error) throws IOException {
        writer.writeString("nodeDefinitionId", error.getNodeDefinitionId());
        writer.writeString("message", error.getMessage());
    }

    @Override
    public Class<? extends ProcessInstanceError> getJavaClass() {
        return ProcessInstanceError.class;
    }

    @Override
    public String getTypeName() {
        return getJavaClass().getName();
    }
}
