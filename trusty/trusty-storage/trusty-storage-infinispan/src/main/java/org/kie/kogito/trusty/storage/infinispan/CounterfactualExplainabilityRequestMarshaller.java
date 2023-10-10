package org.kie.kogito.trusty.storage.infinispan;

import java.io.IOException;

import org.kie.kogito.explainability.api.CounterfactualExplainabilityRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CounterfactualExplainabilityRequestMarshaller extends AbstractModelMarshaller<CounterfactualExplainabilityRequest> {

    public CounterfactualExplainabilityRequestMarshaller(ObjectMapper mapper) {
        super(mapper, CounterfactualExplainabilityRequest.class);
    }

    @Override
    public CounterfactualExplainabilityRequest readFrom(ProtoStreamReader reader) throws IOException {
        return mapper.readValue(reader.readString(Constants.RAW_OBJECT_FIELD), CounterfactualExplainabilityRequest.class);
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, CounterfactualExplainabilityRequest input) throws IOException {
        writer.writeString(CounterfactualExplainabilityRequest.EXECUTION_ID_FIELD, input.getExecutionId());
        writer.writeString(CounterfactualExplainabilityRequest.COUNTERFACTUAL_ID_FIELD, input.getCounterfactualId());
        writer.writeString(Constants.RAW_OBJECT_FIELD, mapper.writeValueAsString(input));
    }
}
