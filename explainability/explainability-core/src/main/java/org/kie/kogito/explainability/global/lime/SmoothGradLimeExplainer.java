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
package org.kie.kogito.explainability.global.lime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import org.kie.kogito.explainability.Config;
import org.kie.kogito.explainability.global.GlobalExplainer;
import org.kie.kogito.explainability.local.LocalExplainer;
import org.kie.kogito.explainability.local.lime.LimeConfig;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.model.PredictionProviderMetadata;
import org.kie.kogito.explainability.model.Saliency;
import org.kie.kogito.explainability.model.SimplePrediction;
import org.kie.kogito.explainability.utils.DataUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adaptation of the SmoothGrad technique to the LIME setting.
 * {@link SmoothGradLimeExplainer} generates noisy input out of the original input and gets several different
 * explanations for the noisy inputs, the final saliency is the mean of all the saliencies on the noisy predictions.
 */
public class SmoothGradLimeExplainer implements LocalExplainer<Map<String, Saliency>>,
        GlobalExplainer<Map<String, Saliency>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmoothGradLimeExplainer.class);

    private final LimeConfig config;

    public SmoothGradLimeExplainer() {
        this(new LimeConfig());
    }

    public SmoothGradLimeExplainer(LimeConfig config) {
        this.config = config;
    }

    @Override
    public CompletableFuture<Map<String, Saliency>> explainAsync(Prediction prediction, PredictionProvider model,
            Consumer<Map<String, Saliency>> intermediateResultsConsumer) {

        AggregatedLimeExplainer aggregatedLimeExplainer = new AggregatedLimeExplainer(new LimeExplainer(config));
        PredictionInput input = prediction.getInput();

        List<Prediction> predictions = new ArrayList<>();
        predictions.add(prediction);
        for (int i = 0; i < config.getNoOfSamples(); i++) {
            PredictionInput noisyInput = new PredictionInput(DataUtils.perturbFeatures(input.getFeatures(), config.getPerturbationContext()));
            List<PredictionOutput> predictionOutputs;
            try {
                predictionOutputs = model.predictAsync(List.of(noisyInput)).get(Config.DEFAULT_ASYNC_TIMEOUT, Config.DEFAULT_ASYNC_TIMEUNIT);
                if (!predictionOutputs.isEmpty()) {
                    predictions.add(new SimplePrediction(noisyInput, predictionOutputs.get(0)));
                }
            } catch (Exception e) {
                LOGGER.error("could not perform prediction", e);
            }
        }
        return aggregatedLimeExplainer.explainFromPredictions(model, predictions);
    }

    @Override
    public Map<String, Saliency> explainFromMetadata(PredictionProvider model, PredictionProviderMetadata metadata) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    @Override
    public Map<String, Saliency> explainFromPredictions(PredictionProvider model, Collection<Prediction> predictions) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
