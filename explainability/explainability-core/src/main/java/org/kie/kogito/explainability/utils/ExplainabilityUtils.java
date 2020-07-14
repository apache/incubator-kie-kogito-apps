package org.kie.kogito.explainability.utils;

import java.util.LinkedList;
import java.util.List;

import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureImportance;
import org.kie.kogito.explainability.model.Model;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.Saliency;
import org.kie.kogito.explainability.model.Type;
import org.apache.commons.lang3.tuple.Pair;

public class ExplainabilityUtils {

    /**
     * measure the explainability of an explanation as per paper "Towards Quantification of Explainability in Explainable
     * Artificial Intelligence Methods" by Islam et al.
     *
     * @param inputCognitiveChunks  the no. of cognitive chunks (pieces of information) required to generate the
     *                              explanation (e.g. the no. of explanation inputs)
     * @param outputCognitiveChunks the no. of cognitive chunks generated within the explanation itself
     * @param interactionRatio      the ratio of interaction (between 0 and 1) required by the explanation
     * @return the quantitative explainability measure
     */
    public static double quantifyExplainability(int inputCognitiveChunks, int outputCognitiveChunks, double interactionRatio) {
        return inputCognitiveChunks + outputCognitiveChunks > 0 ? 0.333 / (double) inputCognitiveChunks
                + 0.333 / (double) outputCognitiveChunks + 0.333 * (1d - interactionRatio) : 0;
    }

    /**
     * Calculate the impact of dropping the most important features (given by {@link Saliency#getTopFeatures(int)} from the input.
     * Highly important features would have rather high impact.
     *
     * @param model      the model to be explained
     * @param prediction a prediction
     * @param topFeatures the list of important features that should be dropped
     * @return the saliency impact
     */
    public static double saliencyImpact(Model model, Prediction prediction, List<FeatureImportance> topFeatures) {
        String[] importantFeatureNames = topFeatures.stream().map(f -> f.getFeature().getName()).toArray(String[]::new);

        List<Feature> newFeatures = new LinkedList<>();
        for (Feature feature : prediction.getInput().getFeatures()) {
            Feature newFeature = DataUtils.dropFeature(feature, importantFeatureNames);
            newFeatures.add(newFeature);
        }
        PredictionInput predictionInput = new PredictionInput(newFeatures);
        List<PredictionOutput> predictionOutputs = model.predict(List.of(predictionInput));
        PredictionOutput predictionOutput = predictionOutputs.get(0);
        double impact = 0;
        double size = predictionOutput.getOutputs().size();
        for (int i = 0; i < size; i++) {
            Output original = prediction.getOutput().getOutputs().get(i);
            Output modified = predictionOutput.getOutputs().get(i);
            impact += DataUtils.euclideanDistance(new double[]{original.getScore()}, new double[]{modified.getScore()});
            impact += DataUtils.hammingDistance(original.getValue().asString(), modified.getValue().asString());
        }
        return impact / size;
    }

    /**
     * calculate fidelity of boolean classification outputs using saliency predictor function = sign(sum(saliency.scores))
     * see papers:
     * - Guidotti Riccardo, et al. "A survey of methods for explaining black box models." ACM computing surveys (2018).
     * - Bodria, Francesco, et al. "Explainability Methods for Natural Language Processing: Applications to Sentiment Analysis (Discussion Paper)."
     *
     * @param pairs pairs composed by the saliency and the related prediction
     * @return the fidelity accuracy
     */
    public static double classificationFidelity(List<Pair<Saliency, Prediction>> pairs) {
        double acc = 0;
        double evals = 0;
        for (Pair<Saliency, Prediction> pair : pairs) {
            Saliency saliency = pair.getLeft();
            Prediction prediction = pair.getRight();
            for (Output output : prediction.getOutput().getOutputs()) {
                Type type = output.getType();
                if (Type.BOOLEAN.equals(type)) {
                    double predictorOutput = saliency.getPerFeatureImportance().stream().map(FeatureImportance::getScore).mapToDouble(d -> d).sum();
                    double v = output.getValue().asNumber();
                    boolean match = (v >= 0 && predictorOutput >= 0) || (v < 0 && predictorOutput < 0);
                    if (match) {
                        acc++;
                    }
                    evals++;
                }
            }
        }
        return acc / evals;
    }
}
