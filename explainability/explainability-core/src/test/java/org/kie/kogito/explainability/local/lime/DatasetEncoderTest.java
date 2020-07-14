package org.kie.kogito.explainability.local.lime;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatasetEncoderTest {

    private final static SecureRandom random = new SecureRandom();

    @Test
    public void testEmptyDatasetEncoding() {
        List<PredictionInput> inputs = new LinkedList<>();
        List<Output> outputs = new LinkedList<>();
        List<Feature> features = new LinkedList<>();
        PredictionInput originalInput = new PredictionInput(features);
        Output originalOutput = new Output("foo", Type.NUMBER, new Value<>(1), 1d);
        DatasetEncoder datasetEncoder = new DatasetEncoder(inputs, outputs, originalInput, originalOutput);
        Collection<Pair<double[], Double>> trainingSet = datasetEncoder.getEncodedTrainingSet();
        assertNotNull(trainingSet);
        assertTrue(trainingSet.isEmpty());
    }

    @Test
    public void testDatasetEncodingWithNumericData() {
        List<PredictionInput> perturbedInputs = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            List<Feature> inputFeatures = new LinkedList<>();
            for (int j = 0; j < 3; j++) {
                inputFeatures.add(FeatureFactory.newNumericalFeature("f" + random.nextInt(), random.nextInt()));
            }
            perturbedInputs.add(new PredictionInput(inputFeatures));
        }
        List<Output> outputs = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            outputs.add(new Output("o", Type.NUMBER, new Value<>(random.nextBoolean()), 1d));
        }
        List<Feature> features = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            features.add(FeatureFactory.newNumericalFeature("f" + random.nextInt(), random.nextInt()));
        }
        PredictionInput originalInput = new PredictionInput(features);
        Output originalOutput = new Output("o", Type.BOOLEAN, new Value<>(random.nextBoolean()), 1d);
        DatasetEncoder datasetEncoder = new DatasetEncoder(perturbedInputs, outputs, originalInput, originalOutput);
        Collection<Pair<double[], Double>> trainingSet = datasetEncoder.getEncodedTrainingSet();
        assertNotNull(trainingSet);
        assertEquals(10, trainingSet.size());
    }
}