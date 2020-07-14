package org.kie.kogito.explainability.local.lime;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.TestUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SampleWeighterTest {

    @Test
    void testSamplingEmptyDataset() {
        Collection<Pair<double[], Double>> trainingSet = new LinkedList<>();
        List<Feature> features = new LinkedList<>();
        PredictionInput targetInput = new PredictionInput(features);
        SampleWeighter.getSampleWeights(targetInput, trainingSet);
    }

    @Test
    void testSamplingNonEmptyDataset() {
        Collection<Pair<double[], Double>> trainingSet = new LinkedList<>();
        List<Feature> features = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            features.add(TestUtils.getRandomFeature());
        }
        // create a dataset whose samples values decrease as the dataset grows (starting from 1)
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            Pair<double[], Double> doubles = new Pair<>() {
                @Override
                public double[] getLeft() {
                    double[] vector = new double[features.size()];
                    Arrays.fill(vector, 1d / (1d + finalI));
                    return vector;
                }

                @Override
                public Double getRight() {
                    return 0d;
                }

                @Override
                public Double setValue(Double aDouble) {
                    return 0d;
                }
            };
            trainingSet.add(doubles);
        }
        PredictionInput targetInput = new PredictionInput(features);
        double[] weights = SampleWeighter.getSampleWeights(targetInput, trainingSet);
        // check that weights decrease with the distance from the 1 vector (the target instance)
        for (int i = 0; i < weights.length - 1; i++) {
            assertTrue(weights[i] > weights[i + 1]);
        }
    }
}