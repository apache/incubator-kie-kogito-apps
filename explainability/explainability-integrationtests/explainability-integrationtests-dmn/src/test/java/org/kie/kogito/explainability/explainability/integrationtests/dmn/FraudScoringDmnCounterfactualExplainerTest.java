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
package org.kie.kogito.explainability.explainability.integrationtests.dmn;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.kogito.decision.DecisionModel;
import org.kie.kogito.dmn.DMNKogito;
import org.kie.kogito.dmn.DmnDecisionModel;
import org.kie.kogito.explainability.Config;
import org.kie.kogito.explainability.local.counterfactual.CounterfactualConfig;
import org.kie.kogito.explainability.local.counterfactual.CounterfactualExplainer;
import org.kie.kogito.explainability.local.counterfactual.CounterfactualResult;
import org.kie.kogito.explainability.local.counterfactual.SolverConfigBuilder;
import org.kie.kogito.explainability.local.lime.LimeConfig;
import org.kie.kogito.explainability.local.lime.optim.LimeConfigOptimizer;
import org.kie.kogito.explainability.model.CounterfactualPrediction;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.PerturbationContext;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;
import org.kie.kogito.explainability.model.domain.NumericalFeatureDomain;
import org.kie.kogito.explainability.utils.DataUtils;
import org.optaplanner.core.config.solver.EnvironmentMode;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FraudScoringDmnCounterfactualExplainerTest {

    @Test
    void testLoanEligibilityDMNExplanation() throws ExecutionException, InterruptedException, TimeoutException {
        PredictionProvider model = getModel();

        PredictionInput input = generateInput(100, 2000, true, false);

        // test model
        List<PredictionOutput> predictionOutputs = model.predictAsync(List.of(input))
                .get(Config.INSTANCE.getAsyncTimeout(), Config.INSTANCE.getAsyncTimeUnit());

        for (PredictionOutput o : predictionOutputs) {
            for (Output op : o.getOutputs()) {
                System.out.println(op);
            }
        }
    }

    @Test
    void testDMNValidCounterfactualExplanation() throws ExecutionException, InterruptedException, TimeoutException {
        PredictionProvider model = getModel();

        final List<Output> goal = generateGoal(0.0, null);

        PredictionInput input = generateInput(2000, 3000, true, false);

        final TerminationConfig terminationConfig = new TerminationConfig().withScoreCalculationCountLimit(10_000L);
        // for the purpose of this test, only a few steps are necessary
        final SolverConfig solverConfig = SolverConfigBuilder
                .builder().withTerminationConfig(terminationConfig).build();
        solverConfig.setRandomSeed((long) 23);
        solverConfig.setEnvironmentMode(EnvironmentMode.REPRODUCIBLE);

        final CounterfactualConfig counterfactualConfig =
                new CounterfactualConfig().withSolverConfig(solverConfig).withGoalThreshold(0.01);
        final CounterfactualExplainer counterfactualExplainer =
                new CounterfactualExplainer(counterfactualConfig);

        PredictionOutput output = new PredictionOutput(goal);
        Prediction prediction = new CounterfactualPrediction(input,
                output,
                null,
                UUID.randomUUID(), 600L);
        final CounterfactualResult counterfactualResult =
                counterfactualExplainer.explainAsync(prediction, model)
                        .get(60L, TimeUnit.SECONDS);

        //        List<Output> cfOutputs = counterfactualResult.getOutput().get(0).getOutputs();
        System.out.println(counterfactualResult.getEntities());
        System.out.println(counterfactualResult.isValid());
        for (PredictionOutput po : counterfactualResult.getOutput()) {
            for (Output o : po.getOutputs()) {
                System.out.println(o);
            }
        }
    }

    private PredictionProvider getModel() {
        DMNRuntime dmnRuntime = DMNKogito.createGenericDMNRuntime(new InputStreamReader(getClass().getResourceAsStream("/dmn/fraud.dmn")));
        assertEquals(1, dmnRuntime.getModels().size());
        final String FRAUD_NS = "http://www.redhat.com/dmn/definitions/_81556584-7d78-4f8c-9d5f-b3cddb9b5c73";
        final String FRAUD_NAME = "fraud-scoring";
        DecisionModel decisionModel = new DmnDecisionModel(dmnRuntime, FRAUD_NS, FRAUD_NAME);
        return new DecisionModelWrapper(decisionModel, List.of("Last Transaction", "Merchant Blacklist"));
    }

    private PredictionInput generateInput(int amount1, int amount2, boolean approved1, boolean approved2) {
        List<Map<String, Object>> transactions = new LinkedList<>();
        Map<String, Object> t1 = new HashMap<>();
        t1.put("Card Type", FeatureFactory.newCategoricalFeature("Card Type", "Prepaid"));
        t1.put("Location", FeatureFactory.newCategoricalFeature("Location", "Global"));
        t1.put("Amount", FeatureFactory.newNumericalFeature("Amount", amount1, NumericalFeatureDomain.create(0.0, 10000.0)));
        t1.put("Auth Code", approved1 ? FeatureFactory.newCategoricalFeature("Auth Code", "Approved") : FeatureFactory.newCategoricalFeature("Auth Code", "Denied"));
        transactions.add(t1);
        Map<String, Object> t2 = new HashMap<>();
        t2.put("Card Type", FeatureFactory.newCategoricalFeature("Card Type", "Debit"));
        t2.put("Location", FeatureFactory.newCategoricalFeature("Location", "Local"));
        t2.put("Amount", FeatureFactory.newNumericalFeature("Amount", amount2, NumericalFeatureDomain.create(0.0, 10000.0)));
        t2.put("Auth Code", approved2 ? FeatureFactory.newCategoricalFeature("Auth Code", "Approved") : FeatureFactory.newCategoricalFeature("Auth Code", "Denied"));
        transactions.add(t2);
        Map<String, Object> map = new HashMap<>();
        map.put("Transactions", transactions);

        List<Feature> features = new ArrayList<>();
        features.add(FeatureFactory.newCompositeFeature("context", map));

        return new PredictionInput(features);
    }

    private List<Output> generateGoal(double score, String amount) {
        return List.of(
                new Output("Risk Score", Type.NUMBER, new Value(score), 0.0),
                new Output("Total Amount from Last 24 hours Transactions", Type.TEXT, new Value(amount), 0.0));
    }

    @Test
    void testExplanationImpactScoreWithOptimization() throws ExecutionException, InterruptedException, TimeoutException {
        PredictionProvider model = getModel();

        List<PredictionInput> samples = DmnTestUtils.randomFraudScoringInputs();
        List<PredictionOutput> predictionOutputs = model.predictAsync(samples.subList(0, 10)).get();
        List<Prediction> predictions = DataUtils.getPredictions(samples, predictionOutputs);
        long seed = 0;
        LimeConfigOptimizer limeConfigOptimizer = new LimeConfigOptimizer().withDeterministicExecution(true).forImpactScore().withSampling(false);

        Random random = new Random();
        PerturbationContext perturbationContext = new PerturbationContext(seed, random, 1);
        LimeConfig initialConfig = new LimeConfig()
                .withSamples(10)
                .withPerturbationContext(perturbationContext);
        LimeConfig optimizedConfig = limeConfigOptimizer.optimize(initialConfig, predictions, model);

        assertThat(optimizedConfig).isNotSameAs(initialConfig);

    }

}
