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
package org.kie.kogito.explainability.local.lime.optim;

import java.util.List;
import java.util.Random;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.Config;
import org.kie.kogito.explainability.TestUtils;
import org.kie.kogito.explainability.local.lime.LimeConfig;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import org.kie.kogito.explainability.model.DataDistribution;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.model.SimplePrediction;
import org.kie.kogito.explainability.utils.DataUtils;
import org.kie.kogito.explainability.utils.ValidationUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LimeConfigOptimizerTest {

    @Test
    void testStabilityOptimization() throws Exception {
        PredictionProvider model = TestUtils.getSumSkipModel(1);
        Random random = new Random();
        random.setSeed(4);
        DataDistribution dataDistribution = DataUtils.generateRandomDataDistribution(5, 100, random);
        List<PredictionInput> samples = dataDistribution.sample(10);
        List<PredictionOutput> predictionOutputs = model.predictAsync(samples).get();
        List<Prediction> predictions = DataUtils.getPredictions(samples, predictionOutputs);
        LimeConfigOptimizer limeConfigOptimizer = new LimeConfigOptimizer();
        LimeConfig initialConfig = new LimeConfig().withSamples(10);
        PredictionInput sample = dataDistribution.sample();
        PredictionOutput output = model.predictAsync(List.of(sample)).get(Config.DEFAULT_ASYNC_TIMEOUT,
                Config.DEFAULT_ASYNC_TIMEUNIT).get(0);
        Prediction prediction = new SimplePrediction(sample, output);
        LimeConfig optimizedConfig = limeConfigOptimizer.optimize(initialConfig, predictions, model);
        assertThat(optimizedConfig).isNotNull();
        Assertions.assertThat(optimizedConfig).isNotSameAs(initialConfig);
        ValidationUtils.validateLocalSaliencyStability(model, prediction, new LimeExplainer(optimizedConfig), 2, 0.8, 0.8);
    }

}