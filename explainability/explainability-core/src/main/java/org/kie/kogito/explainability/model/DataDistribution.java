package org.kie.kogito.explainability.model;

import java.util.Collections;
import java.util.List;

/**
 * Information about distribution of data used for training a model
 */
public class DataDistribution {

    private final List<FeatureDistribution> featureDistributions;

    public DataDistribution(List<FeatureDistribution> featureDistributions) {
        this.featureDistributions = Collections.unmodifiableList(featureDistributions);
    }

    /**
     * Get each feature data distribution
     *
     * @return feature distributions
     */
    public List<FeatureDistribution> getFeatureDistributions() {
        return featureDistributions;
    }
}
