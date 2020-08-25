package org.kie.kogito.trusty.service.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaliencyResponse {

    @JsonProperty("featureName")
    private String outcomeId;

    @JsonProperty("featureImportance")
    private List<FeatureImportanceResponse> featureImportance;

    private SaliencyResponse() {
    }

    public SaliencyResponse(String outcomeId, List<FeatureImportanceResponse> featureImportance) {
        this.outcomeId = outcomeId;
        this.featureImportance = featureImportance;
    }

    public String getOutcomeId() {
        return outcomeId;
    }

    public List<FeatureImportanceResponse> getFeatureImportance() {
        return featureImportance;
    }
}
