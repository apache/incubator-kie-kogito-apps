package org.kie.kogito.explainability.api;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = CounterfactualDomain.TYPE_FIELD)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CounterfactualDomainRange.class, name = CounterfactualDomainRange.TYPE),
        @JsonSubTypes.Type(value = CounterfactualDomainCategorical.class, name = CounterfactualDomainCategorical.TYPE)
})
public abstract class CounterfactualDomain {

    public static final String TYPE_FIELD = "type";
}
