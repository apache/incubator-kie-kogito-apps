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
package org.kie.kogito.trusty.storage.api.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class CounterfactualDomainRange extends CounterfactualDomain {

    public static final String TYPE = "RANGE";
    public static final String LOWER_BOUND = "lowerBound";
    public static final String UPPER_BOUND = "upperBound";

    @JsonProperty(LOWER_BOUND)
    @NotNull(message = "lowerBound object must be provided.")
    private JsonNode lowerBound;

    @JsonProperty(UPPER_BOUND)
    @NotNull(message = "upperBound object must be provided.")
    private JsonNode upperBound;

    public CounterfactualDomainRange() {
    }

    public CounterfactualDomainRange(@NotNull JsonNode lowerBound,
            @NotNull JsonNode upperBound) {
        this.lowerBound = Objects.requireNonNull(lowerBound);
        this.upperBound = Objects.requireNonNull(upperBound);
    }

    @JsonIgnore
    public JsonNode getLowerBound() {
        return this.lowerBound;
    }

    @JsonIgnore
    public JsonNode getUpperBound() {
        return this.upperBound;
    }

    //-------------
    // Test methods
    //-------------

    @JsonIgnore
    public void setLowerBound(JsonNode lowerBound) {
        this.lowerBound = lowerBound;
    }

    @JsonIgnore
    public void setUpperBound(JsonNode upperBound) {
        this.upperBound = upperBound;
    }

    @Override
    public String toString() {
        return "DomainRange{" +
                "lowerBound=" + lowerBound.asText() +
                ", upperBound=" + upperBound.asText() +
                "}";
    }
}
