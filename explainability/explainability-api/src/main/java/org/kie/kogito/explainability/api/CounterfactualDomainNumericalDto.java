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
package org.kie.kogito.explainability.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CounterfactualDomainNumericalDto extends CounterfactualDomainDto {

    public static final String LOWER_BOUND_FIELD = "lowerBound";
    public static final String UPPER_BOUND_FIELD = "upperBound";

    @JsonProperty(LOWER_BOUND_FIELD)
    private double lowerBound;

    @JsonProperty(UPPER_BOUND_FIELD)
    private double upperBound;

    public CounterfactualDomainNumericalDto() {
    }

    public CounterfactualDomainNumericalDto(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public Type getType() {
        return Type.NUMERICAL;
    }

    public Double getLowerBound() {
        return this.lowerBound;
    }

    public Double getUpperBound() {
        return this.upperBound;
    }
}
