package org.kie.kogito.trusty.service.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureImportanceResponse {

    @JsonProperty("featureName")
    private String featureName;

    @JsonProperty("featureScore")
    private Double featureScore;

    private FeatureImportanceResponse() {
    }

    public FeatureImportanceResponse(String featureName, Double featureScore) {
        this.featureName = featureName;
        this.featureScore = featureScore;
    }

    public String getFeatureName() {
        return featureName;
    }

    public Double getFeatureScore() {
        return featureScore;
    }
}
