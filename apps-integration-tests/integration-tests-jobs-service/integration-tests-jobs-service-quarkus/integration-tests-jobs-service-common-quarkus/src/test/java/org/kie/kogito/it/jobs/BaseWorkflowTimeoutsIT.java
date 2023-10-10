package org.kie.kogito.it.jobs;

import org.junit.jupiter.api.Test;

import static org.kie.kogito.test.utils.ProcessInstancesRESTTestUtils.assertProcessInstanceHasFinished;
import static org.kie.kogito.test.utils.ProcessInstancesRESTTestUtils.newProcessInstanceAndGetId;

public abstract class BaseWorkflowTimeoutsIT {

    private static final String EMPTY_WORKFLOW_DATA = "{\"workflowdata\" : \"\"}";

    protected static final String WORKFLOW_TIMEOUTS_URL = "/workflow_timeouts";
    private static final String WORKFLOW_TIMEOUTS_GET_BY_ID_URL = WORKFLOW_TIMEOUTS_URL + "/{id}";

    @Test
    void workflowTimeoutExceeded() {
        // Start a new process instance.
        String processInstanceId = newProcessInstanceAndGetId(WORKFLOW_TIMEOUTS_URL, EMPTY_WORKFLOW_DATA);
        // Give enough time for the timeout to exceed.
        assertProcessInstanceHasFinished(WORKFLOW_TIMEOUTS_GET_BY_ID_URL, processInstanceId, 1, 180);
    }

}
