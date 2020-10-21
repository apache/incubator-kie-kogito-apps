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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.kie.kogito.explainability.global.GlobalExplainer;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.model.PredictionProviderMetadata;
import org.kie.kogito.explainability.model.Saliency;

/**
 * Global explainer aggregating LIME explanations over a number of inputs by reporting the mean feature importance for
 * each feature.
 */
public class AggregatedLimeExplainer implements GlobalExplainer<Map<String, Saliency>> {

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
    public Map<String, Saliency> explain(PredictionProvider model, PredictionProviderMetadata metadata)
            throws InterruptedException, ExecutionException {
        List<PredictionInput> inputs = metadata.getDataDistribution().sample(sampleSize);
        List<Saliency> saliencies = new ArrayList<>();
        for (PredictionInput input : inputs) {
            Prediction prediction = new Prediction(input, model.predictAsync(List.of(input)).get().get(0));
            saliencies.addAll(limeExplainer.explainAsync(prediction, model).get().values());
        }
        List<Saliency> merged = Saliency.merge(saliencies.toArray(new Saliency[0]));
        Map<String, Saliency> meanSaliencyMap = new HashMap<>();
        for (Saliency saliency: merged) {
            meanSaliencyMap.put(saliency.getOutput().getName(), saliency);
        }
        return meanSaliencyMap;
    }
}
