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
                                .withRequestBody(matchingJsonPath("kogitoprocinstanceid", equalTo(processInstanceId)))
                                .withRequestBody(matchingJsonPath("type", equalTo(PROCESS_RESULT_EVENT_TYPE)))
                                .withRequestBody(matchingJsonPath("data.decision", equalTo(DECISION_NO_DECISION)))));

    }

    @Override
    public void setWireMockServer(WireMockServer sink) {
        this.sink = sink;
    }
}
