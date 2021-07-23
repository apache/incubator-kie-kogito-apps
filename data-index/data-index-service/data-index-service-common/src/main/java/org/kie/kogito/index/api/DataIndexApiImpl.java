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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.index.api.predicate.AbortProcessInstancePredicate;
import org.kie.kogito.index.api.predicate.ProcessInstanceInErrorPredicate;
import org.kie.kogito.index.model.Node;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.service.DataIndexServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

import static java.lang.String.format;

@ApplicationScoped
public class DataIndexApiImpl implements DataIndexApi {

    public static final String ABORT_PROCESS_INSTANCE_URI = "/management/processes/%s/instances/%s";
    public static final String RETRY_PROCESS_INSTANCE_URI = "/management/processes/%s/instances/%s/retrigger";
    public static final String SKIP_PROCESS_INSTANCE_URI = "/management/processes/%s/instances/%s/skip";
    public static final String GET_PROCESS_INSTANCE_DIAGRAM_URI = "/svg/processes/%s/instances/%s";
    public static final String GET_PROCESS_INSTANCE_NODE_DEFINITIONS_URI = "/management/processes/%s/nodes";

    private static final Logger LOGGER = LoggerFactory.getLogger(DataIndexApiImpl.class);
    private Vertx vertx;
    private Map<String, WebClient> serviceWebClientMap = new HashMap<>();

    @Inject
    public DataIndexApiImpl(Vertx vertx) {
        this.vertx = vertx;
    }

    protected WebClient getWebClient(String runtimeServiceUrl) {
        return serviceWebClientMap.computeIfAbsent(runtimeServiceUrl, url -> WebClient.create(vertx, getWebClientToURLOptions(runtimeServiceUrl)));
    }

    protected WebClientOptions getWebClientToURLOptions(String targetHttpURL) {
        try {
            URL dataIndexURL = new URL(targetHttpURL);
            return new WebClientOptions()
                    .setDefaultHost(dataIndexURL.getHost())
                    .setDefaultPort((dataIndexURL.getPort() != -1 ? dataIndexURL.getPort() : dataIndexURL.getDefaultPort()))
                    .setSsl(dataIndexURL.getProtocol().compareToIgnoreCase("https") == 0);
        } catch (MalformedURLException malformedURLException) {
            return null;
        }
    }

    @Override
    public CompletableFuture<String> abortProcessInstance(String serviceURL, ProcessInstance processInstance) {
        String requestURI = format(ABORT_PROCESS_INSTANCE_URI, processInstance.getProcessId(), processInstance.getId());
        if (new AbortProcessInstancePredicate().test(processInstance)) {
            return sendDeleteClientRequest(getWebClient(serviceURL), requestURI, "ABORT ProcessInstance with id: " + processInstance.getId());
        }
        CompletableFuture<String> future = new CompletableFuture<>();
        future.completeExceptionally(new DataIndexServiceException("ProcessInstance can't be aborted regarding is not in ACTIVE State"));
        return future;
    }

    @Override
    public CompletableFuture<String> retryProcessInstance(String serviceURL, ProcessInstance processInstance) {
        String requestURI = format(RETRY_PROCESS_INSTANCE_URI, processInstance.getProcessId(), processInstance.getId());
        if (new ProcessInstanceInErrorPredicate().test(processInstance)) {
            return sendPostClientRequest(getWebClient(serviceURL), requestURI, "RETRY ProcessInstance with id: " + processInstance.getId());
        }
        CompletableFuture<String> future = new CompletableFuture<>();
        future.completeExceptionally(new DataIndexServiceException("ProcessInstance can't be retried regarding is not in ERROR State"));
        return future;
    }

    @Override
    public CompletableFuture<String> skipProcessInstance(String serviceURL, ProcessInstance processInstance) {
        String requestURI = format(SKIP_PROCESS_INSTANCE_URI, processInstance.getProcessId(), processInstance.getId());
        if (new ProcessInstanceInErrorPredicate().test(processInstance)) {
            return sendPostClientRequest(getWebClient(serviceURL), requestURI, "SKIP ProcessInstance with id: " + processInstance.getId());
        }
        CompletableFuture<String> future = new CompletableFuture<>();
        future.completeExceptionally(new DataIndexServiceException("ProcessInstance can't be skipped regarding is not in ERROR State"));
        return future;
    }

    @Override
    public CompletableFuture getProcessInstanceDiagram(String serviceURL, ProcessInstance processInstance) {
        String requestURI = format(GET_PROCESS_INSTANCE_DIAGRAM_URI, processInstance.getProcessId(), processInstance.getId());
        return sendGetClientRequest(getWebClient(serviceURL), requestURI, "Get Process Instance diagram with id: " + processInstance.getId(), null);
    }

    @Override
    public CompletableFuture<List<Node>> getProcessInstanceNodeDefinitions(String serviceURL, ProcessInstance processInstance) {
        String requestURI = format(GET_PROCESS_INSTANCE_NODE_DEFINITIONS_URI, processInstance.getProcessId());
        return sendGetClientRequest(getWebClient(serviceURL), requestURI, "Get Process Instance available nodes with id: " + processInstance.getId(), List.class);
    }

    protected CompletableFuture sendDeleteClientRequest(WebClient webClient, String requestURI, String logMessage) {
        CompletableFuture future = new CompletableFuture<>();
        webClient.delete(requestURI).putHeader("Authorization", getAuthHeader())
                .send(res -> {
                    if (res.succeeded() && (res.result().statusCode() == 200)) {
                        future.complete(res.result().bodyAsString());
                    } else {
                        future.completeExceptionally(
                                new DataIndexServiceException("FAILED: " + logMessage +
                                        " errorCode:" + res.result().statusCode() +
                                        " errorStatus:" + res.result().statusMessage()));
                    }
                });
        return future;
    }

    protected CompletableFuture sendPostClientRequest(WebClient webClient, String requestURI, String logMessage) {
        CompletableFuture future = new CompletableFuture<>();
        webClient.post(requestURI)
                .putHeader("Authorization", getAuthHeader())
                .send(res -> {
                    if (res.succeeded() && (res.result().statusCode() == 200)) {
                        future.complete(res.result().bodyAsString());
                    } else {
                        future.completeExceptionally(
                                new DataIndexServiceException("FAILED: " + logMessage +
                                        " errorCode:" + res.result().statusCode() +
                                        " errorStatus:" + res.result().statusMessage()));
                    }
                });
        return future;
    }

    protected CompletableFuture sendGetClientRequest(WebClient webClient, String requestURI, String logMessage, Class type) {
        CompletableFuture future = new CompletableFuture<>();

        webClient.get(requestURI).send(res -> {
            if (res.succeeded() && (res.result().statusCode() == 200)) {
                if (type != null) {
                    future.complete(res.result().bodyAsJson(type));
                } else {
                    future.complete(res.result().bodyAsString());
                }
            } else {
                future.completeExceptionally(
                        new DataIndexServiceException("FAILED: " + logMessage +
                                " errorCode:" + res.result().statusCode() +
                                " errorStatus:" + res.result().statusMessage()));
            }
        });

        return future;
    }

    protected String getAuthHeader() {
        return "";
    }
}
