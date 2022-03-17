/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.explainability.utils;

import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class OneHotterTest
{
    @Test
    public void CollisionTest() {
        List<Value> fruits = List.of(
                new Value("avocado_"),
                new Value("banana"),
                new Value("carrot"),
                new Value("dragonfruit"),
                new Value(""));
        Random rn = new Random(101);

        List<PredictionInput> data = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            List<Feature> fs = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                fs.add(new Feature(String.format("Fruit _OHE_ %d", j), Type.CATEGORICAL, fruits.get(rn.nextInt(5))));
            }
            data.add(new PredictionInput(fs));
        }
        OneHotter oh = new OneHotter(data);
        System.out.println(oh.oneHotEncode(data, true));
    }
}