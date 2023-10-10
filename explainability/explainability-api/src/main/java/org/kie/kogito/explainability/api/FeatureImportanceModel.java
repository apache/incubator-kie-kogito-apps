package org.kie.kogito.explainability.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureImportanceModel {

    public static final String FEATURE_NAME_FIELD = "featureName";
    public static final String SCORE_FIELD = "featureScore";

    @JsonProperty(FEATURE_NAME_FIELD)
    private String featureName;

    @JsonProperty(SCORE_FIELD)
    private Double featureScore;

    public FeatureImportanceModel() {
    }

    public FeatureImportanceModel(String featureName, Double featureScore) {
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
