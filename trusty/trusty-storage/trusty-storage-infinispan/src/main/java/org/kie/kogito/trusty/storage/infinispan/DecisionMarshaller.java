package org.kie.kogito.trusty.storage.infinispan;

import java.io.IOException;

import org.kie.kogito.trusty.storage.api.model.Execution;
import org.kie.kogito.trusty.storage.api.model.decision.Decision;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DecisionMarshaller extends AbstractModelMarshaller<Decision> {

    public DecisionMarshaller(ObjectMapper mapper) {
        super(mapper, Decision.class);
    }

    @Override
    public Decision readFrom(ProtoStreamReader reader) throws IOException {
        return mapper.readValue(reader.readString(Constants.RAW_OBJECT_FIELD), Decision.class);
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, Decision input) throws IOException {
        writer.writeString(Execution.EXECUTION_ID_FIELD, input.getExecutionId());
        writer.writeLong(Execution.EXECUTION_TIMESTAMP_FIELD, input.getExecutionTimestamp());
        writer.writeString(Constants.RAW_OBJECT_FIELD, mapper.writeValueAsString(input));
    }
}
