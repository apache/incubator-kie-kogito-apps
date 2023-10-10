package org.kie.kogito.explainability.api;

import org.kie.kogito.tracing.typedvalue.BaseTypedValue;

import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonSubTypes({
        @JsonSubTypes.Type(value = CounterfactualSearchDomainUnitValue.class, name = "UNIT"),
        @JsonSubTypes.Type(value = CounterfactualSearchDomainCollectionValue.class, name = "COLLECTION"),
        @JsonSubTypes.Type(value = CounterfactualSearchDomainStructureValue.class, name = "STRUCTURE")
})
public abstract class CounterfactualSearchDomainValue extends BaseTypedValue<CounterfactualSearchDomainCollectionValue, CounterfactualSearchDomainStructureValue, CounterfactualSearchDomainUnitValue> {

    protected CounterfactualSearchDomainValue() {
    }

    protected CounterfactualSearchDomainValue(Kind kind, String type) {
        super(kind, type);
    }
}
