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
package org.kie.kogito.explainability.local.lime;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureImportance;
import org.kie.kogito.explainability.model.BlackBoxModel;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.Saliency;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.utils.DataUtils;
import org.kie.kogito.explainability.utils.LinearModel;
import org.kie.kogito.explainability.local.LocalExplainer;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of LIME algorithm (Ribeiro et al., 2016) optimised for tabular data and decision models.
 * Differences with respect to the original (python) implementation:
 * - the linear (interpretable) model is based on a perceptron algorithm instead of Lasso + Ridge regression
 * - perturbing numerical features is done by sampling from a normal distribution centered around the value of the feature value associated with the prediction to be explained
 * - numerical features are max-min scaled and clustered via a gaussian kernel
 */
public class LimeExplainer implements LocalExplainer<Saliency> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * no. of samples to be generated for the local linear model training
     */
    private final int noOfSamples;

    /**
     * no. of perturbations to perform on a prediction
     */
    private final int noOfPerturbations;

    public LimeExplainer(int noOfSamples, int noOfPerturbations) {
        this.noOfSamples = noOfSamples;
        this.noOfPerturbations = noOfPerturbations;
    }

    @Override
    public Saliency explain(Prediction prediction, BlackBoxModel model) {

        long start = System.currentTimeMillis();

        List<FeatureImportance> saliencies = new LinkedList<>();
        PredictionInput originalInput = prediction.getInput();
        List<Feature> inputFeatures = originalInput.getFeatures();
        PredictionInput targetInput = DataUtils.linearizeInputs(List.of(originalInput)).get(0);
        List<Feature> linearizedTargetInputFeatures = targetInput.getFeatures();
        List<Output> actualOutputs = prediction.getOutput().getOutputs();
        int noOfInputFeatures = inputFeatures.size();
        int noOfOutputFeatures = linearizedTargetInputFeatures.size();
        double[] weights = new double[noOfOutputFeatures];

        for (int o = 0; o < actualOutputs.size(); o++) {
            boolean separableDataset = false;

            List<PredictionInput> perturbedInputs = new LinkedList<>();
            List<PredictionOutput> predictionOutputs = new LinkedList<>();

            boolean classification = false;
            int tries = 3;
            Map<Double, Long> rawClassesBalance = new HashMap<>();
            Output currentOutput = actualOutputs.get(o);
            while (!separableDataset && tries > 0) {
                List<PredictionInput> perturbed = getPerturbedInputs(originalInput, noOfInputFeatures, noOfSamples);
                List<PredictionOutput> perturbedOutputs = model.predict(perturbed);

                Object fv = currentOutput != null && currentOutput.getValue() != null ? currentOutput.getValue().getUnderlyingObject() : null;

                int finalO = o;
                rawClassesBalance = perturbedOutputs.stream().map(p -> p.getOutputs().get(finalO)).map(output -> (Type.NUMBER
                        .equals(output.getType())) ? output.getValue().asNumber() : (((output.getValue().getUnderlyingObject() == null
                        && fv == null) || output.getValue().getUnderlyingObject().equals(fv)) ? 1d : 0d))
                        .collect(Collectors.groupingBy(Double::doubleValue, Collectors.counting()));
                logger.debug("raw samples per class: {}", rawClassesBalance);

                if (rawClassesBalance.size() > 1) {
                    Long max = rawClassesBalance.values().stream().max(Long::compareTo).get();
                    if ((double) max / (double) perturbed.size() < 0.9) {
                        separableDataset = true;
                        classification = rawClassesBalance.size() == 2;
                    } else {
                        tries--;
                    }
                } else {
                    tries--;
                }
                if (tries == 0 || separableDataset) {
                    perturbedInputs.addAll(perturbed);
                    predictionOutputs.addAll(perturbedOutputs);
                }
            }
            if (!separableDataset) {
                logger.warn("the perturbed inputs / outputs dataset is not (easily) separable: {}", rawClassesBalance);
            }
            List<Output> predictedOutputs = new LinkedList<>();
            for (int i = 0; i < perturbedInputs.size(); i++) {
                Output output = predictionOutputs.get(i).getOutputs().get(o);
                predictedOutputs.add(output);
            }

            Output originalOutput = prediction.getOutput().getOutputs().get(o);

            DatasetEncoder datasetEncoder = new DatasetEncoder(perturbedInputs, predictedOutputs, targetInput, originalOutput);
            Collection<Pair<double[], Double>> trainingSet = datasetEncoder.getEncodedTrainingSet();

            double[] sampleWeights = SampleWeighter.getSampleWeights(targetInput, trainingSet);

            LinearModel linearModel = new LinearModel(linearizedTargetInputFeatures.size(), classification);
            linearModel.fit(trainingSet, sampleWeights);
            for (int i = 0; i < weights.length; i++) {
                weights[i] += linearModel.getWeights()[i] / (double) actualOutputs.size();
            }
            logger.debug("weights updated for output {}", currentOutput);
        }
        for (int i = 0; i < weights.length; i++) {
            FeatureImportance featureImportance = new FeatureImportance(linearizedTargetInputFeatures.get(i), weights[i]);
            saliencies.add(featureImportance);
        }
        long end = System.currentTimeMillis();
        logger.info("explanation time: {}ms", (end - start));
        return new Saliency(saliencies);
    }



    private List<PredictionInput> getPerturbedInputs(PredictionInput predictionInput, int noOfFeatures, int noOfSamples) {
        List<PredictionInput> perturbedInputs = new LinkedList<>();
        double perturbedDataSize = Math.max(noOfSamples, Math.pow(2, noOfFeatures));
        for (int i = 0; i < perturbedDataSize; i++) {
            perturbedInputs.add(DataUtils.perturbDrop(predictionInput, noOfSamples, noOfPerturbations));
        }
        return perturbedInputs;
    }
}
