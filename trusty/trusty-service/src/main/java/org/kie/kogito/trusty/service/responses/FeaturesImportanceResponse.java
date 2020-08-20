package org.kie.kogito.trusty.service.responses;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FeaturesImportanceResponse {

    @JsonProperty("featureImportance")
    private Collection<FeatureImportanceResponse> featureImportance;

    private FeaturesImportanceResponse() {
    }

    public FeaturesImportanceResponse(Collection<FeatureImportanceResponse> featureImportance) {
        this.featureImportance = featureImportance;
    }

    public Collection<FeatureImportanceResponse> getFeatureImportance() {
        return featureImportance;
    }
}
