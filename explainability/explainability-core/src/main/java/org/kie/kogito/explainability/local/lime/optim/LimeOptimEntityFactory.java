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
import java.util.List;

import org.kie.kogito.explainability.local.lime.LimeConfig;

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
