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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.kie.kogito.explainability.global.GlobalExplainer;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.model.PredictionProviderMetadata;
import org.kie.kogito.explainability.model.Saliency;

/**
 * Global explainer aggregating LIME explanations over a number of inputs by reporting the mean feature importance for
 * each feature.
 */
public class AggregatedLimeExplainer implements GlobalExplainer<CompletableFuture<Map<String, Saliency>>> {

    private final LimeExplainer limeExplainer;
    private final int sampleSize;

    public AggregatedLimeExplainer(LimeExplainer limeExplainer) {
        this(limeExplainer, 100);
    }

    public AggregatedLimeExplainer(LimeExplainer limeExplainer, int sampleSize) {
        this.limeExplainer = limeExplainer;
        this.sampleSize = sampleSize;
    }

    @Override
    public CompletableFuture<Map<String, Saliency>> explain(PredictionProvider model, PredictionProviderMetadata metadata) {
        List<PredictionInput> inputs = metadata.getDataDistribution().sample(sampleSize); // sample inputs from the data distribution

        return model.predictAsync(inputs) // execute the model on the inputs
                .thenApply(os -> getCollect(inputs, os)) // generate predictions from inputs and outputs
                .thenApply(predictions -> predictions.parallelStream().map(p -> limeExplainer.explainAsync(p, model)) // extract saliency for each input
                .map(CompletableFuture::join) // aggregate all the saliencies
                .reduce(Collections.emptyMap(), Saliency::merge)); // merge all the saliencies together
    }

    private List<Prediction> getCollect(List<PredictionInput> inputs, List<PredictionOutput> os) {
        return IntStream.range(0, os.size())
                .mapToObj(i -> new Prediction(inputs.get(i), os.get(i))).collect(Collectors.toList());
    }
}
