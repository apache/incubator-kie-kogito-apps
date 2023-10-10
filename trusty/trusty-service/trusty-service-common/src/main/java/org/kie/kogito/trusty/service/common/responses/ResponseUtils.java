package org.kie.kogito.trusty.service.common.responses;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.kie.kogito.trusty.service.common.responses.decision.DecisionHeaderResponse;
import org.kie.kogito.trusty.service.common.responses.process.ProcessHeaderResponse;
import org.kie.kogito.trusty.storage.api.model.Execution;
import org.kie.kogito.trusty.storage.api.model.decision.Decision;

public class ResponseUtils {

    private ResponseUtils() {
    }

    public static ExecutionHeaderResponse executionHeaderResponseFrom(Execution execution) {
        OffsetDateTime ldt = OffsetDateTime.ofInstant((Instant.ofEpochMilli(execution.getExecutionTimestamp())),
                ZoneOffset.UTC);
        switch (execution.getExecutionType()) {
            case DECISION:
                return getDecisionHeaderResponse(ldt, (Decision) execution);
            case PROCESS:
                return getProcessHeaderResponse(ldt, (Decision) execution);
            default:
                throw new IllegalArgumentException("Unmanaged ExecutionType " + execution.getExecutionType());
        }
    }

    private static ExecutionHeaderResponse getDecisionHeaderResponse(OffsetDateTime ldt, Decision execution) {
        return new DecisionHeaderResponse(execution.getExecutionId(),
                ldt,
                execution.hasSucceeded(),
                execution.getExecutorName(),
                execution.getExecutedModelName(),
                execution.getExecutedModelNamespace());
    }

    private static ExecutionHeaderResponse getProcessHeaderResponse(OffsetDateTime ldt, Decision execution) {
        return new ProcessHeaderResponse(execution.getExecutionId(),
                ldt,
                execution.hasSucceeded(),
                execution.getExecutorName(),
                execution.getExecutedModelName(),
                execution.getExecutedModelNamespace());
    }

}
