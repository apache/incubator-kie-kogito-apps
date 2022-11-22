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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.enterprise.context.ApplicationScoped;

import org.drools.io.InputStreamResource;
import org.jbpm.bpmn2.xml.BPMNDISemanticModule;
import org.jbpm.bpmn2.xml.BPMNExtensionsSemanticModule;
import org.jbpm.bpmn2.xml.BPMNSemanticModule;
import org.jbpm.compiler.xml.XmlProcessReader;
import org.jbpm.compiler.xml.core.SemanticModules;
import org.jbpm.process.core.validation.ProcessValidationError;
import org.jbpm.ruleflow.core.validation.RuleFlowProcessValidator;
import org.kie.api.definition.process.Process;
import org.kie.api.io.Resource;
import org.kie.kogito.jitexecutor.bpmn.responses.JITBPMNValidationResult;
import org.xml.sax.SAXException;

@ApplicationScoped
public class JITBPMNServiceImpl implements JITBPMNService {

    private static final RuleFlowProcessValidator PROCESS_VALIDATOR = RuleFlowProcessValidator.getInstance();

    private static final SemanticModules BPMN_SEMANTIC_MODULES = new SemanticModules();

    private static String ERROR_TEMPLATE = "Process id: %s - name : %s - error : %s";

    static {
        BPMN_SEMANTIC_MODULES.addSemanticModule(new BPMNSemanticModule());
        BPMN_SEMANTIC_MODULES.addSemanticModule(new BPMNExtensionsSemanticModule());
        BPMN_SEMANTIC_MODULES.addSemanticModule(new BPMNDISemanticModule());
    }

    @Override
    public JITBPMNValidationResult validateModel(String modelXML) {
        Collection<String> errors;
        Collection<Process> processes;
        try {
            processes = parseModelXml(modelXML);
            if (processes.isEmpty()) {
                errors = Collections.singleton("No process found");
            } else {
                errors = new ArrayList<>();
                ProcessValidationError[] processValidationErrors = validateProcesses(processes);
                for (ProcessValidationError processValidationError : processValidationErrors) {
                    errors.add(getErrorString(processValidationError));
                }
            }
        } catch (Exception e) {
            String error = e.getMessage() != null && !e.getMessage().isEmpty() ? e.getMessage() : e.toString();
            errors = Collections.singleton(error);
        }
        return new JITBPMNValidationResult(errors);
    }

    static ProcessValidationError[] validateProcesses(Collection<Process> processes) {
        ProcessValidationError[] toReturn = new ProcessValidationError[0];
        for (Process toValidate : processes) {
            ProcessValidationError[] toAdd = PROCESS_VALIDATOR.validateProcess(toValidate);
            ProcessValidationError[] temp = new ProcessValidationError[toReturn.length + toAdd.length];
            System.arraycopy(toReturn, 0, temp, 0, toReturn.length);
            System.arraycopy(toAdd, 0, temp, (temp.length - toAdd.length), toAdd.length);
            toReturn = temp;
        }
        return toReturn;
    }

    static Collection<Process> parseModelXml(String modelXML) {
        Resource r = new InputStreamResource(new ByteArrayInputStream(modelXML.getBytes()));
        return parseModelResource(r);
    }

    static Collection<Process> parseModelResource(Resource r) {
        try (Reader reader = r.getReader()) {
            XmlProcessReader xmlReader = new XmlProcessReader(
                    BPMN_SEMANTIC_MODULES,
                    Thread.currentThread().getContextClassLoader());
            return xmlReader.read(reader);
        } catch (SAXException | IOException e) {
            throw new RuntimeException("Could not parse " + r, e);
        }
    }

    static String getErrorString(ProcessValidationError processValidationError) {
        Process failed = processValidationError.getProcess();
        return String.format(ERROR_TEMPLATE, failed.getId(), failed.getName(), processValidationError.getMessage());
    }
}
