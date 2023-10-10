package org.kie.kogito.trusty.storage.infinispan;

import java.io.IOException;

import org.kie.kogito.explainability.api.BaseExplainabilityResult;
import org.kie.kogito.explainability.api.CounterfactualExplainabilityResult;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CounterfactualExplainabilityResultMarshaller extends AbstractModelMarshaller<CounterfactualExplainabilityResult> {

    public CounterfactualExplainabilityResultMarshaller(ObjectMapper mapper) {
        super(mapper, CounterfactualExplainabilityResult.class);
    }

    @Override
    public CounterfactualExplainabilityResult readFrom(ProtoStreamReader reader) throws IOException {
        return mapper.readValue(reader.readString(Constants.RAW_OBJECT_FIELD), CounterfactualExplainabilityResult.class);
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, CounterfactualExplainabilityResult input) throws IOException {
        writer.writeString(BaseExplainabilityResult.EXECUTION_ID_FIELD, input.getExecutionId());
        writer.writeString(CounterfactualExplainabilityResult.COUNTERFACTUAL_ID_FIELD, input.getCounterfactualId());
        writer.writeString(Constants.RAW_OBJECT_FIELD, mapper.writeValueAsString(input));
    }
}
