package org.kie.kogito.trusty.storage.api.model;

import org.kie.kogito.trusty.api.ExplainabilityResultDto;

public class ExplainabilityResult {
    public static ExplainabilityResult from(ExplainabilityResultDto explainabilityResultDto){
        return new ExplainabilityResult();
    }
}
