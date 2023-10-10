package org.kie.kogito.it.jobs;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;

/**
 * Mock the SinkBinding that links the kogito project with the knative Broker.
 */
public class SinkMock implements QuarkusTestResourceLifecycleManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SinkMock.class);
    private WireMockServer wireMockServer;

    public interface SinkMockAware {
        void setWireMockServer(WireMockServer sink);
    }

    @Override
    public Map<String, String> start() {
        LOGGER.info("Start SinkMock test resource");
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
        wireMockServer.stubFor(post("/").willReturn(aResponse().withBody("{}").withStatus(200)));
        LOGGER.info("SinkMock test resource started");
        return Collections.singletonMap("kogito.sink-mock.url", wireMockServer.baseUrl());
    }

    @Override
    public void stop() {
        LOGGER.info("Stop SinkMock test resource");
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
        LOGGER.info("Stop SinkMock test resource stopped");
    }

    @Override
    public void inject(Object testInstance) {
        if (testInstance instanceof SinkMockAware) {
            ((SinkMockAware) testInstance).setWireMockServer(wireMockServer);
        }
    }
}
