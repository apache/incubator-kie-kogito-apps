package org.kie.kogito.trusty.storage.infinispan;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.infinispan.protostream.EnumMarshaller;
import org.kie.kogito.storage.infinispan.protostream.AbstractMarshaller;
import org.kie.kogito.trusty.storage.api.model.ExecutionTypeEnum;

public class ExecutionTypeMarshaller extends AbstractMarshaller implements EnumMarshaller<ExecutionTypeEnum> {
    private static final ObjectMapper myMapper = new ObjectMapper();

    public ExecutionTypeMarshaller(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public Class<? extends ExecutionTypeEnum> getJavaClass() {
        return ExecutionTypeEnum.class;
    }

    @Override
    public String getTypeName() {
        return getJavaClass().getName();
    }

    @Override
    public ExecutionTypeEnum decode(int enumValue) {
        return enumValue == 0 ? ExecutionTypeEnum.DECISION : ExecutionTypeEnum.PROCESS;
    }

    @Override
    public int encode(ExecutionTypeEnum executionTypeEnum) throws IllegalArgumentException {
        return executionTypeEnum == ExecutionTypeEnum.DECISION ? 0 : 1;
    }
}
