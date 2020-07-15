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
package org.kie.kogito.explainability.model.dmn;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.Prediction;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DMNUtilsTest {

    @Test
    void testEmptyTypedDataConversion() {
        List<TypedData> inputs = new LinkedList<>();
        List<TypedData> outputs = new LinkedList<>();
        Prediction convertedPrediction = DMNUtils.convert(inputs, outputs);
        assertNotNull(convertedPrediction);
    }

    @Test
    void testEmptyFeaturesConversion() {
        List<TypedData> inputs = new LinkedList<>();
        List<Feature> features = DMNUtils.extractInputFeatures(inputs);
        assertNotNull(features);
    }

    @Test
    void testEmptyOutputConversion() {
        List<TypedData> outputs = new LinkedList<>();
        List<Output> extractedOutputs = DMNUtils.extractOutputs(outputs);
        assertNotNull(extractedOutputs);
    }

    @Test
    void testEmptyFlatInputConversion() {
        TypedData input = new TypedData();
        List<Feature> features = DMNUtils.getFlatBuiltInInput(input);
        assertNotNull(features);
    }

    @Test
    void testEmptyFlatOutputConversion() {
        TypedData output = new TypedData();
        List<Output> extractedOutputs = DMNUtils.getFlatBuiltInOutputs(output);
        assertNotNull(extractedOutputs);
    }
}