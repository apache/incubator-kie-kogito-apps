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
package org.kie.kogito.explainability.local.counterfactual;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.kie.kogito.explainability.local.LocalExplainer;
import org.kie.kogito.explainability.local.counterfactual.entities.CounterfactualEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.CounterfactualEntityFactory;
import org.kie.kogito.explainability.model.CounterfactualPrediction;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionFeatureDomain;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.SolverManagerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides exemplar (counterfactual) explanations for a predictive model.
 * This implementation uses the Constraint Solution Problem solver OptaPlanner to search for
 * counterfactuals which minimize a score calculated by {@link CounterFactualScoreCalculator}.
 */
public class CounterfactualExplainer implements LocalExplainer<CounterfactualResult> {

    public static final Consumer<CounterfactualSolution> assignSolutionId =
            counterfactual -> counterfactual.setSolutionId(UUID.randomUUID());
    private static final Logger logger =
            LoggerFactory.getLogger(CounterfactualExplainer.class);
    private final SolverConfig solverConfig;
    private final Function<SolverConfig, SolverManager<CounterfactualSolution, UUID>> solverManagerFactory;
    private final Executor executor;
    private final double goalThreshold;

    public CounterfactualExplainer() {
        this.solverConfig = CounterfactualConfigurationFactory.builder().build();
        this.solverManagerFactory = solverConfig -> SolverManager.create(solverConfig, new SolverManagerConfig());
        this.executor = ForkJoinPool.commonPool();
        this.goalThreshold = CounterfactualConfigurationFactory.DEFAULT_GOAL_THRESHOLD;
    }

    /**
     * Create a new {@link CounterfactualExplainer} using OptaPlanner as the underlying engine.
     * The data distribution information (if available) will be used to scale the features during the search.
     * A customizable OptaPlanner solver configuration can be passed using a {@link SolverConfig}.
     * An specific {@link Executor} can also be provided.
     * The score calculation (as performed by {@link CounterFactualScoreCalculator}) will use the goal threshold
     * to if a proposed solution is close enough to the goal to be considered a match. This will only apply
     * to numerical variables. This threshold is a positive ratio of the variable value (e.g. 0.01 of the value)
     * A strict match can be implemented by using a threshold of zero.
     *
     * @param solverConfig An OptaPlanner {@link SolverConfig} configuration
     * @param executor The {@link Executor} to be used by the explainer
     * @param goalThreshold A positive threshold as a ratio of the variable's value
     */
    protected CounterfactualExplainer(SolverConfig solverConfig,
            Function<SolverConfig, SolverManager<CounterfactualSolution, UUID>> solverManagerFactory,
            Executor executor, double goalThreshold) {
        this.solverConfig = solverConfig;
        this.solverManagerFactory = solverManagerFactory;
        this.executor = executor;
        this.goalThreshold = goalThreshold;
    }

    public static Builder builder() {
        return new Builder();
    }

    public double getGoalThreshold() {
        return goalThreshold;
    }

    public SolverConfig getSolverConfig() {
        return solverConfig;
    }

    /**
     * Wrap the provided {@link Consumer<CounterfactualResult>} in a OptaPlanner-accepted
     * {@link Consumer<CounterfactualSolution>}.
     * The consumer is only called when the provided {@link CounterfactualSolution} is valid.
     *
     * @param consumer {@link Consumer<CounterfactualResult>} provided to the explainer for intermediate results
     * @return {@link Consumer<CounterfactualSolution>} as accepted by OptaPlanner
     */
    private Consumer<CounterfactualSolution> createSolutionConsumer(Consumer<CounterfactualResult> consumer,
            AtomicLong sequenceId) {
        return counterfactualSolution -> {
            if (counterfactualSolution.getScore().isFeasible()) {
                CounterfactualResult result = new CounterfactualResult(counterfactualSolution.getEntities(),
                        counterfactualSolution.getPredictionOutputs(),
                        counterfactualSolution.getScore().isFeasible(),
                        counterfactualSolution.getSolutionId(),
                        counterfactualSolution.getExecutionId(),
                        sequenceId.incrementAndGet());
                consumer.accept(result);
            }
        };
    }

    @Override
    public CompletableFuture<CounterfactualResult> explainAsync(Prediction prediction,
            PredictionProvider model,
            Consumer<CounterfactualResult> intermediateResultsConsumer) {
        final AtomicLong sequenceId = new AtomicLong(0);
        CounterfactualPrediction cfPrediction = (CounterfactualPrediction) prediction;
        final PredictionFeatureDomain featureDomain = cfPrediction.getDomain();
        final List<Boolean> constraints = cfPrediction.getConstraints();
        final UUID executionId = cfPrediction.getExecutionId();
        final List<CounterfactualEntity> entities =
                CounterfactualEntityFactory.createEntities(prediction.getInput(), featureDomain, constraints,
                        cfPrediction.getDataDistribution());

        final List<Output> goal = prediction.getOutput().getOutputs();

        Function<UUID, CounterfactualSolution> initial =
                uuid -> new CounterfactualSolution(entities, model, goal, UUID.randomUUID(), executionId, this.goalThreshold);

        final CompletableFuture<CounterfactualSolution> cfSolution = CompletableFuture.supplyAsync(() -> {
            try (SolverManager<CounterfactualSolution, UUID> solverManager = solverManagerFactory.apply(solverConfig)) {

                SolverJob<CounterfactualSolution, UUID> solverJob =
                        solverManager.solveAndListen(executionId, initial,
                                assignSolutionId.andThen(createSolutionConsumer(intermediateResultsConsumer,
                                        sequenceId)),
                                null);
                try {
                    // Wait until the solving ends
                    return solverJob.getFinalBestSolution();
                } catch (ExecutionException e) {
                    logger.error("Solving failed: {}", e.getMessage());
                    throw new IllegalStateException("Prediction returned an error", e);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Solving failed (Thread interrupted)", e);
                }
            }
        }, this.executor);

        final CompletableFuture<List<PredictionOutput>> cfOutputs =
                cfSolution.thenCompose(s -> model.predictAsync(List.of(new PredictionInput(
                        s.getEntities().stream().map(CounterfactualEntity::asFeature).collect(Collectors.toList())))));
        return CompletableFuture.allOf(cfOutputs, cfSolution).thenApply(v -> {
            CounterfactualSolution solution = cfSolution.join();
            return new CounterfactualResult(solution.getEntities(),
                    cfOutputs.join(),
                    solution.getScore().isFeasible(),
                    UUID.randomUUID(),
                    solution.getExecutionId(),
                    sequenceId.incrementAndGet());
        });

    }

    public static class Builder {
        private Executor executor = ForkJoinPool.commonPool();
        private SolverConfig solverConfig = null;
        private double goalThreshold = CounterfactualConfigurationFactory.DEFAULT_GOAL_THRESHOLD;
        private Function<SolverConfig, SolverManager<CounterfactualSolution, UUID>> solverManagerFactory = null;

        private Builder() {
        }

        public Builder withExecutor(Executor executor) {
            this.executor = executor;
            return this;
        }

        public Builder withSolverConfig(SolverConfig solverConfig) {
            this.solverConfig = solverConfig;
            return this;
        }

        public Builder withGoalThreshold(double goalThreshold) {
            this.goalThreshold = goalThreshold;
            return this;
        }

        public Builder withSolverManagerFactory(
                Function<SolverConfig, SolverManager<CounterfactualSolution, UUID>> solverManagerFactory) {
            this.solverManagerFactory = solverManagerFactory;
            return this;
        }

        public CounterfactualExplainer build() {
            // Create a default solver configuration if none provided
            if (this.solverConfig == null) {
                this.solverConfig = CounterfactualConfigurationFactory.builder().build();
            }
            if (this.solverManagerFactory == null) {
                this.solverManagerFactory = solverConfig -> SolverManager.create(solverConfig, new SolverManagerConfig());
            }
            return new CounterfactualExplainer(
                    this.solverConfig,
                    this.solverManagerFactory,
                    this.executor,
                    this.goalThreshold);
        }
    }
}
