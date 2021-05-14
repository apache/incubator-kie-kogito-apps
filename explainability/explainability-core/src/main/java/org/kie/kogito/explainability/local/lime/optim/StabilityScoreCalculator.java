package org.kie.kogito.explainability.local.lime.optim;

import org.kie.kogito.explainability.local.lime.LimeConfig;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import org.kie.kogito.explainability.model.EncodingParams;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.utils.ExplainabilityMetrics;
import org.kie.kogito.explainability.utils.LocalSaliencyStability;
import org.optaplanner.core.api.score.buildin.simplebigdecimal.SimpleBigDecimalScore;
import org.optaplanner.core.api.score.calculator.EasyScoreCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StabilityScoreCalculator implements EasyScoreCalculator<LimeStabilitySolution, SimpleBigDecimalScore> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StabilityScoreCalculator.class);

    @Override
    public SimpleBigDecimalScore calculateScore(LimeStabilitySolution solution) {
        List<NumericLimeConfigEntity> entities = solution.getEntities();
        Map<String, Double> map = new HashMap<>();
        for (NumericLimeConfigEntity entity : entities) {
            map.put(entity.getName(), entity.getProposedValue());
        }
        double stabilityScore = 0;
        List<Prediction> predictions = solution.getPredictions();
        if (!predictions.isEmpty()) {
            int topK = 2;
            Double numericTypeClusterGaussianFilterWidth = map.get(LimeOptimEntityFactory.EP_NUMERIC_CLUSTER_FILTER_WIDTH);
            Double numericTypeClusterThreshold = map.get(LimeOptimEntityFactory.EP_NUMERIC_CLUSTER_THRESHOLD);
            EncodingParams encodingParams = new EncodingParams(numericTypeClusterGaussianFilterWidth, numericTypeClusterThreshold);
            LimeConfig config = new LimeConfig()
                    .withEncodingParams(encodingParams)
                    .withProximityThreshold(map.get(LimeOptimEntityFactory.PROXIMITY_THRESHOLD))
                    .withProximityKernelWidth(map.get(LimeOptimEntityFactory.KERNEL_WIDTH));

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
