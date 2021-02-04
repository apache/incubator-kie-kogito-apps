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

package org.kie.kogito.jitexecutor.dmn;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import org.kie.api.io.Resource;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.api.core.ast.DecisionNode;
import org.kie.dmn.core.internal.utils.DMNRuntimeBuilder;
import org.kie.internal.io.ResourceFactory;
import org.kie.kogito.dmn.rest.KogitoDMNResult;
import org.kie.kogito.explainability.Config;
import org.kie.kogito.explainability.local.lime.LimeConfig;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.FeatureImportance;
import org.kie.kogito.explainability.model.PerturbationContext;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.Saliency;
import org.kie.kogito.jitexecutor.dmn.responses.DMNResultWithExplanation;
import org.kie.kogito.jitexecutor.dmn.responses.ExplainabilityStatus;
import org.kie.kogito.trusty.service.responses.FeatureImportanceResponse;
import org.kie.kogito.trusty.service.responses.SalienciesResponse;
import org.kie.kogito.trusty.service.responses.SaliencyResponse;

@ApplicationScoped
public class JITDMNServiceImpl implements JITDMNService {

    private static final int EXPLAINABILITY_LIME_SAMPLE_SIZE = 300;
    private static final int EXPLAINABILITY_NO_OF_PERTURBATION = 1;
    private static final String EXPLAINABILITY_FAILED_MESSAGE = "Failed to calculate values";

    @Override
    public KogitoDMNResult evaluateModel(String modelXML, Map<String, Object> context) {
        Resource modelResource = ResourceFactory.newReaderResource(new StringReader(modelXML), "UTF-8");
        DMNRuntime dmnRuntime = DMNRuntimeBuilder.fromDefaults().buildConfiguration()
                .fromResources(Collections.singletonList(modelResource)).getOrElseThrow(RuntimeException::new);
        DMNModel dmnModel = dmnRuntime.getModels().get(0);
        LocalDMNPredictionProvider localDMNPredictionProvider = new LocalDMNPredictionProvider(dmnModel, dmnRuntime);
        DMNResult dmnResult = localDMNPredictionProvider.predict(context);
        return new KogitoDMNResult(dmnModel.getNamespace(), dmnModel.getName(), dmnResult);
    }

    @Override
    public DMNResultWithExplanation evaluateModelAndExplain(String modelXML, Map<String, Object> context) {
        Resource modelResource = ResourceFactory.newReaderResource(new StringReader(modelXML), "UTF-8");
        DMNRuntime dmnRuntime = DMNRuntimeBuilder.fromDefaults().buildConfiguration()
                .fromResources(Collections.singletonList(modelResource)).getOrElseThrow(RuntimeException::new);
        DMNModel dmnModel = dmnRuntime.getModels().get(0);
        LocalDMNPredictionProvider localDMNPredictionProvider = new LocalDMNPredictionProvider(dmnModel, dmnRuntime);

        DMNResult dmnResult = localDMNPredictionProvider.predict(context);

        PredictionInput predictionInput = new PredictionInput(
                // TODO: Date/Time types are considered as strings, proper conversion to be implemented https://issues.redhat.com/browse/KOGITO-4351
                Collections.singletonList(FeatureFactory.newCompositeFeature("context", context))
        );

        Prediction prediction = new Prediction(predictionInput, localDMNPredictionProvider.toPredictionOutput(dmnResult));

        LimeConfig limeConfig = new LimeConfig()
                .withSamples(EXPLAINABILITY_LIME_SAMPLE_SIZE)
                .withPerturbationContext(new PerturbationContext(new Random(), EXPLAINABILITY_NO_OF_PERTURBATION));
        LimeExplainer limeExplainer = new LimeExplainer(limeConfig);

        Map<String, Saliency> saliencyMap;
        try {
            saliencyMap = limeExplainer.explainAsync(prediction, localDMNPredictionProvider)
                    .get(Config.INSTANCE.getAsyncTimeout(), Config.INSTANCE.getAsyncTimeUnit());
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            return new DMNResultWithExplanation(
                    new KogitoDMNResult(dmnModel.getNamespace(), dmnModel.getName(), dmnResult),
                    new SalienciesResponse(ExplainabilityStatus.FAILED.name(), EXPLAINABILITY_FAILED_MESSAGE, null)
            );
        }

        List<SaliencyResponse> saliencyResponses = new ArrayList<>();
        for (Map.Entry<String, Saliency> entry : saliencyMap.entrySet()) {
            DecisionNode decisionByName = dmnModel.getDecisionByName(entry.getKey());
            saliencyResponses.add(new SaliencyResponse(
                                          decisionByName.getId(),
                                          decisionByName.getName(),
                                          entry.getValue().getPerFeatureImportance().stream()
                                                  .map(JITDMNServiceImpl::featureImportanceModelToResponse)
                                                  .filter(Objects::nonNull)
                                                  .collect(Collectors.toList())
                                  )
            );
        }

        return new DMNResultWithExplanation(
                new KogitoDMNResult(dmnModel.getNamespace(), dmnModel.getName(), dmnResult),
                new SalienciesResponse(ExplainabilityStatus.SUCCEEDED.name(), null, saliencyResponses)
        );
    }

    private static FeatureImportanceResponse featureImportanceModelToResponse(FeatureImportance model) {
        if (model == null) {
            return null;
        }
        return new FeatureImportanceResponse(model.getFeature().getName(), model.getScore());
    }
}
