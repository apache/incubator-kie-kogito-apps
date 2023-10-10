package org.kie.kogito.index.model;

public enum ProcessInstanceState {

    PENDING,
    ACTIVE,
    COMPLETED,
    ABORTED,
    SUSPENDED,
    ERROR;

    public static ProcessInstanceState fromStatus(Integer state) {
        return ProcessInstanceState.values()[state];
    }
}
