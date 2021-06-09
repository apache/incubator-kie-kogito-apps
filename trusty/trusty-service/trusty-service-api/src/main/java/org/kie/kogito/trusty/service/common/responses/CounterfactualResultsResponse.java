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
package org.kie.kogito.trusty.service.common.responses;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.kie.kogito.trusty.storage.api.model.CounterfactualExplainabilityRequest;
import org.kie.kogito.trusty.storage.api.model.CounterfactualExplainabilityResult;
import org.kie.kogito.trusty.storage.api.model.CounterfactualSearchDomain;
import org.kie.kogito.trusty.storage.api.model.TypedVariableWithValue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CounterfactualResultsResponse extends CounterfactualExplainabilityRequest {

    public static final String SOLUTIONS_FIELD = "solutions";

    @JsonProperty(SOLUTIONS_FIELD)
    @NotNull(message = "solutions object must be provided.")
    private List<CounterfactualExplainabilityResult> solutions;

    public CounterfactualResultsResponse() {
    }

    public CounterfactualResultsResponse(@NotNull String executionId,
            @NotNull String counterfactualId,
            @NotNull Collection<TypedVariableWithValue> goals,
            @NotNull Collection<CounterfactualSearchDomain> searchDomains,
            @NotNull List<CounterfactualExplainabilityResult> solutions) {
        super(executionId, counterfactualId, goals, searchDomains);
        this.solutions = Objects.requireNonNull(solutions);
    }

    public List<CounterfactualExplainabilityResult> getSolutions() {
        return solutions;
    }
}
