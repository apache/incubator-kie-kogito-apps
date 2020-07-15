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
package org.kie.kogito.explainability.utils;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.BlackBoxModel;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.Saliency;
import org.kie.kogito.explainability.TestUtils;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExplainabilityMetricsTest {

    private static final SecureRandom random = new SecureRandom();

    @Test
    public void testExplainabilityNoExplanation() {
        double v = ExplainabilityMetrics.quantifyExplainability(0, 0, 0);
        assertFalse(Double.isNaN(v));
        assertFalse(Double.isInfinite(v));
        assertEquals(0, v);
    }

    @Test
    public void testExplainabilityNoExplanationWithInteraction() {
        double v = ExplainabilityMetrics.quantifyExplainability(0, 0, 1);
        assertFalse(Double.isNaN(v));
        assertFalse(Double.isInfinite(v));
        assertEquals(0, v);
    }

    @Test
    public void testExplainabilityRandomchunksNoInteraction() {
        double v = ExplainabilityMetrics.quantifyExplainability(random.nextInt(), random.nextInt(), 1d /
                random.nextInt(100));
        assertFalse(Double.isNaN(v));
        assertFalse(Double.isInfinite(v));
        assertTrue(v >= 0 && v <= 1);
    }

    @Test
    public void testExplainabilityRandom() {
        double v = ExplainabilityMetrics.quantifyExplainability(random.nextInt(), random.nextInt(), random.nextDouble());
        assertFalse(Double.isNaN(v));
        assertFalse(Double.isInfinite(v));
        assertTrue(v >= 0 && v <= 1);
    }

    @Test
    public void testFidelity() {
        List<Pair<Saliency, Prediction>> pairs = new LinkedList<>();
        LimeExplainer limeExplainer = new LimeExplainer(100, 1);
        BlackBoxModel model = TestUtils.getDummyTextClassifier();
        for (int i = 0; i < 10; i++) {
            List<Feature> features = new LinkedList<>();
            for (int j = 0; j < 4; j++) {
                features.add(TestUtils.getRandomFeature());
            }
            PredictionInput input = new PredictionInput(features);
            Prediction prediction = new Prediction(input, model.predict(List.of(input)).get(0));
            pairs.add(Pair.of(limeExplainer.explain(prediction, model), prediction));
        }
        double v = ExplainabilityMetrics.classificationFidelity(pairs);
        assertTrue(v > 0);
    }
}