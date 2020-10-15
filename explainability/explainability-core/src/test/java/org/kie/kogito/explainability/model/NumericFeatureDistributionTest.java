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
package org.kie.kogito.explainability.model;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.TestUtils;
import org.kie.kogito.explainability.utils.DataUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NumericFeatureDistributionTest {

    @Test
    void testSample() {
        Feature feature = TestUtils.getMockedNumericFeature();
        double[] doubles = DataUtils.generateSamples(0, 10, 10);
        NumericFeatureDistribution numericFeatureDistribution = new NumericFeatureDistribution(feature, doubles);
        Value<?> sample = numericFeatureDistribution.sample();
        assertNotNull(sample);
        assertNotNull(sample.getUnderlyingObject());
        assertThat(sample.asNumber()).isBetween(0d, 10d);
    }

    @Test
    void testSamples() {
        Feature feature = TestUtils.getMockedNumericFeature();
        double[] doubles = DataUtils.generateSamples(0, 10, 10);
        NumericFeatureDistribution numericFeatureDistribution = new NumericFeatureDistribution(feature, doubles);
        List<Value<?>> samples = numericFeatureDistribution.sample(3);
        assertNotNull(samples);
        assertEquals(3, samples.size());
        for (Value<?> sample : samples) {
            assertNotNull(sample);
            assertNotNull(sample.getUnderlyingObject());
            assertThat(sample.asNumber()).isBetween(0d, 10d);
        }
    }

    @Test
    void testGetAllSamples() {
        Feature feature = TestUtils.getMockedNumericFeature();
        double[] doubles = DataUtils.generateSamples(0, 10, 10);
        NumericFeatureDistribution numericFeatureDistribution = new NumericFeatureDistribution(feature, doubles);
        List<Value<?>> samples = numericFeatureDistribution.getAllSamples();
        assertNotNull(samples);
        assertEquals(10, samples.size());
        for (Value<?> sample : samples) {
            assertNotNull(sample);
            assertNotNull(sample.getUnderlyingObject());
            assertThat(sample.asNumber()).isBetween(0d, 10d);
        }
    }
}