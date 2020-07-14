package org.kie.kogito.explainability.model;

/**
 * the data distribution for a given feature
 */
public class FeatureDistribution {

    private final double min;
    private final double max;
    private final double mean;
    private final double stdDev;

    public FeatureDistribution(double min, double max, double mean, double stdDev) {
        this.min = min;
        this.max = max;
        this.mean = mean;
        this.stdDev = stdDev;
    }

    /**
     * get minimum value for this feature
     *
     * @return the min value
     */
    public double getMin() {
        return min;
    }

    /**
     * get the maximum value for this feature
     *
     * @return the max value
     */
    public double getMax() {
        return max;
    }

    /**
     * get the mean value for this feature
     *
     * @return the mean value
     */
    public double getMean() {
        return mean;
    }

    /**
     * get the standard deviation for this feature
     *
     * @return the standard deviation
     */
    public double getStdDev() {
        return stdDev;
    }
}
