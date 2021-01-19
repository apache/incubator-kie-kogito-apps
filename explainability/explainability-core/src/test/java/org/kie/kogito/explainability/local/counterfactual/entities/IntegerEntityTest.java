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
package org.kie.kogito.explainability.local.counterfactual.entities;

import org.apache.commons.math3.analysis.function.Floor;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.model.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegerEntityTest {

    @Test
    void distanceUnscaled() {
        final Feature integerFeature = FeatureFactory.newNumericalFeature("feature-integer", 20);
        final FeatureDomain featureDomain = FeatureDomain.numerical(0, 100);
        IntegerEntity entity = (IntegerEntity) CounterfactualEntityFactory.from(integerFeature, false, featureDomain);
        entity.proposedValue = 40;
        assertEquals(20.0, entity.distance());
    }

    @Test
    void distanceScaled() {
        final Feature integerFeature = FeatureFactory.newNumericalFeature("feature-integer", 20);
        final FeatureDomain featureDomain = FeatureDomain.numerical(0, 100);
        final FeatureDistribution featureDistribution = new NumericFeatureDistribution(integerFeature, Arrays.stream((new NormalDistribution(25.0, 4.0)).sample(5000)).map(Math::floor).toArray());
        IntegerEntity entity = (IntegerEntity) CounterfactualEntityFactory.from(integerFeature, false, featureDomain, featureDistribution);
        entity.proposedValue = 40;
        final double distance = entity.distance();
        assertTrue(distance > 1.20 && distance < 1.30);

    }
}