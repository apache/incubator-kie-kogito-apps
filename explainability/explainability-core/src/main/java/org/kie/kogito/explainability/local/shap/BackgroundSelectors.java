/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.explainability.local.shap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.kie.kogito.explainability.Config;
import org.kie.kogito.explainability.local.counterfactual.CounterfactualConfig;
import org.kie.kogito.explainability.local.counterfactual.CounterfactualExplainer;
import org.kie.kogito.explainability.local.counterfactual.CounterfactualResult;
import org.kie.kogito.explainability.local.counterfactual.SolverConfigBuilder;
import org.kie.kogito.explainability.local.counterfactual.entities.CounterfactualEntity;
import org.kie.kogito.explainability.model.CounterfactualPrediction;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;
import org.kie.kogito.explainability.utils.DataUtils;
import org.kie.kogito.explainability.utils.MatrixUtilsExtensions;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;

public class BackgroundSelectors {
    public static List<PredictionInput> randomSelector(List<PredictionInput> pis, int n) {
        List<Integer> idx = IntStream.range(0, pis.size()).boxed().collect(Collectors.toList());
        Collections.shuffle(idx);
        return IntStream.range(0, n).mapToObj(i -> pis.get(idx.get(i))).collect(Collectors.toList());
    }

    public static List<PredictionInput> kmeansSelector(List<PredictionInput> pis, int n) {
        PredictionInput prototypePI = pis.get(0);
        List<DoublePoint> datapoints = pis.stream().map(pi -> new DoublePoint(pi.getFeatures().stream()
                .mapToDouble(f -> f.getValue().asNumber()).toArray()))
                .collect(Collectors.toList());
        KMeansPlusPlusClusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<>(n);
        List<CentroidCluster<DoublePoint>> clusters = clusterer.cluster(datapoints);
        List<PredictionInput> background = new ArrayList<>();
        for (CentroidCluster c : clusters) {
            double[] center = c.getCenter().getPoint();
            List<Feature> newFeatures = new ArrayList<>();
            for (int i = 0; i < center.length; i++) {
                Feature f = prototypePI.getFeatures().get(i);
                if (f.getType() != Type.NUMBER) {
                    throw new IllegalArgumentException("KMeans Background Selection Can Only Be Called On Numeric Features");
                }
                newFeatures.add(new Feature(f.getName(), f.getType(), new Value(center[i])));
            }
            background.add(new PredictionInput(newFeatures));
        }
        return background;
    }

    // Counterfactual Background Generation ===========================================================================
    public static List<PredictionInput> cfSelector(List<PredictionInput> pis, PredictionProvider model, PredictionOutput goal, CFBackgroundConfig config)
            throws ExecutionException, InterruptedException, TimeoutException {

        // find the starting points for the search
        List<PredictionInput> bestSeeds = findNClosestSeeds(model, pis, goal, config.getnSeeds());

        // configure Counterfactual search
        final TerminationConfig terminationConfig = new TerminationConfig().withScoreCalculationCountLimit(config.getOptaCalculationLimit());
        final SolverConfig solverConfig = SolverConfigBuilder.builder().withTerminationConfig(terminationConfig).build();
        solverConfig.setRandomSeed(config.getPerturbationContext().getSeed().get());
        final CounterfactualConfig counterfactualConfig = new CounterfactualConfig()
                .withSolverConfig(solverConfig)
                .withGoalThreshold(config.getGoalThreshold());
        final CounterfactualExplainer counterfactualExplainer = new CounterfactualExplainer(counterfactualConfig);

        // run search
        List<PredictionInput> generatedBackground = new ArrayList<>();
        PredictionInput seedPerturb;
        while (generatedBackground.size() < config.getnToGenerate()) {
            for (PredictionInput seed : bestSeeds) {

                // if we've used this seed already, perturb it a bit
                if (generatedBackground.size() > bestSeeds.size()) {
                    seedPerturb = new PredictionInput(DataUtils.perturbFeatures(seed.getFeatures(), config.getPerturbationContext()));
                } else {
                    seedPerturb = seed;
                }

                // find possible CF
                Prediction prediction = new CounterfactualPrediction(seedPerturb, goal, null, UUID.randomUUID(), config.getOptaTimeLimit());
                final CounterfactualResult counterfactualResult = counterfactualExplainer.explainAsync(prediction, model)
                        .get(config.getModelTimeoutSeconds(), Config.INSTANCE.getAsyncTimeUnit());

                // add it to our found list if valid
                if (counterfactualResult.isValid()) {
                    generatedBackground.add(
                            new PredictionInput(
                                    counterfactualResult.getEntities().stream()
                                            .map(CounterfactualEntity::asFeature).collect(Collectors.toList())));
                }
            }
        }
        return generatedBackground;
    }

    private static class ArgVal {
        int idx;
        double val;

        public ArgVal(int idx, double val) {
            this.idx = idx;
            this.val = val;
        }
    }

    private static List<PredictionInput> findNClosestSeeds(PredictionProvider model, List<PredictionInput> seeds, PredictionOutput goal, int n)
            throws ExecutionException, InterruptedException {
        List<PredictionOutput> seedOutputs = model.predictAsync(seeds).get();
        RealVector goalVector = MatrixUtilsExtensions.vectorFromPredictionOutput(goal);
        List<ArgVal> distances = new ArrayList<>();
        for (int i = 0; i < seedOutputs.size(); i++) {
            distances.add(new ArgVal(i, MatrixUtilsExtensions.vectorFromPredictionOutput(new PredictionOutput(seedOutputs.get(i).getOutputs())).getDistance(goalVector)));
        }
        distances.sort(Comparator.comparingDouble(i -> i.val));
        return IntStream.range(0, n).mapToObj(i -> seeds.get(distances.get(i).idx)).collect(Collectors.toList());
    }

}
