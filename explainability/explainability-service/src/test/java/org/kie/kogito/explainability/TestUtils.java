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

package org.kie.kogito.explainability;

import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.kie.kogito.explainability.model.*;
import org.kie.kogito.explainability.models.ExplainabilityRequest;
import org.kie.kogito.explainability.models.ModelIdentifier;
import org.kie.kogito.tracing.typedvalue.TypedValue;
import org.kie.kogito.tracing.typedvalue.UnitValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.*;

public class TestUtils {

    private TestUtils() {
        // prevent initialization
    }

    public static final String executionId = "executionId";
    public static final String serviceUrl = "localhost:8080";

    public static final ModelIdentifier modelIdentifier = new ModelIdentifier("dmn", "name:namespace");

    public static final Value<Boolean> value = new Value<>(true);

    public static final FeatureImportance featureImportance1 = new FeatureImportance(new Feature("input1", Type.NUMBER, new Value<>(1)), 0.6);
    public static final FeatureImportance featureImportance2 = new FeatureImportance(new Feature("input2", Type.NUMBER, new Value<>(2)), 0.5);

    public static final List<FeatureImportance> featureImportances = asList(featureImportance1, featureImportance2);

    public static final Output output = new Output("key", Type.BOOLEAN, value, 1d);
    public static final Saliency saliency = new Saliency(output, featureImportances);

    public static final Map<String, Saliency> saliencyMap = singletonMap("key", saliency);

    public static final Map<String, TypedValue> inputs = new HashMap<>();
    static {
        inputs.put("input1", new UnitValue("string", new TextNode("value")));
        inputs.put("input2", new UnitValue("number", new DoubleNode(10)));
    }

    public static final Map<String, TypedValue> outputs = singletonMap("output1", new UnitValue("string", new TextNode("output")));

    public static final ExplainabilityRequest request = new ExplainabilityRequest(executionId, serviceUrl, modelIdentifier, inputs, outputs);

}