package org.kie.kogito.trusty.storage.infinispan;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.infinispan.protostream.MessageMarshaller;
import org.kie.kogito.storage.infinispan.protostream.AbstractMarshaller;
import org.kie.kogito.trusty.storage.api.model.Decision;

public class DecisionMarshaller extends AbstractMarshaller implements MessageMarshaller<Decision> {

    private static final ObjectMapper myMapper = new ObjectMapper();

    public DecisionMarshaller(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Decision readFrom(ProtoStreamReader reader) throws IOException {
        Decision result =  new Decision();

        result.setExecutionId(reader.readString("executionId"));
        result.setExecutionTimestamp(reader.readLong("executionTimestamp"));
//        result.modelId = reader.readString("modelId");
        result.setExecutedModelName(reader.readString("executedModelName"));
//        result.modelNamespace = reader.readString("modelNamespace");
//        result.context = myMapper.readValue(reader.readString("context"), Map.class);
//        result.decisions = Arrays.asList(reader.readArray("decisions", OutcomeModel.class));
        return result;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, Decision result) throws IOException {
        writer.writeString("executionId", result.getExecutionId() );
        writer.writeLong("executionTimestamp", result.getExecutionTimestamp() );
//        writer.writeString("modelId", result.getExecutedModelName() );
        writer.writeString("executedModelName", result.getExecutedModelName() );
//        writer.writeString("modelName", result.getExecutedModelName() );
//        writer.writeArray("decisions", result.decisions.toArray(), OutcomeModel.class );
//        writer.writeString("context", myMapper.writeValueAsString(result.context));
    }

    @Override
    public Class<? extends Decision> getJavaClass() {
        return Decision.class;
    }

    @Override
    public String getTypeName() {
        return getJavaClass().getName();
    }
}
