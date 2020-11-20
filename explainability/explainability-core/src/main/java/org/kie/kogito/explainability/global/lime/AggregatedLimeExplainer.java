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
package org.kie.kogito.explainability.global.lime;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.kie.kogito.explainability.global.ExistingPredictionsGlobalExplainer;
import org.kie.kogito.explainability.global.GlobalExplainer;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.model.PredictionProviderMetadata;
import org.kie.kogito.explainability.model.Saliency;
import org.kie.kogito.explainability.utils.DataUtils;

/**
 * Global explainer aggregating LIME explanations over a number of inputs by reporting the mean feature importance for
 * each feature.
 */
public class AggregatedLimeExplainer implements GlobalExplainer<CompletableFuture<Map<String, Saliency>>>,
                                                ExistingPredictionsGlobalExplainer<CompletableFuture<Map<String, Saliency>>> {

    private static final int DEFAULT_SAMPLE_SIZE = 100;

    private final LimeExplainer limeExplainer;
    private final int sampleSize;

    public AggregatedLimeExplainer(LimeExplainer limeExplainer) {
        this(limeExplainer, DEFAULT_SAMPLE_SIZE);
    }

    public AggregatedLimeExplainer(LimeExplainer limeExplainer, int sampleSize) {
        this.limeExplainer = limeExplainer;
        this.sampleSize = sampleSize;
    }

    @Override
    public CompletableFuture<Map<String, Saliency>> explain(PredictionProvider model, PredictionProviderMetadata metadata) {
        List<PredictionInput> inputs = metadata.getDataDistribution().sample(sampleSize); // sample inputs from the data distribution

        return model.predictAsync(inputs) // execute the model on the inputs
                .thenApply(os -> DataUtils.getPredictions(inputs, os)) // generate predictions from inputs and outputs
                .thenCompose(ps -> explain(model, ps)); // explain predictions
    }

    @Override
    public CompletableFuture<Map<String, Saliency>> explain(PredictionProvider model, Collection<Prediction> predictions) {
        return CompletableFuture.completedFuture(predictions)
                .thenApply(p -> p.stream().map(prediction -> limeExplainer.explainAsync(prediction, model)) // extract saliency for each input
                        .map(CompletableFuture::join) // aggregate all the saliencies
                        .reduce(Collections.emptyMap(), (m1, m2) -> Saliency.merge(List.of(m1, m2)))); // merge all the saliencies together
    }
}
