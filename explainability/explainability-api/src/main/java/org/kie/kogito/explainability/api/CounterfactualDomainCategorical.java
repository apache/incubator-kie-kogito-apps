package org.kie.kogito.explainability.api;

import java.util.Collection;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class CounterfactualDomainCategorical extends CounterfactualDomain {

    public static final String TYPE = "CATEGORICAL";
    public static final String CATEGORIES = "categories";

    @JsonProperty(CATEGORIES)
    @NotNull(message = "categories object must be provided.")
    private Collection<JsonNode> categories;

    public CounterfactualDomainCategorical() {
    }

    public CounterfactualDomainCategorical(@NotNull Collection<JsonNode> categories) {
        this.categories = Objects.requireNonNull(categories);
    }

    public Collection<JsonNode> getCategories() {
        return this.categories;
    }

    @Override
    public String toString() {
        return "DomainCategorical{" +
                "categories=" + categories +
                "}";
    }
}
