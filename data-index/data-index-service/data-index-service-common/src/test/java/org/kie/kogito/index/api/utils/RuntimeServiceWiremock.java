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

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.okForContentType;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static java.lang.String.format;
import static java.util.Collections.singletonMap;
import static org.kie.kogito.index.TestUtils.readFileContent;
import static org.kie.kogito.index.api.DataIndexApiImpl.ABORT_PROCESS_INSTANCE_URI;
import static org.kie.kogito.index.api.DataIndexApiImpl.GET_PROCESS_INSTANCE_DIAGRAM_URI;
import static org.kie.kogito.index.api.DataIndexApiImpl.GET_PROCESS_INSTANCE_NODES_URI;
import static org.kie.kogito.index.api.DataIndexApiImpl.RETRY_PROCESS_INSTANCE_URI;

public class RuntimeServiceWiremock implements QuarkusTestResourceLifecycleManager {

    public static final String RUNTIME_SERVICE_URL = "kogito.service.url";
    public static final String PROCESS_INSTANCE_ID = "e0a18db6-b57d-4f0c-938b-25d4e76ec2f4";
    public static final String PROCESS_INSTANCE_ID_FAIL = "e0a18db6-b57d-4f0c-938b-25d4e76ec2f8";
    public static final String PROCESS_ID = "travels";

    public final static String OK_RESPONSE_WITH_ID = "{\n" +
            "  \"id\": \"%s\",\n" +
            "}";

    public final static String jsonNodeInstanceListString =
            "[{" +
                    "\"id\":\"a95cbfe8-c9b5-4c7a-b1c7-7ea85afb7e24\"," +
                    "\"nodeInstanceId\":\"966fae5a-738c-4d7a-bc12-1a989ec57e22\"," +
                    "\"nodeId\":\"3\"," +
                    "\"name\":\"HRInterview\"," +
                    "\"state\":0," +
                    "\"phase\":\"active\"," +
                    "\"phaseStatus\":\"Ready\"," +
                    "\"parameters\":{" +
                    "\"candidate\":{" +
                    "\"name\":\"string\"," +
                    "\"email\":\"string\"," +
                    "\"salary\":0," +
                    "\"skills\":\"string\"" +
                    "}," +
                    "\"NodeName\":\"HR Interview\"," +
                    "\"TaskName\":\"HRInterview\"," +
                    "\"Skippable\":\"false\"," +
                    "\"ActorId\":\"john\"}," +
                    "\"results\":{}" +
                    "}]";

    private WireMockServer server;

    @Override
    public Map<String, String> start() {
        server = new WireMockServer(options().dynamicPort());
        server.start();
        configureFor(server.port());
        stubFor(delete(urlEqualTo(format(ABORT_PROCESS_INSTANCE_URI, PROCESS_ID, PROCESS_INSTANCE_ID)))
                .willReturn(okJson(format(OK_RESPONSE_WITH_ID, PROCESS_INSTANCE_ID))));
        stubFor(delete(urlEqualTo(format(ABORT_PROCESS_INSTANCE_URI, PROCESS_ID, PROCESS_INSTANCE_ID_FAIL)))
                .willReturn(notFound()));
        stubFor(post(urlEqualTo(format(RETRY_PROCESS_INSTANCE_URI, PROCESS_ID, PROCESS_INSTANCE_ID)))
                .willReturn(okJson(format(OK_RESPONSE_WITH_ID, PROCESS_INSTANCE_ID))));
        stubFor(post(urlEqualTo(format(RETRY_PROCESS_INSTANCE_URI, PROCESS_ID, PROCESS_INSTANCE_ID_FAIL)))
                .willReturn(notFound()));
        stubFor(get(urlEqualTo(format(GET_PROCESS_INSTANCE_DIAGRAM_URI, PROCESS_ID, PROCESS_INSTANCE_ID)))
                .willReturn(okForContentType("image/svg+xml", getTravelsSVGFile())));
        stubFor(get(urlEqualTo(format(GET_PROCESS_INSTANCE_DIAGRAM_URI, PROCESS_ID, PROCESS_INSTANCE_ID_FAIL)))
                .willReturn(notFound()));
        stubFor(get(urlEqualTo(format(GET_PROCESS_INSTANCE_NODES_URI, PROCESS_ID, PROCESS_INSTANCE_ID)))
                .willReturn(okJson(format(jsonNodeInstanceListString, PROCESS_INSTANCE_ID))));

        stubFor(get(urlEqualTo(format(GET_PROCESS_INSTANCE_NODES_URI, PROCESS_ID, PROCESS_INSTANCE_ID_FAIL)))
                .willReturn(notFound()));

        return singletonMap(RUNTIME_SERVICE_URL, server.baseUrl());
    }

    @Override
    public void stop() {
        if (server != null) {
            server.stop();
        }
    }

    public String getTravelsSVGFile() {
        try {
            return readFileContent("travels.svg");
        } catch (Exception e) {
            return "Not Found";
        }
    }
}
