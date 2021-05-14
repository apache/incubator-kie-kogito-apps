package org.kie.kogito.explainability.local.lime.optim;

import org.kie.kogito.explainability.local.lime.LimeConfig;

import java.util.ArrayList;
import java.util.List;

public class LimeOptimEntityFactory {

    public static String KERNEL_WIDTH = "kernel.width";
    public static String PROXIMITY_THRESHOLD = "proximity.threshold";
    public static String EP_NUMERIC_CLUSTER_FILTER_WIDTH = "ep.numeric.cluster.filter.width";
    public static String EP_NUMERIC_CLUSTER_THRESHOLD = "ep.numeric.cluster.threshold";

    public static List<NumericLimeConfigEntity> createEntities(LimeConfig config) {
        List<NumericLimeConfigEntity> entities = new ArrayList<>();
        double proximityKernelWidth = config.getProximityKernelWidth();
        entities.add(new NumericLimeConfigEntity(KERNEL_WIDTH, proximityKernelWidth, 0.1, 0.9));
        double proximityThreshold = config.getProximityThreshold();
        entities.add(new NumericLimeConfigEntity(PROXIMITY_THRESHOLD, proximityThreshold, 0.5, 0.99));
        double numericTypeClusterGaussianFilterWidth = config.getEncodingParams().getNumericTypeClusterGaussianFilterWidth();
        entities.add(new NumericLimeConfigEntity(EP_NUMERIC_CLUSTER_FILTER_WIDTH, numericTypeClusterGaussianFilterWidth, 0.5, 1));
        double numericTypeClusterThreshold = config.getEncodingParams().getNumericTypeClusterThreshold();
        entities.add(new NumericLimeConfigEntity(EP_NUMERIC_CLUSTER_THRESHOLD, numericTypeClusterThreshold, 1e-4, 1e-1));
        return entities;
    }

}
