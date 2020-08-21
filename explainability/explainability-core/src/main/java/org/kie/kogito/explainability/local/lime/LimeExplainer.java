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

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.kie.kogito.explainability.local.LocalExplainer;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureImportance;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.PerturbationContext;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.model.Saliency;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;
import org.kie.kogito.explainability.utils.DataUtils;
import org.kie.kogito.explainability.utils.LinearModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of LIME algorithm (Ribeiro et al., 2016) that handles tabular data, text data, complex hierarchically
 * organized data, etc. seamlessly.
 * <p>
 * Differences with respect to the original (python) implementation:
 * - the linear (interpretable) model is based on a perceptron algorithm instead of Lasso + Ridge regression
 * - perturbing numerical features is done by sampling from a standard normal distribution centered around the value of the feature value associated with the prediction to be explained
 * - numerical features are max-min scaled and clustered via a gaussian kernel
 */
public class LimeExplainer implements LocalExplainer<CompletableFuture<Map<String, Saliency>>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LimeExplainer.class);
    private static final double SEPARABLE_DATASET_RATIO = 0.99;
    private static final int DEFAULT_NO_OF_RETRIES = 3;

    /**
     * No. of samples to be generated for the local linear model training
     */
    private final int noOfSamples;

    /**
     * No. of retries while trying to find a (linearly) separable dataset
     */
    private final int noOfRetries;

    /**
     * Context object for perturbing features
     */
    private final PerturbationContext perturbationContext;

    public LimeExplainer(int noOfSamples, int noOfPerturbations) {
        this(noOfSamples, new PerturbationContext(new SecureRandom(), noOfPerturbations));
    }

    public LimeExplainer(int noOfSamples, int noOfPerturbations, Random random) {
        this(noOfSamples, new PerturbationContext(random, noOfPerturbations));
    }

    public LimeExplainer(int noOfSamples, PerturbationContext perturbationContext) {
        this(noOfSamples, perturbationContext, DEFAULT_NO_OF_RETRIES);
    }

    public LimeExplainer(int noOfSamples, PerturbationContext perturbationContext, int noOfRetries) {
        this.noOfSamples = noOfSamples;
        this.perturbationContext = perturbationContext;
        this.noOfRetries = noOfRetries;
    }

    public LimeExplainer(int noOfSamples, int noOfPerturbations, int noOfRetries, Random random) {
        this.noOfSamples = noOfSamples;
        this.perturbationContext = new PerturbationContext(random, noOfPerturbations);
        this.noOfRetries = noOfRetries;
    }

    @Override
    public CompletableFuture<Map<String, Saliency>> explain(Prediction prediction, PredictionProvider model) {
        PredictionInput originalInput = prediction.getInput();
        List<PredictionInput> linearizedInputs = DataUtils.linearizeInputs(List.of(originalInput));
        PredictionInput targetInput = linearizedInputs.get(0);
        List<Feature> linearizedTargetInputFeatures = targetInput.getFeatures();
        List<Output> actualOutputs = prediction.getOutput().getOutputs();
        List<PredictionInput> perturbedInputs = getPerturbedInputs(originalInput.getFeatures());

        return model.predict(perturbedInputs)
                .thenApply(getLimeInputs(linearizedTargetInputFeatures, actualOutputs, perturbedInputs))
                .thenApply(limeInputsList -> getSaliencies(targetInput, linearizedTargetInputFeatures, actualOutputs, limeInputsList));
    }

    private Map<String, Saliency> getSaliencies(PredictionInput targetInput, List<Feature> linearizedTargetInputFeatures, List<Output> actualOutputs, List<LimeInputs> limeInputsList) {
        Map<String, Saliency> result = new HashMap<>();
        for (int o = 0; o < actualOutputs.size(); o++) {
            LimeInputs limeInputs = limeInputsList.get(o);
            Output originalOutput = actualOutputs.get(o);

            getSaliency(targetInput, linearizedTargetInputFeatures, result, limeInputs, originalOutput);
            LOGGER.debug("weights set for output {}", originalOutput);
        }
        return result;
    }

    private Function<List<PredictionOutput>, List<LimeInputs>> getLimeInputs(List<Feature> linearizedTargetInputFeatures,
                                                                             List<Output> actualOutputs,
                                                                             List<PredictionInput> perturbedInputs) {
        return predictionOutputs -> {
            List<LimeInputs> limeInputsList = new LinkedList<>();
            for (int o = 0; o < actualOutputs.size(); o++) {
                Output currentOutput = actualOutputs.get(o);
                LimeInputs limeInputs = prepareInputs(perturbedInputs, predictionOutputs, linearizedTargetInputFeatures,
                                                      o, currentOutput);
                limeInputsList.add(limeInputs);
            }
            return limeInputsList;
        };
    }

    private void getSaliency(PredictionInput targetInput, List<Feature> linearizedTargetInputFeatures, Map<String, Saliency> result, LimeInputs limeInputs, Output originalOutput) {
        List<FeatureImportance> featureImportanceList = new LinkedList<>();

        // encode the training data so that it can be fed into the linear model
        DatasetEncoder datasetEncoder = new DatasetEncoder(limeInputs.getPerturbedInputs(),
                                                           limeInputs.getPerturbedOutputs(),
                                                           targetInput, originalOutput);
        Collection<Pair<double[], Double>> trainingSet = datasetEncoder.getEncodedTrainingSet();

        // weight the training samples based on the proximity to the target input to explain
        double[] sampleWeights = SampleWeighter.getSampleWeights(targetInput, trainingSet);
        LinearModel linearModel = new LinearModel(linearizedTargetInputFeatures.size(), limeInputs.isClassification());
        double loss = linearModel.fit(trainingSet, sampleWeights);
        if (!Double.isNaN(loss)) {
            // create the output saliency
            int i = 0;
            for (Feature linearizedFeature : linearizedTargetInputFeatures) {
                FeatureImportance featureImportance = new FeatureImportance(linearizedFeature, linearModel.getWeights()[i]);
                featureImportanceList.add(featureImportance);
                i++;
            }
        }
        Saliency saliency = new Saliency(originalOutput, featureImportanceList);
        result.put(originalOutput.getName(), saliency);
    }

    /**
     * Perturb the inputs so that the perturbed dataset contains more than just one output class, otherwise
     * it would be impossible to linearly separate it, and hence learn meaningful weights to be used as
     * feature importance scores.
     */
    private LimeInputs prepareInputs(List<PredictionInput> perturbedInputs, List<PredictionOutput> perturbedOutputs,
                                     List<Feature> linearizedTargetInputFeatures, int o,
                                     Output currentOutput) {
        LimeInputs limeInputs = null;
        if (currentOutput.getValue() != null && currentOutput.getValue().getUnderlyingObject() != null) {
            boolean classification;
            boolean separableDataset = false;

            Map<Double, Long> rawClassesBalance;

            // calculate the no. of samples belonging to each output class
            Value<?> fv = currentOutput.getValue();
            rawClassesBalance = getClassBalance(perturbedOutputs, fv, o);

            // check if the dataset is separable and also if the linear model should fit a regressor or a classifier
            if (rawClassesBalance.size() > 1) {
                Long max = rawClassesBalance.values().stream().max(Long::compareTo).orElse(1L);
                if ((double) max / (double) perturbedInputs.size() < SEPARABLE_DATASET_RATIO) {
                    separableDataset = true;
                    classification = rawClassesBalance.size() == 2;

                    List<Output> outputs = perturbedOutputs.stream().map(po -> po.getOutputs().get(o)).collect(Collectors.toList());

                    // if dataset creation process succeeds use it to train the linear model
                    limeInputs = new LimeInputs(classification, linearizedTargetInputFeatures, currentOutput, perturbedInputs, outputs);
                }
            }
            if (!separableDataset) { // fail the explanation if the dataset is not separable
                throw new DatasetNotSeparableException(currentOutput, rawClassesBalance);
            }
        } else {
            limeInputs = new LimeInputs(false, linearizedTargetInputFeatures, currentOutput, Collections.emptyList(), Collections.emptyList());
        }
        return limeInputs;
    }

    private Map<Double, Long> getClassBalance(List<PredictionOutput> perturbedOutputs, Value<?> fv, int finalO) {
        Map<Double, Long> rawClassesBalance;
        rawClassesBalance = perturbedOutputs.stream()
                .map(p -> p.getOutputs().get(finalO)) // get the (perturbed) output value corresponding to the one to be explained
                .map(output -> (Type.NUMBER.equals(output.getType())) ?
                        output.getValue().asNumber() : // if numeric use it as it is
                        (((output.getValue().getUnderlyingObject() == null // otherwise check if target and perturbed outputs are both null
                                && fv.getUnderlyingObject() == null)
                                || (output.getValue().getUnderlyingObject() != null  // if not null, check for underlying value equality
                                && output.getValue().asString().equals(fv.asString()))) ? 1d : 0d))
                .collect(Collectors.groupingBy(Double::doubleValue, Collectors.counting())); // then group-count distinct output values
        LOGGER.debug("raw samples per class: {}", rawClassesBalance);
        return rawClassesBalance;
    }

    private List<PredictionInput> getPerturbedInputs(List<Feature> features) {
        List<PredictionInput> perturbedInputs = new LinkedList<>();
        // as per LIME paper, the dataset size should be at least |features|^2
        double perturbedDataSize = Math.max(noOfSamples, Math.pow(2, features.size()));
        for (int i = 0; i < perturbedDataSize; i++) {
            perturbedInputs.add(DataUtils.perturbFeatures(features, perturbationContext));
        }
        return perturbedInputs;
    }
}