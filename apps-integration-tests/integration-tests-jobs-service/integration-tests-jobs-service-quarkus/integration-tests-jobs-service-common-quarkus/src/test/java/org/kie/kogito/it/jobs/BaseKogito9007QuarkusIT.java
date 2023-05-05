/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.it.jobs;

import org.junit.jupiter.api.Test;

public abstract class BaseKogito9007QuarkusIT extends BaseKogito9007IT {

    private static final String KOGITO_9007_EVENT_STATE_TIMEOUTS_URL = "/kogito_9007_event_state_timeouts";
    private static final String KOGITO_9007_EVENT_STATE_TIMEOUTS_GET_BY_ID_URL = KOGITO_9007_EVENT_STATE_TIMEOUTS_URL + "/{id}";
    private static final String EMPTY_WORKFLOW_DATA = "{\"workflowdata\" : \"\"}";

    @Test
    void eventStateTimeouts() {
        executeInstancesAndEnsureTermination(KOGITO_9007_EVENT_STATE_TIMEOUTS_URL,
                KOGITO_9007_EVENT_STATE_TIMEOUTS_GET_BY_ID_URL,
                EMPTY_WORKFLOW_DATA);
    }
}
