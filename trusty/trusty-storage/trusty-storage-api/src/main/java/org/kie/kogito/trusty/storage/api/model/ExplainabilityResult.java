package org.kie.kogito.trusty.storage.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.kie.kogito.explainability.api.ExplainabilityResultDto;

public class ExplainabilityResult {
    @JsonProperty("executionId")
    public String executionId;

    // TODO: add the properties.

    public static ExplainabilityResult from(ExplainabilityResultDto explainabilityResultDto){
        return new ExplainabilityResult();
    }
}
