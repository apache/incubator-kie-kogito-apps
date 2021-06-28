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

package org.kie.kogito.explainability.local.shap;

import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShapConfigTest {
    Random rn = new Random();
    List<Feature> fs = Arrays.asList(
            FeatureFactory.newNumericalFeature("f", 1.),
            FeatureFactory.newNumericalFeature("f", 2.));
    PredictionInput pi = new PredictionInput(fs);
    List<PredictionInput> pis = Arrays.asList(pi, pi);

    // Test that everything recovers as expected
    @Test
    void testRecovery() {
        ShapConfig skConfig = new ShapConfig(ShapConfig.LinkType.IDENTITY, pis, rn, 100);
        assertEquals(ShapConfig.LinkType.IDENTITY, skConfig.getLink());
        assertTrue(skConfig.getNSamples().isPresent());
        assertEquals(100, skConfig.getNSamples().get());
        assertSame(rn, skConfig.getRN());
        assertSame(pis, skConfig.getBackground());
    }

    @Test
    void testNullRecovery() {
        ShapConfig skConfig = new ShapConfig(ShapConfig.LinkType.LOGIT, pis, rn);
        assertEquals(ShapConfig.LinkType.LOGIT, skConfig.getLink());
        assertFalse(skConfig.getNSamples().isPresent());
        assertSame(rn, skConfig.getRN());
        assertSame(pis, skConfig.getBackground());
    }
}
