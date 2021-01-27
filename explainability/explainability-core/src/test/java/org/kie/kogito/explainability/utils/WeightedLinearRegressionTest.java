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
package org.kie.kogito.explainability.utils;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


class WeightedLinearRegressionTest {
    // Setup some consistent random generators for various parameters
    Random rn = new Random();
    int nRandomTests = 1;
    int generateNFeatures(){
        return this.rn.nextInt(5)+1;
    }

    int generateNSamples(int nfeatures){
        return this.rn.nextInt(nfeatures*2) + nfeatures + 1;
    }

    double generateWeight(){
        return 1 + (10 * this.rn.nextDouble());
    }

    double generateXPoint(){
        return -50 + (100 * this.rn.nextDouble());
    }

    double generateCoeff(){
        return -50 + (100 * this.rn.nextDouble());
    }

    // test the case where we have a over/fully specified system of equations, no intercept
    @Test
    void testNoIntercept() {
        for (int test_num = 0; test_num<nRandomTests; test_num++) {

            // create test conditions at random
            int nfeatures = generateNFeatures();
            int nsamples = generateNSamples(nfeatures);

            double[][] X = new double[nsamples][nfeatures];
            double[] Y = new double[nsamples];
            double[] actualCoeffs = new double[nfeatures];
            double[] sampleWeights = new double[nsamples];
            double totalWeight = 0;

            // build input and output data, along with sample weights
            for (int i = 0; i < nsamples; i++) {
                Y[i] = 0;
                double weight = generateWeight();
                totalWeight += weight;
                sampleWeights[i] = weight;
                for (int j = 0; j < nfeatures; j++) {
                    X[i][j] = generateXPoint();
                    if (i == 0) {
                        actualCoeffs[j] = generateCoeff();
                    }
                    Y[i] += actualCoeffs[j] * X[i][j];
                }
            }

            // normalize sample weights
            for (int i = 0; i < nsamples; i++) {
                sampleWeights[i] /= totalWeight;
            }

            // test to recover initial parameters
            WeightedLinearRegression WLR = new WeightedLinearRegression(false);
            assertTrue(Double.isNaN(WLR.getGoodnessOfFit()));
            double[] coeffs = WLR.fit(X, Y, sampleWeights);
            assertArrayEquals(actualCoeffs, coeffs, 1e-3);
            assertEquals(1, WLR.getGoodnessOfFit(),1e-3);
            assertEquals(0, WLR.getMSE(),1e-3);
        }
    }

    @Test
    void testZeroWeights() {
        for (int test_num = 0; test_num<nRandomTests; test_num++) {

            // create test conditions at random
            int nfeatures = generateNFeatures();
            int nsamples = generateNSamples(nfeatures);

            double[][] X = new double[nsamples][nfeatures];
            double[] Y = new double[nsamples];
            double[] actualCoeffs = new double[nfeatures];
            double[] sampleWeights = new double[nsamples];

            // build input and output data, along with sample weights
            for (int i = 0; i < nsamples; i++) {
                Y[i] = 0;
                double weight = generateWeight();
                sampleWeights[i] = 0;
                for (int j = 0; j < nfeatures; j++) {
                    X[i][j] = generateXPoint();
                    if (i == 0) {
                        actualCoeffs[j] = generateCoeff();
                    }
                    Y[i] += actualCoeffs[j] * X[i][j];
                }
            }

            // test to recover initial parameters
            WeightedLinearRegression WLR = new WeightedLinearRegression(false);
            WLR.fit(X, Y, sampleWeights);
            assertThrows(ArithmeticException.class, WLR::getGoodnessOfFit);
            assertThrows(ArithmeticException.class, WLR::getMSE);
        }
    }

    // test the case where we have a over/fully specified system of equations, with intercept
    @Test
    void testWithIntercept() {
        for (int test_num = 0; test_num<nRandomTests; test_num++) {

            // create test conditions at random
            int nfeatures = generateNFeatures();
            int nsamples = generateNSamples(nfeatures);

            double[][] X = new double[nsamples][nfeatures];
            double[] Y = new double[nsamples];
            double[] actualCoeffs = new double[nfeatures+1];
            double[] sampleWeights = new double[nsamples];
            double totalWeight = 0;

            // build input and output data, along with sample weights
            for (int i = 0; i < nsamples; i++) {
                Y[i] = 0;
                double weight = generateWeight();
                totalWeight += weight;
                sampleWeights[i] = weight;
                for (int j = 0; j < nfeatures+1; j++) {
                    if (i==0) {
                        actualCoeffs[j] = generateXPoint();;
                    }
                    if (j < nfeatures) {
                        X[i][j] = generateCoeff();
                        Y[i] += actualCoeffs[j] * X[i][j];
                    } else {
                        // last coefficient is the intercept
                        Y[i] += actualCoeffs[nfeatures];
                    }
                }
            }

            // normalize sample weights
            for (int i = 0; i < nsamples; i++) {
                sampleWeights[i] /= totalWeight;
            }

            // test to recover initial parameters
            WeightedLinearRegression WLR = new WeightedLinearRegression(true);
            double[] coeffs = WLR.fit(X, Y, sampleWeights);
            assertArrayEquals(actualCoeffs, coeffs, 1e-2);
            assertEquals(1, WLR.getGoodnessOfFit(),1e-2);
            assertEquals(0, WLR.getMSE(),1e-3);
        }
    }

    // test the case where we have a under-specified system of equations, with intercept
    @Test
    void testUnderspecified() {
        for (int test_num = 0; test_num<nRandomTests; test_num++) {

            // create test conditions at random
            int nfeatures = generateNFeatures() + 5;
            int nsamples = Math.max(2, nfeatures - rn.nextInt(nfeatures));

            double[][] X = new double[nsamples][nfeatures];
            double[] Y = new double[nsamples];
            double[] actualCoeffs = new double[nfeatures+1];
            double[] sampleWeights = new double[nsamples];
            double totalWeight = 0;

            // build input and output data, along with sample weights
            for (int i = 0; i < nsamples; i++) {
                Y[i] = 0;
                double weight = generateWeight();
                totalWeight += weight;
                sampleWeights[i] = weight;
                for (int j = 0; j < nfeatures+1; j++) {
                    if (i==0) {
                        actualCoeffs[j] = generateCoeff();
                    }
                    if (j < nfeatures) {
                        X[i][j] = generateXPoint();
                        Y[i] += actualCoeffs[j] * X[i][j];
                    } else {
                        // last coefficient is the intercept
                        Y[i] += actualCoeffs[nfeatures];
                    }
                }
            }

            // normalize sample weights
            for (int i = 0; i < nsamples; i++) {
                sampleWeights[i] /= totalWeight;
            }

            // test to recover initial parameters
            WeightedLinearRegression WLR = new WeightedLinearRegression(true);
            double[] coeffs = WLR.fit(X, Y, sampleWeights);
            for (int i=0; i<coeffs.length; i++) {
                assertFalse(Double.isNaN(coeffs[i]));
            }
            assertFalse(Double.isNaN(WLR.getGoodnessOfFit()));
            assertFalse(Double.isNaN(WLR.getMSE()));
        }
    }

    // test the case where we have a vastly under-specified system of equations (only one sample), with intercept
    @Test
    void testSingularMatrix() {
        for (int test_num = 0; test_num<nRandomTests; test_num++) {

            // create test conditions at random
            int nfeatures = generateNFeatures() + 5;
            int nsamples = 1;

            double[][] X = new double[nsamples][nfeatures];
            double[] Y = new double[nsamples];
            double[] actualCoeffs = new double[nfeatures+1];
            double[] sampleWeights = new double[nsamples];
            double totalWeight = 0;

            // build input and output data, along with sample weights
            for (int i = 0; i < nsamples; i++) {
                Y[i] = 0;
                double weight = generateWeight();
                totalWeight += weight;
                sampleWeights[i] = weight;
                for (int j = 0; j < nfeatures+1; j++) {
                    if (i==0) {
                        actualCoeffs[j] = generateCoeff();
                    }
                    if (j < nfeatures) {
                        X[i][j] = generateXPoint();
                        Y[i] += actualCoeffs[j] * X[i][j];
                    } else {
                        // last coefficient is the intercept
                        Y[i] += actualCoeffs[nfeatures];
                    }
                }
            }

            // normalize sample weights
            for (int i = 0; i < nsamples; i++) {
                sampleWeights[i] /= totalWeight;
            }

            // test to recover initial parameters
            WeightedLinearRegression WLR = new WeightedLinearRegression(true);
            double[] coeffs = WLR.fit(X, Y, sampleWeights);
            for (int i=0; i<coeffs.length; i++) {
                assertFalse(Double.isNaN(coeffs[i]));
            }
            assertThrows(ArithmeticException.class, WLR::getGoodnessOfFit);
        }
    }
}