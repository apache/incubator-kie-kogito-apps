package org.kie.kogito.explainability.models;

import org.kie.kogito.trusty.api.ExplainabilityRequestDto;
import org.kie.kogito.trusty.api.ExplainabilityResultDto;

public class ExplainabilityRequest {
    public String executionId;

    public ExplainabilityRequest(String executionId){
        this.executionId = executionId;
    }

    public static ExplainabilityRequest from(ExplainabilityRequestDto explainabilityRequestDto){
        return new ExplainabilityRequest(explainabilityRequestDto.executionId);
    }
}
