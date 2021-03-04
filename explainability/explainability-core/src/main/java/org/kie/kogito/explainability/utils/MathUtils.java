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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MathUtils {
    private static double binomialRaw(int n, int k) {
        if (n < k || n < 0 || k < 0) {
            throw new IllegalArgumentException(String.format(
                    "n >= k >= 0 for binomial coefficients: n: %d, k:%d", n, k));
        }

        if (k == 0 || k == n) {
            return 1.0;
        } else if (k == 1 || k == n - 1) {
            return n;
        } else {
            return ((n + 1. - k) / k) * binomialRaw(n, k - 1);
        }
    }

    public static int binomial(int n, int k) {
        return (int) Math.round(binomialRaw(n, k));
    }

    private static List<List<Integer>> combinations(List<Integer> set, int size, List<Integer> filled) {
        List<List<Integer>> newCombinations = new ArrayList<>();
        if (set.size() < size || size < 1 || set.size() < 1) {
            throw new IllegalArgumentException(String.format(
                    "Set size >= combination size > 0: %d, size:%d", set.size(), size));
        } else if (set.size() == size) {
            List<Integer> newCombination = new ArrayList<>(filled);
            newCombination.addAll(set);
            newCombinations.add(newCombination);
        } else if (size == 1) {
            for (int i = 0; i < set.size(); i++) {
                List<Integer> newCombination = new ArrayList<>(filled);
                newCombination.add(set.get(i));
                newCombinations.add(newCombination);
            }
        } else {
            List<Integer> allButFirst = new ArrayList<>(set);
            allButFirst.remove(0);
            List<Integer> newFilled = new ArrayList<>(filled);
            newFilled.add(set.get(0));
            newCombinations.addAll(combinations(allButFirst, size - 1, newFilled));
            newCombinations.addAll(combinations(allButFirst, size, filled));
        }
        return newCombinations;
    }

    public static List<List<Integer>> combinations(int n, int size) {
        List<Integer> set = IntStream.range(0, n).boxed().collect(Collectors.toList());
        return combinations(set, size, new ArrayList<>());
    }
}
