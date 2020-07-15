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
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.PredictionInput;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.utils.DataUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataUtilsTest {

    private final static SecureRandom random = new SecureRandom();

    @Test
    public void testDataGeneration() {
        double mean = 1d / (1d + random.nextInt(10));
        double stdDeviation = Math.abs(1d / (1d + random.nextInt(10)));
        int size = random.nextInt(100);
        double[] data = DataUtils.generateData(mean, stdDeviation, size);
        // check the sum of deviations from mean is zero
        double sum = 0;
        for (double d : data) {
            sum += d - mean;
        }
        assertEquals(0, sum, 1e-4);
    }

    @Test
    public void testGaussianKernel() {
        double x = 0.218;
        double k = DataUtils.gaussianKernel(x);
        assertEquals(0.551, k, 1e-3);
    }

    @Test
    public void testEuclideanDistance() {
        double[] x = new double[]{1, 1};
        double[] y = new double[]{2, 3};
        double distance = DataUtils.euclideanDistance(x, y);
        assertEquals(2.236, distance, 1e-3);
    }

    @Test
    public void testGowerDistance() {
        double[] x = new double[]{2, 1};
        double[] y = new double[]{2, 3};
        double distance = DataUtils.gowerDistance(x, y, 0.5);
        assertEquals(2.5, distance, 1e-2);
    }

    @Test
    public void testHammingDistance() {
        double[] x = new double[]{2, 1};
        double[] y = new double[]{2, 3};
        double distance = DataUtils.hammingDistance(x, y);
        assertEquals(1, distance, 1e-1);
    }

    @Test
    public void testExponentialSmoothingKernel() {
        double x = 0.218;
        double k = DataUtils.exponentialSmoothingKernel(x, 2);
        assertEquals(0.994, k, 1e-3);
    }

    @Test
    public void testPerturbDrop() {
        List<Feature> features = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            features.add(FeatureFactory.newNumericalFeature("f" + random.nextInt(), random.nextInt()));
        }
        PredictionInput input = new PredictionInput(features);
        int noOfPerturbations = random.nextInt(3);
        PredictionInput perturbedInput = DataUtils.perturbDrop(input, 10, noOfPerturbations);
        int changedFeatures = 0;
        for (int i = 0; i < 3; i++) {
            double v = input.getFeatures().get(i).getValue().asNumber();
            double pv = perturbedInput.getFeatures().get(i).getValue().asNumber();
            if (v != pv) {
                changedFeatures++;
            }
        }
        assertEquals(changedFeatures, noOfPerturbations);
    }
}