package org.kie.kogito.explainability.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaliencyModel {

    public static final String OUTCOME_NAME_FIELD = "outcomeName";

    public static final String FEATURE_IMPORTANCE_FIELD = "featureImportance";

    @JsonProperty(OUTCOME_NAME_FIELD)
    private String outcomeName;

    @JsonProperty(FEATURE_IMPORTANCE_FIELD)
    private List<FeatureImportanceModel> featureImportance;

    public SaliencyModel() {
    }

    public SaliencyModel(String outcomeName,
            List<FeatureImportanceModel> featureImportance) {
        this.outcomeName = outcomeName;
        this.featureImportance = featureImportance;
    }

    public String getOutcomeName() {
        return outcomeName;
    }

    public List<FeatureImportanceModel> getFeatureImportance() {
        return featureImportance;
    }

}
