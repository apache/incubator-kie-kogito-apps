/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.kogito.explainability.local.counterfactual;

import org.kie.kogito.explainability.local.LocalExplainer;
import org.kie.kogito.explainability.local.counterfactual.entities.CounterfactualEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.CounterfactualEntityFactory;
import org.kie.kogito.explainability.model.*;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.SolverManagerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 * Provides exemplar (counterfactual) explanations for a predictive model.
 * This implementation uses the Constraint Solution Problem solver OptaPlanner to search for
 * counterfactuals which minimize a score calculated by {@link CounterFactualScoreCalculator}.
 */
public class CounterfactualExplainer implements LocalExplainer<List<CounterfactualEntity>> {

    private static final long DEFAULT_TIME_LIMIT = 30;
    private static final int DEFAULT_TABU_SIZE = 70;
    private static final int DEFAULT_ACCEPTED_COUNT = 5000;
    private final List<Output> goal;
    private final DataBoundaries dataBoundaries;
    private final List<Boolean> constraints;
    private final SolverConfig solverConfig;

    private final Executor executor;

    /**
     * Create a new {@link CounterfactualExplainer} using OptaPlanner as the underlying engine.
     *
     * @param dataBoundaries A {@link DataBoundaries} which specifies the search space boundaries
     * @param contraints     A list specifying by index which features are constrained
     * @param goal           A collection of {@link Output} representing the desired outcome
     * @param solverConfig   An OptaPlanner {@link SolverConfig} configuration
     */
    public CounterfactualExplainer(DataBoundaries dataBoundaries, List<Boolean> contraints, List<Output> goal, SolverConfig solverConfig, Executor executor) {
        this.constraints = contraints;
        this.goal = goal;
        this.dataBoundaries = dataBoundaries;
        this.solverConfig = solverConfig;
        this.executor = executor;
    }

    public CounterfactualExplainer(DataBoundaries dataBoundaries, List<Boolean> constraints, List<Output> goal) {
        this(dataBoundaries,
                constraints,
                goal,
                CounterfactualConfigurationFactory.createSolverConfig(
                        DEFAULT_TIME_LIMIT,
                        DEFAULT_TABU_SIZE,
                        DEFAULT_ACCEPTED_COUNT),
                ForkJoinPool.commonPool()
        );
    }

    /**
     * Create a new {@link CounterfactualExplainer} using OptaPlanner as the underlying engine.
     *
     * @param dataBoundaries A {@link DataBoundaries} which specifies the search space boundaries
     * @param contraints     A list specifying by index which features are constrained
     * @param goal           A collection of {@link Output} representing the desired outcome
     * @param timeLimit      Computational time spent limit in seconds
     * @param tabuSize       Tabu search limit
     * @param acceptedCount  How many accepted moves should be evaluated during each step
     * @see "Glover, Fred. "Tabu search—part I." ORSA Journal on computing 1, no. 3 (1989): 190-206"
     */
    public CounterfactualExplainer(DataBoundaries dataBoundaries, List<Boolean> contraints, List<Output> goal, Long timeLimit, int tabuSize, int acceptedCount, Executor executor) {
        this(dataBoundaries, contraints, goal, CounterfactualConfigurationFactory.createSolverConfig(timeLimit, tabuSize, acceptedCount), executor);
    }

    public CounterfactualExplainer(DataBoundaries dataBoundaries, List<Boolean> contraints, List<Output> goal, Long timeLimit, int tabuSize, int acceptedCount) {
        this(dataBoundaries, contraints, goal, CounterfactualConfigurationFactory.createSolverConfig(timeLimit, tabuSize, acceptedCount), ForkJoinPool.commonPool());
    }

    private Executor getExecutor() {
        return this.executor;
    }

    private List<CounterfactualEntity> createEntities(PredictionInput predictionInput) {
        final List<CounterfactualEntity> entities = new ArrayList<>();

        for (int i = 0; i < predictionInput.getFeatures().size(); i++) {
            final Feature feature = predictionInput.getFeatures().get(i);
            final Boolean isConstrained = constraints.get(i);
            final FeatureBoundary featureDistribution = dataBoundaries.getFeatureBoundaries().get(i);
            final CounterfactualEntity counterfactualEntity = CounterfactualEntityFactory.from(feature, isConstrained, featureDistribution);
            entities.add(counterfactualEntity);
        }
        return entities;
    }

    @Override
    public CompletableFuture<List<CounterfactualEntity>> explainAsync(Prediction prediction, PredictionProvider model) {

        final List<CounterfactualEntity> entities = createEntities(prediction.getInput());

        final UUID problemId = UUID.randomUUID();

        return CompletableFuture.supplyAsync(() -> {
            SolverManager<CounterfactualSolution, UUID> solverManager =
                    SolverManager.create(solverConfig, new SolverManagerConfig());

            CounterfactualSolution problem =
                    new CounterfactualSolution(entities, model, goal);

            SolverJob<CounterfactualSolution, UUID> solverJob = solverManager.solve(problemId, problem);
            CounterfactualSolution solution;
            try {
                // Wait until the solving ends
                solution = solverJob.getFinalBestSolution();
                return solution.getEntities();
            } catch (InterruptedException | ExecutionException e) {
                throw new IllegalStateException("Solving failed: {}", e);
            } finally {
                solverManager.close();
            }
        }, this.executor);
    }
}
