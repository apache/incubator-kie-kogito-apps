/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.it.jobs;

import org.kie.kogito.test.resources.JobServiceTestResource;
import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@QuarkusIntegrationTest
@QuarkusTestResource(KafkaQuarkusTestResource.class)
@QuarkusTestResource(SinkMock.class)
@JobServiceTestResource(knativeEventingEnabled = true)
class SwitchStateTimeoutsIT extends BaseSwitchStateTimeoutsIT implements SinkMock.SinkMockAware {

    private WireMockServer sink;

    @Override
    protected void verifyNoDecisionEventWasProduced(String processInstanceId) throws Exception {
        // The workflow should emit a new event indicating that NoDecision was made.
        await()
                .atMost(50, SECONDS)
                .with().pollInterval(1, SECONDS)
                .untilAsserted(() -> sink.verify(1,
                        postRequestedFor(urlEqualTo("/"))
                                .withHeader("ce-kogitoprocinstanceid", equalTo(processInstanceId))
                                .withHeader("ce-type", equalTo(PROCESS_RESULT_EVENT_TYPE))
                                .withRequestBody(matchingJsonPath("decision", equalTo(DECISION_NO_DECISION)))));

    }

    @Override
    public void setWireMockServer(WireMockServer sink) {
        this.sink = sink;
    }
}
