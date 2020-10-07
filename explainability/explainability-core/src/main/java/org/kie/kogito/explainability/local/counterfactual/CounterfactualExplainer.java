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
import org.kie.kogito.explainability.local.counterfactual.entities.BooleanEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.CounterfactualEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.DoubleEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.IntegerEntity;
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

    /**
     * Create a new {@link CounterfactualExplainer} using OptaPlanner as the underlying engine.
     *
     * @param dataBoundaries A {@link DataBoundaries} which specifies the search space boundaries
     * @param contraints     A list specifying by index which features are constrained
     * @param goal           A collection of {@link Output} representing the desired outcome
     * @param solverConfig   An OptaPlanner {@link SolverConfig} configuration
     */
    public CounterfactualExplainer(DataBoundaries dataBoundaries, List<Boolean> contraints, List<Output> goal, SolverConfig solverConfig) {
        this.constraints = contraints;
        this.goal = goal;
        this.dataBoundaries = dataBoundaries;
        this.solverConfig = solverConfig;
    }

    public CounterfactualExplainer(DataBoundaries dataBoundaries, List<Boolean> constraints, List<Output> goal) {
        this(dataBoundaries,
                constraints,
                goal,
                CounterfactualConfigurationFactory.createSolverConfig(
                        DEFAULT_TIME_LIMIT,
                        DEFAULT_TABU_SIZE,
                        DEFAULT_ACCEPTED_COUNT)
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
    public CounterfactualExplainer(DataBoundaries dataBoundaries, List<Boolean> contraints, List<Output> goal, Long timeLimit, int tabuSize, int acceptedCount) {
        this(dataBoundaries, contraints, goal, CounterfactualConfigurationFactory.createSolverConfig(timeLimit, tabuSize, acceptedCount));
    }

    private List<CounterfactualEntity> createEntities(PredictionInput predictionInput) {
        final List<CounterfactualEntity> entities = new ArrayList<>();

        for (int i = 0; i < predictionInput.getFeatures().size(); i++) {
            final Feature feature = predictionInput.getFeatures().get(i);
            final Boolean isConstrained = constraints.get(i);
            final FeatureBoundary featureDistribution = dataBoundaries.getFeatureBoundaries().get(i);
            if (feature.getType() == Type.NUMBER) {
                if (feature.getValue().getUnderlyingObject() instanceof Double) {
                    final DoubleEntity doubleEntity = new DoubleEntity(feature, featureDistribution.getStart(), featureDistribution.getEnd(), isConstrained);
                    entities.add(doubleEntity);
                } else if (feature.getValue().getUnderlyingObject() instanceof Integer) {
                    final IntegerEntity integerEntity = new IntegerEntity(feature, (int) featureDistribution.getStart(), (int) featureDistribution.getEnd(), isConstrained);
                    entities.add(integerEntity);
                }
            } else if (feature.getType() == Type.BOOLEAN) {
                final BooleanEntity booleanEntity = new BooleanEntity(feature, isConstrained);
                entities.add(booleanEntity);
            }
        }
        return entities;
    }

    @Override
    public CompletableFuture<List<CounterfactualEntity>> explainAsync(Prediction prediction, PredictionProvider model) {

        final List<CounterfactualEntity> entities = createEntities(prediction.getInput());

        final UUID problemId = UUID.randomUUID();

        SolverManager<CounterfactualSolution, UUID> solverManager =
                SolverManager.create(solverConfig, new SolverManagerConfig());

        CounterfactualSolution problem =
                new CounterfactualSolution(entities, model, goal);


        SolverJob<CounterfactualSolution, UUID> solverJob = solverManager.solve(problemId, problem);
        CounterfactualSolution solution;
        try {
            // Wait until the solving ends
            solution = solverJob.getFinalBestSolution();
            System.out.println(solution.toString());
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Solving failed.", e);
        }

        System.out.println("The counterfactual is:");
        for (CounterfactualEntity cfEntity : solution.getEntities()) {
            System.out.println(cfEntity.asFeature().toString());
        }
        return CompletableFuture.completedFuture(solution.getEntities());
    }
}
