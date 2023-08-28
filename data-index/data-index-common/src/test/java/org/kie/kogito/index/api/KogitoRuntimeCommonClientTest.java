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
package org.kie.kogito.index.api;

import java.nio.Buffer;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.kogito.index.TestUtils;
import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.model.ProcessInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.quarkus.security.credential.TokenCredential;
import io.quarkus.security.identity.SecurityIdentity;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public abstract class KogitoRuntimeCommonClientTest {

    protected static int ACTIVE = 1;
    protected static String SERVICE_URL = "http://runtimeURL.com";
    protected static String PROCESS_INSTANCE_ID = "pId";
    protected static String JOB_ID = "jobId";
    protected static String AUTHORIZED_TOKEN = "authToken";

    @Mock
    protected Vertx vertx;

    @Mock
    protected SecurityIdentity identityMock;

    protected TokenCredential tokenCredential;

    @Mock
    protected WebClient webClientMock;

    @Mock
    protected HttpRequest httpRequestMock;

    protected abstract KogitoRuntimeCommonClient getClient();

    protected void testCancelJobRest() {
        setupIdentityMock();
        when(webClientMock.delete(anyString())).thenReturn(httpRequestMock);
        Job job = createJob(JOB_ID, PROCESS_INSTANCE_ID, "SCHEDULED");
        getClient().cancelJob(SERVICE_URL, job);

        verify(getClient()).sendDeleteClientRequest(webClientMock,
                format(KogitoRuntimeCommonClient.CANCEL_JOB_PATH, job.getId()),
                "CANCEL Job with id: " + JOB_ID);
        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        verify(httpRequestMock).send(handlerCaptor.capture());
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    protected void testRescheduleWithoutJobServiceInstance() {
        String newJobData = "{\"expirationTime\": \"2023-08-27T04:35:54.631Z\",\"retries\": 2}";
        setupIdentityMock();
        when(webClientMock.patch(anyString())).thenReturn(httpRequestMock);

        Job job = createJob(JOB_ID, PROCESS_INSTANCE_ID, "SCHEDULED");

        getClient().rescheduleJob(SERVICE_URL, job, newJobData);
        ArgumentCaptor<JsonObject> jsonCaptor = ArgumentCaptor.forClass(JsonObject.class);

        verify(getClient()).sendPatchClientRequest(eq(webClientMock),
                eq(format(KogitoRuntimeCommonClient.RESCHEDULE_JOB_PATH, JOB_ID)),
                eq("RESCHEDULED JOB with id: " + job.getId()),
                jsonCaptor.capture());

        assertThat(jsonCaptor.getValue().getString("expirationTime")).isEqualTo("2023-08-27T04:35:54.631Z");
        assertThat(jsonCaptor.getValue().getString("retries")).isEqualTo("2");

        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        JsonObject jsonOject = new JsonObject(newJobData);
        verify(httpRequestMock).sendJson(eq(jsonOject), handlerCaptor.capture());
        verify(httpRequestMock).putHeader("Authorization", "Bearer " + AUTHORIZED_TOKEN);
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    void testWebClientToURLOptions() {
        String defaultHost = "localhost";
        int defaultPort = 8180;
        WebClientOptions webClientOptions = getClient().getWebClientToURLOptions("http://" + defaultHost + ":" + defaultPort);
        assertThat(webClientOptions.getDefaultHost()).isEqualTo(defaultHost);
        assertThat(webClientOptions.getDefaultPort()).isEqualTo(defaultPort);
    }

    @Test
    void testWebClientToURLOptionsWithoutPort() {
        String dataIndexUrl = "http://service.com";
        WebClientOptions webClientOptions = getClient().getWebClientToURLOptions(dataIndexUrl);
        assertThat(webClientOptions.getDefaultPort()).isEqualTo(80);
        assertThat(webClientOptions.getDefaultHost()).isEqualTo("service.com");
        assertFalse(webClientOptions.isSsl());
    }

    @Test
    void testWebClientToURLOptionsWithoutPortSSL() {
        String dataIndexurl = "https://service.com";
        WebClientOptions webClientOptions = getClient().getWebClientToURLOptions(dataIndexurl);
        assertThat(webClientOptions.getDefaultPort()).isEqualTo(443);
        assertThat(webClientOptions.getDefaultHost()).isEqualTo("service.com");
        assertTrue(webClientOptions.isSsl());
    }

    @Test
    void testMalformedURL() {
        assertThat(getClient().getWebClientToURLOptions("malformedURL")).isNull();
    }

    @Test
    void testOverrideURL() {
        String host = "host.testcontainers.internal";
        getClient().setGatewayTargetUrl(Optional.of(host));
        WebClientOptions webClientOptions = getClient().getWebClientToURLOptions("http://service.com");
        assertThat(webClientOptions.getDefaultHost()).isEqualTo(host);
    }

    protected AsyncResult createResponseMocks(HttpResponse response, boolean succeed, int statusCode) {
        AsyncResult asyncResultMock = mock(AsyncResult.class);
        when(asyncResultMock.succeeded()).thenReturn(succeed);
        when(asyncResultMock.result()).thenReturn(response);
        when(response.statusCode()).thenReturn(statusCode);
        return asyncResultMock;
    }

    protected Job createJob(String jobId, String processInstanceId, String status) {
        return TestUtils.getJob(jobId, "travels", processInstanceId, null, null, status);
    }

    protected void checkResponseHandling(Handler<AsyncResult<HttpResponse<Buffer>>> handler) {
        HttpResponse response = mock(HttpResponse.class);
        HttpResponse responseWithoutError = mock(HttpResponse.class);

        handler.handle(createResponseMocks(response, false, 404));
        verify(response).statusMessage();
        verify(response).body();
        verify(response, never()).bodyAsString();

        handler.handle(createResponseMocks(responseWithoutError, true, 200));
        verify(responseWithoutError, never()).statusMessage();
        verify(responseWithoutError, never()).body();
        verify(responseWithoutError).bodyAsString();
    }

    protected void setupIdentityMock() {
        tokenCredential = mock(TokenCredential.class);
        when(identityMock.getCredential(TokenCredential.class)).thenReturn(tokenCredential);
        when(tokenCredential.getToken()).thenReturn(AUTHORIZED_TOKEN);
        when(httpRequestMock.putHeader("Authorization", "Bearer " + AUTHORIZED_TOKEN)).thenReturn(httpRequestMock);
    }

    protected ProcessInstance createProcessInstance(String processInstanceId, int status) {
        return TestUtils.getProcessInstance("travels", processInstanceId, status, null, null);
    }

}
