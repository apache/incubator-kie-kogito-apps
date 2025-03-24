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
package org.kie.kogito.jitexecutor.dmn;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.kie.api.builder.Message;
import org.kie.api.io.Resource;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNMessage;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.api.core.ast.DecisionNode;
import org.kie.dmn.api.core.ast.DecisionServiceNode;
import org.kie.dmn.api.core.ast.InputDataNode;
import org.kie.dmn.core.compiler.RuntimeTypeCheckOption;
import org.kie.dmn.core.impl.DMNRuntimeImpl;
import org.kie.dmn.core.internal.utils.DMNRuntimeBuilder;
import org.kie.dmn.core.internal.utils.DynamicDMNContextBuilder;
import org.kie.dmn.model.api.DMNModelInstrumentedBase;
import org.kie.dmn.model.api.Definitions;
import org.kie.internal.io.ResourceFactory;
import org.kie.kogito.jitexecutor.common.requests.MultipleResourcesPayload;
import org.kie.kogito.jitexecutor.common.requests.ResourceWithURI;
import org.kie.kogito.jitexecutor.dmn.responses.JITDMNResult;
import org.kie.kogito.jitexecutor.dmn.utils.ResolveByKey;

public class DMNEvaluator {

    private final DMNModel dmnModel;
    private final DMNRuntime dmnRuntime;

    public static DMNEvaluator fromXML(String modelXML) {
        Resource modelResource = ResourceFactory.newReaderResource(new StringReader(modelXML), "UTF-8");
        DMNRuntime dmnRuntime = DMNRuntimeBuilder.fromDefaults().buildConfiguration()
                .fromResources(Collections.singletonList(modelResource)).getOrElseThrow(RuntimeException::new);
        dmnRuntime.addListener(new JITDMNListener());
        DMNModel dmnModel = dmnRuntime.getModels().get(0);
        return validateForErrors(dmnModel, dmnRuntime);
    }

    public static DMNEvaluator fromMultiple(MultipleResourcesPayload payload) {
        Map<String, Resource> resources = new HashMap<>();
        for (ResourceWithURI r : payload.getResources()) {
            Resource readerResource = ResourceFactory.newReaderResource(new StringReader(r.getContent()), "UTF-8");
            readerResource.setSourcePath(r.getURI());
            resources.put(r.getURI(), readerResource);
        }
        ResolveByKey rbk = new ResolveByKey(resources);
        DMNRuntime dmnRuntime = DMNRuntimeBuilder.fromDefaults()
                .setRelativeImportResolver((x, y, locationURI) -> rbk.readerByKey(locationURI))
                .buildConfiguration()
                .fromResources(resources.values())
                .getOrElseThrow(RuntimeException::new);
        dmnRuntime.addListener(new JITDMNListener());
        DMNModel mainModel = null;
        for (DMNModel m : dmnRuntime.getModels()) {
            if (m.getResource().getSourcePath().equals(payload.getMainURI())) {
                mainModel = m;
                break;
            }
        }
        if (mainModel == null) {
            throw new IllegalStateException("Was not able to identify main model from MultipleResourcesPayload contents.");
        }
        return validateForErrors(mainModel, dmnRuntime);
    }

    static DMNEvaluator validateForErrors(DMNModel dmnModel, DMNRuntime dmnRuntime) {
        if (dmnModel.hasErrors()) {
            List<DMNMessage> messages = dmnModel.getMessages(DMNMessage.Severity.ERROR);
            String errorMessage = messages.stream().map(Message::getText).collect(Collectors.joining(", "));
            throw new IllegalStateException(errorMessage);
        } else {
            return new DMNEvaluator(dmnModel, dmnRuntime);
        }
    }

    static List<String> getPathToRoot(DMNModel dmnModel, String invalidId) {
        List<String> path = new ArrayList<>();
        DMNModelInstrumentedBase node = getNodeById(dmnModel.getDefinitions(), invalidId);

        while (node != null) {
            path.add(node.getIdentifierString());
            if (node instanceof Definitions) {
                break;
            }
            node = node.getParent();
        }
        Collections.reverse(path);
        return path.isEmpty() ? Collections.singletonList(invalidId) : path;
    }

    static DMNModelInstrumentedBase getNodeById(DMNModel dmnModel, String id) {
        return dmnModel.getDefinitions().getChildren().stream().map(child -> getNodeById(child, id))
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    static DMNModelInstrumentedBase getNodeById(DMNModelInstrumentedBase dmnModelInstrumentedBase, String id) {
        if (dmnModelInstrumentedBase.getIdentifierString().equals(id)) {
            return dmnModelInstrumentedBase;
        }
        for (DMNModelInstrumentedBase child : dmnModelInstrumentedBase.getChildren()) {
            DMNModelInstrumentedBase result = getNodeById(child, id);
            if (result != null) {
                return result;
            }
        }
        return null;

//        return dmnModelInstrumentedBase.getChildren().stream().map(child -> getNodeById(child, id))
//                .filter(Objects::nonNull).findFirst().orElse(null);
    }


    private DMNEvaluator(DMNModel dmnModel, DMNRuntime dmnRuntime) {
        this.dmnModel = dmnModel;
        this.dmnRuntime = dmnRuntime;
        ((DMNRuntimeImpl) this.dmnRuntime).setOption(new RuntimeTypeCheckOption(true));
    }

    public DMNModel getDmnModel() {
        return dmnModel;
    }

    public String getNamespace() {
        return dmnModel.getNamespace();
    }

    public String getName() {
        return dmnModel.getName();
    }

    public Collection<DMNModel> getAllDMNModels() {
        return dmnRuntime.getModels();
    }

    public JITDMNResult evaluate(Map<String, Object> context) {
        DMNContext dmnContext =
                new DynamicDMNContextBuilder(dmnRuntime.newContext(), dmnModel).populateContextWith(context);
        DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);

        List<List<String>> invalidPaths = new ArrayList<>();
        for (DMNMessage message : dmnResult.getMessages()) {
            if (message.getSeverity() == DMNMessage.Severity.WARN || message.getSeverity() == DMNMessage.Severity.ERROR) {
                String sourceId = message.getSourceId();
                List<String> path = new ArrayList<>();
                getPathToRoot(dmnModel, sourceId, path);
                Collections.reverse(path);
                invalidPaths.add(path);
            }
        }
        Optional<Map<String, Map<String, Integer>>> decisionEvaluationHitIdsMap = dmnRuntime.getListeners().stream()
                .filter(JITDMNListener.class::isInstance)
                .findFirst()
                .map(JITDMNListener.class::cast)
                .map(JITDMNListener::getDecisionEvaluationHitIdsMap);
        return JITDMNResult.of(getNamespace(), getName(), dmnResult,
                decisionEvaluationHitIdsMap.orElse(Collections.emptyMap()), invalidPaths);
    }

    static void getPathToRoot(DMNModel dmnModel, String elementId, List<String> path) {
        path.add(elementId);
        String parentId = findParentId(dmnModel, elementId);

        if (parentId != null) {
            getPathToRoot(dmnModel, parentId, path);
        }
    }

    static String findParentId(DMNModel dmnModel, String elementId) {
        for (DecisionServiceNode decisionServiceNode : dmnModel.getDecisionServices()) {
            if (decisionServiceNode.getDecisionService().getOutputDecision().stream()
                    .anyMatch(decision -> decision.getIdentifierString().equals(elementId))) {
                return decisionServiceNode.getId();
            }
        }

        for (DecisionNode parentDecisionNode : dmnModel.getDecisions()) {
            Set<InputDataNode> requiredInputs = dmnModel.getRequiredInputsForDecisionId(parentDecisionNode.getId());
            if (requiredInputs.stream().anyMatch(input -> input.getId().equals(elementId))) {
                return parentDecisionNode.getId();
            }
        }

        for (DecisionNode decisionNode : dmnModel.getDecisions()) {
            if (decisionNode.getDecision().getExpression() != null && elementId.equals(decisionNode.getDecision().getExpression().getId())) {
                return decisionNode.getId();
            }
        }

        return null;
    }

}
