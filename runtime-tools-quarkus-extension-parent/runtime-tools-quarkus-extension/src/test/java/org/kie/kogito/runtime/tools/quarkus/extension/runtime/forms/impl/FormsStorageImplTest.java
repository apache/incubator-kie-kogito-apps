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

package org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.FormsStorage;
import org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.model.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FormsStorageImplTest {

    private static final String TEST_FORM_CONTENT = "<div></div>";
    private static final String STYLE1 = "style1";
    private static final String SCRIPT1 = "script1";

    private static final String FORM_NAME = "hiring_HRInterview";
    private static final String PARTIAL_FORM_NAME = "hiring";
    private static final String FORM_NAME_WITH_OUT_CONFIG = "hiring_HRInterviewWithoutConfig";

    @TempDir
    static File storage;

    private FormsStorage formsStorage;

    @BeforeAll
    public void init() throws IOException {
        URL formsFolder = Thread.currentThread().getContextClassLoader().getResource("forms");

        formsStorage = new FormsStorageImpl(formsFolder, storage.toURI().toURL());
    }

    @Test
    public void testGetFormsCount() {
        assertEquals(2, formsStorage.getFormsCount());
    }

    @Test
    public void testGetFormInfoList() {
        Collection<FormInfo> formInfosAll = formsStorage.getFormInfoList(null);
        assertEquals(2, formInfosAll.size());

        FormFilter filterEmpty = new FormFilter();
        filterEmpty.setNames(Collections.emptyList());
        Collection<FormInfo> formInfosAllEmptyFilter = formsStorage.getFormInfoList(filterEmpty);
        assertEquals(2, formInfosAll.size());

        FormFilter filter = new FormFilter();
        filter.setNames(Arrays.asList(FORM_NAME));

        Collection<FormInfo> formInfos = formsStorage.getFormInfoList(filter);
        assertEquals(1, formInfos.size());

        FormFilter partialFilter = new FormFilter();
        partialFilter.setNames(Arrays.asList(PARTIAL_FORM_NAME));

        Collection<FormInfo> formInfosPartial = formsStorage.getFormInfoList(partialFilter);
        assertEquals(2, formInfosPartial.size());
    }

    @Test
    public void testGetFormContent() throws IOException {
        Form formContent = formsStorage.getFormContent(FORM_NAME);
        assertNotNull(formContent);
        FormInfo formInfo = formContent.getFormInfo();
        assertEquals(FORM_NAME, formInfo.getName());
    }

    @Test
    public void testGetFormContentWithoutConfig() {
        assertThrows(RuntimeException.class, () -> formsStorage.getFormContent(FORM_NAME_WITH_OUT_CONFIG));
        assertThrows(RuntimeException.class, () -> formsStorage.getFormContent("ERROR"));
    }

    @Test
    public void testUpdateFormContentInvalidForms() {
        assertThrows(RuntimeException.class, () -> formsStorage.updateFormContent(FORM_NAME_WITH_OUT_CONFIG, new FormContent()));
        assertThrows(RuntimeException.class, () -> formsStorage.updateFormContent(FORM_NAME, null));
    }

    @Test
    public void testUpdateValidForms() throws IOException {

        File sourceFile = new File(storage.toURI().resolve(FORM_NAME + ".html"));
        File contentFile = new File(storage.toURI().resolve(FORM_NAME + ".config"));

        sourceFile.createNewFile();
        contentFile.createNewFile();

        FileUtils.write(sourceFile, "", StandardCharsets.UTF_8);
        FileUtils.write(contentFile, "", StandardCharsets.UTF_8);

        Form form = formsStorage.getFormContent(FORM_NAME);
        FormResources resources = new FormResources();

        resources.getStyles().put(STYLE1, STYLE1);
        resources.getScripts().put(SCRIPT1, SCRIPT1);

        FormContent content = new FormContent(TEST_FORM_CONTENT, new FormConfiguration(form.getConfiguration().getSchema(), resources));

        formsStorage.updateFormContent(FORM_NAME, content);

        Form newForm = formsStorage.getFormContent(FORM_NAME);

        assertEquals(TEST_FORM_CONTENT, newForm.getSource());
        assertEquals(form.getConfiguration().getSchema(), newForm.getConfiguration().getSchema());
        assertEquals(STYLE1, newForm.getConfiguration().getResources().getStyles().get(STYLE1));
        assertEquals(SCRIPT1, newForm.getConfiguration().getResources().getScripts().get(SCRIPT1));
    }
}
