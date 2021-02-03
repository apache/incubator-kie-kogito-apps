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

package org.kie.kogito.jitexecutor.dmn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.core.internal.utils.DynamicDMNContextBuilder;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.model.Type;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class LocalDMNPredictionProvider implements PredictionProvider {

    private final DMNModel dmnModel;
    private final DMNRuntime dmnRuntime;

    public LocalDMNPredictionProvider(DMNModel dmnModel, DMNRuntime dmnRuntime) {
        this.dmnModel = dmnModel;
        this.dmnRuntime = dmnRuntime;
    }

    @Override
    public CompletableFuture<List<PredictionOutput>> predictAsync(List<PredictionInput> inputs) {
        List<PredictionOutput> predictionOutputs = new ArrayList<>();
        for (PredictionInput input : inputs) {
            Map<String, Object> contextVariables = toMap(input.getFeatures());
            System.out.println(contextVariables);
            predictionOutputs.add(predict(contextVariables));
        }
        return completedFuture(predictionOutputs);
    }

    private PredictionOutput predict(Map<String, Object> contextVariables) {
        DMNContext dmnContext = new DynamicDMNContextBuilder(dmnRuntime.newContext(), dmnModel).populateContextWith(contextVariables);
        org.kie.dmn.api.core.DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, dmnContext);
        List<Output> outputs = new ArrayList<>();
        for (DMNDecisionResult decisionResult : dmnResult.getDecisionResults()) {
            Output output = buildOutput(decisionResult);
            outputs.add(output);
        }
        return new PredictionOutput(outputs);
    }

    private Output buildOutput(DMNDecisionResult dmnDecisionResult) {
        Feature result = FeatureFactory.parseFeatureValue(dmnDecisionResult.getDecisionName(), dmnDecisionResult.getResult());
        return new Output(dmnDecisionResult.getDecisionName(), result.getType(), result.getValue(), 1d);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> toMap(List<Feature> features) {
        Map<String, Object> map = new HashMap<>();
        for (Feature f : features) {
            if (Type.COMPOSITE.equals(f.getType())) {
                List<Feature> compositeFeatures = (List<Feature>) f.getValue().getUnderlyingObject();
                boolean isList = compositeFeatures.stream().allMatch(feature -> feature.getName().startsWith(f.getName() + "_"));
                if (isList) {
                    List<Object> objects = new ArrayList<>(compositeFeatures.size());
                    for (Feature fs : compositeFeatures) {
                        objects.add(fs.getValue().getUnderlyingObject());
                    }
                    map.put(f.getName(), objects);
                } else {
                    Map<String, Object> maps = new HashMap<>();
                    for (Feature cf : compositeFeatures) {
                        Map<String, Object> compositeFeatureMap = toMap(Collections.singletonList((cf)));
                        maps.putAll(compositeFeatureMap);
                    }
                    map.put(f.getName(), maps);
                }
            } else {
                if (Type.UNDEFINED.equals(f.getType())) {
                    Feature underlyingFeature = (Feature) f.getValue().getUnderlyingObject();
                    map.put(f.getName(), toMap(Collections.singletonList(underlyingFeature)));
                } else {
                    Object underlyingObject = f.getValue().getUnderlyingObject();
                    map.put(f.getName(), underlyingObject);
                }
            }
        }
        if (map.containsKey("context")) {
            map = (Map<String, Object>) map.get("context");
        }
        return map;
    }
}
