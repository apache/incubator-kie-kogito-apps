package org.kie.kogito.explainability.utils;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.utils.LinearModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LinearModelTest {

    private final static SecureRandom random = new SecureRandom();

    @Test
    public void testEmptyFitClassificationDoesNothing() {
        int size = 10;
        LinearModel linearModel = new LinearModel(size, true);
        Collection<Pair<double[], Double>> trainingSet = new LinkedList<>();
        linearModel.fit(trainingSet);
        assertEquals(Arrays.toString(new double[size]), Arrays.toString(linearModel.getWeights()));
    }

    @Test
    public void testEmptyFitRegressionDoesNothing() {
        int size = 10;
        LinearModel linearModel = new LinearModel(size, false);
        Collection<Pair<double[], Double>> trainingSet = new LinkedList<>();
        linearModel.fit(trainingSet);
        assertEquals(Arrays.toString(new double[size]), Arrays.toString(linearModel.getWeights()));
    }

    @Test
    public void testRegressionFit() {
        int size = 10;
        LinearModel linearModel = new LinearModel(size, false);
        Collection<Pair<double[], Double>> trainingSet = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            double[] x = new double[size];
            for (int j = 0; j < size; j++) {
                x[j] = random.nextDouble();
            }
            Double y = random.nextDouble();
            trainingSet.add(new ImmutablePair<>(x, y));
        }
        assertTrue(linearModel.fit(trainingSet) < 1d);
    }

    @Test
    public void testClassificationFit() {
        int size = 10;
        LinearModel linearModel = new LinearModel(size, true);
        Collection<Pair<double[], Double>> trainingSet = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            double[] x = new double[size];
            for (int j = 0; j < size; j++) {
                x[j] = random.nextDouble();
            }
            Double y = (double) (random.nextBoolean() ? 1 : 0);
            trainingSet.add(new ImmutablePair<>(x, y));
        }
        assertTrue(linearModel.fit(trainingSet) < 1d);
    }
}