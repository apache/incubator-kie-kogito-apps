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
package org.kie.kogito.explainability.local.counterfactual.entities;

import java.util.List;
import java.util.stream.Collectors;

import org.kie.kogito.explainability.local.counterfactual.entities.fixed.FixedBooleanEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.fixed.FixedCategoricalEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.fixed.FixedDoubleEntity;
import org.kie.kogito.explainability.local.counterfactual.entities.fixed.FixedIntegerEntity;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureDistribution;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.utils.DataUtils;

public class CounterfactualEntityFactory {

    private CounterfactualEntityFactory() {
    }

    public static CounterfactualEntity from(Feature feature) {
        return CounterfactualEntityFactory.from(feature, null);
    }

    public static List<CounterfactualEntity> from(List<Feature> features) {
        return DataUtils.getLinearizedFeatures(features).stream().map(CounterfactualEntityFactory::from)
                .collect(Collectors.toList());
    }

    public static CounterfactualEntity from(Feature feature,
            FeatureDistribution featureDistribution) {
        CounterfactualEntity entity = null;
        if (feature.getType() == Type.NUMBER) {
            if (feature.getValue().getUnderlyingObject() instanceof Double) {
                if (feature.isConstrained()) {
                    entity = FixedDoubleEntity.from(feature);
                } else {
                    entity = DoubleEntity.from(feature, feature.getDomain().getLowerBound(),
                            feature.getDomain().getUpperBound(),
                            featureDistribution, feature.isConstrained());
                }
            } else if (feature.getValue().getUnderlyingObject() instanceof Integer) {
                if (feature.isConstrained()) {
                    entity = FixedIntegerEntity.from(feature);
                } else {
                    entity = IntegerEntity.from(feature, feature.getDomain().getLowerBound().intValue(),
                            feature.getDomain().getUpperBound().intValue(), featureDistribution, feature.isConstrained());
                }
            }
        } else if (feature.getType() == Type.BOOLEAN) {
            if (feature.isConstrained()) {
                entity = FixedBooleanEntity.from(feature);
            } else {
                entity = BooleanEntity.from(feature, feature.isConstrained());
            }

        } else if (feature.getType() == Type.CATEGORICAL) {
            if (feature.isConstrained()) {
                entity = FixedCategoricalEntity.from(feature);
            } else {
                entity = CategoricalEntity.from(feature, feature.getDomain().getCategories(), feature.isConstrained());
            }
        } else {
            throw new IllegalArgumentException("Unsupported feature type: " + feature.getType());
        }
        return entity;
    }

    public static List<CounterfactualEntity> createEntities(PredictionInput predictionInput) {
        final List<Feature> linearizedFeatures = NestedFeatureHandler.flattenFeatures(predictionInput.getFeatures());
        return linearizedFeatures.stream().map(
                (Feature feature) -> {
                    return CounterfactualEntityFactory.from(feature, feature.getDistribution());
                }).collect(Collectors.toList());
    }
}