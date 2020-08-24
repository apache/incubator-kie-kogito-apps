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

package org.kie.kogito.trusty.storage.api.model;

import java.util.Map;

import org.kie.kogito.explainability.api.ExplainabilityResultDto;

public class ExplainabilityResult {

    private String executionId;

    // TODO: implement this in a better way
    private Map<String, Map<String, Double>> saliency;

    public ExplainabilityResult() {
    }

    public ExplainabilityResult(String executionId, Map<String, Map<String, Double>> saliency) {
        this.executionId = executionId;
        this.saliency = saliency;
    }

    public static ExplainabilityResult from(ExplainabilityResultDto input) {
        return new ExplainabilityResult(input.getExecutionId(), input.getSaliency());
    }

    public String getExecutionId() {
        return this.executionId;
    }

    public Map<String, Map<String, Double>> getSaliency() {
        return saliency;
    }
}
