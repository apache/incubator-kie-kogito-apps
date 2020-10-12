/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.kogito.explainability.local.counterfactual.entities;

import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureBoundary;
import org.kie.kogito.explainability.model.Type;

public class CounterfactualEntityFactory {

    public static CounterfactualEntity from(Feature feature, Boolean isConstrained, FeatureBoundary featureDistribution) {

        CounterfactualEntity entity = null;
        if (feature.getType() == Type.NUMBER) {
            if (feature.getValue().getUnderlyingObject() instanceof Double) {
                entity = new DoubleEntity(feature, featureDistribution.getStart(), featureDistribution.getEnd(), isConstrained);
            } else if (feature.getValue().getUnderlyingObject() instanceof Integer) {
                entity = new IntegerEntity(feature, (int) featureDistribution.getStart(), (int) featureDistribution.getEnd(), isConstrained);
            }
        } else if (feature.getType() == Type.BOOLEAN) {
            entity = new BooleanEntity(feature, isConstrained);
        } else {
            throw new IllegalArgumentException("Unsupported feature type: " + feature.getType());
        }
        return entity;
    }
}

