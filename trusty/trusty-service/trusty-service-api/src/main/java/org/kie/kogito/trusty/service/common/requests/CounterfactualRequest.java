package org.kie.kogito.trusty.service.common.requests;

import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.kie.kogito.explainability.api.CounterfactualSearchDomain;
import org.kie.kogito.explainability.api.NamedTypedValue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CounterfactualRequest {

    @JsonProperty("goals")
    @NotNull(message = "goals object must be provided.")
    private List<NamedTypedValue> goals;

    @JsonProperty("searchDomains")
    @NotNull(message = "searchDomains object must be provided.")
    private List<CounterfactualSearchDomain> searchDomains;

    private CounterfactualRequest() {
    }

    public CounterfactualRequest(@NotNull List<NamedTypedValue> goals,
            @NotNull List<CounterfactualSearchDomain> searchDomains) {
        this.goals = Objects.requireNonNull(goals);
        this.searchDomains = Objects.requireNonNull(searchDomains);
    }

    public List<NamedTypedValue> getGoals() {
        return goals;
    }

    public List<CounterfactualSearchDomain> getSearchDomains() {
        return searchDomains;
    }
}
