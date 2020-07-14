package org.kie.kogito.explainability.local.lime;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.FeatureImportance;
import org.kie.kogito.explainability.model.Model;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.Saliency;
import org.kie.kogito.explainability.utils.DataUtils;
import org.kie.kogito.explainability.utils.ExplainabilityUtils;
import org.kie.kogito.explainability.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BiasedModelsLimeExplainerTest {

    @BeforeAll
    public static void setUpBefore() {
        DataUtils.seed(4);
    }

    @RepeatedTest(10)
    public void testMapOneFeatureToOutputRegression() {
        int idx = 1;
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newNumericalFeature("f1", 100));
        features.add(FeatureFactory.newNumericalFeature("f2", 20));
        features.add(FeatureFactory.newNumericalFeature("f3", 0.1));
        PredictionInput input = new PredictionInput(features);
        Model model = TestUtils.getFeaturePassModel(idx);
        List<PredictionOutput> outputs = model.predict(List.of(input));
        Prediction prediction = new Prediction(input, outputs.get(0));

        LimeExplainer limeExplainer = new LimeExplainer(100, 1);
        Saliency saliency = limeExplainer.explain(prediction, model);

        assertNotNull(saliency);
        List<FeatureImportance> topFeatures = saliency.getTopFeatures(3);
        assertEquals(topFeatures.get(0).getFeature().getName(), features.get(idx).getName());
        assertTrue(topFeatures.get(1).getScore() < topFeatures.get(0).getScore() * 10);
        assertTrue(topFeatures.get(2).getScore() < topFeatures.get(0).getScore() * 10);
        double v = ExplainabilityUtils.saliencyImpact(model, prediction, saliency.getTopFeatures(1));
        assertTrue(v > 0);
    }

    @RepeatedTest(10)
    public void testUnusedFeatureRegression() {
        int idx = 2;
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newNumericalFeature("f1", 100));
        features.add(FeatureFactory.newNumericalFeature("f2", 20));
        features.add(FeatureFactory.newNumericalFeature("f3", 10));
        Model model = TestUtils.getSumSkipModel(idx);
        PredictionInput input = new PredictionInput(features);
        List<PredictionOutput> outputs = model.predict(List.of(input));
        Prediction prediction = new Prediction(input, outputs.get(0));
        LimeExplainer limeExplainer = new LimeExplainer(1000, 1);
        Saliency saliency = limeExplainer.explain(prediction, model);

        assertNotNull(saliency);
        List<FeatureImportance> perFeatureImportance = saliency.getPerFeatureImportance();

        perFeatureImportance.sort((t1, t2) -> (int) (t2.getScore() - t1.getScore()));
        assertTrue(perFeatureImportance.get(0).getScore() > 0);
        assertTrue(perFeatureImportance.get(1).getScore() > 0);
        assertEquals(features.get(idx).getName(), perFeatureImportance.get(2).getFeature().getName());
        double v = ExplainabilityUtils.saliencyImpact(model, prediction, saliency.getTopFeatures(1));
        assertTrue(v > 0);
    }

    @RepeatedTest(10)
    public void testMapOneFeatureToOutputClassification() {
        int idx = 1;
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newNumericalFeature("f1", 3));
        features.add(FeatureFactory.newNumericalFeature("f2", 2));
        features.add(FeatureFactory.newNumericalFeature("f3", 7));
        PredictionInput input = new PredictionInput(features);
        Model model = TestUtils.getEvenFeatureModel(idx);
        List<PredictionOutput> outputs = model.predict(List.of(input));
        Prediction prediction = new Prediction(input, outputs.get(0));

        LimeExplainer limeExplainer = new LimeExplainer(1000, 1);
        Saliency saliency = limeExplainer.explain(prediction, model);

        assertNotNull(saliency);
        List<FeatureImportance> topFeatures = saliency.getTopFeatures(1);
        assertEquals(features.get(idx).getName(), topFeatures.get(0).getFeature().getName());
    }

    @RepeatedTest(10)
    public void testTextSpamClassification() {
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newTextFeature("f1","we go here and there"));
        features.add(FeatureFactory.newTextFeature("f2", "please give me some money"));
        features.add(FeatureFactory.newTextFeature("f3", "dear friend, please reply"));
        PredictionInput input = new PredictionInput(features);
        Model model = TestUtils.getDummyTextClassifier();
        List<PredictionOutput> outputs = model.predict(List.of(input));
        Prediction prediction = new Prediction(input, outputs.get(0));

        LimeExplainer limeExplainer = new LimeExplainer(1000, 1);
        Saliency saliency = limeExplainer.explain(prediction, model);

        assertNotNull(saliency);
        List<FeatureImportance> topFeatures = saliency.getPositiveFeatures(1);
        assertEquals("money (f2)", topFeatures.get(0).getFeature().getName());
        double v = ExplainabilityUtils.saliencyImpact(model, prediction, saliency.getTopFeatures(1));
        assertTrue(v > 0);
    }

    @RepeatedTest(10)
    public void testUnusedFeatureClassification() {
        int idx = 2;
        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newNumericalFeature("f1",6));
        features.add(FeatureFactory.newNumericalFeature("f2",3));
        features.add(FeatureFactory.newNumericalFeature("f3",5));
        Model model = TestUtils.getEvenSumModel(idx);
        PredictionInput input = new PredictionInput(features);
        List<PredictionOutput> outputs = model.predict(List.of(input));
        Prediction prediction = new Prediction(input, outputs.get(0));
        LimeExplainer limeExplainer = new LimeExplainer(100, 1);
        Saliency saliency = limeExplainer.explain(prediction, model);

        assertNotNull(saliency);
        List<FeatureImportance> perFeatureImportance = saliency.getNegativeFeatures(3);
        assertFalse(perFeatureImportance.stream().map(fi -> fi.getFeature().getName()).collect(Collectors.toList()).contains(features.get(idx).getName()));
        double v = ExplainabilityUtils.saliencyImpact(model, prediction, saliency.getNegativeFeatures(2));
        assertTrue(v >= 0);
    }


}