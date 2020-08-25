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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.kie.kogito.explainability.api.ExplainabilityResultDto;
import org.kie.kogito.explainability.api.FeatureImportanceDto;
import org.kie.kogito.explainability.api.SaliencyDto;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureImportance;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.Saliency;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;
import org.kie.kogito.explainability.models.ExplainabilityRequest;
import org.kie.kogito.tracing.typedvalue.TypedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ExplanationServiceImpl implements ExplanationService {

    private static final Logger LOG = LoggerFactory.getLogger(ExplanationServiceImpl.class);

    @Inject
    ManagedExecutor executor;

    @Override
    public CompletionStage<ExplainabilityResultDto> explainAsync(ExplainabilityRequest request) {
        LOG.info("** explainAsync called ***");
        // TODO: restore limeExplainer when loop works and fix triggered exceptions
//        RemoteKogitoPredictionProvider provider = new RemoteKogitoPredictionProvider(request, Vertx.vertx(), ThreadContext.builder().build());
//        LimeExplainer limeExplainer = new LimeExplainer(100, 1);
        Prediction prediction = getPrediction(request.getInputs(), request.getOutputs());
        return CompletableFuture
                // .supplyAsync(() -> limeExplainer.explain(prediction, provider))
                .supplyAsync(() -> mockedExplainationOf(prediction))
                .exceptionally((throwable) -> {
                    LOG.error("Exception thrown during explainAsync [1]", throwable);
                    return CompletableFuture.failedFuture(throwable);
                })
                .thenApply(inputFuture -> createResultDto(inputFuture, request.getExecutionId()))
                .exceptionally((throwable) -> {
                    LOG.error("Exception thrown during explainAsync [2]", throwable);
                    return new ExplainabilityResultDto(request.getExecutionId(), Collections.emptyMap());
                });
        // .thenApplyAsync(saliencies -> new ExplainabilityResultDto(request.getExecutionId(), Collections.emptyMap()), executor);
    }

    private static CompletableFuture<Map<String, Saliency>> mockedExplainationOf(Prediction prediction) {
        return CompletableFuture.completedFuture(
                prediction.getOutput().getOutputs().stream()
                        .collect(HashMap::new, (m, v) -> m.put(v.getName(), mockedSaliencyOf(prediction.getInput(), v)), HashMap::putAll)
        );
    }

    private static Saliency mockedSaliencyOf(PredictionInput input, Output output) {
        return new Saliency(
                output,
                input.getFeatures().stream()
                        .map(f -> new FeatureImportance(f, 1.0))
                        .collect(Collectors.toList())
        );
    }

    private static ExplainabilityResultDto createResultDto(CompletableFuture<Map<String, Saliency>> inputFuture, String executionId) {
        if (!inputFuture.isDone() || inputFuture.isCompletedExceptionally() || inputFuture.isCancelled()) {
            return new ExplainabilityResultDto(executionId, Collections.emptyMap());
        }
        try {
            // FIXME this is wrong, the mapping should be applied as additional step of CompletableFuture
            Map<String, SaliencyDto> saliencies = inputFuture.get().entrySet().stream().collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> new SaliencyDto(e.getValue().getPerFeatureImportance().stream()
                            .map(fi -> new FeatureImportanceDto(fi.getFeature().getName(), fi.getScore()))
                            .collect(Collectors.toList())
                    )
            ));
            return new ExplainabilityResultDto(executionId, saliencies);
        } catch (ExecutionException | InterruptedException e) {
            LOG.error("Exception on createResultDto", e);
            throw new RuntimeException(e);
        }
    }

    private static Prediction getPrediction(Map<String, TypedValue> inputs, Map<String, TypedValue> outputs) {
        PredictionInput input = getPredictionInput(inputs);
        PredictionOutput output = getPredictionOutput(outputs);
        return new Prediction(input, output);
    }

    private static PredictionInput getPredictionInput(Map<String, TypedValue> inputs) {
        // TODO : convert inputs to a PredictionInput
        LOG.info("** getPredictionInput called with " + inputs.size() + " inputs ***");

        List<Feature> features = inputs.entrySet().stream()
                .flatMap(entry -> toList(entry.getKey(), entry.getValue(), ExplanationServiceImpl::toFeature).stream())
                .peek(obj -> LOG.info("Feature[name={}, type={}, value={}]", obj.getName(), obj.getType(), obj.getValue()))
                .collect(Collectors.toList());

        return new PredictionInput(features);
    }

    private static PredictionOutput getPredictionOutput(Map<String, TypedValue> outputs) {
        // TODO: convert outputs to a PredictionOutput
        LOG.info("** getPredictionOutput called with " + outputs.size() + " inputs ***");

        List<Output> features = outputs.entrySet().stream()
                .flatMap(entry -> toList(entry.getKey(), entry.getValue(), ExplanationServiceImpl::toOutput).stream())
                .peek(obj -> LOG.info("Output[name={}, type={}, value={}]", obj.getName(), obj.getType(), obj.getValue()))
                .collect(Collectors.toList());

        return new PredictionOutput(features);
    }

    // TODO: this is just a stub: implement it properly
    private static <T> List<T> toList(String name, TypedValue value, BiFunction<String, JsonNode, T> unitConverter) {
        if (value.isCollection()) {
            int counter = 0;
            List<T> result = new LinkedList<>();
            for (TypedValue childValue : value.toCollection().getValue()) {
                result.addAll(toList(String.format("%s[%s]", name, counter++), childValue, unitConverter));
            }
            return result;
        }
        if (value.isStructure()) {
            return value.toStructure().getValue().entrySet().stream()
                    .flatMap(entry -> toList(String.format("%s.%s", name, entry.getKey()), entry.getValue(), unitConverter).stream())
                    .collect(Collectors.toList());
        }
        JsonNode jsonValue = value.toUnit().getValue();
        return List.of(unitConverter.apply(name, jsonValue));
    }

    // TODO: this is just a stub: implement it properly
    private static Feature toFeature(String name, JsonNode jsonValue) {
        return jsonValue.isNumber()
                ? new Feature(name, Type.NUMBER, new Value<>(jsonValue.asDouble()))
                : new Feature(name, Type.TEXT, new Value<>(jsonValue.asText()));
    }

    // TODO: this is just a stub: implement it properly
    private static Output toOutput(String name, JsonNode jsonValue) {
        return jsonValue.isNumber()
                ? new Output(name, Type.NUMBER, new Value<>(jsonValue.asDouble()), 0.0)
                : new Output(name, Type.TEXT, new Value<>(jsonValue.asText()), 0.0);
    }
}
