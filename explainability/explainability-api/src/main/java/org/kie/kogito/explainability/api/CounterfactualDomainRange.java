package org.kie.kogito.explainability.api;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class CounterfactualDomainRange extends CounterfactualDomain {

    public static final String TYPE = "RANGE";
    public static final String LOWER_BOUND = "lowerBound";
    public static final String UPPER_BOUND = "upperBound";

    @JsonProperty(LOWER_BOUND)
    @NotNull(message = "lowerBound object must be provided.")
    private JsonNode lowerBound;

    @JsonProperty(UPPER_BOUND)
    @NotNull(message = "upperBound object must be provided.")
    private JsonNode upperBound;

    public CounterfactualDomainRange() {
    }

    public CounterfactualDomainRange(@NotNull JsonNode lowerBound,
            @NotNull JsonNode upperBound) {
        this.lowerBound = Objects.requireNonNull(lowerBound);
        this.upperBound = Objects.requireNonNull(upperBound);
    }

    public JsonNode getLowerBound() {
        return this.lowerBound;
    }

    public JsonNode getUpperBound() {
        return this.upperBound;
    }

    @Override
    public String toString() {
        return "DomainRange{" +
                "lowerBound=" + lowerBound.asText() +
                ", upperBound=" + upperBound.asText() +
                "}";
    }
}
