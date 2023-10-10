package org.kie.kogito.explainability.api;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CounterfactualSearchDomainCollectionValue extends CounterfactualSearchDomainValue {

    @JsonProperty("value")
    private Collection<CounterfactualSearchDomainValue> value;

    protected CounterfactualSearchDomainCollectionValue() {
    }

    public CounterfactualSearchDomainCollectionValue(String type) {
        super(Kind.COLLECTION, type);
    }

    public CounterfactualSearchDomainCollectionValue(String type, Collection<CounterfactualSearchDomainValue> value) {
        super(Kind.COLLECTION, type);
        this.value = value;
    }

    public Collection<CounterfactualSearchDomainValue> getValue() {
        return value;
    }
}
