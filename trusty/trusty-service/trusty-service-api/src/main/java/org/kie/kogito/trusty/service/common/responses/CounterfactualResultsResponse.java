package org.kie.kogito.trusty.service.common.responses;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.kie.kogito.explainability.api.CounterfactualExplainabilityRequest;
import org.kie.kogito.explainability.api.CounterfactualExplainabilityResult;
import org.kie.kogito.explainability.api.CounterfactualSearchDomain;
import org.kie.kogito.explainability.api.ModelIdentifier;
import org.kie.kogito.explainability.api.NamedTypedValue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CounterfactualResultsResponse extends CounterfactualExplainabilityRequest {

    public static final String SOLUTIONS_FIELD = "solutions";

    @JsonProperty(SOLUTIONS_FIELD)
    @NotNull(message = "solutions object must be provided.")
    private List<CounterfactualExplainabilityResult> solutions;

    public CounterfactualResultsResponse() {
    }

    public CounterfactualResultsResponse(@NotNull String executionId,
            @NotBlank String serviceUrl,
            @NotNull ModelIdentifier modelIdentifier,
            @NotNull String counterfactualId,
            @NotNull Collection<NamedTypedValue> originalInputs,
            @NotNull Collection<NamedTypedValue> goals,
            @NotNull Collection<CounterfactualSearchDomain> searchDomains,
            Long maxRunningTimeSeconds,
            @NotNull List<CounterfactualExplainabilityResult> solutions) {
        super(executionId,
                serviceUrl,
                modelIdentifier,
                counterfactualId,
                originalInputs,
                goals,
                searchDomains,
                maxRunningTimeSeconds);
        this.solutions = Objects.requireNonNull(solutions);
    }

    public List<CounterfactualExplainabilityResult> getSolutions() {
        return solutions;
    }
}
