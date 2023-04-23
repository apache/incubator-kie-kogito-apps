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

package org.kie.kogito.index.addon.api;

import java.net.URL;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.dashboard.impl.CustomDashboardStorageService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class KogitoAddonRuntimeClientImplTest {

    private static String DASHBOARD_NAMES = "age.dash.yaml,products.dash.yaml";
    private static String DASHBOARD_NAME = "age.dash.yaml";

    KogitoAddonRuntimeClientImpl kogitoAddonRuntimeClientImpl;
    private CustomDashboardStorageService customDashboardStorage;

    @BeforeEach
    public void setUp() {
        URL tempFolder = Thread.currentThread().getContextClassLoader().getResource("dashboard/");

        customDashboardStorage = new CustomDashboardStorageService(tempFolder, Optional.empty());
        kogitoAddonRuntimeClientImpl = new KogitoAddonRuntimeClientImpl();
        kogitoAddonRuntimeClientImpl.setStorage(customDashboardStorage);
    }

    @Test
    public void testGetCustomDashboardCount() throws ExecutionException, InterruptedException {
        assertEquals(2, kogitoAddonRuntimeClientImpl.getCustomDashboardCount("host").get());
    }

    @Test
    public void testGetCustomDashboards() throws ExecutionException, InterruptedException {
        assertEquals(2, kogitoAddonRuntimeClientImpl.getCustomDashboards(null, null).get().size());

        assertEquals(2, kogitoAddonRuntimeClientImpl.getCustomDashboards("", "").get().size());

        assertEquals(2, kogitoAddonRuntimeClientImpl.getCustomDashboards("", DASHBOARD_NAMES).get().size());

        assertEquals(1, kogitoAddonRuntimeClientImpl.getCustomDashboards("", DASHBOARD_NAME).get().size());
    }

    @Test
    public void testGetCustomDashboardContent() throws ExecutionException, InterruptedException {
        String content = kogitoAddonRuntimeClientImpl.getCustomDashboardContent("", DASHBOARD_NAME).get();
        assertNotNull(content);
    }
}
