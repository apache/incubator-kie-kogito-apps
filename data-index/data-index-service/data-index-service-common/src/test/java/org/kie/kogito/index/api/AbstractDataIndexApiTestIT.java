/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.kogito.index.TestUtils;
import org.kie.kogito.index.model.NodeInstance;
import org.kie.kogito.index.model.ProcessInstance;
import org.mockito.junit.jupiter.MockitoExtension;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClientOptions;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.kie.kogito.index.TestUtils.readFileContent;
import static org.kie.kogito.index.api.utils.RuntimeServiceWiremock.OK_RESPONSE_WITH_ID;
import static org.kie.kogito.index.api.utils.RuntimeServiceWiremock.PROCESS_INSTANCE_ID;
import static org.kie.kogito.index.api.utils.RuntimeServiceWiremock.PROCESS_INSTANCE_ID_FAIL;
import static org.kie.kogito.index.api.utils.RuntimeServiceWiremock.RUNTIME_SERVICE_URL;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractDataIndexApiTestIT {

    @Inject
    public Vertx vertx;

    private DataIndexApiImpl client;

    @BeforeEach
    public void setup() {
        client = new DataIndexApiImpl(vertx);
    }

    @Test
    public void testAbortProcessInstance() throws InterruptedException, ExecutionException {
        String serviceUrl = ConfigProvider.getConfig().getValue(RUNTIME_SERVICE_URL, String.class);
        ProcessInstance pI = TestUtils.getProcessInstance("travels", PROCESS_INSTANCE_ID, 1, null, null);
        CompletableFuture<String> future = client.abortProcessInstance(serviceUrl, pI);
        checkSuccessfulResults(future, format(OK_RESPONSE_WITH_ID, PROCESS_INSTANCE_ID));

        ProcessInstance pI2 = TestUtils.getProcessInstance("travels", PROCESS_INSTANCE_ID, 4, null, null);
        future = client.abortProcessInstance(serviceUrl, pI2);
        checkFailedResults(future, "ProcessInstance can't be aborted regarding is not in ACTIVE State");

        ProcessInstance pI3 = TestUtils.getProcessInstance("travels", PROCESS_INSTANCE_ID_FAIL, 1, null, null);
        future = client.abortProcessInstance(serviceUrl, pI3);
        checkFailedResults(future, "FAILED: ABORT ProcessInstance with id: e0a18db6-b57d-4f0c-938b-25d4e76ec2f8 errorCode:404 errorStatus:Not Found");
    }

    @Test
    public void testRetryProcessInstance() throws InterruptedException, ExecutionException {
        String serviceUrl = ConfigProvider.getConfig().getValue(RUNTIME_SERVICE_URL, String.class);
        ProcessInstance pI = TestUtils.getProcessInstance("travels", PROCESS_INSTANCE_ID, 5, null, null);
        CompletableFuture<String> future = client.retryProcessInstance(serviceUrl, pI);
        checkSuccessfulResults(future, format(OK_RESPONSE_WITH_ID, PROCESS_INSTANCE_ID));

        ProcessInstance pI2 = TestUtils.getProcessInstance("travels", PROCESS_INSTANCE_ID, 1, null, null);
        future = client.retryProcessInstance(serviceUrl, pI2);
        checkFailedResults(future, "ProcessInstance can't be retried regarding is not in ERROR State");

        ProcessInstance pI3 = TestUtils.getProcessInstance("travels", PROCESS_INSTANCE_ID_FAIL, 5, null, null);
        future = client.retryProcessInstance(serviceUrl, pI3);
        checkFailedResults(future, "FAILED: RETRY ProcessInstance with id: e0a18db6-b57d-4f0c-938b-25d4e76ec2f8 errorCode:404 errorStatus:Not Found");
    }

    @Test
    public void testGetProcessInstanceDiagram() throws InterruptedException, ExecutionException {
        String serviceUrl = ConfigProvider.getConfig().getValue(RUNTIME_SERVICE_URL, String.class);
        ProcessInstance pI = TestUtils.getProcessInstance("travels", PROCESS_INSTANCE_ID, 5, null, null);
        CompletableFuture<String> future = client.getProcessInstanceDiagram(serviceUrl, pI);
        checkSuccessfulResults(future, getTravelsSVGFile());
        ProcessInstance pI2 = TestUtils.getProcessInstance("travels", PROCESS_INSTANCE_ID_FAIL, 5, null, null);

        future = client.getProcessInstanceDiagram(serviceUrl, pI2);
        checkFailedResults(future, "FAILED: Get Process Instance diagram with id: e0a18db6-b57d-4f0c-938b-25d4e76ec2f8 errorCode:404 errorStatus:Not Found");
    }

    @Test
    public void testGetProcessInstanceNodes() throws InterruptedException, ExecutionException {
        String serviceUrl = ConfigProvider.getConfig().getValue(RUNTIME_SERVICE_URL, String.class);
        ProcessInstance pI = TestUtils.getProcessInstance("travels", PROCESS_INSTANCE_ID, 5, null, null);
        CompletableFuture<List<NodeInstance>> future = client.getProcessInstanceNodes(serviceUrl, pI);
        assertThat(future.isDone());
        assertThat(future.isCompletedExceptionally()).isFalse();

        assertThat(((Map) ((List) future.get()).get(0)).get("id")).isEqualTo("a95cbfe8-c9b5-4c7a-b1c7-7ea85afb7e24");
        assertThat(((Map) ((List) future.get()).get(0)).get("nodeInstanceId")).isEqualTo("966fae5a-738c-4d7a-bc12-1a989ec57e22");

        ProcessInstance pI2 = TestUtils.getProcessInstance("travels", PROCESS_INSTANCE_ID_FAIL, 5, null, null);

        future = client.getProcessInstanceNodes(serviceUrl, pI2);
        checkFailedResults(future, "FAILED: Get Process Instance nodes with id: e0a18db6-b57d-4f0c-938b-25d4e76ec2f8 errorCode:404 errorStatus:Not Found");
    }

    @Test
    public void testWebClientToURLOptions() {
        String defaultHost = "localhost";
        int defaultPort = 8180;
        WebClientOptions webClientOptions = client.getWebClientToURLOptions("http://" + defaultHost + ":" + defaultPort);
        assertThat(webClientOptions.getDefaultHost()).isEqualTo(defaultHost);
        assertThat(webClientOptions.getDefaultPort()).isEqualTo(defaultPort);
    }

    @Test
    public void testWebClientToURLOptionsWithoutPort() {
        String dataIndexUrl = "http://service.com";
        WebClientOptions webClientOptions = client.getWebClientToURLOptions(dataIndexUrl);
        assertThat(webClientOptions.getDefaultPort()).isEqualTo(80);
        assertThat(webClientOptions.getDefaultHost()).isEqualTo("service.com");
        assertFalse(webClientOptions.isSsl());
    }

    @Test
    public void testWebClientToURLOptionsWithoutPortSSL() {
        String dataIndexurl = "https://service.com";
        WebClientOptions webClientOptions = client.getWebClientToURLOptions(dataIndexurl);
        assertThat(webClientOptions.getDefaultPort()).isEqualTo(443);
        assertThat(webClientOptions.getDefaultHost()).isEqualTo("service.com");
        assertTrue(webClientOptions.isSsl());
    }

    @Test
    public void testMalformedURL() {
        assertThat(client.getWebClientToURLOptions("malformedURL")).isNull();
    }

    public String getTravelsSVGFile() {
        try {
            return readFileContent("travels.svg");
        } catch (Exception e) {
            return "Not Found";
        }
    }

    private void checkSuccessfulResults(CompletableFuture future, Object expectedReturnValue) throws ExecutionException, InterruptedException {
        assertThat(future.isDone());
        assertThat(future.isCompletedExceptionally()).isFalse();
        assertThat(future.get().toString()).isEqualTo(expectedReturnValue.toString());
    }

    private void checkFailedResults(CompletableFuture future, String errorMessage) throws InterruptedException {
        assertThat(future.isDone());
        try {
            future.get();
        } catch (ExecutionException e) {
            assertThat(e.getCause().getMessage()).isEqualTo(errorMessage);
        }
        assertThat(future.isCompletedExceptionally()).isTrue();
    }

    protected abstract String getTestProtobufFileContent() throws Exception;
}
