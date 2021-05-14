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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.kogito.explainability.local.lime.LimeConfig;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import org.kie.kogito.explainability.model.EncodingParams;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.utils.ExplainabilityMetrics;
import org.kie.kogito.explainability.utils.LocalSaliencyStability;
import org.optaplanner.core.api.score.buildin.simplebigdecimal.SimpleBigDecimalScore;
import org.optaplanner.core.api.score.calculator.EasyScoreCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LimeStabilityScoreCalculator implements EasyScoreCalculator<LimeStabilitySolution, SimpleBigDecimalScore> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LimeStabilityScoreCalculator.class);

    @Override
    public SimpleBigDecimalScore calculateScore(LimeStabilitySolution solution) {
        LimeConfig config = LimeOptimEntityFactory.toLimeConfig(solution);
        double stabilityScore = 0;
        List<Prediction> predictions = solution.getPredictions();
        if (!predictions.isEmpty()) {
            int topK = 2;
            LimeExplainer limeExplainer = new LimeExplainer(config);
            for (Prediction prediction : predictions) {
                try {
                    LocalSaliencyStability stability = ExplainabilityMetrics.getLocalSaliencyStability(solution.getModel(),
                            prediction, limeExplainer, topK, 5);
                    for (String decision : stability.getDecisions()) {
                        double positiveStabilityScore = 0;
                        double negativeStabilityScore = 0;
                        for (int i = 1; i <= topK; i++) {
                            positiveStabilityScore += stability.getPositiveStabilityScore(decision, i);
                            negativeStabilityScore += stability.getNegativeStabilityScore(decision, i);
                        }
                        positiveStabilityScore /= topK;
                        negativeStabilityScore /= topK;
                        // TODO: differentiate (or weight) between positive and negative
                        // TODO: some samples might generate exceptions, hence they shouldn't count
                        stabilityScore += (positiveStabilityScore + negativeStabilityScore) / (2d * predictions.size());
                    }
                } catch (Exception e) {
                    LOGGER.error("could not calculate stability for {}", prediction, e);
                }
            }
        }
        // TODO: maybe switch to hard-soft score for pos-neg scores
        return SimpleBigDecimalScore.parseScore(Double.toString(stabilityScore));
    }

}
