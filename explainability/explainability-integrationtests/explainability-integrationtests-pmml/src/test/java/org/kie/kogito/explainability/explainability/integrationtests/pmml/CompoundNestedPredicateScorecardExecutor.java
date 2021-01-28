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
package org.kie.kogito.explainability.explainability.integrationtests.pmml;

import java.util.HashMap;
import java.util.Map;

import org.kie.api.pmml.PMML4Result;
import org.kie.pmml.api.runtime.PMMLRuntime;

public class CompoundNestedPredicateScorecardExecutor extends AbstractPMMLExecutor {

    public static final String TARGET_FIELD = "Score";
    public static final String REASON_CODE1_FIELD = "Reason Code 1";
    public static final String REASON_CODE2_FIELD = "Reason Code 2";
    private static final String MODEL_NAME = "CompoundNestedPredicateScorecard";
    private final double input1;
    private final String input2;

    public CompoundNestedPredicateScorecardExecutor(double input1, String input2) {
        this.input1 = input1;
        this.input2 = input2;
    }

    public PMML4Result execute(final PMMLRuntime pmmlRuntime) {
        final Map<String, Object> inputData = new HashMap<>();
        inputData.put("input1", input1);
        inputData.put("input2", input2);
        return evaluate(pmmlRuntime, inputData, MODEL_NAME);
    }
}
