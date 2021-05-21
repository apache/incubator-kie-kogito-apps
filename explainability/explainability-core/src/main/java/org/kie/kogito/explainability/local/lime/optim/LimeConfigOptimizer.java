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
package org.kie.kogito.explainability.local.lime.optim;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.kie.kogito.explainability.local.lime.LimeConfig;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.optaplanner.core.api.score.buildin.simplebigdecimal.SimpleBigDecimalScore;
import org.optaplanner.core.api.score.calculator.EasyScoreCalculator;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.config.localsearch.LocalSearchPhaseConfig;
import org.optaplanner.core.config.localsearch.decider.acceptor.LocalSearchAcceptorConfig;
import org.optaplanner.core.config.localsearch.decider.forager.LocalSearchForagerConfig;
import org.optaplanner.core.config.phase.PhaseConfig;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.SolverManagerConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LimeConfigOptimizer {

    private static final Logger logger = LoggerFactory.getLogger(LimeConfigOptimizer.class);

    private static final long DEFAULT_TIME_LIMIT = 30;
    private static final int DEFAULT_TABU_SIZE = 100;
    private static final int DEFAULT_ACCEPTED_COUNT = 5000;
    private static final boolean DEFAULT_BOOLEAN_ENTITIES = true;
    private static final boolean DEFAULT_NUMERIC_ENTITIES = true;

    private long timeLimit;
    private int tabuSize;
    private int acceptedCount;
    private boolean numericEntities;
    private boolean booleanEntities;
    private EasyScoreCalculator<LimeStabilitySolution, SimpleBigDecimalScore> scoreCalculator;

    private LimeConfigOptimizer(long timeLimit, int tabuSize, int acceptedCount,
            EasyScoreCalculator<LimeStabilitySolution, SimpleBigDecimalScore> scoreCalculator, boolean numericEntities,
            boolean booleanEntities) {
        this.timeLimit = timeLimit;
        this.tabuSize = tabuSize;
        this.acceptedCount = acceptedCount;
        this.scoreCalculator = scoreCalculator;
        this.numericEntities = numericEntities;
        this.booleanEntities = booleanEntities;
    }

    public LimeConfigOptimizer() {
        this(DEFAULT_TIME_LIMIT, DEFAULT_TABU_SIZE, DEFAULT_ACCEPTED_COUNT, new LimeStabilityScoreCalculator(),
                DEFAULT_NUMERIC_ENTITIES, DEFAULT_BOOLEAN_ENTITIES);
    }

    public LimeConfigOptimizer withTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
        return this;
    }

    public LimeConfigOptimizer withTabuSize(int tabuSize) {
        this.tabuSize = tabuSize;
        return this;
    }

    public LimeConfigOptimizer withAcceptedCount(int acceptedCount) {
        this.acceptedCount = acceptedCount;
        return this;
    }

    public LimeConfigOptimizer withNumericEntities(boolean numericEntities) {
        this.numericEntities = numericEntities;
        return this;
    }

    public LimeConfigOptimizer withBooleanEntities(boolean booleanEntities) {
        this.booleanEntities = booleanEntities;
        return this;
    }

    public LimeConfigOptimizer withScoreCalculator(EasyScoreCalculator<LimeStabilitySolution, SimpleBigDecimalScore> scoreCalculator) {
        this.scoreCalculator = scoreCalculator;
        return this;
    }

    public LimeConfig optimize(LimeConfig config, List<Prediction> predictions, PredictionProvider model) {
        List<LimeConfigEntity> entities = new ArrayList<>();
        if (numericEntities) {
            entities.addAll(LimeConfigEntityFactory.createNumericEntities(config));
        }
        if (booleanEntities) {
            entities.addAll(LimeConfigEntityFactory.createBooleanEntities(config));
        }

        if (entities.isEmpty()) {
            return config;
        } else {
            LimeStabilitySolution initialSolution = new LimeStabilitySolution(config, predictions, entities, model);
            SolverConfig solverConfig = new SolverConfig();

            solverConfig.withEntityClasses(NumericLimeConfigEntity.class, BooleanLimeConfigEntity.class);

            solverConfig.withSolutionClass(LimeStabilitySolution.class);

            ScoreDirectorFactoryConfig scoreDirectorFactoryConfig = new ScoreDirectorFactoryConfig();
            scoreDirectorFactoryConfig.setEasyScoreCalculatorClass(scoreCalculator.getClass());
            solverConfig.setScoreDirectorFactoryConfig(scoreDirectorFactoryConfig);

            TerminationConfig terminationConfig = new TerminationConfig();
            terminationConfig.setSecondsSpentLimit(timeLimit);
            solverConfig.setTerminationConfig(terminationConfig);

            LocalSearchAcceptorConfig acceptorConfig = new LocalSearchAcceptorConfig();
            acceptorConfig.setEntityTabuSize(tabuSize);

            LocalSearchForagerConfig localSearchForagerConfig = new LocalSearchForagerConfig();
            localSearchForagerConfig.setAcceptedCountLimit(acceptedCount);

            LocalSearchPhaseConfig localSearchPhaseConfig = new LocalSearchPhaseConfig();
            localSearchPhaseConfig.setAcceptorConfig(acceptorConfig);
            localSearchPhaseConfig.setForagerConfig(localSearchForagerConfig);

            @SuppressWarnings("rawtypes")
            List<PhaseConfig> phaseConfigs = new ArrayList<>();
            phaseConfigs.add(localSearchPhaseConfig);

            solverConfig.setPhaseConfigList(phaseConfigs);

            try (SolverManager<LimeStabilitySolution, UUID> solverManager =
                    SolverManager.create(solverConfig, new SolverManagerConfig())) {

                UUID executionId = UUID.randomUUID();
                SolverJob<LimeStabilitySolution, UUID> solverJob =
                        solverManager.solve(executionId, initialSolution);
                try {
                    // Wait until the solving ends
                    LimeStabilitySolution finalBestSolution = solverJob.getFinalBestSolution();
                    logger.info("final best solution score {}", finalBestSolution.getScore().getScore());
                    return LimeConfigEntityFactory.toLimeConfig(finalBestSolution);
                } catch (ExecutionException e) {
                    logger.error("Solving failed: {}", e.getMessage());
                    throw new IllegalStateException("Prediction returned an error", e);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Solving failed (Thread interrupted)", e);
                }
            }
        }
    }
}
