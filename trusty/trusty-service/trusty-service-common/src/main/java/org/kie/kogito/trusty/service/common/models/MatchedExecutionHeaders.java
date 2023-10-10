package org.kie.kogito.trusty.service.common.models;

import java.util.List;

import org.kie.kogito.trusty.storage.api.model.Execution;

public class MatchedExecutionHeaders {

    private List<Execution> executions;

    private int availableResults;

    public MatchedExecutionHeaders(List<Execution> executions, int availableResults) {
        this.executions = executions;
        this.availableResults = availableResults;
    }

    public int getAvailableResults() {
        return availableResults;
    }

    public List<Execution> getExecutions() {
        return executions;
    }
}
