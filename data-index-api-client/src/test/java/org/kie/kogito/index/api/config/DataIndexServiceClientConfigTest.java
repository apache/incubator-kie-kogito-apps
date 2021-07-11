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
package org.kie.kogito.index.api.config;

import org.junit.jupiter.api.Test;
import org.kie.kogito.index.api.DataIndexServiceClientConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class DataIndexServiceClientConfigTest {

    public static final String SERVICE_URL = "http://localhost:8080";
    public static final long CONNECT_TIMEOUT = 1;
    public static final long READ_TIMOUT = 2;

    private DataIndexServiceClientConfig config;

    @Test
    void getServiceURL() {
        config = createConfig(SERVICE_URL);
        assertThat(config.getServiceUrl())
                .isNotNull()
                .hasToString(SERVICE_URL);
    }

    @Test
    public void testNullServiceUrl() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> createConfig(null));
    }

    @Test
    void getConnectTimoutMillis() {
        config = createConfig(SERVICE_URL);
        assertThat(config.getConnectTimeoutMillis()).isEqualTo(CONNECT_TIMEOUT);
    }

    @Test
    void getReadTimoutMillis() {
        config = createConfig(SERVICE_URL);
        assertThat(config.getReadTimeoutMillis()).isEqualTo(READ_TIMOUT);
    }

    private DataIndexServiceClientConfig createConfig(String serviceURL) {
        return DataIndexServiceClientConfig.newBuilder()
                .serviceUrl(serviceURL)
                .connectTimeoutMillis(CONNECT_TIMEOUT)
                .readTimeoutMillis(READ_TIMOUT).build();
    }
}
