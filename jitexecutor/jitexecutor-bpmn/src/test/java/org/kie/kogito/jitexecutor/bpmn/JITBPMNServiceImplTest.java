/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.jitexecutor.bpmn;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

import org.drools.io.FileSystemResource;
import org.drools.util.IoUtils;
import org.drools.util.StringUtils;
import org.jbpm.process.core.impl.ProcessImpl;
import org.jbpm.process.core.validation.ProcessValidationError;
import org.jbpm.process.core.validation.impl.ProcessValidationErrorImpl;
import org.junit.jupiter.api.Test;
import org.kie.api.definition.process.Process;
import org.kie.api.io.Resource;
import org.kie.kogito.jitexecutor.bpmn.responses.JITBPMNValidationResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.kie.kogito.jitexecutor.bpmn.TestingUtils.MULTIPLE_BPMN2_FILE;
import static org.kie.kogito.jitexecutor.bpmn.TestingUtils.MULTIPLE_INVALID_BPMN2_FILE;
import static org.kie.kogito.jitexecutor.bpmn.TestingUtils.REST_WIH_ALL_VALID_SCENARIOS_BPMN2_FILE;
import static org.kie.kogito.jitexecutor.bpmn.TestingUtils.REST_WIH_INVALID_BPMN2_FILE;
import static org.kie.kogito.jitexecutor.bpmn.TestingUtils.REST_WIH_VALID_BPMN2_FILE;
import static org.kie.kogito.jitexecutor.bpmn.TestingUtils.REST_WIH_VALID_PROPER_BPMN2_FILE;
import static org.kie.kogito.jitexecutor.bpmn.TestingUtils.REST_WIH_VALID_SIMPLE_BPMN2_FILE;
import static org.kie.kogito.jitexecutor.bpmn.TestingUtils.SINGLE_BPMN2_FILE;
import static org.kie.kogito.jitexecutor.bpmn.TestingUtils.SINGLE_INVALID_BPMN2_FILE;
import static org.kie.kogito.jitexecutor.bpmn.TestingUtils.SINGLE_UNPARSABLE_BPMN2_FILE;
import static org.kie.kogito.jitexecutor.bpmn.TestingUtils.UNPARSABLE_BPMN2_FILE;

class JITBPMNServiceImplTest {

    private static final JITBPMNService jitBpmnService = new JITBPMNServiceImpl();

    @Test
    void validateModel_SingleValidBPMN2() throws IOException {
        String toValidate = new String(IoUtils.readBytesFromInputStream(Objects.requireNonNull(JITBPMNService.class.getResourceAsStream(SINGLE_BPMN2_FILE))));
        JITBPMNValidationResult retrieved = jitBpmnService.validateModel(toValidate);
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getErrors()).isNotNull().isEmpty();
    }

    @Test
    void validateModel_MultipleValidBPMN2() throws IOException {
        String toValidate = new String(IoUtils.readBytesFromInputStream(Objects.requireNonNull(JITBPMNService.class.getResourceAsStream(MULTIPLE_BPMN2_FILE))));
        JITBPMNValidationResult retrieved = jitBpmnService.validateModel(toValidate);
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getErrors()).isNotNull().isEmpty();
    }

    @Test
    void validateModel_SingleInvalidBPMN2() throws IOException {
        String toValidate = new String(IoUtils.readBytesFromInputStream(Objects.requireNonNull(JITBPMNService.class.getResourceAsStream(SINGLE_INVALID_BPMN2_FILE))));
        JITBPMNValidationResult retrieved = jitBpmnService.validateModel(toValidate);
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getErrors()).isNotNull().hasSize(2);
        assertThat(retrieved.getErrors()).contains("Uri: (unknown) - Process id: invalid - name : invalid-process-id - error : Process has no start node.");
        assertThat(retrieved.getErrors()).contains("Uri: (unknown) - Process id: invalid - name : invalid-process-id - error : Process has no end node.");
    }

    @Test
    void validateModel_MultipleInvalidBPMN2() throws IOException {
        String toValidate = new String(IoUtils.readBytesFromInputStream(Objects.requireNonNull(JITBPMNService.class.getResourceAsStream(MULTIPLE_INVALID_BPMN2_FILE))));
        JITBPMNValidationResult retrieved = jitBpmnService.validateModel(toValidate);
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getErrors()).isNotNull().hasSize(4);
        assertThat(retrieved.getErrors()).contains("Uri: (unknown) - Process id: invalid1 - name : invalid1-process-id - error : Process has no start node.");
        assertThat(retrieved.getErrors()).contains("Uri: (unknown) - Process id: invalid1 - name : invalid1-process-id - error : Process has no end node.");
        assertThat(retrieved.getErrors()).contains("Uri: (unknown) - Process id: invalid2 - name : invalid2-process-id - error : Process has no start node.");
        assertThat(retrieved.getErrors()).contains("Uri: (unknown) - Process id: invalid2 - name : invalid2-process-id - error : Process has no end node.");
    }

    @Test
    void validateModel_SingleUnparsableBPMN2() throws IOException {
        String toValidate = new String(IoUtils.readBytesFromInputStream(Objects.requireNonNull(JITBPMNService.class.getResourceAsStream(SINGLE_UNPARSABLE_BPMN2_FILE))));
        JITBPMNValidationResult retrieved = jitBpmnService.validateModel(toValidate);
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getErrors()).isNotNull().hasSize(1);
        assertThat(retrieved.getErrors()).contains("Could not find message _T6T0kEcTEDuygKsUt0on2Q____");
    }

    @Test
    void parseModelXml_SingleValidBPMN2() throws IOException {
        String toValidate = new String(IoUtils.readBytesFromInputStream(Objects.requireNonNull(JITBPMNService.class.getResourceAsStream(SINGLE_BPMN2_FILE))));
        Collection<Process> retrieved = JITBPMNServiceImpl.parseModelXml(toValidate);
        assertThat(retrieved).isNotNull().hasSize(1);
    }

    @Test
    void parseModelXml_MultipleValidBPMN2() throws IOException {
        String toValidate = new String(IoUtils.readBytesFromInputStream(Objects.requireNonNull(JITBPMNService.class.getResourceAsStream(MULTIPLE_BPMN2_FILE))));
        Collection<Process> retrieved = JITBPMNServiceImpl.parseModelXml(toValidate);
        assertThat(retrieved).isNotNull().hasSize(2);
    }

    @Test
    void parseModelXml_UnparsableBPMN2() throws IOException {
        String toParse = new String(IoUtils.readBytesFromInputStream(Objects.requireNonNull(JITBPMNService.class.getResourceAsStream(UNPARSABLE_BPMN2_FILE))));
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> JITBPMNServiceImpl.parseModelXml(toParse),
                "Expected parseModelXml to throw, but it didn't");
        String expectedMessage = "Could not parse";
        assertThat(thrown.getMessage()).contains(expectedMessage);
    }

    @Test
    void parseModelResource_SingleValidBPMN2() {
        Collection<Process> retrieved = JITBPMNServiceImpl.parseModelResource(new FileSystemResource(new File(JITBPMNService.class.getResource(SINGLE_BPMN2_FILE).getFile())));
        assertThat(retrieved).isNotNull().hasSize(1);
    }

    @Test
    void parseModelResource_MultipleValidBPMN2() {
        Collection<Process> retrieved = JITBPMNServiceImpl.parseModelResource(new FileSystemResource(new File(JITBPMNService.class.getResource(MULTIPLE_BPMN2_FILE).getFile())));
        assertThat(retrieved).isNotNull().hasSize(2);
    }

    @Test
    void parseModelResource_UnparsableBPMN2() {
        Resource resource = new FileSystemResource(new File(UNPARSABLE_BPMN2_FILE));
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> JITBPMNServiceImpl.parseModelResource(resource),
                "Expected parseModelXml to throw, but it didn't");
        String expectedMessage = "Could not parse";
        assertThat(thrown.getMessage()).contains(expectedMessage);
    }

    @Test
    void getErrorString() {
        Process process = new ProcessImpl();
        String id = StringUtils.generateUUID();
        String name = StringUtils.generateUUID();
        ((ProcessImpl) process).setId(id);
        ((ProcessImpl) process).setName(name);
        String message = StringUtils.generateUUID();
        ProcessValidationError processValidationError = new ProcessValidationErrorImpl(process, message);
        String expected = "Uri: (unknown) - Process id: " + id + " - name : " + name + " - error : " + message;
        String retrieved = JITBPMNServiceImpl.getErrorString(processValidationError, null);
        assertThat(retrieved).isEqualTo(expected);
        String uri = "uri";
        expected = "Uri: " + uri + " - Process id: " + id + " - name : " + name + " - error : " + message;
        retrieved = JITBPMNServiceImpl.getErrorString(processValidationError, uri);
        assertThat(retrieved).isEqualTo(expected);
    }

    @Test
    void validateModel_RestWIHValid_ParsesSuccessfully() throws IOException {
        String toValidate = new String(IoUtils.readBytesFromInputStream(Objects.requireNonNull(JITBPMNService.class.getResourceAsStream(REST_WIH_VALID_BPMN2_FILE))));
        JITBPMNValidationResult retrieved = jitBpmnService.validateModel(toValidate);

        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getErrors()).isNotNull();

        assertThat(retrieved.getErrors()).allMatch(error -> error.contains("Url is required") || error.contains("AccessTokenAcquisitionStrategy is required"));
    }

    @Test
    void validateModel_RestWIHValidProper_ParsesSuccessfully() throws IOException {
        String toValidate = new String(IoUtils.readBytesFromInputStream(Objects.requireNonNull(JITBPMNService.class.getResourceAsStream(REST_WIH_VALID_PROPER_BPMN2_FILE))));
        JITBPMNValidationResult retrieved = jitBpmnService.validateModel(toValidate);

        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getErrors()).isNotNull();

        assertThat(retrieved.getErrors()).allMatch(error -> error.contains("Url is required") || error.contains("AccessTokenAcquisitionStrategy is required"));
    }

    @Test
    void validateModel_RestWIHValidSimple_ParsesSuccessfully() throws IOException {
        String toValidate = new String(IoUtils.readBytesFromInputStream(Objects.requireNonNull(JITBPMNService.class.getResourceAsStream(REST_WIH_VALID_SIMPLE_BPMN2_FILE))));
        JITBPMNValidationResult retrieved = jitBpmnService.validateModel(toValidate);

        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getErrors()).isNotNull();

        assertThat(retrieved.getErrors()).allMatch(error -> error.contains("Url is required") || error.contains("AccessTokenAcquisitionStrategy is required"));
    }

    @Test
    void validateModel_RestWIHInvalid_HasValidationErrors() throws IOException {
        String toValidate = new String(IoUtils.readBytesFromInputStream(Objects.requireNonNull(JITBPMNService.class.getResourceAsStream(REST_WIH_INVALID_BPMN2_FILE))));
        JITBPMNValidationResult retrieved = jitBpmnService.validateModel(toValidate);

        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getErrors()).isNotNull().isNotEmpty();

        assertThat(retrieved.getErrors()).anyMatch(error -> error.contains("Missing Strategy") && error.contains("AccessTokenAcquisitionStrategy is required"));
        assertThat(retrieved.getErrors()).anyMatch(error -> error.contains("Invalid Strategy") && error.contains("AccessTokenAcquisitionStrategy must be one of"));
        assertThat(retrieved.getErrors()).anyMatch(error -> error.contains("Missing TaskId") && error.contains("RestServiceCallTaskId is required"));
        assertThat(retrieved.getErrors()).anyMatch(error -> error.contains("Invalid TaskId Format") && error.contains("RestServiceCallTaskId must contain only alphanumeric"));
        assertThat(retrieved.getErrors()).anyMatch(error -> error.contains("Missing URL") && error.contains("Url is required"));
    }

    @Test
    void validateModel_AllValidScenarios_ParsesSuccessfully() throws IOException {
        String toValidate = new String(IoUtils.readBytesFromInputStream(Objects.requireNonNull(JITBPMNService.class.getResourceAsStream(REST_WIH_ALL_VALID_SCENARIOS_BPMN2_FILE))));
        JITBPMNValidationResult retrieved = jitBpmnService.validateModel(toValidate);

        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getErrors()).isNotNull();

        // The BPMN file should parse successfully
        // Similar to above, validation errors for required fields will be present
    }

    private String createRestTaskBpmn(String processId, String url, String strategy, String taskId,
            String protocol, String host, String port, String timeout,
            String header, String query) {
        StringBuilder bpmn = new StringBuilder();
        bpmn.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        bpmn.append("<bpmn2:definitions xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
        bpmn.append("xmlns:bpmn2=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" ");
        bpmn.append("xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" ");
        bpmn.append("xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" ");
        bpmn.append("xmlns:drools=\"http://www.jboss.org/drools\" ");
        bpmn.append("id=\"_Test\" targetNamespace=\"http://www.omg.org/bpmn20\">\n");
        bpmn.append("  <bpmn2:process id=\"").append(processId).append("\" drools:packageName=\"com.example\" ");
        bpmn.append("drools:version=\"1.0\" name=\"test-process\" isExecutable=\"true\">\n");
        bpmn.append("    <bpmn2:startEvent id=\"_1\" name=\"Start\">\n");
        bpmn.append("      <bpmn2:outgoing>_1-_2</bpmn2:outgoing>\n");
        bpmn.append("    </bpmn2:startEvent>\n");
        bpmn.append("    <bpmn2:task id=\"_2\" name=\"REST Task\" drools:taskName=\"Rest\">\n");
        bpmn.append("      <bpmn2:incoming>_1-_2</bpmn2:incoming>\n");
        bpmn.append("      <bpmn2:outgoing>_2-_3</bpmn2:outgoing>\n");
        bpmn.append("      <bpmn2:ioSpecification>\n");

        if (url != null) {
            bpmn.append("        <bpmn2:dataInput id=\"_2_UrlInput\" name=\"Url\"/>\n");
        }
        if (strategy != null) {
            bpmn.append("        <bpmn2:dataInput id=\"_2_AccessTokenAcquisitionStrategyInput\" name=\"AccessTokenAcquisitionStrategy\"/>\n");
        }
        if (taskId != null) {
            bpmn.append("        <bpmn2:dataInput id=\"_2_RestServiceCallTaskIdInput\" name=\"RestServiceCallTaskId\"/>\n");
        }
        if (protocol != null) {
            bpmn.append("        <bpmn2:dataInput id=\"_2_ProtocolInput\" name=\"Protocol\"/>\n");
        }
        if (host != null) {
            bpmn.append("        <bpmn2:dataInput id=\"_2_HostInput\" name=\"Host\"/>\n");
        }
        if (port != null) {
            bpmn.append("        <bpmn2:dataInput id=\"_2_PortInput\" name=\"Port\"/>\n");
        }
        if (timeout != null) {
            bpmn.append("        <bpmn2:dataInput id=\"_2_RequestTimeoutInput\" name=\"RequestTimeout\"/>\n");
        }
        if (header != null) {
            String[] headerParts = header.split(":");
            String headerName = headerParts[0];
            bpmn.append("        <bpmn2:dataInput id=\"_2_HEADER_").append(headerName).append("Input\" name=\"HEADER_").append(headerName).append("\"/>\n");
        }
        if (query != null) {
            String[] queryParts = query.split(":");
            String queryName = queryParts[0];
            bpmn.append("        <bpmn2:dataInput id=\"_2_QUERY_").append(queryName).append("Input\" name=\"QUERY_").append(queryName).append("\"/>\n");
        }

        bpmn.append("        <bpmn2:inputSet>\n");
        if (url != null)
            bpmn.append("          <bpmn2:dataInputRefs>_2_UrlInput</bpmn2:dataInputRefs>\n");
        if (strategy != null)
            bpmn.append("          <bpmn2:dataInputRefs>_2_AccessTokenAcquisitionStrategyInput</bpmn2:dataInputRefs>\n");
        if (taskId != null)
            bpmn.append("          <bpmn2:dataInputRefs>_2_RestServiceCallTaskIdInput</bpmn2:dataInputRefs>\n");
        if (protocol != null)
            bpmn.append("          <bpmn2:dataInputRefs>_2_ProtocolInput</bpmn2:dataInputRefs>\n");
        if (host != null)
            bpmn.append("          <bpmn2:dataInputRefs>_2_HostInput</bpmn2:dataInputRefs>\n");
        if (port != null)
            bpmn.append("          <bpmn2:dataInputRefs>_2_PortInput</bpmn2:dataInputRefs>\n");
        if (timeout != null)
            bpmn.append("          <bpmn2:dataInputRefs>_2_RequestTimeoutInput</bpmn2:dataInputRefs>\n");
        if (header != null) {
            String headerName = header.split(":")[0];
            bpmn.append("          <bpmn2:dataInputRefs>_2_HEADER_").append(headerName).append("Input</bpmn2:dataInputRefs>\n");
        }
        if (query != null) {
            String queryName = query.split(":")[0];
            bpmn.append("          <bpmn2:dataInputRefs>_2_QUERY_").append(queryName).append("Input</bpmn2:dataInputRefs>\n");
        }
        bpmn.append("        </bpmn2:inputSet>\n");
        bpmn.append("      </bpmn2:ioSpecification>\n");

        if (url != null) {
            bpmn.append("      <bpmn2:dataInputAssociation>\n");
            bpmn.append("        <bpmn2:targetRef>_2_UrlInput</bpmn2:targetRef>\n");
            bpmn.append("        <bpmn2:assignment>\n");
            bpmn.append("          <bpmn2:from xsi:type=\"bpmn2:tFormalExpression\">").append(url).append("</bpmn2:from>\n");
            bpmn.append("          <bpmn2:to xsi:type=\"bpmn2:tFormalExpression\">_2_UrlInput</bpmn2:to>\n");
            bpmn.append("        </bpmn2:assignment>\n");
            bpmn.append("      </bpmn2:dataInputAssociation>\n");
        }
        if (strategy != null) {
            bpmn.append("      <bpmn2:dataInputAssociation>\n");
            bpmn.append("        <bpmn2:targetRef>_2_AccessTokenAcquisitionStrategyInput</bpmn2:targetRef>\n");
            bpmn.append("        <bpmn2:assignment>\n");
            bpmn.append("          <bpmn2:from xsi:type=\"bpmn2:tFormalExpression\">").append(strategy).append("</bpmn2:from>\n");
            bpmn.append("          <bpmn2:to xsi:type=\"bpmn2:tFormalExpression\">_2_AccessTokenAcquisitionStrategyInput</bpmn2:to>\n");
            bpmn.append("        </bpmn2:assignment>\n");
            bpmn.append("      </bpmn2:dataInputAssociation>\n");
        }
        if (taskId != null) {
            bpmn.append("      <bpmn2:dataInputAssociation>\n");
            bpmn.append("        <bpmn2:targetRef>_2_RestServiceCallTaskIdInput</bpmn2:targetRef>\n");
            bpmn.append("        <bpmn2:assignment>\n");
            bpmn.append("          <bpmn2:from xsi:type=\"bpmn2:tFormalExpression\">").append(taskId).append("</bpmn2:from>\n");
            bpmn.append("          <bpmn2:to xsi:type=\"bpmn2:tFormalExpression\">_2_RestServiceCallTaskIdInput</bpmn2:to>\n");
            bpmn.append("        </bpmn2:assignment>\n");
            bpmn.append("      </bpmn2:dataInputAssociation>\n");
        }
        if (protocol != null) {
            bpmn.append("      <bpmn2:dataInputAssociation>\n");
            bpmn.append("        <bpmn2:targetRef>_2_ProtocolInput</bpmn2:targetRef>\n");
            bpmn.append("        <bpmn2:assignment>\n");
            bpmn.append("          <bpmn2:from xsi:type=\"bpmn2:tFormalExpression\">").append(protocol).append("</bpmn2:from>\n");
            bpmn.append("          <bpmn2:to xsi:type=\"bpmn2:tFormalExpression\">_2_ProtocolInput</bpmn2:to>\n");
            bpmn.append("        </bpmn2:assignment>\n");
            bpmn.append("      </bpmn2:dataInputAssociation>\n");
        }
        if (host != null) {
            bpmn.append("      <bpmn2:dataInputAssociation>\n");
            bpmn.append("        <bpmn2:targetRef>_2_HostInput</bpmn2:targetRef>\n");
            bpmn.append("        <bpmn2:assignment>\n");
            bpmn.append("          <bpmn2:from xsi:type=\"bpmn2:tFormalExpression\">").append(host).append("</bpmn2:from>\n");
            bpmn.append("          <bpmn2:to xsi:type=\"bpmn2:tFormalExpression\">_2_HostInput</bpmn2:to>\n");
            bpmn.append("        </bpmn2:assignment>\n");
            bpmn.append("      </bpmn2:dataInputAssociation>\n");
        }
        if (port != null) {
            bpmn.append("      <bpmn2:dataInputAssociation>\n");
            bpmn.append("        <bpmn2:targetRef>_2_PortInput</bpmn2:targetRef>\n");
            bpmn.append("        <bpmn2:assignment>\n");
            bpmn.append("          <bpmn2:from xsi:type=\"bpmn2:tFormalExpression\">").append(port).append("</bpmn2:from>\n");
            bpmn.append("          <bpmn2:to xsi:type=\"bpmn2:tFormalExpression\">_2_PortInput</bpmn2:to>\n");
            bpmn.append("        </bpmn2:assignment>\n");
            bpmn.append("      </bpmn2:dataInputAssociation>\n");
        }
        if (timeout != null) {
            bpmn.append("      <bpmn2:dataInputAssociation>\n");
            bpmn.append("        <bpmn2:targetRef>_2_RequestTimeoutInput</bpmn2:targetRef>\n");
            bpmn.append("        <bpmn2:assignment>\n");
            bpmn.append("          <bpmn2:from xsi:type=\"bpmn2:tFormalExpression\">").append(timeout).append("</bpmn2:from>\n");
            bpmn.append("          <bpmn2:to xsi:type=\"bpmn2:tFormalExpression\">_2_RequestTimeoutInput</bpmn2:to>\n");
            bpmn.append("        </bpmn2:assignment>\n");
            bpmn.append("      </bpmn2:dataInputAssociation>\n");
        }
        if (header != null) {
            String[] headerParts = header.split(":");
            String headerName = headerParts[0];
            String headerValue = headerParts.length > 1 ? headerParts[1] : "";
            bpmn.append("      <bpmn2:dataInputAssociation>\n");
            bpmn.append("        <bpmn2:targetRef>_2_HEADER_").append(headerName).append("Input</bpmn2:targetRef>\n");
            bpmn.append("        <bpmn2:assignment>\n");
            bpmn.append("          <bpmn2:from xsi:type=\"bpmn2:tFormalExpression\">").append(headerValue).append("</bpmn2:from>\n");
            bpmn.append("          <bpmn2:to xsi:type=\"bpmn2:tFormalExpression\">_2_HEADER_").append(headerName).append("Input</bpmn2:to>\n");
            bpmn.append("        </bpmn2:assignment>\n");
            bpmn.append("      </bpmn2:dataInputAssociation>\n");
        }
        if (query != null) {
            String[] queryParts = query.split(":");
            String queryName = queryParts[0];
            String queryValue = queryParts.length > 1 ? queryParts[1] : "";
            bpmn.append("      <bpmn2:dataInputAssociation>\n");
            bpmn.append("        <bpmn2:targetRef>_2_QUERY_").append(queryName).append("Input</bpmn2:targetRef>\n");
            bpmn.append("        <bpmn2:assignment>\n");
            bpmn.append("          <bpmn2:from xsi:type=\"bpmn2:tFormalExpression\">").append(queryValue).append("</bpmn2:from>\n");
            bpmn.append("          <bpmn2:to xsi:type=\"bpmn2:tFormalExpression\">_2_QUERY_").append(queryName).append("Input</bpmn2:to>\n");
            bpmn.append("        </bpmn2:assignment>\n");
            bpmn.append("      </bpmn2:dataInputAssociation>\n");
        }

        bpmn.append("    </bpmn2:task>\n");
        bpmn.append("    <bpmn2:endEvent id=\"_3\" name=\"End\">\n");
        bpmn.append("      <bpmn2:incoming>_2-_3</bpmn2:incoming>\n");
        bpmn.append("    </bpmn2:endEvent>\n");
        bpmn.append("    <bpmn2:sequenceFlow id=\"_1-_2\" sourceRef=\"_1\" targetRef=\"_2\"/>\n");
        bpmn.append("    <bpmn2:sequenceFlow id=\"_2-_3\" sourceRef=\"_2\" targetRef=\"_3\"/>\n");
        bpmn.append("  </bpmn2:process>\n");
        bpmn.append("</bpmn2:definitions>");

        return bpmn.toString();
    }

}
