package org.kie.kogito.explainability.local.counterfactual;

import java.time.LocalTime;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CounterfactualScoreCalculatorTest {

    private static Output outputFromFeature(Feature feature) {
        return new Output(feature.getName(), feature.getType(), feature.getValue(), 0d);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3, 4 })
    void IntegerDistanceSameValue(int seed) {
        final Random random = new Random(seed);
        final int value = random.nextInt();
        Feature x = FeatureFactory.newNumericalFeature("x", value);
        Feature y = FeatureFactory.newNumericalFeature("y", value);

        Output ox = outputFromFeature(x);
        Output oy = outputFromFeature(y);

        final double distance = CounterFactualScoreCalculator.outputDistance(ox, oy);

        assertEquals(Type.NUMBER, ox.getType());
        assertEquals(0.0, distance);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3, 4 })
    void DoubleDistanceSameValue(int seed) {
        final Random random = new Random(seed);
        final double value = random.nextDouble();
        Feature x = FeatureFactory.newNumericalFeature("x", value);
        Feature y = FeatureFactory.newNumericalFeature("y", value);

        Output ox = outputFromFeature(x);
        Output oy = outputFromFeature(y);

        final double distance = CounterFactualScoreCalculator.outputDistance(ox, oy);

        assertEquals(Type.NUMBER, ox.getType());
        assertEquals(0.0, distance);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3, 4 })
    void BooleanDistanceSameValue(int seed) {
        final Random random = new Random(seed);
        final boolean value = random.nextBoolean();
        Feature x = FeatureFactory.newBooleanFeature("x", value);
        Feature y = FeatureFactory.newBooleanFeature("y", value);

        Output ox = outputFromFeature(x);
        Output oy = outputFromFeature(y);

        final double distance = CounterFactualScoreCalculator.outputDistance(ox, oy);

        assertEquals(Type.BOOLEAN, ox.getType());
        assertEquals(0.0, distance);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3, 4 })
    void CategoricalDistanceSameValue(int seed) {
        final String value = UUID.randomUUID().toString();
        Feature x = FeatureFactory.newCategoricalFeature("x", value);
        Feature y = FeatureFactory.newCategoricalFeature("y", value);

        Output ox = outputFromFeature(x);
        Output oy = outputFromFeature(y);

        final double distance = CounterFactualScoreCalculator.outputDistance(ox, oy);

        assertEquals(Type.CATEGORICAL, ox.getType());
        assertEquals(0.0, distance);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3, 4 })
    void IntegerDistanceDifferentValue(int seed) {
        final Random random = new Random(seed);
        int value = random.nextInt();
        Feature x = FeatureFactory.newNumericalFeature("x", value);
        Feature y = FeatureFactory.newNumericalFeature("y", value + 100);

        Output ox = outputFromFeature(x);
        Output oy = outputFromFeature(y);

        double distance = CounterFactualScoreCalculator.outputDistance(ox, oy);

        assertEquals(Type.NUMBER, ox.getType());
        assertEquals(Type.NUMBER, oy.getType());
        assertTrue(distance * distance > 0);

        y = FeatureFactory.newNumericalFeature("y", value - 100);
        oy = outputFromFeature(y);
        distance = CounterFactualScoreCalculator.outputDistance(ox, oy);

        assertTrue(distance * distance > 0);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3, 4 })
    void DoubleDistanceDifferentValue(int seed) {
        final Random random = new Random(seed);
        double value = random.nextDouble() * 100.0;
        Feature x = FeatureFactory.newNumericalFeature("x", value);
        Feature y = FeatureFactory.newNumericalFeature("y", value + 100.0);

        Output ox = outputFromFeature(x);
        Output oy = outputFromFeature(y);

        double distance = CounterFactualScoreCalculator.outputDistance(ox, oy);

        assertEquals(Type.NUMBER, ox.getType());
        assertEquals(Type.NUMBER, oy.getType());
        assertTrue(distance * distance > 0);

        y = FeatureFactory.newNumericalFeature("y", value - 100);
        oy = outputFromFeature(y);
        distance = CounterFactualScoreCalculator.outputDistance(ox, oy);

        assertTrue(distance * distance > 0);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3, 4 })
    void BooleanDistanceDifferentValue(int seed) {
        final Random random = new Random(seed);
        boolean value = random.nextBoolean();
        Feature x = FeatureFactory.newBooleanFeature("x", value);
        Feature y = FeatureFactory.newBooleanFeature("y", !value);

        Output ox = outputFromFeature(x);
        Output oy = outputFromFeature(y);

        double distance = CounterFactualScoreCalculator.outputDistance(ox, oy);

        assertEquals(Type.BOOLEAN, ox.getType());
        assertEquals(Type.BOOLEAN, oy.getType());
        assertEquals(1.0, distance);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3, 4 })
    void CategoricalDistanceDifferentValue(int seed) {
        final Random random = new Random(seed);
        Feature x = FeatureFactory.newCategoricalFeature("x", UUID.randomUUID().toString());
        Feature y = FeatureFactory.newCategoricalFeature("y", UUID.randomUUID().toString());

        Output ox = outputFromFeature(x);
        Output oy = outputFromFeature(y);

        double distance = CounterFactualScoreCalculator.outputDistance(ox, oy);

        assertEquals(Type.CATEGORICAL, ox.getType());
        assertEquals(Type.CATEGORICAL, oy.getType());
        assertEquals(1.0, distance);
    }

    @Test
    void differentFeatureTypes() {
        Feature x = FeatureFactory.newCategoricalFeature("x", UUID.randomUUID().toString());
        Feature y = FeatureFactory.newNumericalFeature("y", 0.0);

        Output ox = outputFromFeature(x);
        Output oy = outputFromFeature(y);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CounterFactualScoreCalculator.outputDistance(ox, oy);
        });

        assertEquals("Features must have the same type, got categorical and number", exception.getMessage());
    }

    @Test
    void unsupportedFeatureType() {
        Feature x = FeatureFactory.newTimeFeature("x", LocalTime.now());
        Feature y = FeatureFactory.newTimeFeature("y", LocalTime.now());

        Output ox = outputFromFeature(x);
        Output oy = outputFromFeature(y);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CounterFactualScoreCalculator.outputDistance(ox, oy);
        });

        assertEquals("Feature type not supported", exception.getMessage());
    }

}
