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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.model.DataDistribution;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureDistribution;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DataUtilsTest {

    @Test
    void testDataGeneration() {
        double mean = 0.5;
        double stdDeviation = 0.1;
        int size = 100;
        double[] data = DataUtils.generateData(mean, stdDeviation, size);
        // check the sum of deviations from mean is zero
        double sum = 0;
        for (double d : data) {
            sum += d - mean;
        }
        assertEquals(0, sum, 1e-4);
    }

    @Test
    void testGaussianKernel() {
        double x = 0.218;
        double k = DataUtils.gaussianKernel(x);
        assertEquals(0.551, k, 1e-3);
    }

    @Test
    void testEuclideanDistance() {
        double[] x = new double[]{1, 1};
        double[] y = new double[]{2, 3};
        double distance = DataUtils.euclideanDistance(x, y);
        assertEquals(2.236, distance, 1e-3);
    }

    @Test
    void testGowerDistance() {
        double[] x = new double[]{2, 1};
        double[] y = new double[]{2, 3};
        double distance = DataUtils.gowerDistance(x, y, 0.5);
        assertEquals(2.5, distance, 1e-2);
    }

    @Test
    void testHammingDistance() {
        double[] x = new double[]{2, 1};
        double[] y = new double[]{2, 3};
        double distance = DataUtils.hammingDistance(x, y);
        assertEquals(1, distance, 1e-1);
    }

    @Test
    void testExponentialSmoothingKernel() {
        double x = 0.218;
        double k = DataUtils.exponentialSmoothingKernel(x, 2);
        assertEquals(0.994, k, 1e-3);
    }

    @Test
    void testPerturbDropNumericZero() {
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newNumericalFeature("f0", 1));
        features.add(FeatureFactory.newNumericalFeature("f1", 3.14));
        features.add(FeatureFactory.newNumericalFeature("f2", 5));
        PredictionInput input = new PredictionInput(features);
        assertPerturbDropNumeric(input, 0);
    }

    @Test
    void testPerturbDropNumericOne() {
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newNumericalFeature("f0", 1));
        features.add(FeatureFactory.newNumericalFeature("f1", 3.14));
        features.add(FeatureFactory.newNumericalFeature("f2", 0.55));
        PredictionInput input = new PredictionInput(features);
        assertPerturbDropNumeric(input, 1);
    }

    @Test
    void testPerturbDropNumericTwo() {
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newNumericalFeature("f0", 1));
        features.add(FeatureFactory.newNumericalFeature("f1", 3.14));
        features.add(FeatureFactory.newNumericalFeature("f2", 0.55));
        PredictionInput input = new PredictionInput(features);
        assertPerturbDropNumeric(input, 2);
    }

    @Test
    void testPerturbDropNumericThree() {
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newNumericalFeature("f0", 1));
        features.add(FeatureFactory.newNumericalFeature("f1", 3.14));
        features.add(FeatureFactory.newNumericalFeature("f2", 0.55));
        PredictionInput input = new PredictionInput(features);
        assertPerturbDropNumeric(input, 3);
    }

    @Test
    void testPerturbDropStringZero() {
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newTextFeature("f0", "foo"));
        features.add(FeatureFactory.newTextFeature("f1", "foo bar"));
        features.add(FeatureFactory.newTextFeature("f2", " "));
        features.add(FeatureFactory.newTextFeature("f3", "foo bar "));
        PredictionInput input = new PredictionInput(features);
        assertPerturbDropString(input, 0);
    }

    @Test
    void testPerturbDropStringOne() {
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newTextFeature("f0", "foo"));
        features.add(FeatureFactory.newTextFeature("f1", "foo bar"));
        features.add(FeatureFactory.newTextFeature("f2", " "));
        features.add(FeatureFactory.newTextFeature("f3", "foo bar "));
        PredictionInput input = new PredictionInput(features);
        assertPerturbDropString(input, 1);
    }

    @Test
    void testPerturbDropStringTwo() {
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newTextFeature("f0", "foo"));
        features.add(FeatureFactory.newTextFeature("f1", "foo bar"));
        features.add(FeatureFactory.newTextFeature("f2", " "));
        features.add(FeatureFactory.newTextFeature("f3", "foo bar "));
        PredictionInput input = new PredictionInput(features);
        assertPerturbDropString(input, 2);
    }

    @Test
    void testPerturbDropStringThree() {
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newTextFeature("f0", "foo"));
        features.add(FeatureFactory.newTextFeature("f1", "foo bar"));
        features.add(FeatureFactory.newTextFeature("f2", " "));
        features.add(FeatureFactory.newTextFeature("f3", "foo bar "));
        PredictionInput input = new PredictionInput(features);
        assertPerturbDropString(input, 3);
    }

    private void assertPerturbDropNumeric(PredictionInput input, int noOfPerturbations) {
        PredictionInput perturbedInput = DataUtils.perturbDrop(input, 10, noOfPerturbations);
        int changedFeatures = 0;
        for (int i = 0; i < input.getFeatures().size(); i++) {
            double v = input.getFeatures().get(i).getValue().asNumber();
            double pv = perturbedInput.getFeatures().get(i).getValue().asNumber();
            if (v != pv) {
                changedFeatures++;
            }
        }
        assertEquals(noOfPerturbations, changedFeatures);
    }

    private void assertPerturbDropString(PredictionInput input, int noOfPerturbations) {
        PredictionInput perturbedInput = DataUtils.perturbDrop(input, 10, noOfPerturbations);
        int changedFeatures = 0;
        for (int i = 0; i < input.getFeatures().size(); i++) {
            String v = input.getFeatures().get(i).getValue().asString();
            String pv = perturbedInput.getFeatures().get(i).getValue().asString();
            if (!v.equals(pv)) {
                changedFeatures++;
            }
        }
        assertEquals(noOfPerturbations, changedFeatures);
    }

    @Test
    void testDoublesToFeatures() {
        double[] inputs = new double[10];
        for (int i = 0; i < 10; i++) {
            inputs[i] = i % 2 == 0 ? 1 : 0;
        }
        List<Feature> features = DataUtils.doublesToFeatures(inputs);
        assertNotNull(features);
        assertEquals(10, features.size());
        for (Feature f : features) {
            assertNotNull(f);
            assertNotNull(f.getName());
            assertEquals(Type.NUMBER, f.getType());
            assertNotNull(f.getValue());
        }
    }

    @Test
    void testDoubleToFeature() {
        double d = 0.5;
        Feature f = DataUtils.doubleToFeature(d);
        assertNotNull(f);
        assertNotNull(f.getName());
        assertEquals(Type.NUMBER, f.getType());
        assertNotNull(f.getValue());
    }

    @Test
    void testRandomDistributionGeneration() {
        DataDistribution dataDistribution = DataUtils.generateRandomDataDistribution(10);
        assertNotNull(dataDistribution);
        assertNotNull(dataDistribution.getFeatureDistributions());
        for (FeatureDistribution featureDistribution : dataDistribution.getFeatureDistributions()) {
            assertNotNull(featureDistribution);
        }
    }

    @Test
    void testGetFeatureDistribution() {
        double[] doubles = new double[10];
        Arrays.fill(doubles, 1);
        FeatureDistribution featureDistribution = DataUtils.getFeatureDistribution(doubles);
        assertNotNull(featureDistribution);
    }

    @Test
    void testGetLinearizedFeatures() {
        List<Feature> features = new LinkedList<>();
        Feature f = mock(Feature.class);
        Value<?> value = mock(Value.class);
        when(f.getValue()).thenReturn(value);
        when(f.getName()).thenReturn("name");
        when(f.getType()).thenReturn(Type.NUMBER);
        features.add(f);
        List<Feature> linearizedFeatures = DataUtils.getLinearizedFeatures(features);
        assertEquals(features.size(), linearizedFeatures.size());
    }

}