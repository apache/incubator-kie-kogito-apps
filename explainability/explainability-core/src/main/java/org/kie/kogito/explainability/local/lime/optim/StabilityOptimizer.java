package org.kie.kogito.explainability.local.lime.optim;

import org.kie.kogito.explainability.local.counterfactual.CounterFactualScoreCalculator;
import org.kie.kogito.explainability.local.lime.LimeConfig;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.config.localsearch.decider.acceptor.LocalSearchAcceptorConfig;
import org.optaplanner.core.config.localsearch.decider.forager.LocalSearchForagerConfig;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.SolverManagerConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class StabilityOptimizer {

    private static final Logger logger = LoggerFactory.getLogger(StabilityOptimizer.class);

    private static final long DEFAULT_TIME_LIMIT = 300;
    private static final int DEFAULT_TABU_SIZE = 100;
    private static final int DEFAULT_ACCEPTED_COUNT = 5000;

    public static LimeStabilitySolution optimize(List<Prediction> predictions, PredictionProvider model) {
        List<NumericLimeConfigEntity> entities = LimeOptimEntityFactory.createEntities(new LimeConfig());
        LimeStabilitySolution initialSolution = new LimeStabilitySolution(predictions, entities, model);

        SolverConfig solverConfig = new SolverConfig();

        solverConfig.withEntityClasses(NumericLimeConfigEntity.class);
        solverConfig.withSolutionClass(LimeStabilitySolution.class);
        ScoreDirectorFactoryConfig scoreDirectorFactoryConfig = new ScoreDirectorFactoryConfig();
        scoreDirectorFactoryConfig.setEasyScoreCalculatorClass(StabilityScoreCalculator.class);
        solverConfig.setScoreDirectorFactoryConfig(scoreDirectorFactoryConfig);
        TerminationConfig terminationConfig = new TerminationConfig();
        terminationConfig.setSecondsSpentLimit(DEFAULT_TIME_LIMIT);
        solverConfig.setTerminationConfig(terminationConfig);
        LocalSearchAcceptorConfig acceptorConfig = new LocalSearchAcceptorConfig();
        acceptorConfig.setEntityTabuSize(DEFAULT_TABU_SIZE);
        LocalSearchForagerConfig localSearchForagerConfig = new LocalSearchForagerConfig();
        localSearchForagerConfig.setAcceptedCountLimit(DEFAULT_ACCEPTED_COUNT);

        try (SolverManager<LimeStabilitySolution, UUID> solverManager =
                     SolverManager.create(solverConfig, new SolverManagerConfig())) {


            UUID executionId = UUID.randomUUID();
            SolverJob<LimeStabilitySolution, UUID> solverJob =
                    solverManager.solve(executionId, initialSolution);
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
    }
}
