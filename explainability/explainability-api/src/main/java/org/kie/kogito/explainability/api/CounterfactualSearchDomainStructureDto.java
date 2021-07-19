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

import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CounterfactualSearchDomainStructureDto extends CounterfactualSearchDomainDto {

    public static final String VALUE_FIELD = "value";

    @JsonProperty(VALUE_FIELD)
    @NotNull(message = "value object must be provided.")
    private Map<String, CounterfactualSearchDomainDto> value;

    public CounterfactualSearchDomainStructureDto() {
    }

    public CounterfactualSearchDomainStructureDto(@NotNull String type, @NotNull Map<String, CounterfactualSearchDomainDto> value) {
        super(Kind.STRUCTURE, type);
        this.value = Objects.requireNonNull(value);
    }

    @JsonIgnore
    public Map<String, CounterfactualSearchDomainDto> getValue() {
        return value;
    }
}
