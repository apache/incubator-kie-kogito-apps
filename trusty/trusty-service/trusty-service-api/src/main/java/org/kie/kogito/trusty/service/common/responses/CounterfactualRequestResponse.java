package org.kie.kogito.trusty.service.common.responses;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CounterfactualRequestResponse {

    public static final String EXECUTION_ID_FIELD = "executionId";
    public static final String COUNTERFACTUAL_ID_FIELD = "counterfactualId";
    public static final String MAX_RUNNING_TIME_SECONDS_FIELD = "maxRunningTimeSeconds";

    @JsonProperty(EXECUTION_ID_FIELD)
    @NotNull(message = "executionId must be provided.")
    private String executionId;

    @JsonProperty(COUNTERFACTUAL_ID_FIELD)
    @NotNull(message = "counterfactualId must be provided.")
    private String counterfactualId;

    @JsonProperty(MAX_RUNNING_TIME_SECONDS_FIELD)
    @NotNull(message = "maxRunningTimeSeconds must be provided.")
    private Long maxRunningTimeSeconds;

    public CounterfactualRequestResponse() {
    }

    public CounterfactualRequestResponse(@NotNull String executionId,
            @NotNull String counterfactualId,
            Long maxRunningTimeSeconds) {
        this.executionId = Objects.requireNonNull(executionId);
        this.counterfactualId = Objects.requireNonNull(counterfactualId);
        this.maxRunningTimeSeconds = maxRunningTimeSeconds;
    }

    public String getExecutionId() {
        return executionId;
    }

    public String getCounterfactualId() {
        return counterfactualId;
    }

    public Long getMaxRunningTimeSeconds() {
        return maxRunningTimeSeconds;
    }

}
