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

package org.kie.kogito.taskassigning.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.quarkus.oidc.client.OidcClient;
import io.quarkus.oidc.client.OidcClients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OidcClientLookupTest {

    @Mock
    private OidcClients oidcClients;

    @Mock
    private OidcClient oidcClient;

    private OidcClientLookup oidcClientLookup;

    @BeforeEach
    void setUp() {
        oidcClientLookup = new OidcClientLookup(oidcClients);
    }

    @Test
    void lookup() {
        String oidcClientId = "oidcClientId";
        doReturn(oidcClient).when(oidcClients).getClient(oidcClientId);
        assertThat(oidcClientLookup.lookup(oidcClientId)).isSameAs(oidcClient);
    }

    @Test
    void lookupDefault() {
        doReturn(oidcClient).when(oidcClients).getClient();
        assertThat(oidcClientLookup.lookup("Default")).isSameAs(oidcClient);
    }
}
