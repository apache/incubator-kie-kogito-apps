package org.kie.kogito.explainability.local.counterfactual;

import org.kie.kogito.explainability.local.counterfactual.entities.BooleanEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.DoubleEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.IntegerEntity;
import org.optaplanner.core.config.localsearch.LocalSearchPhaseConfig;
import org.optaplanner.core.config.localsearch.decider.acceptor.LocalSearchAcceptorConfig;
import org.optaplanner.core.config.localsearch.decider.forager.LocalSearchForagerConfig;
import org.optaplanner.core.config.phase.PhaseConfig;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;

import java.util.ArrayList;
import java.util.List;

public class CounterfactualConfigurationFactory {

    private CounterfactualConfigurationFactory() {
        
    }

    public static SolverConfig createSolverConfig(Long timeLimit, int tabuSize, int acceptedCount) {
        SolverConfig solverConfig = new SolverConfig();

        solverConfig.withEntityClasses(IntegerEntity.class, DoubleEntity.class, BooleanEntity.class);
        solverConfig.setSolutionClass(CounterfactualSolution.class);

        ScoreDirectorFactoryConfig scoreDirectorFactoryConfig = new ScoreDirectorFactoryConfig();
        scoreDirectorFactoryConfig.setEasyScoreCalculatorClass(CounterFactualScoreCalculator.class);
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

        List<PhaseConfig> phaseConfigs = new ArrayList<>();
        phaseConfigs.add(localSearchPhaseConfig);

        solverConfig.setPhaseConfigList(phaseConfigs);
        return solverConfig;
    }
}
