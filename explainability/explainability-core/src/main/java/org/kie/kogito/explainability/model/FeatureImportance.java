package org.kie.kogito.explainability.model;

/**
 * The importance associated to a given {@link Feature}.
 * This is usually the output of an explanation algorithm (local or global).
 */
public class FeatureImportance {

    private final Feature feature;
    private final double score;

    public FeatureImportance(Feature feature, double score) {
        this.feature = feature;
        this.score = score;
    }

    public Feature getFeature() {
        return feature;
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "FeatureImportance{" +
                "feature=" + feature +
                ", score=" + score +
                '}';
    }
}
