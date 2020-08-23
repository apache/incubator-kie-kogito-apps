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

package org.kie.kogito.explainability;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.vertx.mutiny.core.Vertx;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;
import org.kie.kogito.explainability.api.ExplainabilityResultDto;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.models.ExplainabilityRequest;
import org.kie.kogito.tracing.typedvalue.TypedValue;

@ApplicationScoped
public class ExplanationServiceImpl implements ExplanationService {

    @Inject
    ManagedExecutor executor;

    @Override
    public CompletionStage<ExplainabilityResultDto> explainAsync(ExplainabilityRequest request) {
        RemoteKogitoPredictionProvider provider = new RemoteKogitoPredictionProvider(request, Vertx.vertx(), ThreadContext.builder().build());
        LimeExplainer limeExplainer = new LimeExplainer(100, 1);
        Prediction prediction = getPrediction(request.getInputs(), request.getOutputs());
        return CompletableFuture.supplyAsync(() -> limeExplainer.explain(prediction, provider)).thenApplyAsync(
                saliencies -> new ExplainabilityResultDto(request.getExecutionId(), Collections.emptyMap()), executor);
    }

    private Prediction getPrediction(Map<String, TypedValue> inputs, Map<String, TypedValue> outputs) {
        PredictionInput input = getPredictionInput(inputs);
        PredictionOutput output = getPredictionOutput(outputs);
        return new Prediction(input, output);
    }

    private PredictionInput getPredictionInput(Map<String, TypedValue> inputs) {
        // TODO : convert inputs to a PredictionInput
        return null;
    }

    private PredictionOutput getPredictionOutput(Map<String, TypedValue> outputs) {
        // TODO: convert outputs to a PredictionOutput
        return null;
    }
}
