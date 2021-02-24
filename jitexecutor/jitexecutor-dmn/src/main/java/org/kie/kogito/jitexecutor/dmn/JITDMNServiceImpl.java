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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.ast.DecisionNode;
import org.kie.kogito.dmn.rest.KogitoDMNResult;
import org.kie.kogito.explainability.Config;
import org.kie.kogito.explainability.local.lime.LimeConfig;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import org.kie.kogito.explainability.model.FeatureImportance;
import org.kie.kogito.explainability.model.PerturbationContext;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.Saliency;
import org.kie.kogito.jitexecutor.dmn.responses.DMNResultWithExplanation;
import org.kie.kogito.trusty.service.common.responses.FeatureImportanceResponse;
import org.kie.kogito.trusty.service.common.responses.SalienciesResponse;
import org.kie.kogito.trusty.service.common.responses.SaliencyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class JITDMNServiceImpl implements JITDMNService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JITDMNServiceImpl.class);

    private static final String EXPLAINABILITY_FAILED = "FAILED";
    private static final String EXPLAINABILITY_FAILED_MESSAGE = "Failed to calculate values";
    private static final String EXPLAINABILITY_SUCCEEDED = "SUCCEEDED";

    @ConfigProperty(name = "kogito.explainability.lime.sample-size", defaultValue = "300")
    int explainabilityLimeSampleSize;

    @ConfigProperty(name = "kogito.explainability.lime.no-of-perturbation", defaultValue = "1")
    int explainabilityLimeNoOfPerturbation;

    public JITDMNServiceImpl() {
    }

    public JITDMNServiceImpl(int explainabilityLimeSampleSize, int explainabilityLimeNoOfPerturbation) {
        this.explainabilityLimeSampleSize = explainabilityLimeSampleSize;
        this.explainabilityLimeNoOfPerturbation = explainabilityLimeNoOfPerturbation;
    }

    @Override
    public KogitoDMNResult evaluateModel(String modelXML, Map<String, Object> context) {
        DMNEvaluator dmnEvaluator = DMNEvaluator.fromXML(modelXML);
        DMNResult dmnResult = dmnEvaluator.evaluate(context);
        return new KogitoDMNResult(dmnEvaluator.getNamespace(), dmnEvaluator.getName(), dmnResult);
    }

    @Override
    public DMNResultWithExplanation evaluateModelAndExplain(String modelXML, Map<String, Object> context) {
        DMNEvaluator dmnEvaluator = DMNEvaluator.fromXML(modelXML);
        LocalDMNPredictionProvider localDMNPredictionProvider = new LocalDMNPredictionProvider(dmnEvaluator);

        DMNResult dmnResult = dmnEvaluator.evaluate(context);

        Prediction prediction = new Prediction(LocalDMNPredictionProvider.toPredictionInput(context),
                                               LocalDMNPredictionProvider.toPredictionOutput(dmnResult)
        );

        LimeConfig limeConfig = new LimeConfig()
                .withSamples(explainabilityLimeSampleSize)
                .withPerturbationContext(new PerturbationContext(new Random(), explainabilityLimeNoOfPerturbation));
        LimeExplainer limeExplainer = new LimeExplainer(limeConfig);
        Map<String, Saliency> saliencyMap;
        try {
            saliencyMap = limeExplainer.explainAsync(prediction, localDMNPredictionProvider)
                    .get(Config.INSTANCE.getAsyncTimeout(), Config.INSTANCE.getAsyncTimeUnit());
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            if (e instanceof InterruptedException) {
                LOGGER.error("Critical InterruptedException occurred", e);
                Thread.currentThread().interrupt();
            }
            return new DMNResultWithExplanation(
                    new KogitoDMNResult(dmnEvaluator.getNamespace(), dmnEvaluator.getName(), dmnResult),
                    new SalienciesResponse(EXPLAINABILITY_FAILED, EXPLAINABILITY_FAILED_MESSAGE, null)
            );
        }

        List<SaliencyResponse> saliencyResponses = buildSalienciesResponse(dmnEvaluator.getDmnModel(), saliencyMap);

        return new DMNResultWithExplanation(
                new KogitoDMNResult(dmnEvaluator.getNamespace(), dmnEvaluator.getName(), dmnResult),
                new SalienciesResponse(EXPLAINABILITY_SUCCEEDED, null, saliencyResponses)
        );
    }

    private List<SaliencyResponse> buildSalienciesResponse(DMNModel dmnModel, Map<String, Saliency> saliencyMap) {
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
        return saliencyResponses;
    }

    private static FeatureImportanceResponse featureImportanceModelToResponse(FeatureImportance model) {
        if (model == null) {
            return null;
        }
        return new FeatureImportanceResponse(model.getFeature().getName(), model.getScore());
    }
}
