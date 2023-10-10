package org.kie.kogito.it.jobs;

import org.junit.jupiter.api.Test;

public abstract class BaseMultipleTimerInstancesQuarkusIT extends BaseMultipleTimerInstancesIT {

    private static final String MULTIPLE_TIMER_INSTANCES_EVENT_STATE_TIMEOUTS_URL = "/multiple_timer_instances_event_state_timeouts";
    private static final String MULTIPLE_TIMER_INSTANCES_EVENT_STATE_TIMEOUTS_GET_BY_ID_URL = MULTIPLE_TIMER_INSTANCES_EVENT_STATE_TIMEOUTS_URL + "/{id}";
    private static final String EMPTY_WORKFLOW_DATA = "{\"workflowdata\" : \"\"}";

    @Test
    void eventStateTimeouts() {
        executeInstancesAndEnsureTermination(MULTIPLE_TIMER_INSTANCES_EVENT_STATE_TIMEOUTS_URL,
                MULTIPLE_TIMER_INSTANCES_EVENT_STATE_TIMEOUTS_GET_BY_ID_URL,
                EMPTY_WORKFLOW_DATA);
    }
}
