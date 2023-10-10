package org.kie.kogito.trusty.service.common.responses.process;

import java.time.OffsetDateTime;

import org.kie.kogito.ModelDomain;
import org.kie.kogito.trusty.service.common.responses.ExecutionHeaderResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The <b>Process</b> implementation of <code>ExecutionHeaderResponse</code>.
 */
public final class ProcessHeaderResponse extends ExecutionHeaderResponse {

    @JsonProperty("executedModelNamespace")
    private String executedModelNamespace;

    protected ProcessHeaderResponse() {
        // serialization
    }

    public ProcessHeaderResponse(String executionId,
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
                ModelDomain.PROCESS);
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
