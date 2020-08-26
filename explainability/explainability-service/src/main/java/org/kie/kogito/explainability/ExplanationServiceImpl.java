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
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.vertx.mutiny.core.Vertx;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;
import org.kie.kogito.explainability.api.ExplainabilityResultDto;
import org.kie.kogito.explainability.api.FeatureImportanceDto;
import org.kie.kogito.explainability.api.SaliencyDto;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import org.kie.kogito.explainability.model.FeatureImportance;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.Saliency;
import org.kie.kogito.explainability.models.ExplainabilityRequest;
import org.kie.kogito.tracing.typedvalue.TypedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.kie.kogito.explainability.ConversionUtils.toFeatureList;
import static org.kie.kogito.explainability.ConversionUtils.toOutputList;

@ApplicationScoped
public class ExplanationServiceImpl implements ExplanationService {

    private static final Logger LOG = LoggerFactory.getLogger(ExplanationServiceImpl.class);

    private final boolean mockExplanation;

    private final ManagedExecutor executor;
    private final LimeExplainer limeExplainer;
    private final ThreadContext threadContext;
    private final Vertx vertx;

    @Inject
    public ExplanationServiceImpl(
            @ConfigProperty(name = "trusty.explainability.mockExplanation", defaultValue = "true") Boolean mockExplanation,
            @ConfigProperty(name = "trusty.explainability.numberOfSamples", defaultValue = "100") Integer numberOfSamples,
            @ConfigProperty(name = "trusty.explainability.numberOfPerturbations", defaultValue = "1") Integer numberOfPerturbations,
            ManagedExecutor executor,
            ThreadContext threadContext,
            Vertx vertx) {
        this.mockExplanation = Boolean.TRUE.equals(mockExplanation);
        this.executor = executor;
        this.threadContext = threadContext;
        this.vertx = vertx;
        this.limeExplainer = new LimeExplainer(numberOfSamples, numberOfPerturbations);

        LOG.info("LimeExplainer created (numberOfSamples={}, numberOfPerturbations={})", numberOfSamples, numberOfPerturbations);
        if (mockExplanation) {
            LOG.info("Mocked explanation ENABLED");
        }
    }

    @Override
    public CompletionStage<ExplainabilityResultDto> explainAsync(ExplainabilityRequest request) {
        Function<ExplainabilityRequest, CompletableFuture<Map<String, Saliency>>> explanationFunction = mockExplanation
                ? this::mockedExplanationOf
                : this::explanationOf;

        return explanationFunction.apply(request)
                .thenApply(input -> createResultDto(input, request.getExecutionId()))
                .exceptionally((throwable) -> {
                    LOG.error("Exception thrown during explainAsync", throwable);
                    return new ExplainabilityResultDto(request.getExecutionId(), Collections.emptyMap());
                });
    }

    private CompletableFuture<Map<String, Saliency>> explanationOf(ExplainabilityRequest request) {
        RemoteKogitoPredictionProvider provider = new RemoteKogitoPredictionProvider(request, vertx, threadContext, executor);
        Prediction prediction = getPrediction(request.getInputs(), request.getOutputs());
        return limeExplainer.explain(prediction, provider);
    }

    private CompletableFuture<Map<String, Saliency>> mockedExplanationOf(ExplainabilityRequest request) {
        Prediction prediction = getPrediction(request.getInputs(), request.getOutputs());
        return CompletableFuture.supplyAsync(
                () -> prediction.getOutput().getOutputs().stream()
                        .collect(HashMap::new, (m, v) -> m.put(v.getName(), mockedSaliencyOf(prediction.getInput(), v)), HashMap::putAll),
                executor
        );
    }

    private static Saliency mockedSaliencyOf(PredictionInput input, Output output) {
        return new Saliency(
                output,
                input.getFeatures().stream()
                        .map(f -> new FeatureImportance(f, Math.random() * 2.0 - 1.0))
                        .collect(Collectors.toList())
        );
    }

    private static ExplainabilityResultDto createResultDto(Map<String, Saliency> saliencies, String executionId) {
        return new ExplainabilityResultDto(
                executionId,
                saliencies.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new SaliencyDto(e.getValue().getPerFeatureImportance().stream()
                                .map(fi -> new FeatureImportanceDto(fi.getFeature().getName(), fi.getScore()))
                                .collect(Collectors.toList())
                        )
                ))
        );
    }

    private static Prediction getPrediction(Map<String, TypedValue> inputs, Map<String, TypedValue> outputs) {
        PredictionInput input = getPredictionInput(inputs);
        PredictionOutput output = getPredictionOutput(outputs);
        return new Prediction(input, output);
    }

    private static PredictionInput getPredictionInput(Map<String, TypedValue> inputs) {
        return new PredictionInput(toFeatureList(inputs));
    }

    private static PredictionOutput getPredictionOutput(Map<String, TypedValue> outputs) {
        return new PredictionOutput(toOutputList(outputs));
    }
}
