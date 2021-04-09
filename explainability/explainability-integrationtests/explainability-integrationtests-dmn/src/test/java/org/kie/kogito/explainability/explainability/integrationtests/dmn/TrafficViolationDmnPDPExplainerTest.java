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
package org.kie.kogito.explainability.explainability.integrationtests.dmn;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.kogito.decision.DecisionModel;
import org.kie.kogito.dmn.DMNKogito;
import org.kie.kogito.dmn.DmnDecisionModel;
import org.kie.kogito.explainability.Config;
import org.kie.kogito.explainability.global.pdp.PartialDependencePlotExplainer;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.PartialDependenceGraph;
import org.kie.kogito.explainability.model.PerturbationContext;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.utils.DataUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrafficViolationDmnPDPExplainerTest {

    @Test
    void testTrafficViolationDMNExplanation() throws ExecutionException, InterruptedException, TimeoutException {
        DMNRuntime dmnRuntime = DMNKogito.createGenericDMNRuntime(new InputStreamReader(getClass().getResourceAsStream("/dmn/TrafficViolation.dmn")));
        assertEquals(1, dmnRuntime.getModels().size());

        final String TRAFFIC_VIOLATION_NS = "https://github.com/kiegroup/drools/kie-dmn/_A4BCA8B8-CF08-433F-93B2-A2598F19ECFF";
        final String TRAFFIC_VIOLATION_NAME = "Traffic Violation";
        DecisionModel decisionModel = new DmnDecisionModel(dmnRuntime, TRAFFIC_VIOLATION_NS, TRAFFIC_VIOLATION_NAME);

        PredictionProvider model = new DecisionModelWrapper(decisionModel);
        List<PredictionInput> inputs = getInputs();
        List<PredictionOutput> predictionOutputs = model.predictAsync(inputs)
                .get(Config.INSTANCE.getAsyncTimeout(), Config.INSTANCE.getAsyncTimeUnit());
        List<Prediction> predictions = new ArrayList<>();
        for (int i = 0; i < predictionOutputs.size(); i++) {
            predictions.add(new Prediction(inputs.get(i), predictionOutputs.get(i)));
        }

        PartialDependencePlotExplainer partialDependencePlotExplainer = new PartialDependencePlotExplainer();
        List<PartialDependenceGraph> pdps = partialDependencePlotExplainer.explainFromPredictions(model, predictions);

        AssertionsForClassTypes.assertThat(pdps).isNotNull();
        Assertions.assertThat(pdps).hasSize(8);
    }

    private List<PredictionInput> getInputs() {
        List<PredictionInput> predictionInputs = new ArrayList<>();

        final Map<String, Object> driver = new HashMap<>();
        driver.put("Points", 10);
        final Map<String, Object> violation = new HashMap<>();
        violation.put("Type", "speed");
        violation.put("Actual Speed", 150);
        violation.put("Speed Limit", 130);
        final Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("Driver", driver);
        contextVariables.put("Violation", violation);
        List<Feature> features = new ArrayList<>();
        features.add(FeatureFactory.newCompositeFeature("context", contextVariables));
        PredictionInput predictionInput = new PredictionInput(features);

        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            List<Feature> perturbFeatures = DataUtils.perturbFeatures(predictionInput.getFeatures(), new PerturbationContext(random, predictionInput.getFeatures().size()));
            predictionInputs.add(new PredictionInput(perturbFeatures));
        }

        return predictionInputs;
    }
}
