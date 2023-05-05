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

import io.restassured.RestAssured;

import static org.kie.kogito.test.utils.ProcessInstancesRESTTestUtils.assertProcessInstanceHasFinished;
import static org.kie.kogito.test.utils.ProcessInstancesRESTTestUtils.newProcessInstanceAndGetId;

public abstract class BaseKogito9007IT implements JobServiceHealthAware {

    private static final String KOGITO_9007_BOUNDARY_TIMER_EVENT_URL = "/Kogito9007BoundaryTimerEvent";
    private static final String KOGITO_9007_BOUNDARY_TIMER_EVENT_URL_GET_BY_ID_URL = KOGITO_9007_BOUNDARY_TIMER_EVENT_URL + "/{id}";
    private static final String KOGITO_9007_TIMER_EVENT_URL = "/Kogito9007TimerEvent";
    private static final String KOGITO_9007_TIMER_EVENT_URL_GET_BY_ID_URL = KOGITO_9007_TIMER_EVENT_URL + "/{id}";
    private static final String EMPTY_DATA = "{}";
    private static final int AT_LEAST_SECONDS = 1;
    private static final int AT_MOST_SECONDS = 120;

    static {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void boundaryTimerEvent() {
        executeInstancesAndEnsureTermination(KOGITO_9007_BOUNDARY_TIMER_EVENT_URL,
                KOGITO_9007_BOUNDARY_TIMER_EVENT_URL_GET_BY_ID_URL,
                EMPTY_DATA);
    }

    @Test
    void timerEvent() {
        executeInstancesAndEnsureTermination(KOGITO_9007_TIMER_EVENT_URL,
                KOGITO_9007_TIMER_EVENT_URL_GET_BY_ID_URL,
                EMPTY_DATA);
    }

    public static void executeInstancesAndEnsureTermination(String processUrl, String processGetByIdQuery, String processData) {
        // Start simultaneous instances.
        String processInstanceId1 = newProcessInstanceAndGetId(processUrl, processData);
        String processInstanceId2 = newProcessInstanceAndGetId(processUrl, processData);
        String processInstanceId3 = newProcessInstanceAndGetId(processUrl, processData);

        // All the instances must finish in a period of time, otherwise the issue is still present.
        assertProcessInstanceHasFinished(processGetByIdQuery, processInstanceId1, AT_LEAST_SECONDS, AT_MOST_SECONDS);
        assertProcessInstanceHasFinished(processGetByIdQuery, processInstanceId2, AT_LEAST_SECONDS, AT_MOST_SECONDS);
        assertProcessInstanceHasFinished(processGetByIdQuery, processInstanceId3, AT_LEAST_SECONDS, AT_MOST_SECONDS);
    }
}
