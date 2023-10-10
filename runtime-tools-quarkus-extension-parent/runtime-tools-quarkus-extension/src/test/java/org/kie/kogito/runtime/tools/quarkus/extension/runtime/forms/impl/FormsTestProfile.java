package org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;

import io.quarkus.test.junit.QuarkusTestProfile;

import static org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.impl.FormsStorageImpl.PROJECT_FORM_STORAGE_PROP;

public class FormsTestProfile implements QuarkusTestProfile {

    private String storagePath;

    public FormsTestProfile() throws IOException {
        File storage = Files.createTempDirectory("FormsTestProfile").toFile();
        storage.deleteOnExit();
        storage.mkdir();
        storagePath = storage.getAbsolutePath();
    }

    @Override
    public Map<String, String> getConfigOverrides() {
        return Collections.singletonMap(PROJECT_FORM_STORAGE_PROP, storagePath);
    }
}
