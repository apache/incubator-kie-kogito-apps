package org.kie.kogito.trusty.service.common.responses.decision;

import java.time.OffsetDateTime;

import org.kie.kogito.ModelDomain;
import org.kie.kogito.trusty.service.common.responses.ExecutionHeaderResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The <b>Decision</b> implementation of <code>ExecutionHeaderResponse</code>.
 */
public final class DecisionHeaderResponse extends ExecutionHeaderResponse {

    @JsonProperty("executedModelNamespace")
    private String executedModelNamespace;

    protected DecisionHeaderResponse() {
        // serialization
    }

    public DecisionHeaderResponse(String executionId,
            OffsetDateTime executionDate,
            Boolean hasSucceeded,
            String executorName,
            String executedModelName,
            String executedModelNamespace) {
        super(executionId,
                executionDate,
                hasSucceeded,
                executorName,
                executedModelName,
                ModelDomain.DECISION);
        this.executedModelNamespace = executedModelNamespace;
    }

    /**
     * Gets the namespace of the executed model.
     * 
     * @return The namespace of the executed model.
     */
    public String getExecutedModelNamespace() {
        return executedModelNamespace;
    }
}
