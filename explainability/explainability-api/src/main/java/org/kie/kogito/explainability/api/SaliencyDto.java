/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaliencyDto {

    @JsonProperty("featureImportance")
    @NotNull(message = "featureImportance object must be provided.")
    private List<FeatureImportanceDto> featureImportance;

    private SaliencyDto() {
    }

    public SaliencyDto(@NotNull List<FeatureImportanceDto> featureImportance) {
        this.featureImportance = Objects.requireNonNull(featureImportance);
    }

    @JsonIgnore
    public List<FeatureImportanceDto> getFeatureImportance() {
        return featureImportance;
    }
}
