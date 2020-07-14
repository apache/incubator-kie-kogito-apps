package org.kie.kogito.explainability.local.lime;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.TestUtils;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.Model;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.Saliency;
import org.kie.kogito.explainability.utils.DataUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LimeStabilityTest {

    private static SecureRandom random = new SecureRandom();

    @BeforeAll
    public static void setUpBefore() {
        DataUtils.seed(4);
        random.setSeed(4);
    }

    @Test
    public void testStabilityWithNumericData() {
        Model sumSkipModel = TestUtils.getSumSkipModel(0);
        List<Feature> featureList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            featureList.add(FeatureFactory.newNumericalFeature("f-" + i, random.nextFloat()));
        }
        PredictionInput input = new PredictionInput(featureList);
        List<PredictionOutput> predictionOutputs = sumSkipModel.predict(List.of(input));
        Prediction prediction = new Prediction(input, predictionOutputs.get(0));
        List<Saliency> saliencies = new LinkedList<>();
        LimeExplainer limeExplainer = new LimeExplainer(100, 1);
        for (int i = 0; i < 100; i++) {
            Saliency saliency = limeExplainer.explain(prediction, sumSkipModel);
            saliencies.add(saliency);
        }
        List<String> names = new LinkedList<>();
        saliencies.stream().map(s -> s.getPositiveFeatures(1)).forEach(f -> names.add(f.get(0).getFeature().getName()));
        Map<String, Long> frequencyMap = names.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        boolean topFeature = false;
        for (Map.Entry<String, Long> entry : frequencyMap.entrySet()) {
            topFeature = entry.getValue() >= 0.9;
        }
        assertTrue(topFeature);
    }

    @Test
    public void testStabilityWithTextData() {
        Model sumSkipModel = TestUtils.getDummyTextClassifier();
        List<Feature> featureList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            featureList.add(FeatureFactory.newTextFeature("f-" + i, TestUtils.randomString()));
        }
        PredictionInput input = new PredictionInput(featureList);
        List<PredictionOutput> predictionOutputs = sumSkipModel.predict(List.of(input));
        Prediction prediction = new Prediction(input, predictionOutputs.get(0));
        List<Saliency> saliencies = new LinkedList<>();
        LimeExplainer limeExplainer = new LimeExplainer(100, 1);
        for (int i = 0; i < 100; i++) {
            Saliency saliency = limeExplainer.explain(prediction, sumSkipModel);
            saliencies.add(saliency);
        }
        List<String> names = new LinkedList<>();
        saliencies.stream().map(s -> s.getPositiveFeatures(1)).forEach(f -> names.add(f.get(0).getFeature().getName()));
        Map<String, Long> frequencyMap = names.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        boolean topFeature = false;
        for (Map.Entry<String, Long> entry : frequencyMap.entrySet()) {
            topFeature = entry.getValue() >= 0.9;
        }
        assertTrue(topFeature);
    }
}
