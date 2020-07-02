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

import java.util.List;

/**
 * A decision.
 */
public class Decision extends Execution {

    private List<TypedValue> inputs;
    private List<DecisionOutcome> outcomes;

    public Decision() {
    }

    public Decision(String executionId, Long executionTimestamp, boolean hasSucceeded, String executorName, String executedModelName, List<TypedValue> inputs, List<DecisionOutcome> outcomes) {
        super(executionId, executionTimestamp, hasSucceeded, executorName, executedModelName, ExecutionTypeEnum.DECISION);
        this.inputs = inputs;
        this.outcomes = outcomes;
    }

    public List<TypedValue> getInputs() {
        return inputs;
    }

    public void setInputs(List<TypedValue> inputs) {
        this.inputs = inputs;
    }

    public List<DecisionOutcome> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(List<DecisionOutcome> outcomes) {
        this.outcomes = outcomes;
    }
}
