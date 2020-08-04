package org.kie.kogito.explainability.models;

import org.kie.kogito.explainability.api.ExplainabilityRequestDto;

public class ExplainabilityRequest {

    private String executionId;

    public ExplainabilityRequest(String executionId) {
        this.executionId = executionId;
    }

    public static ExplainabilityRequest from(ExplainabilityRequestDto explainabilityRequestDto) {
        // TODO: Update the converter with all the properties in ExplainabilityRequestDto when they will be defined. https://issues.redhat.com/browse/KOGITO-2944
        return new ExplainabilityRequest(explainabilityRequestDto.getExecutionId());
    }

    public String getExecutionId(){
        return this.executionId;
    }
}
