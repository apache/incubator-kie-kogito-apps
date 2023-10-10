package org.kie.kogito.explainability.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CounterfactualSearchDomain implements HasNameValue<CounterfactualSearchDomainValue> {

    public static final String NAME_FIELD = "name";
    public static final String VALUE_FIELD = "value";

    @JsonProperty(NAME_FIELD)
    private String name;

    @JsonProperty(VALUE_FIELD)
    private CounterfactualSearchDomainValue value;

    public CounterfactualSearchDomain() {
    }

    public CounterfactualSearchDomain(String name, CounterfactualSearchDomainValue value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CounterfactualSearchDomainValue getValue() {
        return value;
    }
}
