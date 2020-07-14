package org.kie.kogito.explainability.local.lime;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.Model;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.Saliency;
import org.kie.kogito.explainability.utils.DataUtils;
import org.kie.kogito.explainability.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class LimeExplainerTest {

    private final static SecureRandom random = new SecureRandom();

    @BeforeAll
    public static void setUpBefore() {
        DataUtils.seed(4);
    }

    @Test
    void testEmptyPrediction() {
        LimeExplainer limeExplainer = new LimeExplainer(10, 1);
        PredictionOutput output = mock(PredictionOutput.class);
        PredictionInput input = mock(PredictionInput.class);
        Prediction prediction = new Prediction(input, output);
        Model model = mock(Model.class);
        Saliency saliency = limeExplainer.explain(prediction, model);
        assertNotNull(saliency);
    }

    @Test
    void testNonEmptyInput() {
        LimeExplainer limeExplainer = new LimeExplainer(10, 1);
        PredictionOutput output = mock(PredictionOutput.class);
        List<Feature> features = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            features.add(TestUtils.getRandomFeature());
        }
        PredictionInput input = new PredictionInput(features);
        Prediction prediction = new Prediction(input, output);
        Model model = mock(Model.class);
        Saliency saliency = limeExplainer.explain(prediction, model);
        assertNotNull(saliency);
    }

    @Test
    void testNonEmptyInputAndOutputWithTextClassifier() {
        LimeExplainer limeExplainer = new LimeExplainer(10, 1);
        List<Feature> features = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            features.add(TestUtils.getRandomFeature());
        }
        PredictionInput input = new PredictionInput(features);
        Model model = TestUtils.getDummyTextClassifier();
        Prediction prediction = new Prediction(input, model.predict(List.of(input)).get(0));
        Saliency saliency = limeExplainer.explain(prediction, model);
        assertNotNull(saliency);
    }
}