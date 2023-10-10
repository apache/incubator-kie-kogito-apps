package org.kie.kogito.swf.tools.custom.dashboard.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;

import io.quarkus.test.junit.QuarkusTestProfile;

import static org.kie.kogito.swf.tools.custom.dashboard.impl.CustomDashboardStorageImpl.PROJECT_CUSTOM_DASHBOARD_STORAGE_PROP;

public class CustomDashboardStorageTestProfile implements QuarkusTestProfile {

    private String storagePath;

    public CustomDashboardStorageTestProfile() throws IOException {
        File storage = Files.createTempDirectory("CustomDashboardStorageTestProfile").toFile();
        storage.deleteOnExit();
        storage.mkdir();
        storagePath = storage.getAbsolutePath();
    }

    @Override
    public Map<String, String> getConfigOverrides() {
        return Collections.singletonMap(PROJECT_CUSTOM_DASHBOARD_STORAGE_PROP, storagePath);
    }
}
