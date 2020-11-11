/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.explainability.explainability.integrationtests.dmn;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.kogito.decision.DecisionModel;
import org.kie.kogito.dmn.DMNKogito;
import org.kie.kogito.dmn.DmnDecisionModel;
import org.kie.kogito.explainability.Config;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.FeatureImportance;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.model.Saliency;
import org.kie.kogito.explainability.utils.ExplainabilityMetrics;
import org.kie.kogito.explainability.utils.LocalSaliencyStability;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PrequalificationDmnLimeExplainerTest {

    @Test
    void testPrequalificationDMNExplanation1() throws ExecutionException, InterruptedException, TimeoutException {
        DMNRuntime dmnRuntime = DMNKogito.createGenericDMNRuntime(new InputStreamReader(getClass().getResourceAsStream("/dmn/Prequalification-1.dmn")));
        assertEquals(1, dmnRuntime.getModels().size());

        final String NS = "http://www.trisotech.com/definitions/_f31e1f8e-d4ce-4a3a-ac3b-747efa6b3401";
        final String NAME = "Prequalification";
        DecisionModel decisionModel = new DmnDecisionModel(dmnRuntime, NS, NAME);

        final Map<String, Object> borrower = new HashMap<>();
        borrower.put("Monthly Other Debt", 1000);
        borrower.put("Monthly Income", 10000);
        final Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("Appraised Value", 500000);
        contextVariables.put("Loan Amount", 300000);
        contextVariables.put("Credit Score", 600);
        contextVariables.put("Borrower", borrower);
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newCompositeFeature("context", contextVariables));
        PredictionInput predictionInput = new PredictionInput(features);

        PredictionProvider model = new DecisionModelWrapper(decisionModel);

        LimeExplainer limeExplainer = new LimeExplainer(3000, 3);

        List<PredictionOutput> predictionOutputs = model.predictAsync(List.of(predictionInput))
                .get(Config.INSTANCE.getAsyncTimeout(), Config.INSTANCE.getAsyncTimeUnit());
        Prediction prediction = new Prediction(predictionInput, predictionOutputs.get(0));
        Map<String, Saliency> saliencyMap = limeExplainer.explainAsync(prediction, model)
                .get(Config.INSTANCE.getAsyncTimeout(), Config.INSTANCE.getAsyncTimeUnit());
        for (Saliency saliency : saliencyMap.values()) {
            assertNotNull(saliency);
            List<FeatureImportance> topFeatures = saliency.getTopFeatures(2);
            if (!topFeatures.isEmpty()) {
                assertThat(ExplainabilityMetrics.impactScore(model, prediction, topFeatures)).isPositive();
            }
        }

        int topK = 1;
        LocalSaliencyStability stability = ExplainabilityMetrics.getLocalSaliencyStability(model, predictionInput, limeExplainer, topK, 10);
        for (int i = 1; i <= topK; i++) {
            for (String decision : stability.getDecisions()) {
                double positiveStabilityScore = stability.getPositiveStabilityScore(decision, i);
                double negativeStabilityScore = stability.getNegativeStabilityScore(decision, i);
                assertThat(positiveStabilityScore).isGreaterThanOrEqualTo(0.5);
                assertThat(negativeStabilityScore).isGreaterThanOrEqualTo(0.5);
            }
        }
    }
}
