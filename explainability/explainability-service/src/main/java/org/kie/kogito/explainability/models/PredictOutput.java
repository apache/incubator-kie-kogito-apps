package org.kie.kogito.explainability.models;

import java.util.Map;

import org.kie.kogito.explainability.api.ModelIdentifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PredictOutput {

    @JsonProperty("modelIdentifier")
    private ModelIdentifier modelIdentifier;

    @JsonProperty("result")
    private Map<String, Object> result;

    public PredictOutput() {
    }

    public PredictOutput(ModelIdentifier modelIdentifier, Map<String, Object> result) {
        this.modelIdentifier = modelIdentifier;
        this.result = result;
    }

    public ModelIdentifier getModelIdentifier() {
        return modelIdentifier;
    }

    public void setModelIdentifier(ModelIdentifier modelIdentifier) {
        this.modelIdentifier = modelIdentifier;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}
