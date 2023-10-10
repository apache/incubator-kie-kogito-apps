package org.kie.kogito.explainability.models;

import java.util.Map;

import org.kie.kogito.explainability.api.ModelIdentifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PredictInput {

    @JsonProperty("modelIdentifier")
    private ModelIdentifier modelIdentifier;

    @JsonProperty("request")
    private Map<String, Object> request;

    public PredictInput() {
    }

    public PredictInput(ModelIdentifier modelIdentifier, Map<String, Object> request) {
        this.modelIdentifier = modelIdentifier;
        this.request = request;
    }

    public ModelIdentifier getModelIdentifier() {
        return modelIdentifier;
    }

    public void setModelIdentifier(ModelIdentifier modelIdentifier) {
        this.modelIdentifier = modelIdentifier;
    }

    public Map<String, Object> getRequest() {
        return request;
    }

    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }
}
