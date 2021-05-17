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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.kogito.explainability.local.lime.LimeConfig;
import org.kie.kogito.explainability.model.EncodingParams;

public class LimeConfigEntityFactory {

    public static final String KERNEL_WIDTH = "kernel.width";
    public static final String PROXIMITY_THRESHOLD = "proximity.threshold";
    public static final String PROXIMITY_FILTERED_DATASET_MINIMUM = "proximity.filtered.dataset.minimum";
    public static final String EP_NUMERIC_CLUSTER_FILTER_WIDTH = "ep.numeric.cluster.filter.width";
    public static final String EP_NUMERIC_CLUSTER_THRESHOLD = "ep.numeric.cluster.threshold";

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
        double proximityFilteredDatasetMinimum = config.getProximityFilteredDatasetMinimum().doubleValue();
        entities.add(new NumericLimeConfigEntity(PROXIMITY_FILTERED_DATASET_MINIMUM, proximityFilteredDatasetMinimum, 0.5, 0.9));
        return entities;
    }

    public static LimeConfig toLimeConfig(LimeStabilitySolution solution) {
        List<NumericLimeConfigEntity> entities = solution.getEntities();
        Map<String, Double> map = new HashMap<>();
        for (NumericLimeConfigEntity entity : entities) {
            map.put(entity.getName(), entity.getProposedValue());
        }
        Double numericTypeClusterGaussianFilterWidth = map.get(LimeConfigEntityFactory.EP_NUMERIC_CLUSTER_FILTER_WIDTH);
        Double numericTypeClusterThreshold = map.get(LimeConfigEntityFactory.EP_NUMERIC_CLUSTER_THRESHOLD);
        EncodingParams encodingParams = new EncodingParams(numericTypeClusterGaussianFilterWidth, numericTypeClusterThreshold);
        return solution.getInitialConfig()
                .withEncodingParams(encodingParams)
                .withProximityFilteredDatasetMinimum(map.get(LimeConfigEntityFactory.PROXIMITY_FILTERED_DATASET_MINIMUM))
                .withProximityThreshold(map.get(LimeConfigEntityFactory.PROXIMITY_THRESHOLD))
                .withProximityKernelWidth(map.get(LimeConfigEntityFactory.KERNEL_WIDTH));
    }

}
