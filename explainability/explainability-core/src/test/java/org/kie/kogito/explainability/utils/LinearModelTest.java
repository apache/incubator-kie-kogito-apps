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
    void testEmptyFitClassificationDoesNothing() {
        int size = 10;
        LinearModel linearModel = new LinearModel(size, true);
        Collection<Pair<double[], Double>> trainingSet = new LinkedList<>();
        linearModel.fit(trainingSet);
        assertEquals(Arrays.toString(new double[size]), Arrays.toString(linearModel.getWeights()));
    }

    @Test
    void testEmptyFitRegressionDoesNothing() {
        int size = 10;
        LinearModel linearModel = new LinearModel(size, false);
        Collection<Pair<double[], Double>> trainingSet = new LinkedList<>();
        linearModel.fit(trainingSet);
        assertEquals(Arrays.toString(new double[size]), Arrays.toString(linearModel.getWeights()));
    }

    @Test
    void testRegressionFit() {
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
    void testClassificationFit() {
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