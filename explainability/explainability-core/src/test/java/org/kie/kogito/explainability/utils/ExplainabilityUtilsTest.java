package org.kie.kogito.explainability.utils;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.Model;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.Saliency;
import org.kie.kogito.explainability.TestUtils;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExplainabilityUtilsTest {

    private static final SecureRandom random = new SecureRandom();

    @Test
    public void testExplainabilityNoExplanation() {
        double v = ExplainabilityUtils.quantifyExplainability(0, 0, 0);
        assertFalse(Double.isNaN(v));
        assertFalse(Double.isInfinite(v));
        assertEquals(0, v);
    }

    @Test
    public void testExplainabilityNoExplanationWithInteraction() {
        double v = ExplainabilityUtils.quantifyExplainability(0, 0, 1);
        assertFalse(Double.isNaN(v));
        assertFalse(Double.isInfinite(v));
        assertEquals(0, v);
    }

    @Test
    public void testExplainabilityRandomchunksNoInteraction() {
        double v = ExplainabilityUtils.quantifyExplainability(random.nextInt(), random.nextInt(), 1d /
                random.nextInt(100));
        assertFalse(Double.isNaN(v));
        assertFalse(Double.isInfinite(v));
        assertTrue(v >= 0 && v <= 1);
    }

    @Test
    public void testExplainabilityRandom() {
        double v = ExplainabilityUtils.quantifyExplainability(random.nextInt(), random.nextInt(), random.nextDouble());
        assertFalse(Double.isNaN(v));
        assertFalse(Double.isInfinite(v));
        assertTrue(v >= 0 && v <= 1);
    }

    @Test
    public void testFidelity() {
        List<Pair<Saliency, Prediction>> pairs = new LinkedList<>();
        LimeExplainer limeExplainer = new LimeExplainer(100, 1);
        Model model = TestUtils.getDummyTextClassifier();
        for (int i = 0; i < 10; i++) {
            List<Feature> features = new LinkedList<>();
            for (int j = 0; j < 4; j++) {
                features.add(TestUtils.getRandomFeature());
            }
            PredictionInput input = new PredictionInput(features);
            Prediction prediction = new Prediction(input, model.predict(List.of(input)).get(0));
            pairs.add(Pair.of(limeExplainer.explain(prediction, model), prediction));
        }
        double v = ExplainabilityUtils.classificationFidelity(pairs);
        assertTrue(v > 0);
    }
}