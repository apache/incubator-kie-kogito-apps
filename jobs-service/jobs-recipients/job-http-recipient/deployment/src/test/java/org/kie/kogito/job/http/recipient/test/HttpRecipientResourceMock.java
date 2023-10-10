package org.kie.kogito.job.http.recipient.test;

import java.util.Map;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.UrlPattern;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

public class HttpRecipientResourceMock implements QuarkusTestResourceLifecycleManager {

    public static final String MOCK_SERVICE_URL = "mock.service.url";
    WireMockServer wireMockServer;

    public static final String RESOURCE_URL = "my-service";

    private static final UrlPattern RESOURCE_URL_PATTERN = WireMock.urlMatching("/" + RESOURCE_URL + "\\?limit=0");

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        stubFor(WireMock.post(RESOURCE_URL_PATTERN).willReturn(WireMock.ok("POST")));
        stubFor(WireMock.get(RESOURCE_URL_PATTERN).willReturn(WireMock.ok("GET")));
        stubFor(WireMock.put(RESOURCE_URL_PATTERN).willReturn(WireMock.ok("PUT")));
        stubFor(WireMock.delete(RESOURCE_URL_PATTERN).willReturn(WireMock.ok("DELETE")));
        stubFor(WireMock.patch(RESOURCE_URL_PATTERN).willReturn(WireMock.ok("PATCH")));
        return Map.of(MOCK_SERVICE_URL, "http://localhost:" + wireMockServer.port());
    }

    @Override
    public synchronized void stop() {
        if (wireMockServer != null) {
            wireMockServer.stop();
            wireMockServer = null;
        }
    }

    @Override
    public void inject(TestInjector testInjector) {
        testInjector.injectIntoFields(wireMockServer, new TestInjector.MatchesType(WireMockServer.class));
    }
}
