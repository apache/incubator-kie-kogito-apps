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
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
     * @param samples a list of inputs to be tested for consistency
     * @param predictionProvider the model under inspection
     * @return the consistency measure
     * @throws ExecutionException if any error occurs during model prediction
     * @throws InterruptedException if timeout or other interruption issues occur during model prediction
     */
    public static double individualConsistency(BiFunction<PredictionInput, List<PredictionInput>, List<PredictionInput>> proximityFunction,
                                               List<PredictionInput> samples, PredictionProvider predictionProvider) throws ExecutionException, InterruptedException {
        double consistency = 1;
        for (PredictionInput input : samples) {
            List<PredictionOutput> predictionOutputs = predictionProvider.predictAsync(List.of(input)).get();
            PredictionOutput predictionOutput = predictionOutputs.get(0);
            List<PredictionInput> neighbors = proximityFunction.apply(input, samples);
            List<PredictionOutput> neighborsOutputs = predictionProvider.predictAsync(neighbors).get();
            for (Output output : predictionOutput.getOutputs()) {
                Value originalValue = output.getValue();
                for (PredictionOutput neighborOutput : neighborsOutputs) {
                    Output currentOutput = neighborOutput.getByName(output.getName()).orElse(null);
                    if (currentOutput != null) {
                        if (!originalValue.equals(currentOutput.getValue())) {
                            consistency -= 1f / (neighbors.size() * predictionOutput.getOutputs().size() * samples.size());
                        }
                    }
                }
            }
        }
        return consistency;
    }

    /**
     * Calculate statistical/demographic parity difference (SPD).
     *
     * @param groupSelector a predicate used to select the privileged group
     * @param samples a list of inputs to be used for testing fairness
     * @param model the model to be tested for fairness
     * @param favorableOutput an output that is considered favorable / desirable
     * @return SPD, between 0 and 1
     * @throws ExecutionException if any error occurs during model prediction
     * @throws InterruptedException if timeout or other interruption issues occur during model prediction
     */
    public double groupStatisticalParityDifference(Predicate<PredictionInput> groupSelector, List<PredictionInput> samples,
                                                   PredictionProvider model, Output favorableOutput)
            throws ExecutionException, InterruptedException {

        double probabilityUnprivileged = getFavorableLabelProbability(groupSelector.negate(), samples, model, favorableOutput);
        double probabilityPrivileged = getFavorableLabelProbability(groupSelector, samples, model, favorableOutput);

        return probabilityUnprivileged - probabilityPrivileged;
    }

    /**
     * Calculate disparate impact ratio (DIR).
     *
     * @param groupSelector a predicate used to select the privileged group
     * @param samples a list of inputs to be used for testing fairness
     * @param model the model to be tested for fairness
     * @param favorableOutput an output that is considered favorable / desirable
     * @return SPD, between 0 and 1
     * @throws ExecutionException if any error occurs during model prediction
     * @throws InterruptedException if timeout or other interruption issues occur during model prediction
     */
    public double groupDisparateImpactRatio(Predicate<PredictionInput> groupSelector, List<PredictionInput> samples,
                                                   PredictionProvider model, Output favorableOutput)
            throws ExecutionException, InterruptedException {

        double probabilityUnprivileged = getFavorableLabelProbability(groupSelector.negate(), samples, model, favorableOutput);
        double probabilityPrivileged = getFavorableLabelProbability(groupSelector, samples, model, favorableOutput);

        return probabilityUnprivileged / probabilityPrivileged;
    }

    private static double getFavorableLabelProbability(Predicate<PredictionInput> groupSelector, List<PredictionInput> samples,
                                                       PredictionProvider model, Output favorableOutput) throws ExecutionException, InterruptedException {
        String outputName = favorableOutput.getName();
        Value outputValue = favorableOutput.getValue();

        List<PredictionInput> selected = samples.stream().filter(groupSelector).collect(Collectors.toList());

        List<PredictionOutput> selectedOutputs = model.predictAsync(selected).get();

        double numSelected = selectedOutputs.size();
        long numFavorableSelected = selectedOutputs.stream().map(po -> po.getByName(outputName)).map(Optional::get)
                .filter(o -> o.getValue().equals(outputValue)).count();

        return numFavorableSelected / numSelected;
    }
}
