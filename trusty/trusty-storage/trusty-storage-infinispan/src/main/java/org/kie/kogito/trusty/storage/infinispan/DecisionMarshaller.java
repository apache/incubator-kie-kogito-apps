package org.kie.kogito.trusty.storage.infinispan;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.infinispan.protostream.EnumMarshaller;
import org.infinispan.protostream.MessageMarshaller;
import org.kie.kogito.persistence.infinispan.protostream.AbstractMarshaller;
import org.kie.kogito.trusty.storage.api.model.Decision;
import org.kie.kogito.trusty.storage.api.model.ExecutionTypeEnum;

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
        result.setSuccess(reader.readBoolean("hasSucceeded"));
        result.setExecutorName(reader.readString("executorName"));
        result.setExecutedModelName(reader.readString("executedModelName"));

        // TODO: Replace with EnumMarshaller that I can't make working :)
        //result.setExecutionType(reader.readEnum("executionType", ExecutionTypeEnum.class));
        result.setExecutionType(myMapper.readValue(reader.readString("executionType"), ExecutionTypeEnum.class));

        return result;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, Decision result) throws IOException {
        writer.writeString("executionId", result.getExecutionId() );
        writer.writeLong("executionTimestamp", result.getExecutionTimestamp() );
        writer.writeBoolean("hasSucceeded", result.hasSucceeded());
        writer.writeString("executorName", result.getExecutorName() );
        writer.writeString("executedModelName", result.getExecutedModelName() );
        //writer.writeEnum("executionType", result.getExecutionType());
        writer.writeString("executionType", myMapper.writeValueAsString(result.getExecutionType()));
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


//public class ExecutionTypeMarshaller implements EnumMarshaller<ExecutionTypeEnum> {
//
//    @Override
//    public ExecutionTypeEnum decode(int enumValue) {
//        return null;
//    }
//
//    @Override
//    public int encode(ExecutionTypeEnum executionTypeEnum) throws IllegalArgumentException {
//        return 0;
//    }
//
//    @Override
//    public Class<? extends ExecutionTypeEnum> getJavaClass() {
//        return ExecutionTypeEnum.class;
//    }
//
//    @Override
//    public String getTypeName() {
//        return getJavaClass().getName();
//    }
//}
