/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.taskassigning.index.service.client;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.kie.kogito.taskassigning.auth.NoAuthenticationCredentials;

import io.quarkus.test.junit.QuarkusTest;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class DataIndexServiceClientFactoryTest {

    @Inject
    DataIndexServiceClientFactory serviceClientFactory;

    @Test
    void newClient() {
        String serviceUrlMock = "http://localhost:8180/service";
        DataIndexServiceClient client = serviceClientFactory.newClient(DataIndexServiceClientConfig.newBuilder()
                .serviceUrl(serviceUrlMock).build(),
                NoAuthenticationCredentials.INSTANCE);
        assertThat(client).isNotNull();
    }
}
