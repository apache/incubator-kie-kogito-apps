/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.jitexecutor.bpmn;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.drools.io.FileSystemResource;
import org.drools.util.IoUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.api.definition.process.Process;
import org.kie.kogito.jitexecutor.bpmn.responses.JITBPMNValidationResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JITBPMNServiceImplTest {

    private static String validModel;
    private static String invalidModel;

    private static JITBPMNService jitBpmnService;

    @BeforeAll
    public static void setup() throws IOException {
        validModel = new String(IoUtils.readBytesFromInputStream(JITBPMNService.class.getResourceAsStream("/ValidModel.bpmn")));
        invalidModel = new String(IoUtils.readBytesFromInputStream(JITBPMNService.class.getResourceAsStream("/InvalidModel.bpmn")));

        jitBpmnService = new JITBPMNServiceImpl();
    }

    @Test
    void evaluateModel_SingleValid() {
        JITBPMNValidationResult retrieved = jitBpmnService.evaluateModel(validModel);
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getErrors()).isNotNull().isEmpty();
    }

    @Test
    void evaluateModel_SingleInvalid() {
        JITBPMNValidationResult retrieved = jitBpmnService.evaluateModel(invalidModel);
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getErrors()).isNotNull().hasSize(1);
    }

    @Test
    void parseModelXml_SingleValid() {
        Collection<Process> retrieved = JITBPMNServiceImpl.parseModelXml(validModel);
        assertThat(retrieved).isNotNull().hasSize(1);
    }

    @Test
    void parseModelResource_SingleValid() {
        String fileName = "src/test/resources/ProcessWithDocumentation.bpmn";
        Collection<Process> retrieved = JITBPMNServiceImpl.parseModelResource(new FileSystemResource(new File(fileName)));
        assertThat(retrieved).isNotNull().hasSize(1);
    }

    @Test
    void parseModelXml_SingleInValid() {
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> JITBPMNServiceImpl.parseModelXml(invalidModel),
                "Expected parseModelXml to throw, but it didn't");
        String expectedMessage = "Could not parse";
        assertThat(thrown.getMessage()).contains(expectedMessage);
    }

    @Test
    void parseModelResource_SingleInValid() {
        String fileName = "src/test/resources/UnparsableModel.bpmn";
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> JITBPMNServiceImpl.parseModelResource(new FileSystemResource(new File(fileName))),
                "Expected parseModelXml to throw, but it didn't");
        String expectedMessage = "Could not parse";
        assertThat(thrown.getMessage()).contains(expectedMessage);
    }
}
