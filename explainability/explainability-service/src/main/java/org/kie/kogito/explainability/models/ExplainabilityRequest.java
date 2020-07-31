package org.kie.kogito.explainability.models;

import org.kie.kogito.explainability.api.ExplainabilityRequestDto;

public class ExplainabilityRequest {

    private String executionId;

    public ExplainabilityRequest(String executionId) {
        this.executionId = executionId;
    }

    public static ExplainabilityRequest from(ExplainabilityRequestDto explainabilityRequestDto) {
        return new ExplainabilityRequest(explainabilityRequestDto.executionId);
    }

    public String getExecutionId(){
        return this.executionId;
    }
}
