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

import java.util.Set;

import org.optaplanner.core.api.domain.entity.PlanningEntity;

/**
 * Abstract categorical feature an OptaPlanner {@link PlanningEntity}
 */
@PlanningEntity
public abstract class AbstractCategoricalEntity<T> extends AbstractEntity<T> {

    protected Set<Object> allowedCategories;

    public AbstractCategoricalEntity() {
        super();
    }

    protected AbstractCategoricalEntity(T originalValue, String featureName, Set<Object> allowedCategories, boolean constrained) {
        super(originalValue, featureName, constrained);
        this.allowedCategories = allowedCategories;
    }

    /**
     * Calculates the distance between the current planning value and the reference value
     * for this feature.
     *
     * @return Numerical distance
     */
    @Override
    public double distance() {
        return proposedValue.equals(originalValue) ? 0.0 : 1.0;
    }

    @Override
    public double similarity() {
        return 1.0 - distance();
    }

    public abstract void setProposedValue(Object proposedValue);

    public abstract Object getProposedValue();

    public abstract Set<Object> getValueRange();
}
