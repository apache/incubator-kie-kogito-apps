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

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.model.DataDistribution;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.BlackBoxModel;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.Saliency;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;
import org.kie.kogito.explainability.utils.DataUtils;
import org.kie.kogito.explainability.utils.ExplainabilityMetrics;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.kie.api.pmml.PMML4Result;
import org.kie.pmml.evaluator.api.executor.PMMLRuntime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.kie.kogito.explainability.explainability.integrationtests.pmml.AbstractPMMLTest.getPMMLRuntime;

class PmmlLimeExplainerTest {

    private static PMMLRuntime logisticRegressionIris;
    private static PMMLRuntime categoricalVariableRegression;
    private static PMMLRuntime scorecardCategorical;
    private static PMMLRuntime compoundScoreCard;

    @BeforeAll
    static void setUpBefore() {
        DataUtils.seed(4);
        logisticRegressionIris = getPMMLRuntime("LogisticRegressionIrisData");
        categoricalVariableRegression = getPMMLRuntime("categoricalVariables_Model");
        scorecardCategorical = getPMMLRuntime("SimpleScorecardCategorical");
        compoundScoreCard = getPMMLRuntime("CompoundNestedPredicateScorecard");
    }

    @Test
    void testPMMLRegression() throws Exception {
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newNumericalFeature("sepalLength", 6.9));
        features.add(FeatureFactory.newNumericalFeature("sepalWidth", 3.1));
        features.add(FeatureFactory.newNumericalFeature("petalLength", 5.1));
        features.add(FeatureFactory.newNumericalFeature("petalWidth", 2.3));
        PredictionInput input = new PredictionInput(features);

        LimeExplainer limeExplainer = new LimeExplainer(100, 2);
        BlackBoxModel model = new BlackBoxModel() {
            @Override
            public List<PredictionOutput> predict(List<PredictionInput> inputs) {
                List<PredictionOutput> outputs = new LinkedList<>();
                for (PredictionInput input : inputs) {
                    List<Feature> features = input.getFeatures();
                    LogisticRegressionIrisDataExecutor pmmlModel = new LogisticRegressionIrisDataExecutor(
                            features.get(0).getValue().asNumber(), features.get(1).getValue().asNumber(),
                            features.get(2).getValue().asNumber(), features.get(3).getValue().asNumber());
                    PMML4Result result = pmmlModel.execute(logisticRegressionIris);
                    String species = result.getResultVariables().get("Species").toString();
                    PredictionOutput predictionOutput = new PredictionOutput(List.of(new Output("species", Type.TEXT, new Value<>(species), 1d)));
                    outputs.add(predictionOutput);
                }
                return outputs;
            }

            @Override
            public DataDistribution getDataDistribution() {
                return null;
            }

            @Override
            public PredictionInput getInputShape() {
                return null;
            }

            @Override
            public PredictionOutput getOutputShape() {
                return null;
            }
        };
        PredictionOutput output = model.predict(List.of(input)).get(0);
        Prediction prediction = new Prediction(input, output);
        Saliency saliency = limeExplainer.explain(prediction, model);
        assertNotNull(saliency);
        List<String> strings = saliency.getPositiveFeatures(2).stream().map(f -> f.getFeature().getName()).collect(Collectors.toList());
        assertTrue(strings.contains("petalWidth"));
        double v = ExplainabilityMetrics.saliencyImpact(model, prediction, saliency.getTopFeatures(1));
        assertTrue(v > 0);
    }

    @Disabled
    void testPMMLRegressionCategorical() throws Exception {
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newTextFeature("mapX", "red"));
        features.add(FeatureFactory.newTextFeature("mapY", "classB"));
        PredictionInput input = new PredictionInput(features);

        LimeExplainer limeExplainer = new LimeExplainer(10, 1);
        BlackBoxModel model = new BlackBoxModel() {
            @Override
            public List<PredictionOutput> predict(List<PredictionInput> inputs) {
                List<PredictionOutput> outputs = new LinkedList<>();
                for (PredictionInput input : inputs) {
                    List<Feature> features = input.getFeatures();
                    CategoricalVariablesRegressionExecutor pmmlModel = new CategoricalVariablesRegressionExecutor(
                            features.get(0).getValue().asString(), features.get(1).getValue().asString());
                    PMML4Result result = pmmlModel.execute(categoricalVariableRegression);
                    String score = result.getResultVariables().get("result").toString();
                    PredictionOutput predictionOutput = new PredictionOutput(List.of(new Output("result", Type.NUMBER, new Value<>(score), 1d)));
                    outputs.add(predictionOutput);
                }
                return outputs;
            }

            @Override
            public DataDistribution getDataDistribution() {
                return null;
            }

            @Override
            public PredictionInput getInputShape() {
                return null;
            }

            @Override
            public PredictionOutput getOutputShape() {
                return null;
            }
        };
        PredictionOutput output = model.predict(List.of(input)).get(0);
        Prediction prediction = new Prediction(input, output);
        Saliency saliency = limeExplainer.explain(prediction, model);
        assertNotNull(saliency);
        List<String> strings = saliency.getPositiveFeatures(1).stream().map(f -> f.getFeature().getName()).collect(Collectors.toList());
        assertTrue(strings.contains("red (mapX)"));
    }

    @Disabled()
    void testPMMLScorecardCategorical() throws Exception {
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newTextFeature("input1", "classA"));
        features.add(FeatureFactory.newTextFeature("input2", "classB"));
        PredictionInput input = new PredictionInput(features);

        LimeExplainer limeExplainer = new LimeExplainer(10, 1);
        BlackBoxModel model = new BlackBoxModel() {
            @Override
            public List<PredictionOutput> predict(List<PredictionInput> inputs) {
                List<PredictionOutput> outputs = new LinkedList<>();
                for (PredictionInput input : inputs) {
                    List<Feature> features = input.getFeatures();
                    SimpleScorecardCategoricalExecutor pmmlModel = new SimpleScorecardCategoricalExecutor(
                            features.get(0).getValue().asString(), features.get(1).getValue().asString());
                    PMML4Result result = pmmlModel.execute(scorecardCategorical);
                    String score = "" + result.getResultVariables().get(SimpleScorecardCategoricalExecutor.TARGET_FIELD);
                    String reason1 = "" + result.getResultVariables().get(SimpleScorecardCategoricalExecutor.REASON_CODE1_FIELD);
                    String reason2 = "" + result.getResultVariables().get(SimpleScorecardCategoricalExecutor.REASON_CODE2_FIELD);
                    PredictionOutput predictionOutput = new PredictionOutput(List.of(
                            new Output("score", Type.TEXT, new Value<>(score), 1d),
                            new Output("reason1", Type.TEXT, new Value<>(reason1), 1d),
                            new Output("reason2", Type.TEXT, new Value<>(reason2), 1d)
                    ));
                    outputs.add(predictionOutput);
                }
                return outputs;
            }

            @Override
            public DataDistribution getDataDistribution() {
                return null;
            }

            @Override
            public PredictionInput getInputShape() {
                return null;
            }

            @Override
            public PredictionOutput getOutputShape() {
                return null;
            }
        };

        PredictionOutput output = model.predict(List.of(input)).get(0);
        Prediction prediction = new Prediction(input, output);
        Saliency saliency = limeExplainer.explain(prediction, model);
        assertNotNull(saliency);
        List<String> strings = saliency.getPositiveFeatures(1).stream().map(f -> f.getFeature().getName()).collect(Collectors.toList());
        assertTrue(strings.contains("classA (input1)"));
    }

    @Test
    void testPMMLCompoundScorecard() throws Exception {
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newNumericalFeature("input1", -50));
        features.add(FeatureFactory.newTextFeature("input2", "classB"));
        PredictionInput input = new PredictionInput(features);

        LimeExplainer limeExplainer = new LimeExplainer(100, 2);
        BlackBoxModel model = new BlackBoxModel() {
            @Override
            public List<PredictionOutput> predict(List<PredictionInput> inputs) {
                List<PredictionOutput> outputs = new LinkedList<>();
                for (PredictionInput input : inputs) {
                    List<Feature> features = input.getFeatures();
                    CompoundNestedPredicateScorecardExecutor pmmlModel = new CompoundNestedPredicateScorecardExecutor(
                            features.get(0).getValue().asNumber(), features.get(1).getValue().asString());
                    PMML4Result result = pmmlModel.execute(compoundScoreCard);
                    String score = "" + result.getResultVariables().get(CompoundNestedPredicateScorecardExecutor.TARGET_FIELD);
                    String reason1 = "" + result.getResultVariables().get(CompoundNestedPredicateScorecardExecutor.REASON_CODE1_FIELD);
                    PredictionOutput predictionOutput = new PredictionOutput(List.of(
                            new Output("score", Type.TEXT, new Value<>(score), 1d),
                            new Output("reason1", Type.TEXT, new Value<>(reason1), 1d)
                    ));
                    outputs.add(predictionOutput);
                }
                return outputs;
            }

            @Override
            public DataDistribution getDataDistribution() {
                return null;
            }

            @Override
            public PredictionInput getInputShape() {
                return null;
            }

            @Override
            public PredictionOutput getOutputShape() {
                return null;
            }
        };
        PredictionOutput output = model.predict(List.of(input)).get(0);
        Prediction prediction = new Prediction(input, output);
        Saliency saliency = limeExplainer.explain(prediction, model);
        assertNotNull(saliency);
        List<String> strings = saliency.getPositiveFeatures(1).stream().map(f -> f.getFeature().getName()).collect(Collectors.toList());
        assertTrue(strings.contains("classB (input2)"));
        double v = ExplainabilityMetrics.saliencyImpact(model, prediction, saliency.getTopFeatures(1));
        assertTrue(v > 0);
    }
}