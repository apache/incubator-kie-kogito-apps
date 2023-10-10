package org.kie.kogito.trusty.service.common.responses;

import java.util.List;

import org.kie.kogito.explainability.api.FeatureImportanceModel;
import org.kie.kogito.explainability.api.SaliencyModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaliencyResponse extends SaliencyModel {

    @JsonProperty("outcomeId")
    private String outcomeId;

    private SaliencyResponse() {
    }

    public SaliencyResponse(String outcomeId,
            String outcomeName,
            List<FeatureImportanceModel> featureImportance) {
        super(outcomeName, featureImportance);
        this.outcomeId = outcomeId;
    }

    public String getOutcomeId() {
        return outcomeId;
    }
}
