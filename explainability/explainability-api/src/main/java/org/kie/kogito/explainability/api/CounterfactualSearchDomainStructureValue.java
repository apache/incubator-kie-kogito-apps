package org.kie.kogito.explainability.api;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CounterfactualSearchDomainStructureValue extends CounterfactualSearchDomainValue {

    @JsonProperty("value")
    private Map<String, CounterfactualSearchDomainValue> value;

    protected CounterfactualSearchDomainStructureValue() {
    }

    public CounterfactualSearchDomainStructureValue(String type) {
        super(Kind.STRUCTURE, type);
    }

    public CounterfactualSearchDomainStructureValue(String type, Map<String, CounterfactualSearchDomainValue> value) {
        super(Kind.STRUCTURE, type);
        this.value = value;
    }

    public Map<String, CounterfactualSearchDomainValue> getValue() {
        return value;
    }
}
