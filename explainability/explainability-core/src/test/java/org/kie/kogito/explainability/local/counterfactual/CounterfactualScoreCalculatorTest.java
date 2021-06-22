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
package org.kie.kogito.explainability.local.counterfactual;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.TestUtils;
import org.kie.kogito.explainability.local.counterfactual.entities.CounterfactualEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.CounterfactualEntityFactory;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.PredictionFeatureDomain;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;
import org.kie.kogito.explainability.model.domain.EmptyFeatureDomain;
import org.kie.kogito.explainability.model.domain.FeatureDomain;
import org.kie.kogito.explainability.model.domain.NumericalFeatureDomain;
import org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CounterfactualScoreCalculatorTest {

    /**
     * If the goal and the model's output is the same, the distances should all be zero.
     */
    @Test
    void testGoalSizeMatch() throws ExecutionException, InterruptedException {
        final CounterFactualScoreCalculator scoreCalculator = new CounterFactualScoreCalculator();

        PredictionProvider model = TestUtils.getFeatureSkipModel(0);

        List<Feature> features = new ArrayList<>();
        List<FeatureDomain> featureDomains = new ArrayList<>();
        List<Boolean> constraints = new ArrayList<>();

        // f-1
        features.add(FeatureFactory.newNumericalFeature("f-1", 1.0));
        featureDomains.add(NumericalFeatureDomain.create(0.0, 10.0));
        constraints.add(false);

        // f-2
        features.add(FeatureFactory.newNumericalFeature("f-2", 2.0));
        featureDomains.add(NumericalFeatureDomain.create(0.0, 10.0));
        constraints.add(false);

        // f-3
        features.add(FeatureFactory.newBooleanFeature("f-3", true));
        featureDomains.add(EmptyFeatureDomain.create());
        constraints.add(false);

        PredictionInput input = new PredictionInput(features);
        PredictionFeatureDomain domains = new PredictionFeatureDomain(featureDomains);
        List<CounterfactualEntity> entities = CounterfactualEntityFactory.createEntities(input, domains, constraints, null);

        List<Output> goal = new ArrayList<>();
        goal.add(new Output("f-2", Type.NUMBER, new Value(2.0), 0.0));
        goal.add(new Output("f-3", Type.BOOLEAN, new Value(true), 0.0));

        final CounterfactualSolution solution =
                new CounterfactualSolution(entities, model, goal, UUID.randomUUID(), UUID.randomUUID());

        BendableBigDecimalScore score = scoreCalculator.calculateScore(solution);

        List<PredictionOutput> predictionOutputs = model.predictAsync(List.of(input)).get();

        assertTrue(score.isFeasible());

        assertEquals(2, goal.size());
        assertEquals(1, predictionOutputs.size()); // A single prediction is expected
        assertEquals(2, predictionOutputs.get(0).getOutputs().size()); // Single prediction with two features
        assertEquals(0, score.getHardScore(0).compareTo(BigDecimal.ZERO));
        assertEquals(0, score.getHardScore(1).compareTo(BigDecimal.ZERO));
        assertEquals(0, score.getHardScore(2).compareTo(BigDecimal.ZERO));
        assertEquals(0, score.getSoftScore(0).compareTo(BigDecimal.ZERO));
        assertEquals(0, score.getSoftScore(1).compareTo(BigDecimal.ZERO));
        assertEquals(3, score.getHardLevelsSize());
        assertEquals(2, score.getSoftLevelsSize());
    }

    /**
     * Using a smaller number of features in the goals (1) than the model's output (2) should
     * throw an {@link IllegalArgumentException} with the appropriate message.
     */
    @Test
    void testGoalSizeSmaller() throws ExecutionException, InterruptedException {
        final CounterFactualScoreCalculator scoreCalculator = new CounterFactualScoreCalculator();

        PredictionProvider model = TestUtils.getFeatureSkipModel(0);

        List<Feature> features = new ArrayList<>();
        List<FeatureDomain> featureDomains = new ArrayList<>();
        List<Boolean> constraints = new ArrayList<>();

        // f-1
        features.add(FeatureFactory.newNumericalFeature("f-1", 1.0));
        featureDomains.add(NumericalFeatureDomain.create(0.0, 10.0));
        constraints.add(false);

        // f-2
        features.add(FeatureFactory.newNumericalFeature("f-2", 2.0));
        featureDomains.add(NumericalFeatureDomain.create(0.0, 10.0));
        constraints.add(false);

        // f-3
        features.add(FeatureFactory.newBooleanFeature("f-3", true));
        featureDomains.add(EmptyFeatureDomain.create());
        constraints.add(false);

        PredictionInput input = new PredictionInput(features);
        PredictionFeatureDomain domains = new PredictionFeatureDomain(featureDomains);
        List<CounterfactualEntity> entities = CounterfactualEntityFactory.createEntities(input, domains, constraints, null);

        List<Output> goal = new ArrayList<>();
        goal.add(new Output("f-2", Type.NUMBER, new Value(2.0), 0.0));

        List<PredictionOutput> predictionOutputs = model.predictAsync(List.of(input)).get();

        assertEquals(1, goal.size());
        assertEquals(1, predictionOutputs.size()); // A single prediction is expected
        assertEquals(2, predictionOutputs.get(0).getOutputs().size()); // Single prediction with two features

        final CounterfactualSolution solution =
                new CounterfactualSolution(entities, model, goal, UUID.randomUUID(), UUID.randomUUID());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            scoreCalculator.calculateScore(solution);
        });

        assertEquals("Prediction size must be equal to goal size", exception.getMessage());

    }

    /**
     * Using a larger number of features in the goals (3) than the model's output (2) should
     * throw an {@link IllegalArgumentException} with the appropriate message.
     */
    @Test
    void testGoalSizeLarger() throws ExecutionException, InterruptedException {
        final CounterFactualScoreCalculator scoreCalculator = new CounterFactualScoreCalculator();

        PredictionProvider model = TestUtils.getFeatureSkipModel(0);

        List<Feature> features = new ArrayList<>();
        List<FeatureDomain> featureDomains = new ArrayList<>();
        List<Boolean> constraints = new ArrayList<>();

        // f-1
        features.add(FeatureFactory.newNumericalFeature("f-1", 1.0));
        featureDomains.add(NumericalFeatureDomain.create(0.0, 10.0));
        constraints.add(false);

        // f-2
        features.add(FeatureFactory.newNumericalFeature("f-2", 2.0));
        featureDomains.add(NumericalFeatureDomain.create(0.0, 10.0));
        constraints.add(false);

        // f-3
        features.add(FeatureFactory.newBooleanFeature("f-3", true));
        featureDomains.add(EmptyFeatureDomain.create());
        constraints.add(false);

        PredictionInput input = new PredictionInput(features);
        PredictionFeatureDomain domains = new PredictionFeatureDomain(featureDomains);
        List<CounterfactualEntity> entities = CounterfactualEntityFactory.createEntities(input, domains, constraints, null);

        List<Output> goal = new ArrayList<>();
        goal.add(new Output("f-1", Type.NUMBER, new Value(1.0), 0.0));
        goal.add(new Output("f-2", Type.NUMBER, new Value(2.0), 0.0));
        goal.add(new Output("f-3", Type.BOOLEAN, new Value(true), 0.0));

        List<PredictionOutput> predictionOutputs = model.predictAsync(List.of(input)).get();

        assertEquals(3, goal.size());
        assertEquals(1, predictionOutputs.size()); // A single prediction is expected
        assertEquals(2, predictionOutputs.get(0).getOutputs().size()); // Single prediction with two features

        final CounterfactualSolution solution =
                new CounterfactualSolution(entities, model, goal, UUID.randomUUID(), UUID.randomUUID());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            scoreCalculator.calculateScore(solution);
        });

        assertEquals("Prediction size must be equal to goal size", exception.getMessage());

    }
}
