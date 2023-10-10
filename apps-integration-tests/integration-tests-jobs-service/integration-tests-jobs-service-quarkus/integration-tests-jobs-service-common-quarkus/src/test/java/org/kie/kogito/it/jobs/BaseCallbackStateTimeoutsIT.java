package org.kie.kogito.it.jobs;

import org.junit.jupiter.api.Test;

import static org.kie.kogito.test.utils.ProcessInstancesRESTTestUtils.assertProcessInstanceExists;
import static org.kie.kogito.test.utils.ProcessInstancesRESTTestUtils.assertProcessInstanceHasFinished;
import static org.kie.kogito.test.utils.ProcessInstancesRESTTestUtils.newProcessInstanceAndGetId;

public abstract class BaseCallbackStateTimeoutsIT {

    private static final String CALLBACK_STATE_TIMEOUTS_SERVICE_URL = "/callback_state_timeouts";
    private static final String CALLBACK_STATE_TIMEOUTS_GET_BY_ID_URL = CALLBACK_STATE_TIMEOUTS_SERVICE_URL + "/{id}";
    private static final String NAME = "NAME";

    @Test
    void callbackStateTimeoutsExceeded() {
        // start a new process instance dnd collect the instance id.
        String processInput = buildProcessInput(NAME);
        String processInstanceId = newProcessInstanceAndGetId(CALLBACK_STATE_TIMEOUTS_SERVICE_URL, processInput);
        // assert the process instance is there
        assertProcessInstanceExists(CALLBACK_STATE_TIMEOUTS_GET_BY_ID_URL, processInstanceId);
        // do nothing more and wait until eventTimeout is fired and the process instance finalizes.
        assertProcessInstanceHasFinished(CALLBACK_STATE_TIMEOUTS_GET_BY_ID_URL, processInstanceId, 1, 10);
    }

    private static String buildProcessInput(String value) {
        return "{\"workflowdata\": {\"name\": \"" + value + "\"} }";
    }
}
