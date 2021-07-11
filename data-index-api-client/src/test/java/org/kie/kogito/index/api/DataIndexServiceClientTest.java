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

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.kogito.index.api.impl.DataIndexServiceClientImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.kie.kogito.index.api.impl.DataIndexServiceClientImpl.ABORT_MUTATION_NAME;
import static org.kie.kogito.index.api.impl.DataIndexServiceClientImpl.RETRY_MUTATION_NAME;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DataIndexServiceClientTest {

    @Mock
    private GraphQLServiceClient queryServiceClient;

    private DataIndexServiceClient client;

    @BeforeEach
    void setUp() {
        client = new DataIndexServiceClientImpl(queryServiceClient);
    }

    @Test
    void abortProcessInstanceTest() {
        client.abortProcessInstance("pId");
        verify(queryServiceClient).executeGraphqlRequest(eq(ABORT_MUTATION_NAME),
                eq(String.format("{ \"query\" : \"mutation{ %s ( processInstanceId: \\\"%s\\\") }\"}", ABORT_MUTATION_NAME, "pId")),
                eq(String.class));
    }

    @Test
    void retryProcessInstanceTest() {
        client.retryProcessInstance("pId");
        verify(queryServiceClient).executeGraphqlRequest(eq(RETRY_MUTATION_NAME),
                eq(String.format("{ \"query\" : \"mutation{ %s ( processInstanceId: \\\"%s\\\") }\"}", RETRY_MUTATION_NAME, "pId")),
                eq(String.class));
    }

    @Test
    void close() throws IOException {
        client.close();
        verify(queryServiceClient).close();
    }
}
