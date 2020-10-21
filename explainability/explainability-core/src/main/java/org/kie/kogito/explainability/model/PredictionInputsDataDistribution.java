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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data distribution based on list of {@code PredictionInputs}.
 */
public class PredictionInputsDataDistribution implements DataDistribution {

    private final Logger LOGGER = LoggerFactory.getLogger(PredictionInputsDataDistribution.class);

    private final List<PredictionInput> inputs;

    public PredictionInputsDataDistribution(List<PredictionInput> inputs) {
        this.inputs = Collections.unmodifiableList(inputs);
    }

    @Override
    public PredictionInput sample() {
        List<PredictionInput> copy = new java.util.ArrayList<>(inputs);
        Collections.shuffle(copy);
        return copy.get(0);
    }

    @Override
    public List<PredictionInput> sample(int sampleSize) {
        if (sampleSize >= inputs.size()) {
            LOGGER.warn("required {} samples, but only {} are available", sampleSize, inputs.size());
            return getAllSamples();
        } else {
            List<PredictionInput> copy = new java.util.ArrayList<>(inputs);
            Collections.shuffle(copy);
            List<PredictionInput> samples = new ArrayList<>(sampleSize);
            for (int i = 0; i < sampleSize; i++) {
                samples.add(copy.get(i));
            }
            return samples;
        }
    }

    @Override
    public List<PredictionInput> getAllSamples() {
        return inputs;
    }

    @Override
    public List<FeatureDistribution> asFeatureDistributions() {
        if (inputs.isEmpty()) {
            return Collections.emptyList();
        } else {
            PredictionInput firstInput = inputs.get(0);
            int shape = firstInput.getFeatures().size();
            List<FeatureDistribution> featureDistributions = new ArrayList<>(shape);
            for (int i = 0; i < shape; i++) {
                Feature firstInputIthfeature = firstInput.getFeatures().get(i);
                List<Value<?>> values = new ArrayList<>(inputs.size());
                for (PredictionInput input : inputs) {
                    values.add(input.getFeatures().get(i).getValue());
                }
                Feature feature = FeatureFactory.copyOf(firstInputIthfeature, new Value<>(null));
                featureDistributions.add(new GenericFeatureDistribution(feature, values));
            }
            return featureDistributions;
        }
    }
}
