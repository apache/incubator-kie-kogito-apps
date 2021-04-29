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

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.kie.kogito.explainability.TestUtils;
import org.kie.kogito.explainability.model.DataDistribution;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureDistribution;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.GenericFeatureDistribution;
import org.kie.kogito.explainability.model.IndependentFeaturesDataDistribution;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.PartialDependenceGraph;
import org.kie.kogito.explainability.model.PerturbationContext;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DataUtilsTest {

    private final static Random random = new Random();

    @BeforeAll
    static void setupBefore() {
        random.setSeed(4);
    }

    @Test
    void testDataGeneration() {
        double mean = 0.5;
        double stdDeviation = 0.1;
        int size = 100;
        double[] data = DataUtils.generateData(mean, stdDeviation, size, random);

        assertEquals(mean, DataUtils.getMean(data), 1e-2);
        assertEquals(stdDeviation, DataUtils.getStdDev(data, mean), 1e-2);

        // check the sum of deviations from mean is zero
        double sum = 0;
        for (double d : data) {
            sum += d - mean;
        }
        assertEquals(0, sum, 1e-4);
    }

    @Test
    void testGetMean() {
        double[] data = new double[5];
        data[0] = 2;
        data[1] = 4;
        data[2] = 3;
        data[3] = 5;
        data[4] = 1;
        assertEquals(3, DataUtils.getMean(data), 1e-6);
    }

    @Test
    void testGetStdDev() {
        double[] data = new double[5];
        data[0] = 2;
        data[1] = 4;
        data[2] = 3;
        data[3] = 5;
        data[4] = 1;
        assertEquals(1.41, DataUtils.getStdDev(data, 3), 1e-2);
    }

    @Test
    void testGaussianKernel() {
        double x = 0;
        double k = DataUtils.gaussianKernel(x, 0, 1);
        assertEquals(0.398, k, 1e-3);
        x = 0.218;
        k = DataUtils.gaussianKernel(x, 0, 1);
        assertEquals(0.389, k, 1e-3);
    }

    @Test
    void testEuclideanDistance() {
        double[] x = new double[] { 1, 1 };
        double[] y = new double[] { 2, 3 };
        double distance = DataUtils.euclideanDistance(x, y);
        assertEquals(2.236, distance, 1e-3);

        assertTrue(Double.isNaN(DataUtils.euclideanDistance(x, new double[0])));
    }

    @Test
    void testHammingDistanceDouble() {
        double[] x = new double[] { 2, 1 };
        double[] y = new double[] { 2, 3 };
        double distance = DataUtils.hammingDistance(x, y);
        assertEquals(1, distance, 1e-1);

        assertTrue(Double.isNaN(DataUtils.hammingDistance(x, new double[0])));
    }

    @Test
    void testHammingDistanceString() {
        String x = "test1";
        String y = "test2";
        double distance = DataUtils.hammingDistance(x, y);
        assertEquals(1, distance, 1e-1);

        assertTrue(Double.isNaN(DataUtils.hammingDistance(x, "testTooLong")));
    }

    @Test
    void testExponentialSmoothingKernel() {
        double x = 0.218;
        double k = DataUtils.exponentialSmoothingKernel(x, 2);
        assertEquals(0.994, k, 1e-3);
    }

    @Test
    void testPerturbFeaturesEmpty() {
        List<Feature> features = new LinkedList<>();
        PerturbationContext perturbationContext = new PerturbationContext(random, 0);
        List<Feature> newFeatures = DataUtils.perturbFeatures(features, perturbationContext);
        assertNotNull(newFeatures);
        assertEquals(features.size(), newFeatures.size());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    void testPerturbDropNumeric(int param) {
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newNumericalFeature("f0", 1));
        features.add(FeatureFactory.newNumericalFeature("f1", 3.14));
        features.add(FeatureFactory.newNumericalFeature("f2", 0.55));
        PredictionInput input = new PredictionInput(features);
        assertPerturbDropNumeric(input, param);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    void testPerturbDropString(int param) {
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newTextFeature("f0", "foo"));
        features.add(FeatureFactory.newTextFeature("f1", "foo bar"));
        features.add(FeatureFactory.newTextFeature("f2", " "));
        features.add(FeatureFactory.newTextFeature("f3", "foo bar "));
        PredictionInput input = new PredictionInput(features);
        assertPerturbDropString(input, param);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    void testPerturbDropCompositeString(int param) {
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newTextFeature("f0", "foo"));
        features.add(FeatureFactory.newTextFeature("f1", "foo bar"));
        features.add(FeatureFactory.newTextFeature("f2", " "));
        features.add(FeatureFactory.newTextFeature("f3", "foo bar "));
        PredictionInput input = new PredictionInput(List.of(FeatureFactory.newCompositeFeature("composite", features)));
        assertPerturbDropString(input, param);
    }

    private void assertPerturbDropNumeric(PredictionInput input, int noOfPerturbations) {
        List<Feature> newFeatures = DataUtils.perturbFeatures(input.getFeatures(), new PerturbationContext(random, noOfPerturbations));
        int changedFeatures = 0;
        for (int i = 0; i < input.getFeatures().size(); i++) {
            double v = input.getFeatures().get(i).getValue().asNumber();
            double pv = newFeatures.get(i).getValue().asNumber();
            if (v != pv) {
                changedFeatures++;
            }
        }
        assertThat(changedFeatures).isBetween((int) Math.min(noOfPerturbations, input.getFeatures().size() * 0.5),
                (int) Math.max(noOfPerturbations, input.getFeatures().size() * 0.5));
    }

    private void assertPerturbDropString(PredictionInput input, int noOfPerturbations) {
        List<Feature> newFeatures = DataUtils.perturbFeatures(input.getFeatures(), new PerturbationContext(random, noOfPerturbations));
        int changedFeatures = 0;
        for (int i = 0; i < input.getFeatures().size(); i++) {
            String v = input.getFeatures().get(i).getValue().asString();
            String pv = newFeatures.get(i).getValue().asString();
            if (!v.equals(pv)) {
                changedFeatures++;
            }
        }
        assertThat(changedFeatures).isBetween((int) Math.min(noOfPerturbations, input.getFeatures().size() * 0.5),
                (int) Math.max(noOfPerturbations, input.getFeatures().size() * 0.5));
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
        DataDistribution dataDistribution = DataUtils.generateRandomDataDistribution(10, 10, random);
        assertNotNull(dataDistribution);
        assertNotNull(dataDistribution.asFeatureDistributions());
        for (FeatureDistribution featureDistribution : dataDistribution.asFeatureDistributions()) {
            assertNotNull(featureDistribution);
        }
    }

    @Test
    void testLinearizedNumericFeatures() {
        List<Feature> features = new LinkedList<>();
        Feature f = TestUtils.getMockedNumericFeature();
        features.add(f);
        List<Feature> linearizedFeatures = DataUtils.getLinearizedFeatures(features);
        assertEquals(features.size(), linearizedFeatures.size());
    }

    @Test
    void testLinearizedTextFeatures() {
        List<Feature> features = new LinkedList<>();
        Feature f = TestUtils.getMockedTextFeature("foo bar ");
        features.add(f);
        List<Feature> linearizedFeatures = DataUtils.getLinearizedFeatures(features);
        assertEquals(1, linearizedFeatures.size());
    }

    @Test
    void testCompositeLinearizedFeatures() {
        List<Feature> features = new LinkedList<>();
        List<Feature> list = new LinkedList<>();
        list.add(FeatureFactory.newTextFeature("f0", "foo bar"));
        list.add(FeatureFactory.newFulltextFeature("f0", "foo bar", s -> Arrays.asList(s.split(" "))));
        list.add(FeatureFactory.newCategoricalFeature("f0", "1"));
        list.add(FeatureFactory.newBooleanFeature("f1", true));
        list.add(FeatureFactory.newNumericalFeature("f2", 13));
        list.add(FeatureFactory.newDurationFeature("f3", Duration.ofDays(13)));
        list.add(FeatureFactory.newTimeFeature("f4", LocalTime.now()));
        list.add(FeatureFactory.newObjectFeature("f5", new float[] { 0.4f, 0.4f }));
        list.add(FeatureFactory.newObjectFeature("f6", FeatureFactory.newObjectFeature("nf-0", new Object())));
        Feature f = FeatureFactory.newCompositeFeature("name", list);
        features.add(f);
        List<Feature> linearizedFeatures = DataUtils.getLinearizedFeatures(features);
        assertEquals(10, linearizedFeatures.size());
    }

    @Test
    void testDropFeature() {
        for (Type t : Type.values()) {
            Feature target = TestUtils.getMockedFeature(t, new Value(1d));
            List<Feature> features = new LinkedList<>();
            features.add(TestUtils.getMockedNumericFeature());
            features.add(target);
            features.add(TestUtils.getMockedTextFeature("foo bar"));
            features.add(TestUtils.getMockedNumericFeature());
            List<Feature> newFeatures = DataUtils.dropFeature(features, target);
            assertNotEquals(features, newFeatures);
        }
    }

    @Test
    void testDropLinearizedFeature() {
        for (Type t : Type.values()) {
            Feature target = TestUtils.getMockedFeature(t, new Value(1d));
            List<Feature> features = new LinkedList<>();
            features.add(TestUtils.getMockedNumericFeature());
            features.add(target);
            features.add(TestUtils.getMockedTextFeature("foo bar"));
            features.add(TestUtils.getMockedNumericFeature());
            Feature source = FeatureFactory.newCompositeFeature("composite", features);
            Feature newFeature = DataUtils.dropOnLinearizedFeatures(target, source);
            assertNotEquals(source, newFeature);
        }
    }

    @Test
    void testSampleWithReplacement() {
        List<Double> emptyValues = new ArrayList<>();
        List<Double> emptySamples = DataUtils.sampleWithReplacement(emptyValues, 1, random);
        assertNotNull(emptySamples);
        assertEquals(0, emptySamples.size());

        List<Double> values = Arrays.stream(DataUtils.generateData(0, 1, 100, random)).boxed().collect(Collectors.toList());
        int sampleSize = 10;
        List<Double> samples = DataUtils.sampleWithReplacement(values, sampleSize, random);
        assertNotNull(samples);
        assertEquals(sampleSize, samples.size());
        assertThat(values).contains(samples.get(random.nextInt(sampleSize - 1)));

        int largerSampleSize = 300;
        List<Double> largerSamples = DataUtils.sampleWithReplacement(values, largerSampleSize, random);
        assertThat(largerSamples).isNotNull();
        assertThat(largerSampleSize).isEqualTo(largerSamples.size());
        assertThat(values).contains(largerSamples.get(random.nextInt(largerSampleSize - 1)));
    }

    @Test
    void testBootstrap() {
        List<Value> values = new ArrayList<>();
        PerturbationContext perturbationContext = new PerturbationContext(random, 1);
        for (int i = 0; i < 4; i++) {
            values.add(Type.NUMBER.randomValue(perturbationContext));
        }
        Feature mockedNumericFeature = TestUtils.getMockedNumericFeature();
        DataDistribution dataDistribution = new IndependentFeaturesDataDistribution(List.of(
                new GenericFeatureDistribution(mockedNumericFeature, values)));
        Map<String, FeatureDistribution> featureDistributionMap = DataUtils.boostrapFeatureDistributions(dataDistribution,
                perturbationContext, 10, 1, 500);
        assertThat(featureDistributionMap).isNotNull();
        assertThat(featureDistributionMap).isNotEmpty();
        FeatureDistribution actual = featureDistributionMap.get(mockedNumericFeature.getName());
        assertThat(actual).isNotNull();
        List<Value> allSamples = actual.getAllSamples();
        assertThat(allSamples).isNotNull();
        assertThat(allSamples).hasSize(10);
    }

    @Test
    void toCSV() {
        Feature feature = mock(Feature.class);
        when(feature.getName()).thenReturn("feature-1");
        Output output = mock(Output.class);
        when(output.getName()).thenReturn("decision-1");
        List<Value> x = new ArrayList<>();
        x.add(new Value(1));
        x.add(new Value(2));
        x.add(new Value(3));
        List<Value> y = new ArrayList<>();
        y.add(new Value(4));
        y.add(new Value(5));
        y.add(new Value(4));
        PartialDependenceGraph partialDependenceGraph = new PartialDependenceGraph(feature, output, x, y);
        assertDoesNotThrow(() -> DataUtils.toCSV(partialDependenceGraph, Paths.get("target/test-pdp.csv")));
    }

    @Test
    void testReadCsv() throws IOException {
        List<Type> schema = new ArrayList<>();
        schema.add(Type.CATEGORICAL);
        schema.add(Type.BOOLEAN);
        schema.add(Type.BOOLEAN);
        schema.add(Type.BOOLEAN);
        schema.add(Type.BOOLEAN);
        schema.add(Type.BOOLEAN);
        schema.add(Type.BOOLEAN);
        schema.add(Type.BOOLEAN);
        schema.add(Type.BOOLEAN);
        schema.add(Type.BOOLEAN);
        schema.add(Type.BOOLEAN);
        schema.add(Type.NUMBER);
        schema.add(Type.NUMBER);
        DataDistribution dataDistribution = DataUtils.readCSV(
                Paths.get(getClass().getResource("/mini-train.csv").getFile()), schema);
        assertThat(dataDistribution).isNotNull();
        assertThat(dataDistribution.getAllSamples()).hasSize(10);
    }

    @Test
    void testReplaceFeature() {
        List<Feature> features = new ArrayList<>();
        Feature replacingFeature = FeatureFactory.newTextFeature("f1", "replacement");
        features.add(FeatureFactory.newTextFeature("f0", "one two three"));
        features.add(FeatureFactory.newTextFeature("f1", "to be replaced"));
        features.add(FeatureFactory.newTextFeature("f2", "four five six"));
        List<Feature> updatedFeatures = DataUtils.replaceFeatures(replacingFeature, features);
        assertThat(updatedFeatures.get(0)).isEqualTo(features.get(0));
        assertThat(updatedFeatures.get(2)).isEqualTo(features.get(2));
        assertThat(updatedFeatures.get(1)).isNotEqualTo(features.get(2));
        assertThat(updatedFeatures.get(1).getValue().asString()).isEqualTo("replacement");
    }
}
