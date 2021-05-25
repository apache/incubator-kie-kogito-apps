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
import org.kie.kogito.explainability.model.PerturbationContext;

public class LimeConfigEntityFactory {

    public static final String KERNEL_WIDTH = "kernel.width";
    public static final String PROXIMITY_THRESHOLD = "proximity.threshold";
    public static final String PROXIMITY_FILTERED_DATASET_MINIMUM = "proximity.filtered.dataset.minimum";
    public static final String EP_NUMERIC_CLUSTER_FILTER_WIDTH = "ep.numeric.cluster.filter.width";
    public static final String EP_NUMERIC_CLUSTER_THRESHOLD = "ep.numeric.cluster.threshold";
    public static final String SEPARABLE_DATASET_RATIO = "separable.dataset.ratio";
    public static final String NUMBER_OF_SAMPLES = "samples";
    public static final String NUMBER_OF_PERTURBATIONS = "perturbations";
    public static final String PROXIMITY_FILTER_ENABLED = "proximity.filter.enabled";
    public static final String PENALIZE_BALANCE_SPARSE = "penalize.balance.sparse";
    public static final String ADAPT_DATASET_VARIANCE = "penalize.balance.sparse";

    private LimeConfigEntityFactory() {
    }

    public static List<NumericLimeConfigEntity> createNumericEntities(LimeConfig config) {
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
        entities.add(new NumericLimeConfigEntity(PROXIMITY_FILTERED_DATASET_MINIMUM, proximityFilteredDatasetMinimum, 0.1, 0.9));
        double separableDatasetRatio = config.getSeparableDatasetRatio();
        entities.add(new NumericLimeConfigEntity(SEPARABLE_DATASET_RATIO, separableDatasetRatio, 0.7, 0.99));
        double noOfSamples = config.getNoOfSamples();
        entities.add(new NumericLimeConfigEntity(NUMBER_OF_SAMPLES, noOfSamples, 10, 1000));
        double noOfPerturbations = config.getPerturbationContext().getNoOfPerturbations();
        entities.add(new NumericLimeConfigEntity(NUMBER_OF_PERTURBATIONS, noOfPerturbations, 1, 10));
        return entities;
    }

    public static LimeConfig toLimeConfig(LimeStabilitySolution solution) {
        Map<String, Double> numericEntitiesMap = new HashMap<>();
        Map<String, Boolean> booleanEntitiesMap = new HashMap<>();
        List<LimeConfigEntity> entities = solution.getEntities();
        for (LimeConfigEntity entity : entities) {
            if (entity.isNumeric()) {
                numericEntitiesMap.put(entity.getName(), entity.asDouble());
            } else {
                booleanEntitiesMap.put(entity.getName(), entity.asBoolean());
            }
        }

        LimeConfig config = solution.getConfig();
        if (!numericEntitiesMap.isEmpty()) {
            Double numericTypeClusterGaussianFilterWidth = numericEntitiesMap.get(LimeConfigEntityFactory.EP_NUMERIC_CLUSTER_FILTER_WIDTH);
            Double numericTypeClusterThreshold = numericEntitiesMap.get(LimeConfigEntityFactory.EP_NUMERIC_CLUSTER_THRESHOLD);
            EncodingParams encodingParams = new EncodingParams(numericTypeClusterGaussianFilterWidth, numericTypeClusterThreshold);
            config = config.withPerturbationContext(new PerturbationContext(solution.getConfig()
                    .getPerturbationContext().getRandom(), numericEntitiesMap.get(NUMBER_OF_PERTURBATIONS).intValue()))
                    .withEncodingParams(encodingParams)
                    .withSamples(numericEntitiesMap.get(NUMBER_OF_SAMPLES).intValue())
                    .withSeparableDatasetRatio(numericEntitiesMap.get(SEPARABLE_DATASET_RATIO))
                    .withProximityFilteredDatasetMinimum(numericEntitiesMap.get(LimeConfigEntityFactory.PROXIMITY_FILTERED_DATASET_MINIMUM))
                    .withProximityThreshold(numericEntitiesMap.get(LimeConfigEntityFactory.PROXIMITY_THRESHOLD))
                    .withProximityKernelWidth(numericEntitiesMap.get(LimeConfigEntityFactory.KERNEL_WIDTH));
        }
        if (!booleanEntitiesMap.isEmpty()) {
            config = config
                    .withProximityFilter(booleanEntitiesMap.get(PROXIMITY_FILTER_ENABLED))
                    .withAdaptiveVariance(booleanEntitiesMap.get(ADAPT_DATASET_VARIANCE))
                    .withPenalizeBalanceSparse(booleanEntitiesMap.get(PENALIZE_BALANCE_SPARSE));
        }
        return config;

    }

    public static List<BooleanLimeConfigEntity> createBooleanEntities(LimeConfig config) {
        List<BooleanLimeConfigEntity> entities = new ArrayList<>();
        boolean proximityKernelWidth = config.isProximityFilter();
        entities.add(new BooleanLimeConfigEntity(PROXIMITY_FILTER_ENABLED, proximityKernelWidth));
        boolean penalizeBalanceSparse = config.isPenalizeBalanceSparse();
        entities.add(new BooleanLimeConfigEntity(PENALIZE_BALANCE_SPARSE, penalizeBalanceSparse));
        boolean adaptDatasetVariance = config.isAdaptDatasetVariance();
        entities.add(new BooleanLimeConfigEntity(ADAPT_DATASET_VARIANCE, adaptDatasetVariance));
        return entities;
    }
}
