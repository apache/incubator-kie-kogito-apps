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


import org.kie.kogito.explainability.local.counterfactual.entities.CounterfactualEntity;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScore;
import org.optaplanner.core.api.score.calculator.EasyScoreCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Counterfactual score calculator.
 * The score is implementabled as a {@link BendableBigDecimalScore} with two hard levels and one soft level.
 * The primary hard level penalizes solutions which do not meet the required outcome.
 * The second hard level penalizes solutions which change constrained {@link CounterfactualEntity}.
 * The soft level penalizes solutions according to their distance from the original prediction inputs.
 */
public class CounterFactualScoreCalculator implements EasyScoreCalculator<CounterfactualSolution, BendableBigDecimalScore> {

    private static final Logger logger =
            LoggerFactory.getLogger(CounterFactualScoreCalculator.class);

    /**
     * Calculate the prediction's (as a {@link List<PredictionOutput>}) squared distance from the desired outcome (as a {@link List<Output>}).
     *
     * @param predictions The list of predictions (containing a single prediction)
     * @param goal        The desired outcome a list of {@link Output}
     * @return Numerical distance between prediction and desired outcome
     */
    private double predictionDistance(List<PredictionOutput> predictions, List<Output> goal) {
        final List<Output> outputs = predictions.get(0).getOutputs();
        double distance = 0.0;
        if (outputs.size() != predictions.size()) {
            throw new IllegalArgumentException("Prediction size must be equal to goal size");
        }
        for (int i = 0; i < outputs.size(); i++) {
            final Output output = outputs.get(i);
            distance += goal.get(i).getValue().asNumber() - output.getValue().asNumber();
        }
        return distance * distance;
    }

    @Override
    public BendableBigDecimalScore calculateScore(CounterfactualSolution solution) {

        double primaryHardScore = 0;
        int secondaryHardScore = 0;
        double primarySoftScore = 0.0;

        StringBuilder builder = new StringBuilder();

        for (CounterfactualEntity entity : solution.getEntities()) {
            primarySoftScore += entity.distance();
            final Feature f = entity.asFeature();
            builder.append(f.getName()).append("=").append(f.getValue().getUnderlyingObject()).append("(d: ").append(entity.distance()).append("),");

            if (entity.isConstrained() && (entity.isChanged())) {
                secondaryHardScore -= 1;
            }
        }

        logger.debug("Current solution: {}", builder);

        List<Feature> input = solution.getEntities().stream().map(CounterfactualEntity::asFeature).collect(Collectors.toList());

        PredictionInput predictionInput = new PredictionInput(input);

        List<PredictionInput> inputs = new ArrayList<>();
        inputs.add(predictionInput);
        CompletableFuture<List<PredictionOutput>> predictionAsync = solution.getModel().predictAsync(inputs);

        try {
            List<PredictionOutput> predictions = predictionAsync.get();
            final double distance = predictionDistance(predictions, solution.getGoal());
            primaryHardScore -= distance;
            logger.info("Penalise outcome (output: {})", distance);

        } catch (InterruptedException | ExecutionException e) {
            logger.error("Impossible to obtain prediction {}", e.getMessage());
        }

        logger.debug("Soft score: {}", -Math.abs(primarySoftScore));
        return BendableBigDecimalScore.of(
                new BigDecimal[]{
                        BigDecimal.valueOf(primaryHardScore), BigDecimal.valueOf(secondaryHardScore)
                },
                new BigDecimal[]{BigDecimal.valueOf(-Math.abs(primarySoftScore))});
    }
}
