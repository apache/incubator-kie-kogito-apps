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
package org.kie.kogito.explainability.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Data distribution based on list of {@code FeatureDistributions}.
 */
public class IndependentFeaturesDatatDistribution implements DataDistribution {

    private final List<FeatureDistribution> featureDistributions;

    public IndependentFeaturesDatatDistribution(List<FeatureDistribution> featureDistributions) {
        this.featureDistributions = Collections.unmodifiableList(featureDistributions);
    }

    @Override
    public PredictionInput sample() {
        List<Feature> features = new ArrayList<>(featureDistributions.size());
        for (FeatureDistribution featureDistribution : featureDistributions) {
            Feature feature = featureDistribution.getFeature();
            features.add(FeatureFactory.copyOf(feature, featureDistribution.sample()));
        }
        return new PredictionInput(features);
    }

    @Override
    public List<PredictionInput> sample(int sampleSize) {
        List<PredictionInput> inputs = new ArrayList<>(sampleSize);
        for (int i = 0; i < sampleSize; i++) {
            inputs.add(sample());
        }
        return inputs;
    }

    @Override
    public List<PredictionInput> getAllSamples() {
        List<Set<Feature>> featureEnumerations = new ArrayList<>(featureDistributions.size());
        for (FeatureDistribution featureDistribution : featureDistributions) {
            List<Value<?>> allValues = featureDistribution.getAllSamples();
            Set<Feature> currentFeatures = new HashSet<>(allValues.size());
            Feature feature = featureDistribution.getFeature();
            for (Value<?> v : allValues) {
                Feature f = FeatureFactory.copyOf(feature, v);
                currentFeatures.add(f);
            }
            featureEnumerations.add(currentFeatures);
        }
        Set<List<Feature>> cartesianProduct = Sets.cartesianProduct(featureEnumerations);
        List<PredictionInput> inputs = new ArrayList<>(cartesianProduct.size());
        for (List<Feature> features : cartesianProduct) {
            inputs.add(new PredictionInput(features));
        }
        return inputs;
    }

    @Override
    public List<FeatureDistribution> asFeatureDistributions() {
        return featureDistributions;
    }
}
