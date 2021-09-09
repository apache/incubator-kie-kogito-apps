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
package org.kie.kogito.explainability.local.lime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.TestUtils;
import org.kie.kogito.explainability.model.DataDistribution;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureDistribution;
import org.kie.kogito.explainability.model.IndependentFeaturesDataDistribution;
import org.kie.kogito.explainability.model.PredictionProvider;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HighScoreNumericFeatureZonesProviderTest {

    @Test
    void testEmptyData() {
        List<Feature> features = new ArrayList<>();
        PredictionProvider predictionProvider = TestUtils.getSumThresholdModel(0.1, 0.1);
        List<FeatureDistribution> featureDistributions = new ArrayList<>();
        DataDistribution dataDistribution = new IndependentFeaturesDataDistribution(featureDistributions);
        Map<String, HighScoreNumericFeatureZones> highScoreFeatureZones =
                HighScoreNumericFeatureZonesProvider.getHighScoreFeatureZones(dataDistribution, predictionProvider, features);
        assertThat(highScoreFeatureZones).isNotNull();
        assertThat(highScoreFeatureZones.size()).isEqualTo(0);
    }

}