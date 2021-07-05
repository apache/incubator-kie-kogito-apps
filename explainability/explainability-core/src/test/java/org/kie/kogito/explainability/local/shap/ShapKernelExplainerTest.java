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

package org.kie.kogito.explainability.local.shap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.TestUtils;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.model.ShapPrediction;
import org.kie.kogito.explainability.utils.MatrixUtils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ShapKernelExplainerTest {
    double[][] backgroundRaw = {
            { 1., 2., 3., -4., 5. },
            { 10., 11., 12., -4., 13. },
            { 2., 3, 4., -4., 6. },
    };
    double[][] toExplainRaw = {
            { 5., 6., 7., -4., 8. },
            { 11., 12., 13., -5., 14. },
            { 0., 0, 1., 4., 2. },
    };

    // no variance test case matrices  ===============
    double[][] backgroundNoVariance = {
            { 1., 2., 3. },
            { 1., 2., 3. }
    };

    double[][] toExplainZeroVariance = {
            { 1., 2., 3. },
            { 1., 2., 3. },
    };

    double[][][] zeroVarianceOneOutputSHAP = {
            { { 0., 0., 0. } },
            { { 0., 0., 0. } },
    };

    double[][][] zeroVarianceMultiOutputSHAP = {
            { { 0., 0., 0. }, { 0., 0., 0 } },
            { { 0., 0., 0. }, { 0., 0., 0 } },
    };

    // single variance test case matrices ===============
    double[][] toExplainOneVariance = {
            { 3., 2., 3. },
            { 1., 2., 2. },
    };

    double[][][] oneVarianceOneOutputSHAP = {
            { { 2., 0., 0. } },
            { { 0., 0., -1. } },
    };

    double[][][] oneVarianceMultiOutputSHAP = {
            { { 2., 0., 0. }, { 4., 0., 0. } },
            { { 0., 0., -1. }, { 0., 0., -2 } },
    };

    // multi variance, one output logit test case matrices ===============
    double[][] toExplainLogit = {
            { 0.1, 0.12, 0.14, -0.08, 0.16 },
            { 0.22, 0.24, 0.26, -0.1, 0.38 },
            { -0.1, 0., 0.02, 0.1, 0.04 }
    };

    double[][] backgroundLogit = {
            { 0.02380952, 0.04761905, 0.07142857, -0.0952381, 0.11904762 },
            { 0.23809524, 0.26190476, 0.28571429, -0.0952381, 0.30952381 },
            { 0.04761905, 0.07142857, 0.11904762, -0.0952381, 0.14285714 }
    };

    double[][][] logitSHAP = {
            { { -0.01420862, 0., -0.08377778, 0.06825253, -0.13625127 } },
            { { 0.50970797, 0., 0.44412765, -0.02169177, 0.80832232 } },
            { { Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN } }
    };

    // multiple variance test case matrices ===============
    double[][][] multiVarianceOneOutputSHAP = {
            { { 0.66666667, 0., 0.66666667, 0., 0. } },
            { { 6.66666667, 0., 6.66666667, -1., 6. } },
            { { -4.33333333, 0., -5.33333333, 8., -6. } },
    };

    double[][][] multiVarianceMultiOutputSHAP = {
            { { 0.66666667, 0., 0.66666667, 0., 0. }, { 1.333333333, 0., 1.33333333, 0., 0. } },
            { { 6.66666667, 0., 6.66666667, -1., 6. }, { 13.333333333, 0., 13.333333333, -2., 12. } },
            { { -4.33333333, 0., -5.33333333, 8., -6. }, { -8.6666666667, 0., -10.666666666, 16., -12. } },
    };

    Random rn = new Random(0);

    ShapConfig.Builder testConfig = ShapConfig.builder().withLink(ShapConfig.LinkType.IDENTITY).withRN(rn);

    // test helper functions ===========================================================================================
    // create a list of prediction inputs from double matrix
    private List<PredictionInput> createPIFromMatrix(double[][] m) {
        List<PredictionInput> pis = new ArrayList<>();
        int[] shape = MatrixUtils.getShape(m);
        for (int i = 0; i < shape[0]; i++) {
            List<Feature> fs = new ArrayList<>();
            for (int j = 0; j < shape[1]; j++) {
                fs.add(FeatureFactory.newNumericalFeature("f", m[i][j]));
            }
            pis.add(new PredictionInput(fs));
        }
        return pis;
    }

    /*
     * given a specific model, config, background, explanations, and expected shap values,
     * test that the computed shape values match expected shap values
     */
    private void shapTestCase(PredictionProvider model, ShapConfig skConfig,
            double[][] toExplainRaw, double[][][] expected)
            throws InterruptedException, TimeoutException, ExecutionException {

        // establish background data and desired data to explain

        List<PredictionInput> toExplain = createPIFromMatrix(toExplainRaw);

        //initialize explainer
        List<PredictionOutput> predictionOutputs = model.predictAsync(toExplain).get(5, TimeUnit.SECONDS);
        List<Prediction> predictions = new ArrayList<>();
        for (int i = 0; i < predictionOutputs.size(); i++) {
            predictions.add(new ShapPrediction(toExplain.get(i), predictionOutputs.get(i), skConfig));
        }

        // evaluate if the explanations match the expected value
        for (int i = 0; i < toExplain.size(); i++) {
            //explanations shape: outputSize x nfeatures
            double[][] explanations = new ShapKernelExplainer().explainAsync(predictions.get(i), model).get(5, TimeUnit.SECONDS);
            for (int j = 0; j < explanations.length; j++) {
                assertArrayEquals(expected[i][j], explanations[j], 1e-6);
            }
        }
    }

    /*
     * given a specific model, config, background, explanations, ske, and expected shap values,
     * test that the computed shape values match expected shap values
     */
    private void shapTestCase(PredictionProvider model, ShapConfig skConfig, ShapKernelExplainer ske,
            double[][] toExplainRaw, double[][][] expected)
            throws InterruptedException, TimeoutException, ExecutionException {

        // establish background data and desired data to explain

        List<PredictionInput> toExplain = createPIFromMatrix(toExplainRaw);

        //initialize explainer
        List<PredictionOutput> predictionOutputs = model.predictAsync(toExplain).get(5, TimeUnit.SECONDS);
        List<Prediction> predictions = new ArrayList<>();
        for (int i = 0; i < predictionOutputs.size(); i++) {
            predictions.add(new ShapPrediction(toExplain.get(i), predictionOutputs.get(i), skConfig));
        }

        // evaluate if the explanations match the expected value
        for (int i = 0; i < toExplain.size(); i++) {
            //explanations shape: outputSize x nfeatures
            double[][] explanations = ske.explainAsync(predictions.get(i), model).get(5, TimeUnit.SECONDS);
            for (int j = 0; j < explanations.length; j++) {
                assertArrayEquals(expected[i][j], explanations[j], 1e-6);
            }
        }
    }

    // Single output models ============================================================================================
    // test a single output model with no varying features
    @Test
    void testNoVarianceOneOutput() throws InterruptedException, TimeoutException, ExecutionException {
        PredictionProvider model = TestUtils.getSumSkipModel(1);
        List<PredictionInput> background = createPIFromMatrix(backgroundNoVariance);
        ShapConfig skConfig = testConfig.withBackground(background).withNSamples(100).build();
        shapTestCase(model, skConfig, toExplainZeroVariance, zeroVarianceOneOutputSHAP);
    }

    // test a single output model with one varying feature
    @Test
    void testOneVarianceOneOutput() throws InterruptedException, TimeoutException, ExecutionException {
        PredictionProvider model = TestUtils.getSumSkipModel(1);
        List<PredictionInput> background = createPIFromMatrix(backgroundNoVariance);
        ShapConfig skConfig = testConfig.withBackground(background).withNSamples(100).build();
        shapTestCase(model, skConfig, toExplainOneVariance, oneVarianceOneOutputSHAP);
    }

    // test a single output model with many varying features
    @Test
    void testMultiVarianceOneOutput() throws InterruptedException, TimeoutException, ExecutionException {
        PredictionProvider model = TestUtils.getSumSkipModel(1);
        List<PredictionInput> background = createPIFromMatrix(backgroundRaw);
        ShapConfig skConfig = testConfig.withBackground(background).withNSamples(35).build();
        shapTestCase(model, skConfig, toExplainRaw, multiVarianceOneOutputSHAP);
    }

    // test a single output model with many varying features and logit link
    @Test
    void testMultiVarianceOneOutputLogit() throws InterruptedException, TimeoutException, ExecutionException {
        PredictionProvider model = TestUtils.getSumSkipModel(1);
        List<PredictionInput> background = createPIFromMatrix(backgroundLogit);
        ShapConfig skConfig = ShapConfig.builder()
                .withBackground(background)
                .withLink(ShapConfig.LinkType.LOGIT)
                .withNSamples(100)
                .withRN(rn)
                .build();
        shapTestCase(model, skConfig, toExplainLogit, logitSHAP);
    }

    // Multi-output models =============================================================================================
    // test a multi-output model with no varying features
    @Test
    void testNoVarianceMultiOutput() throws InterruptedException, TimeoutException, ExecutionException {
        PredictionProvider model = TestUtils.getSumSkipTwoOutputModel(1);
        List<PredictionInput> background = createPIFromMatrix(backgroundNoVariance);
        ShapConfig skConfig = testConfig.withBackground(background).build();
        shapTestCase(model, skConfig, toExplainZeroVariance, zeroVarianceMultiOutputSHAP);
    }

    // test a multi-output model with one varying feature
    @Test
    void testOneVarianceMultiOutput() throws InterruptedException, TimeoutException, ExecutionException {
        PredictionProvider model = TestUtils.getSumSkipTwoOutputModel(1);
        List<PredictionInput> background = createPIFromMatrix(backgroundNoVariance);
        ShapConfig skConfig = testConfig.withBackground(background).build();
        shapTestCase(model, skConfig, toExplainOneVariance, oneVarianceMultiOutputSHAP);
    }

    // test a multi-output model with many varying features
    @Test
    void testMultiVarianceMultiOutput() throws InterruptedException, TimeoutException, ExecutionException {
        PredictionProvider model = TestUtils.getSumSkipTwoOutputModel(1);
        List<PredictionInput> background = createPIFromMatrix(backgroundRaw);
        ShapConfig skConfig = testConfig.withBackground(background).build();
        shapTestCase(model, skConfig, toExplainRaw, multiVarianceMultiOutputSHAP);
    }

    // Test cases where search space cannot be fully enumerated ========================================================
    @Test
    void testLargeBackground() throws InterruptedException, TimeoutException, ExecutionException {
        // establish background data and desired data to explain
        double[][] largeBackground = new double[100][10];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 10; j++) {
                largeBackground[i][j] = i / 100. + j;
            }
        }
        double[][] toExplainLargeBackground = {
                { 0, 1., -2., 3.5, -4.1, 5.5, -12., .8, .11, 15. }
        };

        double[][][] expected = {
                { { -0.495, 0., -4.495, 0.005, -8.595, 0.005, -18.495,
                        -6.695, -8.385, 5.505 } }
        };

        List<PredictionInput> background = createPIFromMatrix(largeBackground);
        List<PredictionInput> toExplain = createPIFromMatrix(toExplainLargeBackground);

        PredictionProvider model = TestUtils.getSumSkipModel(1);
        ShapConfig skConfig = testConfig.withBackground(background).build();

        //initialize explainer
        List<PredictionOutput> predictionOutputs = model.predictAsync(toExplain).get();
        List<Prediction> predictions = new ArrayList<>();
        for (int i = 0; i < predictionOutputs.size(); i++) {
            predictions.add(new ShapPrediction(toExplain.get(i), predictionOutputs.get(i), skConfig));
        }

        // evaluate if the explanations match the expected value
        for (int i = 0; i < toExplain.size(); i++) {
            double[][] explanations = new ShapKernelExplainer().explainAsync(predictions.get(i), model).get(5, TimeUnit.SECONDS);
            for (int j = 0; j < explanations.length; j++) {
                assertArrayEquals(expected[i][j], explanations[j], 1e-2);
            }
        }
    }

    @Test
    void testParallel() throws InterruptedException, ExecutionException {
        // establish background data and desired data to explain
        double[][] largeBackground = new double[100][10];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 10; j++) {
                largeBackground[i][j] = i / 100. + j;
            }
        }
        double[][] toExplainLargeBackground = {
                { 0, 1., -2., 3.5, -4.1, 5.5, -12., .8, .11, 15. }
        };

        double[][][] expected = {
                { { -0.495, 0., -4.495, 0.005, -8.595, 0.005, -18.495,
                        -6.695, -8.385, 5.505 } }
        };

        List<PredictionInput> background = createPIFromMatrix(largeBackground);
        List<PredictionInput> toExplain = createPIFromMatrix(toExplainLargeBackground);

        PredictionProvider model = TestUtils.getSumSkipModel(1);
        ShapConfig skConfig = testConfig.withBackground(background).build();

        //initialize explainer
        List<PredictionOutput> predictionOutputs = model.predictAsync(toExplain).get();
        List<Prediction> predictions = new ArrayList<>();
        for (int i = 0; i < predictionOutputs.size(); i++) {
            predictions.add(new ShapPrediction(toExplain.get(i), predictionOutputs.get(i), skConfig));
        }

        // evaluate if the explanations match the expected value{
        CompletableFuture<double[][]> explanationsCF = new ShapKernelExplainer().explainAsync(predictions.get(0), model);

        ExecutorService executor = ForkJoinPool.commonPool();
        executor.submit(() -> {
            double[][] explanations = explanationsCF.join();
            assertArrayEquals(expected[0][0], explanations[0], 1e-2);
        });
    }

    // Test cases with size errors ========================================================
    @Test
    void testTooLargeBackground() throws InterruptedException, TimeoutException, ExecutionException {
        // establish background data and desired data to explain
        double[][] tooLargeBackground = new double[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tooLargeBackground[i][j] = i / 10. + j;
            }
        }
        double[][] toExplainTooSmall = {
                { 0, 1., 2., 3., 4. }
        };

        List<PredictionInput> background = createPIFromMatrix(tooLargeBackground);
        List<PredictionInput> toExplain = createPIFromMatrix(toExplainTooSmall);

        PredictionProvider model = TestUtils.getSumSkipModel(1);
        ShapConfig skConfig = testConfig.withBackground(background).build();

        //initialize explainer
        List<PredictionOutput> predictionOutputs = model
                .predictAsync(toExplain)
                .get(5, TimeUnit.SECONDS);
        List<Prediction> predictions = new ArrayList<>();
        for (int i = 0; i < predictionOutputs.size(); i++) {
            predictions.add(new ShapPrediction(toExplain.get(i), predictionOutputs.get(i), skConfig));
        }

        // make sure we get an illegal argument exception because our background is bigger than the point to be explained
        Prediction p = predictions.get(0);
        ShapKernelExplainer ske = new ShapKernelExplainer();
        assertThrows(IllegalArgumentException.class, () -> ske.explainAsync(p, model));
    }

    // Test cases with prediction size mismatches ========================================================
    @Test
    void testPredictionWrongSize() throws InterruptedException, TimeoutException, ExecutionException {
        // establish background data and desired data to explain
        double[][] backgroundMat = new double[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                backgroundMat[i][j] = i / 5. + j;
            }
        }
        double[][] toExplainTooSmall = {
                { 0, 1., 2., 3., 4. }
        };

        List<PredictionInput> background = createPIFromMatrix(backgroundMat);
        List<PredictionInput> toExplain = createPIFromMatrix(toExplainTooSmall);

        PredictionProvider modelForPredictions = TestUtils.getSumSkipTwoOutputModel(1);
        PredictionProvider modelForShap = TestUtils.getSumSkipModel(1);
        ShapConfig skConfig = testConfig.withBackground(background).build();

        //initialize explainer
        List<PredictionOutput> predictionOutputs = modelForPredictions
                .predictAsync(toExplain)
                .get(5, TimeUnit.SECONDS);
        List<Prediction> predictions = new ArrayList<>();
        for (int i = 0; i < predictionOutputs.size(); i++) {
            predictions.add(new ShapPrediction(toExplain.get(i), predictionOutputs.get(i), skConfig));
        }

        // make sure we get an illegal argument exception; our prediction to explain has a different shape t
        // than the background predictions will
        Prediction p = predictions.get(0);
        ShapKernelExplainer ske = new ShapKernelExplainer();
        assertThrows(ExecutionException.class, () -> ske.explainAsync(p, modelForShap).get());
    }

    // See if using the same explainer multiple times causes issues ====================================================
    @Test
    void testStateless() throws InterruptedException, TimeoutException, ExecutionException {
        PredictionProvider model = TestUtils.getSumSkipModel(1);
        ShapKernelExplainer ske = new ShapKernelExplainer();

        ShapConfig skConfig1 = testConfig
                .withBackground(createPIFromMatrix(backgroundNoVariance))
                .withNSamples(100)
                .build();
        ShapConfig skConfig2 = testConfig.withBackground(createPIFromMatrix(backgroundRaw)).withNSamples(35).build();
        ShapConfig skConfig3 = ShapConfig.builder()
                .withBackground(createPIFromMatrix(backgroundLogit))
                .withLink(ShapConfig.LinkType.LOGIT)
                .withNSamples(100)
                .withRN(rn)
                .build();
        for (int i = 0; i < 10; i++) {
            shapTestCase(model, skConfig1, ske, toExplainOneVariance, oneVarianceOneOutputSHAP);
            shapTestCase(model, skConfig2, ske, toExplainRaw, multiVarianceOneOutputSHAP);
            shapTestCase(model, skConfig3, ske, toExplainLogit, logitSHAP);
        }
    }
}
