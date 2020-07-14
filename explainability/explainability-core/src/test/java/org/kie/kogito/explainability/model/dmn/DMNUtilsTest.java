package org.kie.kogito.explainability.model.dmn;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.Prediction;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DMNUtilsTest {

    @Test
    void testEmptyTypedDataConversion() {
        List<TypedData> inputs = new LinkedList<>();
        List<TypedData> outputs = new LinkedList<>();
        Prediction convertedPrediction = DMNUtils.convert(inputs, outputs);
        assertNotNull(convertedPrediction);
    }

    @Test
    void testEmptyFeaturesConversion() {
        List<TypedData> inputs = new LinkedList<>();
        List<Feature> features = DMNUtils.extractInputFeatures(inputs);
        assertNotNull(features);
    }

    @Test
    void testEmptyOutputConversion() {
        List<TypedData> outputs = new LinkedList<>();
        List<Output> extractedOutputs = DMNUtils.extractOutputs(outputs);
        assertNotNull(extractedOutputs);
    }

    @Test
    void testEmptyFlatInputConversion() {
        TypedData input = new TypedData();
        List<Feature> features = DMNUtils.getFlatBuiltInInput(input);
        assertNotNull(features);
    }

    @Test
    void testEmptyFlatOutputConversion() {
        TypedData output = new TypedData();
        List<Output> extractedOutputs = DMNUtils.getFlatBuiltInOutputs(output);
        assertNotNull(extractedOutputs);
    }
}