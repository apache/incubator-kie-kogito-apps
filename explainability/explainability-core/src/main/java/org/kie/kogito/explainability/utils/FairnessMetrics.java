/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

import org.kie.kogito.explainability.model.DataDistribution;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.model.Value;

public class FairnessMetrics {

    /**
     * Calculate individual fairness in terms of consistency of predictions across similar inputs.
     *
     * @param proximityFunction a function that finds the top k similar inputs, given a reference input and a list of inputs
     * @param dataDistribution a data distribution holding the set of inputs to be tested for consistency
     * @param predictionProvider the model under inspection
     * @return the consistency measure
     * @throws ExecutionException if any error occurs during model prediction
     * @throws InterruptedException if timeout or other interruption issues occur during model prediction
     */
    public static double individualConsistency(BiFunction<PredictionInput, List<PredictionInput>, List<PredictionInput>> proximityFunction,
                                     DataDistribution dataDistribution, PredictionProvider predictionProvider) throws ExecutionException, InterruptedException {
        double consistency = 1;
        List<PredictionInput> allSamples = dataDistribution.getAllSamples();
        for (PredictionInput input : allSamples) {
            List<PredictionOutput> predictionOutputs = predictionProvider.predictAsync(List.of(input)).get();
            PredictionOutput predictionOutput = predictionOutputs.get(0);
            List<PredictionInput> neighbors = proximityFunction.apply(input, allSamples);
            List<PredictionOutput> neighborsOutputs = predictionProvider.predictAsync(neighbors).get();
            for (Output output : predictionOutput.getOutputs()) {
                Value originalValue = output.getValue();
                for (PredictionOutput neighborOutput : neighborsOutputs) {
                    Output currentOutput = neighborOutput.getByName(output.getName()).orElse(null);
                    if (currentOutput != null) {
                        if (!originalValue.equals(currentOutput.getValue())) {
                            consistency -= 1f / (neighbors.size() * predictionOutput.getOutputs().size() * allSamples.size());
                        }
                    }
                }
            }
        }
        return consistency;
    }
}
