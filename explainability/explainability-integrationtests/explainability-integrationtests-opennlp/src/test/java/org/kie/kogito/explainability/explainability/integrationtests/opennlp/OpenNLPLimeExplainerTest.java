package org.kie.kogito.explainability.explainability.integrationtests.opennlp;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.kie.kogito.explainability.model.DataDistribution;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.Model;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.Saliency;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;
import org.kie.kogito.explainability.utils.DataUtils;
import org.kie.kogito.explainability.utils.ExplainabilityUtils;
import org.kie.kogito.explainability.local.lime.LimeExplainer;
import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetector;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpenNLPLimeExplainerTest {

    @BeforeAll
    public static void setUpBefore() {
        DataUtils.seed(4);
    }

    @RepeatedTest(10)
    public void testOpenNLPLangDetect() throws IOException {
        InputStream is = getClass().getResourceAsStream("/opennlp/langdetect-183.bin");
        LanguageDetectorModel languageDetectorModel = new LanguageDetectorModel(is);
        String inputText = "italiani, spaghetti pizza mandolino";
        LanguageDetector languageDetector = new LanguageDetectorME(languageDetectorModel);
        Language bestLanguage = languageDetector.predictLanguage(inputText);

        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newTextFeature("text", inputText));
        PredictionInput input = new PredictionInput(features);
        PredictionOutput output = new PredictionOutput(List.of(new Output("lang", Type.TEXT, new Value<>(bestLanguage.getLang()),
                                                                          bestLanguage.getConfidence())));
        Prediction prediction = new Prediction(input, output);

        LimeExplainer limeExplainer = new LimeExplainer(100, 2);
        Model model = new Model() {
            @Override
            public List<PredictionOutput> predict(List<PredictionInput> inputs) {
                List<PredictionOutput> results = new LinkedList<>();
                for (PredictionInput predictionInput : inputs) {
                    StringBuilder builder = new StringBuilder();
                    for (Feature f : predictionInput.getFeatures()) {
                        if (Type.TEXT.equals(f.getType())) {
                            if (builder.length() > 0) {
                                builder.append(' ');
                            }
                            builder.append(f.getValue().asString());
                        }
                    }
                    Language language = languageDetector.predictLanguage(builder.toString());
                    PredictionOutput predictionOutput = new PredictionOutput(List.of(new Output("lang", Type.TEXT, new Value<>(language.getLang()), language.getConfidence())));
                    results.add(predictionOutput);
                }
                return results;
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
        Saliency saliency = limeExplainer.explain(prediction, model);
        assertNotNull(saliency);
        double i1 = ExplainabilityUtils.saliencyImpact(model, prediction, saliency.getTopFeatures(1));
        assertTrue(i1 > 0);
    }
}