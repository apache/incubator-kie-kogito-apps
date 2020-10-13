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
import org.kie.kogito.explainability.model.FeatureFactory;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRange;
import org.optaplanner.core.api.domain.valuerange.ValueRangeFactory;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

/**
 * Mapping between a Double feature an OptaPlanner {@link PlanningEntity}
 */
@PlanningEntity
public class DoubleEntity implements CounterfactualEntity {
    @PlanningVariable(valueRangeProviderRefs = {"doubleRange"})
    public Double proposedValue;

    double doubleRangeMinimum;
    double doubleRangeMaximum;

    private Double originalValue;

    private boolean constrained;
    private String featureName;

    public DoubleEntity() {
    }

    private DoubleEntity(Double originalValue, String featureName, double minimum, double maximum, boolean constrained) {
        this.proposedValue = originalValue;
        this.originalValue = originalValue;
        this.featureName = featureName;
        this.doubleRangeMinimum = minimum;
        this.doubleRangeMaximum = maximum;
        this.constrained = constrained;
    }

    /**
     * Creates a {@link DoubleEntity}, taking the original input value from the
     * provided {@link Feature} and specifying whether the entity is constrained or not.
     *
     * @param originalFeature Original input {@link Feature}
     * @param constrained     Whether this entity's value should be fixed or not
     */
    public static DoubleEntity from(Feature originalFeature, double minimum, double maximum, boolean constrained) {
        return new DoubleEntity(originalFeature.getValue().asNumber(), originalFeature.getName(), minimum, maximum, constrained);
    }

    /**
     * Creates an unconstrained {@link DoubleEntity}, taking the original input value from the
     * provided {@link Feature}.
     *
     * @param originalFeature feature Original input {@link Feature}
     */
    public static DoubleEntity from(Feature originalFeature, double minimum, double maximum) {
        return DoubleEntity.from(originalFeature, minimum, maximum, false);
    }


    @ValueRangeProvider(id = "doubleRange")
    public ValueRange getValueRange() {
        return ValueRangeFactory.createDoubleValueRange(doubleRangeMinimum, doubleRangeMaximum);
    }

    @Override
    public String toString() {
        return "DoubleFeature{"
                + "value="
                + proposedValue
                + ", doubleRangeMinimum="
                + doubleRangeMinimum
                + ", doubleRangeMaximum="
                + doubleRangeMaximum
                + ", id='"
                + featureName
                + '\''
                + '}';
    }

    /**
     * Calculates the distance between the current planning value and the reference value
     * for this feature.
     *
     * @return Numerical distance
     */
    @Override
    public double distance() {
        return Math.abs(this.proposedValue - originalValue);
    }

    /**
     * Returns the {@link BooleanEntity} as a {@link Feature}
     *
     * @return {@link Feature}
     */
    @Override
    public Feature asFeature() {
        return FeatureFactory.newNumericalFeature(featureName, this.proposedValue);
    }

    @Override
    public boolean isConstrained() {
        return constrained;
    }

    /**
     * Returns whether the {@link BooleanEntity} new value is different from the reference
     * {@link Feature} value.
     *
     * @return boolean
     */
    @Override
    public boolean isChanged() {
        return !originalValue.equals(this.proposedValue);
    }
}
