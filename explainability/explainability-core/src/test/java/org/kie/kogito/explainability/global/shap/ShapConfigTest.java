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

package org.kie.kogito.explainability.global.shap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ShapConfigTest {

    // Test that everything recovers as expected
    @Test
    void testRecovery() {
        ShapConfig skConfig = new ShapConfig(ShapConfig.LinkType.IDENTITY, 100);
        assertEquals(ShapConfig.LinkType.IDENTITY, skConfig.getLink());
        assertEquals(100, skConfig.getnSamples());
    }

    @Test
    void testNullRecovery() {
        ShapConfig skConfig = new ShapConfig(ShapConfig.LinkType.LOGIT);
        assertEquals(ShapConfig.LinkType.LOGIT, skConfig.getLink());
        assertNull(skConfig.getnSamples());
    }
}
