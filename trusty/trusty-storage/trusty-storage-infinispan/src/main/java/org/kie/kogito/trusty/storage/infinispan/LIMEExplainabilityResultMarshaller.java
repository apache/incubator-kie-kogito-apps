package org.kie.kogito.trusty.storage.infinispan;

import java.io.IOException;

import org.kie.kogito.explainability.api.BaseExplainabilityResult;
import org.kie.kogito.explainability.api.LIMEExplainabilityResult;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LIMEExplainabilityResultMarshaller extends AbstractModelMarshaller<LIMEExplainabilityResult> {

    public LIMEExplainabilityResultMarshaller(ObjectMapper mapper) {
        super(mapper, LIMEExplainabilityResult.class);
    }

    @Override
    public LIMEExplainabilityResult readFrom(ProtoStreamReader reader) throws IOException {
        return mapper.readValue(reader.readString(Constants.RAW_OBJECT_FIELD), LIMEExplainabilityResult.class);
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, LIMEExplainabilityResult input) throws IOException {
        writer.writeString(BaseExplainabilityResult.EXECUTION_ID_FIELD, input.getExecutionId());
        writer.writeString(Constants.RAW_OBJECT_FIELD, mapper.writeValueAsString(input));
    }
}
