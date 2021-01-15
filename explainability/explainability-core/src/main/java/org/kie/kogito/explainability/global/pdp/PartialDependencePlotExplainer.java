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
package org.kie.kogito.explainability.global.pdp;

import org.kie.kogito.explainability.Config;
import org.kie.kogito.explainability.global.GlobalExplainer;
import org.kie.kogito.explainability.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

/**
 * Generates the partial dependence plot for the features of a {@link PredictionProvider}.
 * While a strict PD implementation would need the whole training set used to train the model, this implementation seeks
 * to reproduce an approximate version of the training data by means of data distribution information (min, max, mean,
 * stdDev).
 * <p>
 * see also https://christophm.github.io/interpretable-ml-book/pdp.html
 */
public class PartialDependencePlotExplainer implements GlobalExplainer<List<PartialDependenceGraph>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartialDependencePlotExplainer.class);
    private static final int DEFAULT_SERIES_LENGTH = 100;

    private final int seriesLength;

    /**
     * Create a PDP provider.
     *
     * @param seriesLength the no. of data points sampled for each given feature.
     */
    public PartialDependencePlotExplainer(int seriesLength) {
        this.seriesLength = seriesLength;
    }

    /**
     * Create a PDP provider.
     * <p>
     * Each feature is sampled {@code DEFAULT_SERIES_LENGTH} times.
     */
    public PartialDependencePlotExplainer() {
        this(DEFAULT_SERIES_LENGTH);
    }

    @Override
    public List<PartialDependenceGraph> explainFromMetadata(PredictionProvider model, PredictionProviderMetadata metadata)
            throws InterruptedException, ExecutionException, TimeoutException {
        return explainFromDataDistribution(model, metadata.getOutputShape().getOutputs().size(), metadata.getDataDistribution());
    }

    @Override
    public List<PartialDependenceGraph> explainFromPredictions(PredictionProvider model, Collection<Prediction> predictions)
            throws InterruptedException, ExecutionException, TimeoutException {
        int outputSize = predictions.isEmpty() ? 0 : predictions.stream().findAny().map(p -> p.getOutput().getOutputs().size()).orElse(0);
        List<PredictionInput> inputs = predictions.stream().map(Prediction::getInput).collect(Collectors.toList());
        return explainFromDataDistribution(model, outputSize, new PredictionInputsDataDistribution(inputs));
    }

    private List<PartialDependenceGraph> explainFromDataDistribution(PredictionProvider model, int outputSize,
                                                                     DataDistribution dataDistribution)
            throws InterruptedException, ExecutionException, TimeoutException {
        long start = System.currentTimeMillis();
        List<PartialDependenceGraph> pdps = new ArrayList<>();
        List<FeatureDistribution> featureDistributions = dataDistribution.asFeatureDistributions();
        int noOfFeatures = featureDistributions.size();

        // fetch entire data distributions for all features
        List<PredictionInput> trainingData = dataDistribution.sample(seriesLength);

        // create a PDP for each feature
        for (int featureIndex = 0; featureIndex < noOfFeatures; featureIndex++) {
            // generate (further) samples for the feature under analysis
            // TBD: maybe just reuse trainingData
            FeatureDistribution featureDistribution = featureDistributions.get(featureIndex);
            List<Value<?>> xsValues = featureDistribution.sample(seriesLength).stream()
                    .sorted(Comparator.comparing(Value::asString)) // sort alphanumerically (if Value#asNumber is NaN)
                    .sorted((v1, v2) -> Comparator.comparingDouble((ToDoubleFunction<Value<?>>) Value::asNumber).compare(v1, v2)) // sort by natural order
                    .distinct() // drop duplicates
                    .collect(Collectors.toList());
            List<Feature> featureXSvalues = xsValues.stream() // transform sampled Values into Features
                    .map(v -> FeatureFactory.copyOf(featureDistribution.getFeature(), v)).collect(Collectors.toList());

            // create a PDP for each feature and each output
            for (int outputIndex = 0; outputIndex < outputSize; outputIndex++) {
                PartialDependenceGraph partialDependenceGraph = getPartialDependenceGraph(model, trainingData,
                        xsValues, featureXSvalues, outputIndex);
                pdps.add(partialDependenceGraph);
            }
        }
        long end = System.currentTimeMillis();
        LOGGER.debug("explanation time: {}ms", (end - start));
        return pdps;
    }

    private PartialDependenceGraph getPartialDependenceGraph(PredictionProvider model,
                                                             List<PredictionInput> trainingData,
                                                             List<Value<?>> xsValues,
                                                             List<Feature> featureXSvalues, int outputIndex)
            throws InterruptedException, ExecutionException, TimeoutException {
        Output outputDecision = null;
        Feature feature = null;
        // each feature value of the feature under analysis should have a corresponding output value (composed by the marginal impacts of the other features)
        List<Value<?>> marginalImpacts = new ArrayList<>(featureXSvalues.size());
        for (int i = 0; i < featureXSvalues.size(); i++) {
            // initialize an empty feature to use in the generated PDP
            if (feature == null) {
                feature = FeatureFactory.copyOf(featureXSvalues.get(i), new Value<>(null));
            }
            List<PredictionInput> predictionInputs = prepareInputs(featureXSvalues.get(i), trainingData);
            List<PredictionOutput> predictionOutputs = getOutputs(model, predictionInputs);
            // prediction requests are batched per value of feature 'Xs' under analysis
            for (PredictionOutput predictionOutput : predictionOutputs) {
                Output output = predictionOutput.getOutputs().get(outputIndex);
                if (outputDecision == null) {
                    outputDecision = new Output(output.getName(), output.getType());
                }
                // update marginal impacts
                updateMarginalImpact(marginalImpacts, i, output);
            }
        }
        return new PartialDependenceGraph(feature, outputDecision, xsValues, marginalImpacts);
    }

    private void updateMarginalImpact(List<Value<?>> marginalImpacts, int i, Output output) {
        if (Type.NUMBER.equals(output.getType())) {
            double v = output.getValue().asNumber();
            if (marginalImpacts.size() > i) {
                marginalImpacts.set(i, new Value<>(marginalImpacts.get(i).asNumber() + v / (double) seriesLength));
            } else {
                marginalImpacts.add(i, new Value<>(v / (double) seriesLength));
            }
        } else {
            String categoricalOutput = output.getValue().asString();
            if (marginalImpacts.size() <= i) {
                Map<String, Long> classCount = new HashMap<>();
                classCount.put(categoricalOutput, 1L);
                marginalImpacts.add(new Value<>(classCount));
            } else {
                Map<String, Long> classCount = (Map<String, Long>) marginalImpacts.get(i).getUnderlyingObject();
                if (classCount.containsKey(categoricalOutput)) {
                    classCount.put(categoricalOutput, classCount.get(categoricalOutput) + 1);
                } else {
                    classCount.put(categoricalOutput, 1L);
                }
                marginalImpacts.set(i, new Value<>(classCount));
            }
        }
    }

    /**
     * Perform batch predictions on the model.
     *
     * @param model            the model to be queried
     * @param predictionInputs a batch of inputs
     * @return a batch of outputs
     */
    private List<PredictionOutput> getOutputs(PredictionProvider model, List<PredictionInput> predictionInputs)
            throws InterruptedException, ExecutionException, TimeoutException {
        List<PredictionOutput> predictionOutputs;
        try {
            predictionOutputs = model.predictAsync(predictionInputs).get(Config.INSTANCE.getAsyncTimeout(), Config.INSTANCE.getAsyncTimeUnit());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("Impossible to obtain prediction {}", e.getMessage());
            throw e;
        }
        return predictionOutputs;
    }

    /**
     * Generate inputs for a particular feature, using 1) a specific discrete value from the data distribution of the
     * feature under analysis for that particular feature and 2) values from a training data distribution (which we sample)
     * for all the other feature values.
     * The resulting list of prediction inputs will have the very same value for the feature under analysis, and values
     * from the training data for all other features.
     *
     * @param featureXs    specific value of the feature under analysis
     * @param trainingData training data
     * @return a list of prediction inputs
     */
    private List<PredictionInput> prepareInputs(Feature featureXs,
                                                List<PredictionInput> trainingData) {
        List<PredictionInput> predictionInputs = new ArrayList<>(seriesLength);

        for (PredictionInput trainingSample : trainingData) {
            List<Feature> features = trainingSample.getFeatures();
            List<Feature> newFeatures = replaceFeatures(featureXs, features);
            predictionInputs.add(new PredictionInput(newFeatures));
        }
        return predictionInputs;
    }

    private List<Feature> replaceFeatures(Feature featureXs, List<Feature> features) {
        List<Feature> newFeatures = new ArrayList<>();
        for (Feature f : features) {
            Feature newFeature;
            if (f.getName().equals(featureXs.getName())) {
                newFeature = FeatureFactory.copyOf(f, featureXs.getValue());
            } else {
                if (Type.COMPOSITE == f.getType()) {
                    List<Feature> elements = (List<Feature>) f.getValue().getUnderlyingObject();
                    newFeature = FeatureFactory.newCompositeFeature(f.getName(), replaceFeatures(featureXs, elements));
                } else {
                    newFeature = FeatureFactory.copyOf(f, f.getValue());
                }
            }
            newFeatures.add(newFeature);
        }
        return newFeatures;
    }

}
