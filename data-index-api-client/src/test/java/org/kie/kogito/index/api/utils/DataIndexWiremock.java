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

package org.kie.kogito.index.api.utils;

import java.util.Map;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static java.lang.String.format;
import static java.util.Collections.singletonMap;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.kie.kogito.index.api.impl.DataIndexServiceClientImpl.ABORT_MUTATION_NAME;
import static org.kie.kogito.index.api.impl.DataIndexServiceClientImpl.GET_PROCESS_INSTANCE_DIAGRAM_MUTATION_NAME;
import static org.kie.kogito.index.api.impl.DataIndexServiceClientImpl.GET_PROCESS_INSTANCE_NODES_MUTATION_NAME;
import static org.kie.kogito.index.api.impl.DataIndexServiceClientImpl.MUTATION_BY_PROCESS_INSTANCE_ID_REQUEST;
import static org.kie.kogito.index.api.impl.DataIndexServiceClientImpl.RETRY_MUTATION_NAME;

public class DataIndexWiremock implements QuarkusTestResourceLifecycleManager {

    public static final String DATAINDEX_SERVICE_URL = "kogito.dataindex.http.url";
    public static final String PROCESS_INSTANCE_ID = "9e41bc95-81b2-4893-91a9-947a36f36776";
    public static final String PROCESS_INSTANCE_ID_FAIL = "8021ded8-e9bf-4761-92b4-4fcac3a8af85";
    private final static String abortSuccessResponse = "{\n" +
            "  \"data\": {\n" +
            "    \"ProcessInstanceAbort\": \"{\\\"id\\\":\\\"9e41bc95-81b2-4893-91a9-947a36f36776\\\",\\\"it_approval\\\":true,\\\"hr_approval\\\":true}\"\n" +
            "  }\n" +
            "}";
    private final static String abortErrorResponse = "{\n" +
            "  \"errors\": [\n" +
            "    {\n" +
            "      \"message\": \"Exception while fetching data (/ProcessInstanceAbort) : ProcessInstance can't be aborted regarding is not in ACTIVE State\",\n" +
            "      \"locations\": [\n" +
            "        {\n" +
            "          \"line\": 32,\n" +
            "          \"column\": 12\n" +
            "        }\n" +
            "      ],\n" +
            "      \"path\": [\n" +
            "        \"ProcessInstanceAbort\"\n" +
            "      ],\n" +
            "      \"extensions\": {\n" +
            "        \"classification\": \"DataFetchingException\"\n" +
            "      }\n" +
            "    }\n" +
            "  ],\n" +
            "  \"data\": {\n" +
            "    \"ProcessInstanceAbort\": null\n" +
            "  }\n" +
            "}";
    private final static String retrySuccessResponse = "{\n" +
            "  \"data\": {\n" +
            "    \"ProcessInstanceRetry\": \"{\\\"id\\\":\\\"9e41bc95-81b2-4893-91a9-947a36f36776\\\",\\\"it_approval\\\":true,\\\"hr_approval\\\":true}\"\n" +
            "  }\n" +
            "}";
    private final static String retryErrorResponse = "{\n" +
            "  \"errors\": [\n" +
            "    {\n" +
            "      \"message\": \"Exception while fetching data (/ProcessInstanceRetry) : ProcessInstance can't be retried regarding is not in ERROR State\",\n" +
            "      \"locations\": [\n" +
            "        {\n" +
            "          \"line\": 32,\n" +
            "          \"column\": 12\n" +
            "        }\n" +
            "      ],\n" +
            "      \"path\": [\n" +
            "        \"ProcessInstanceRetry\"\n" +
            "      ],\n" +
            "      \"extensions\": {\n" +
            "        \"classification\": \"DataFetchingException\"\n" +
            "      }\n" +
            "    }\n" +
            "  ],\n" +
            "  \"data\": {\n" +
            "    \"ProcessInstanceRetry\": null\n" +
            "  }\n" +
            "}";

    private final static String getProcessInstanceDiagramResponse = "{\n" +
            "  \"data\": {\n" +
            "    \"GetProcessInstanceDiagram\": \"svgContent\"\n" +
            "  }\n" +
            "}";
    private final static String getProcessInstanceNodesResponse = "{\n" +
            "  \"data\": {\n" +
            "    \"GetProcessInstanceNodes\": [\n" +
            "      {\n" +
            "        \"id\": \"8021ded8-e9bf-4761-92b4-4fcac3a8af85\",\n" +
            "        \"nodeId\": \"3\",\n" +
            "        \"name\": \"HRInterview\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    private WireMockServer server;

    @Override
    public Map<String, String> start() {
        server = new WireMockServer(options().dynamicPort());
        server.start();
        configureFor(server.port());
        stubFor(post(urlEqualTo("/graphql"))
                .withRequestBody(equalTo(String.format(MUTATION_BY_PROCESS_INSTANCE_ID_REQUEST,
                        ABORT_MUTATION_NAME, PROCESS_INSTANCE_ID, "")))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withBody(abortSuccessResponse)));
        stubFor(post(urlEqualTo("/graphql"))
                .withRequestBody(equalTo(String.format(MUTATION_BY_PROCESS_INSTANCE_ID_REQUEST,
                        ABORT_MUTATION_NAME, PROCESS_INSTANCE_ID_FAIL, "")))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withBody(abortErrorResponse)));
        stubFor(post(urlEqualTo("/graphql"))
                .withRequestBody(equalTo(String.format(MUTATION_BY_PROCESS_INSTANCE_ID_REQUEST,
                        RETRY_MUTATION_NAME, PROCESS_INSTANCE_ID, "")))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withBody(retrySuccessResponse)));
        stubFor(post(urlEqualTo("/graphql"))
                .withRequestBody(equalTo(String.format(MUTATION_BY_PROCESS_INSTANCE_ID_REQUEST,
                        RETRY_MUTATION_NAME, PROCESS_INSTANCE_ID_FAIL, "")))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withBody(retryErrorResponse)));
        stubFor(post(urlEqualTo("/graphql"))
                .withRequestBody(equalTo(String.format(MUTATION_BY_PROCESS_INSTANCE_ID_REQUEST,
                        GET_PROCESS_INSTANCE_DIAGRAM_MUTATION_NAME, PROCESS_INSTANCE_ID, "")))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withBody(getProcessInstanceDiagramResponse)));
        stubFor(post(urlEqualTo("/graphql"))
                .withRequestBody(equalTo(String.format(MUTATION_BY_PROCESS_INSTANCE_ID_REQUEST,
                        GET_PROCESS_INSTANCE_DIAGRAM_MUTATION_NAME, PROCESS_INSTANCE_ID_FAIL, "")))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withBody(retryErrorResponse)));
        stubFor(post(urlEqualTo("/graphql"))
                .withRequestBody(equalTo(String.format(MUTATION_BY_PROCESS_INSTANCE_ID_REQUEST,
                        GET_PROCESS_INSTANCE_NODES_MUTATION_NAME, PROCESS_INSTANCE_ID, "{id nodeId name}")))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withBody(getProcessInstanceNodesResponse)));
        stubFor(post(urlEqualTo("/graphql"))
                .withRequestBody(equalTo(String.format(MUTATION_BY_PROCESS_INSTANCE_ID_REQUEST,
                        GET_PROCESS_INSTANCE_NODES_MUTATION_NAME, PROCESS_INSTANCE_ID_FAIL, "{id nodeId name}")))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withBody(retryErrorResponse)));

        return singletonMap(DATAINDEX_SERVICE_URL, server.baseUrl());
    }

    @Override
    public void stop() {
        if (server != null) {
            server.stop();
        }
    }
}
