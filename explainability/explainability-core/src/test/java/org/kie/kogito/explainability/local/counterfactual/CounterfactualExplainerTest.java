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
package org.kie.kogito.explainability.local.counterfactual;

import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.Config;
import org.kie.kogito.explainability.TestUtils;
import org.kie.kogito.explainability.local.counterfactual.entities.CounterfactualEntity;
import org.kie.kogito.explainability.model.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class CounterfactualExplainerTest {

    @Test
    void testNonEmptyInput() throws ExecutionException, InterruptedException, TimeoutException {
        Random random = new Random();

        final List<Output> goal = List.of(new Output("class", Type.BOOLEAN, new Value<>(false), 1d));
        for (int seed = 0; seed < 5; seed++) {
            random.setSeed(seed);


            List<Feature> features = new LinkedList<>();
            List<FeatureBoundary> featureBoundaries = new LinkedList<>();
            List<Boolean> constraints = new LinkedList<>();
            for (int i = 0; i < 4; i++) {
                features.add(TestUtils.getMockedNumericFeature(i));
                featureBoundaries.add(new FeatureBoundary(0.0, 1000.0));
                constraints.add(false);
            }
            final DataBoundaries dataBoundaries = new DataBoundaries(featureBoundaries);
            CounterfactualExplainer counterfactualExplainer = new CounterfactualExplainer(dataBoundaries, constraints, goal, 1L, 70, 5000);

            PredictionInput input = new PredictionInput(features);
            PredictionProvider model = TestUtils.getSumSkipModel(0);
            PredictionOutput output = model.predictAsync(List.of(input))
                    .get(Config.INSTANCE.getAsyncTimeout(), Config.INSTANCE.getAsyncTimeUnit())
                    .get(0);
            Prediction prediction = new Prediction(input, output);
            List<CounterfactualEntity> counterfactualEntities = counterfactualExplainer.explainAsync(prediction, model)
                    .get(Config.INSTANCE.getAsyncTimeout(), Config.INSTANCE.getAsyncTimeUnit());
            for (CounterfactualEntity entity : counterfactualEntities) {
                System.out.println(entity);
            }
            assertNotNull(counterfactualEntities);
        }
    }

    @Test
    void testCounterfactualMatch() throws ExecutionException, InterruptedException, TimeoutException {
        Random random = new Random();
        final List<Output> goal = List.of(new Output("inside", Type.BOOLEAN, new Value<>(true), 1d));
        for (int seed = 0; seed < 5; seed++) {
            random.setSeed(seed);


            List<Feature> features = new LinkedList<>();
            List<FeatureBoundary> featureBoundaries = new LinkedList<>();
            List<Boolean> constraints = new LinkedList<>();
            features.add(FeatureFactory.newNumericalFeature("f-num1", 100.0));
            constraints.add(false);
            featureBoundaries.add(new FeatureBoundary(0.0, 1000.0));
            features.add(FeatureFactory.newNumericalFeature("f-num2", 150.0));
            constraints.add(false);
            featureBoundaries.add(new FeatureBoundary(0.0, 1000.0));
            features.add(FeatureFactory.newNumericalFeature("f-num3", 1.0));
            constraints.add(false);
            featureBoundaries.add(new FeatureBoundary(0.0, 1000.0));
            features.add(FeatureFactory.newNumericalFeature("f-num4", 2.0));
            constraints.add(false);
            featureBoundaries.add(new FeatureBoundary(0.0, 1000.0));

            final DataBoundaries dataBoundaries = new DataBoundaries(featureBoundaries);
            CounterfactualExplainer counterfactualExplainer = new CounterfactualExplainer(dataBoundaries, constraints, goal, 5L, 70, 5000);

            final double center = 500.0;
            final double epsilon = 10.0;
            PredictionInput input = new PredictionInput(features);
            PredictionProvider model = TestUtils.getSumThresholdModel(center, epsilon);
            PredictionOutput output = model.predictAsync(List.of(input))
                    .get(Config.INSTANCE.getAsyncTimeout(), Config.INSTANCE.getAsyncTimeUnit())
                    .get(0);
            Prediction prediction = new Prediction(input, output);
            List<CounterfactualEntity> counterfactualEntities = counterfactualExplainer.explainAsync(prediction, model)
                    .get(Config.INSTANCE.getAsyncTimeout(), Config.INSTANCE.getAsyncTimeUnit());

            double totalSum = 0;
            for (CounterfactualEntity entity : counterfactualEntities) {
                totalSum += entity.asFeature().getValue().asNumber();
                System.out.println(entity);
            }
            assertTrue(totalSum <= center + epsilon);
            assertTrue(totalSum >= center - epsilon);
        }
    }

    @Test
    void testCounterfactualConstrainedMatch() throws ExecutionException, InterruptedException, TimeoutException {
        Random random = new Random();

        final List<Output> goal = List.of(new Output("inside", Type.BOOLEAN, new Value<>(true), 1d));
        for (int seed = 0; seed < 5; seed++) {
            random.setSeed(seed);

            List<Feature> features = new LinkedList<>();
            List<FeatureBoundary> featureBoundaries = new LinkedList<>();
            List<Boolean> constraints = new LinkedList<>();
            for (int i = 0; i < 4; i++) {
                features.add(TestUtils.getMockedNumericFeature(i));
                featureBoundaries.add(new FeatureBoundary(0.0, 1000.0));
                constraints.add(false);
            }
            // add a constraint
            constraints.set(2, true);
            final DataBoundaries dataBoundaries = new DataBoundaries(featureBoundaries);
            CounterfactualExplainer counterfactualExplainer = new CounterfactualExplainer(dataBoundaries, constraints, goal, 5L, 70, 5000);

            final double center = 500.0;
            final double epsilon = 10.0;
            PredictionInput input = new PredictionInput(features);
            PredictionProvider model = TestUtils.getSumThresholdModel(center, epsilon);
            PredictionOutput output = model.predictAsync(List.of(input))
                    .get(Config.INSTANCE.getAsyncTimeout(), Config.INSTANCE.getAsyncTimeUnit())
                    .get(0);
            Prediction prediction = new Prediction(input, output);
            List<CounterfactualEntity> counterfactualEntities = counterfactualExplainer.explainAsync(prediction, model)
                    .get(Config.INSTANCE.getAsyncTimeout(), Config.INSTANCE.getAsyncTimeUnit());

            double totalSum = 0;
            for (CounterfactualEntity entity : counterfactualEntities) {
                totalSum += entity.asFeature().getValue().asNumber();
                System.out.println(entity);
            }
            assertFalse(counterfactualEntities.get(2).isChanged());
            assertTrue(totalSum <= center + epsilon);
            assertTrue(totalSum >= center - epsilon);
        }
    }

    @Test
    void testCounterfactualBoolean() throws ExecutionException, InterruptedException, TimeoutException {
        Random random = new Random();

        final List<Output> goal = List.of(new Output("inside", Type.BOOLEAN, new Value<>(true), 1d));
        for (int seed = 0; seed < 5; seed++) {
            random.setSeed(seed);

            List<Feature> features = new LinkedList<>();
            List<FeatureBoundary> featureBoundaries = new LinkedList<>();
            List<Boolean> constraints = new LinkedList<>();
            for (int i = 0; i < 4; i++) {
                features.add(TestUtils.getMockedNumericFeature(i));
                featureBoundaries.add(new FeatureBoundary(0.0, 1000.0));
                constraints.add(false);
            }
            features.add(FeatureFactory.newBooleanFeature("f-bool", true));
            featureBoundaries.add(null);
            constraints.add(false);
            // add a constraint
            constraints.set(2, true);
            final DataBoundaries dataBoundaries = new DataBoundaries(featureBoundaries);
            CounterfactualExplainer counterfactualExplainer = new CounterfactualExplainer(dataBoundaries, constraints, goal, 5L, 70, 5000);

            final double center = 500.0;
            final double epsilon = 10.0;
            PredictionInput input = new PredictionInput(features);
            PredictionProvider model = TestUtils.getSumThresholdModel(center, epsilon);
            PredictionOutput output = model.predictAsync(List.of(input))
                    .get(Config.INSTANCE.getAsyncTimeout(), Config.INSTANCE.getAsyncTimeUnit())
                    .get(0);
            Prediction prediction = new Prediction(input, output);
            List<CounterfactualEntity> counterfactualEntities = counterfactualExplainer.explainAsync(prediction, model)
                    .get(Config.INSTANCE.getAsyncTimeout(), Config.INSTANCE.getAsyncTimeUnit());

            double totalSum = 0;
            for (CounterfactualEntity entity : counterfactualEntities) {
                totalSum += entity.asFeature().getValue().asNumber();
                System.out.println(entity);
            }
            assertFalse(counterfactualEntities.get(2).isChanged());
            assertTrue(totalSum <= center + epsilon);
            assertTrue(totalSum >= center - epsilon);
        }
    }
}