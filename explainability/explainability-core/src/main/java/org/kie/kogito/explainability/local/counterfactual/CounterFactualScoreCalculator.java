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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.kie.kogito.explainability.Config;
import org.kie.kogito.explainability.local.counterfactual.entities.CounterfactualEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.NestedFeatureHandler;
import org.kie.kogito.explainability.model.*;
import org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScore;
import org.optaplanner.core.api.score.calculator.EasyScoreCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static Double outputDistance(Output a, Output b) throws IllegalArgumentException {
        return outputDistance(a, b, 0.0);
    }

    public static Double outputDistance(Output a, Output b, double threshold) throws IllegalArgumentException {
        final Type aType = a.getType();
        final Type bType = b.getType();

        if (aType != bType) {
            String message = "Features must have the same type, got " + aType.toString() + " and " + bType.toString();
            logger.error(message);
            throw new IllegalArgumentException(message);
        }

        if (a.getValue() == null || b.getValue() == null) {
            return 1.0;
        }

        if (a.getType() == Type.NUMBER) {
            final double aValue = a.getValue().asNumber();
            final double bValue = b.getValue().asNumber();
            final double difference = Math.abs(aValue - bValue);
            // If any of the values is zero use the difference instead of change
            // If neither of the values is zero use the change rate
            double distance;
            if (aValue == 0 || bValue == 0) {
                distance = difference;
            } else {
                distance = difference / Math.max(aValue, bValue);
            }
            if (distance < threshold) {
                return 0d;
            } else {
                return distance;
            }

        } else if (a.getType() == Type.CATEGORICAL || a.getType() == Type.BOOLEAN || a.getType() == Type.TEXT) {
            //            System.out.println(a.getValue());
            //            System.out.println(b.getValue());
            return a.getValue().equals(b.getValue()) ? 0.0 : 1.0;
        } else {
            String message = "Feature type " + aType.toString() + " not supported";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Calculates the counterfactual score for each proposed solution.
     * This method assumes that each model used as {@link org.kie.kogito.explainability.model.PredictionProvider} is
     * consistent, in the sense that for repeated operations, the size of the returned collection of
     * {@link PredictionOutput} is the same, if the size of {@link PredictionInput} doesn't change.
     *
     * @param solution Proposed solution
     * @return A {@link BendableBigDecimalScore} with three "hard" levels and one "soft" level
     */
    @Override
    public BendableBigDecimalScore calculateScore(CounterfactualSolution solution) {

        double primaryHardScore = 0;
        int secondaryHardScore = 0;
        int tertiaryHardScore = 0;
        int secondarySoftscore = 0;

        StringBuilder builder = new StringBuilder();

        // Calculate similarities between original inputs and proposed inputs
        double inputSimilarities = 0.0;
        final int numberOfEntities = solution.getEntities().size();
        for (CounterfactualEntity entity : solution.getEntities()) {
            final double entitySimilarity = entity.similarity();
            inputSimilarities += entitySimilarity / (double) numberOfEntities;
            final Feature f = entity.asFeature();
            builder.append(String.format("%s=%s (d:%f)", f.getName(), f.getValue().getUnderlyingObject(), entitySimilarity));

            if (entity.isChanged()) {
                secondarySoftscore -= 1;

                if (entity.isConstrained()) {
                    secondaryHardScore -= 1;
                }
            }
        }
        // Calculate Gower distance from the similarities
        final double primarySoftScore = -Math.sqrt(1.0 - inputSimilarities);

        logger.debug("Current solution: {}", builder);

        List<Feature> flattenedFeatures = solution.getEntities().stream().map(CounterfactualEntity::asFeature).collect(Collectors.toList());

        List<Feature> input = NestedFeatureHandler.getDelinearizedFeatures(flattenedFeatures, solution.getOriginalFeatures());

        List<PredictionInput> inputs = List.of(new PredictionInput(input));

        CompletableFuture<List<PredictionOutput>> predictionAsync = solution.getModel().predictAsync(inputs);

        final List<Output> goal = solution.getGoal();

        try {
            List<PredictionOutput> predictions = predictionAsync.get(Config.INSTANCE.getAsyncTimeout(),
                    Config.INSTANCE.getAsyncTimeUnit());

            solution.setPredictionOutputs(predictions);

            double outputDistance = 0.0;

            for (PredictionOutput predictionOutput : predictions) {

                final List<Output> outputs = predictionOutput.getOutputs();

                if (goal.size() != outputs.size()) {
                    throw new IllegalArgumentException("Prediction size must be equal to goal size");
                }

                final int numberOutputs = outputs.size();
                for (int i = 0; i < numberOutputs; i++) {
                    final Output output = outputs.get(i);
                    final Output goalOutput = goal.get(i);
                    final double d =
                            CounterFactualScoreCalculator.outputDistance(output, goalOutput, solution.getGoalThreshold());
                    outputDistance += d * d;

                    if (output.getScore() < goalOutput.getScore()) {
                        tertiaryHardScore -= 1;
                    }
                }
                primaryHardScore -= Math.sqrt(outputDistance);
                logger.debug("Distance penalty: {}", primaryHardScore);
                logger.debug("Changed constraints penalty: {}", secondaryHardScore);
                logger.debug("Confidence threshold penalty: {}", tertiaryHardScore);
            }

        } catch (ExecutionException e) {
            logger.error("Prediction returned an error {}", e.getMessage());
        } catch (InterruptedException e) {
            logger.error("Interrupted while waiting for prediction {}", e.getMessage());
            Thread.currentThread().interrupt();
        } catch (TimeoutException e) {
            logger.error("Timed out while waiting for prediction");
        }

        logger.debug("Feature distance: {}", -Math.abs(primarySoftScore));
        return BendableBigDecimalScore.of(
                new BigDecimal[] {
                        BigDecimal.valueOf(primaryHardScore),
                        BigDecimal.valueOf(secondaryHardScore),
                        BigDecimal.valueOf(tertiaryHardScore)
                },
                new BigDecimal[] { BigDecimal.valueOf(-Math.abs(primarySoftScore)), BigDecimal.valueOf(secondarySoftscore) });
    }

    private PredictionInput getTestInput() {
        final Map<String, Object> client = new HashMap<>();
        client.put("Age", 43);
        client.put("Salary", 1950);
        client.put("Existing payments", 100);
        final Map<String, Object> loan = new HashMap<>();
        loan.put("Duration", 15);
        loan.put("Installment", 100);
        final Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("Client", client);
        contextVariables.put("Loan", loan);

        List<Feature> features = new ArrayList<>();
        features.add(FeatureFactory.newCompositeFeature("context", contextVariables));
        return new PredictionInput(features);
    }
}
