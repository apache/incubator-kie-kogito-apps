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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Local {@code Saliency} stability evaluation result.
 */
public class LocalSaliencyStability {

    private final Map<String, Map<Integer, SaliencyFrequencyMetadata>> map;

    public LocalSaliencyStability(Set<String> decisions) {
        map = new HashMap<>();
        for (String k : decisions) {
            map.put(k, new HashMap<>());
        }
    }

    public Collection<String> getDecisions() {
        return map.keySet();
    }

    public List<String> getMostFrequentPositive(String decision, int k) {
        return map.get(decision).get(k).getPositiveFeatureNames();
    }

    public List<String> getMostFrequentNegative(String decision, int k) {
        return map.get(decision).get(k).getNegativeFeatureNames();
    }

    public double getPositiveStabilityScore(String decision, int k) {
        return map.get(decision).get(k).getPositiveFrequencyRate();
    }

    public double getNegativeStabilityScore(String decision, int k) {
        return map.get(decision).get(k).getNegativeFrequencyRate();
    }

    /**
     * Record stability data about a given decision, on top k features.
     * @param decision the decision
     * @param k the no. of top features considered
     * @param positiveFeatureNames the names of top positive features
     * @param positiveFrequencyRate the frequency rate of the top positive features
     * @param negativeFeatureNames the names of top negative features
     * @param negativeFrequencyRate the frequency rate of the top negative features
     */
    public void add(String decision, int k, List<String> positiveFeatureNames, double positiveFrequencyRate,
                    List<String> negativeFeatureNames, double negativeFrequencyRate) {
        if (map.containsKey(decision)) {
            Map<Integer, SaliencyFrequencyMetadata> integerMap = map.get(decision);
            integerMap.put(k, new SaliencyFrequencyMetadata(positiveFeatureNames, positiveFrequencyRate,
                                                            negativeFeatureNames, negativeFrequencyRate));
        }
    }

    /**
     * Internal utility class to record stability evaluations for a single decision on top k features.
     */
    private static class SaliencyFrequencyMetadata {

        private final List<String> positiveFeatureNames;
        private final double positiveFrequencyRate;
        private final List<String> negativeFeatureNames;
        private final double negativeFrequencyRate;

        private SaliencyFrequencyMetadata(List<String> positiveFeatureNames, double positiveFrequencyRate,
                                          List<String> negativeFeatureNames, double negativeFrequencyRate) {
            this.positiveFeatureNames = positiveFeatureNames;
            this.positiveFrequencyRate = positiveFrequencyRate;
            this.negativeFeatureNames = negativeFeatureNames;
            this.negativeFrequencyRate = negativeFrequencyRate;
        }

        double getNegativeFrequencyRate() {
            return negativeFrequencyRate;
        }

        double getPositiveFrequencyRate() {
            return positiveFrequencyRate;
        }

        List<String> getNegativeFeatureNames() {
            return negativeFeatureNames;
        }

        List<String> getPositiveFeatureNames() {
            return positiveFeatureNames;
        }
    }
}
