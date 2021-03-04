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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class MathUtilsTest {

    // binomial coefficient testing ====================================================================================
    int[][] binomialTests = {
            { 10, 1, 10 },
            { 10, 10, 1 },
            { 10, 9, 10 },
            { 10, 3, 120 },
            { 10, 7, 120 },
            { 25, 24, 25 },
            { 25, 0, 1 },
            { 25, 20, 53130 },
            { 23, 7, 245157 },
            { 23, 18, 33649 },
    };

    @Test
    void testBinomial() {
        for (int i = 0; i < binomialTests.length; i++) {
            int[] values = binomialTests[i];
            assertEquals(values[2], MathUtils.binomial(values[0], values[1]));
        }
    }

    @Test
    void testBinomialError() {
        assertThrows(IllegalArgumentException.class, () -> MathUtils.binomial(5, 6));
        assertThrows(IllegalArgumentException.class, () -> MathUtils.binomial(5, -1));
        assertThrows(IllegalArgumentException.class, () -> MathUtils.binomial(-2, -1));
    }

    // combinations testing ============================================================================================
    List<List<Integer>> expected4Get3Combinations = List.of(
            List.of(0, 1, 2),
            List.of(0, 2, 3),
            List.of(0, 1, 3),
            List.of(1, 2, 3));

    List<List<Integer>> expected5Get2Combinations = List.of(
            List.of(0, 1),
            List.of(0, 2),
            List.of(0, 3),
            List.of(0, 4),
            List.of(1, 2),
            List.of(1, 3),
            List.of(1, 4),
            List.of(2, 3),
            List.of(2, 4),
            List.of(3, 4));

    @Test
    void testCombinations4Get3() {
        List<List<Integer>> expectedCombinationsCopy = new ArrayList<>(expected4Get3Combinations);
        List<List<Integer>> combs = MathUtils.combinations(4, 3);
        assertEquals(MathUtils.binomial(4, 3), combs.size());
        for (int i = 0; i < combs.size(); i++) {
            boolean pass = false;
            for (int j = 0; j < expectedCombinationsCopy.size(); j++) {
                if (expectedCombinationsCopy.get(j).equals(combs.get(i))) {
                    expectedCombinationsCopy.remove(j);
                    pass = true;
                    break;
                }
            }
            // if none of the combinations match:
            if (!pass) {
                fail(String.format("Comb %s not in expected", combs.get(i)));
            }
            ;
        }
    }

    @Test
    void testCombinations5Get2() {
        List<List<Integer>> expectedCombinationsCopy = new ArrayList<>(expected5Get2Combinations);
        List<List<Integer>> combs = MathUtils.combinations(5, 2);
        assertEquals(MathUtils.binomial(5, 2), combs.size());
        for (int i = 0; i < combs.size(); i++) {
            boolean pass = false;
            for (int j = 0; j < expectedCombinationsCopy.size(); j++) {
                if (expectedCombinationsCopy.get(j).equals(combs.get(i))) {
                    expectedCombinationsCopy.remove(j);
                    pass = true;
                    break;
                }
            }
            // if none of the combinations match:
            if (!pass) {
                fail(String.format("Comb %s not in expected", combs.get(i)));
            }
            ;
        }
    }

    @Test
    void testCombinationsError() {
        assertThrows(IllegalArgumentException.class, () -> MathUtils.combinations(5, 6));
        assertThrows(IllegalArgumentException.class, () -> MathUtils.combinations(-2, -1));
    }
}
