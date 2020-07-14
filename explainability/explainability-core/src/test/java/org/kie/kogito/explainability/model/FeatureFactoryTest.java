package org.kie.kogito.explainability.model;

import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FeatureFactoryTest {

    @Test
    void testTimeFeature() {
        String name = "some-name";
        LocalTime time = LocalTime.now();
        Feature feature = FeatureFactory.newTimeFeature(name, time);
        assertNotNull(feature);
        assertNotNull(feature.getName());
        assertNotNull(feature.getType());
        assertEquals(Type.TIME, feature.getType());
        assertNotNull(feature.getValue());
        assertEquals(time, feature.getValue().getUnderlyingObject());
    }

    @Test
    void testCategoricalFeature() {
        String name = "some-name";
        String category = "FIXED-CAT";
        Feature feature = FeatureFactory.newCategoricalFeature(name, category);
        assertNotNull(feature);
        assertNotNull(feature.getName());
        assertNotNull(feature.getType());
        assertEquals(Type.CATEGORICAL, feature.getType());
        assertNotNull(feature.getValue());
        assertEquals(category, feature.getValue().getUnderlyingObject());
    }

    @Test
    void testNumberFeature() {
        String name = "some-name";
        Number number = 0.1d;
        Feature feature = FeatureFactory.newNumericalFeature(name, number);
        assertNotNull(feature);
        assertNotNull(feature.getName());
        assertNotNull(feature.getType());
        assertEquals(Type.NUMBER, feature.getType());
        assertNotNull(feature.getValue());
        assertEquals(number, feature.getValue().getUnderlyingObject());
    }

    @Test
    void testBooleanFeature() {
        String name = "some-name";
        Feature feature = FeatureFactory.newBooleanFeature(name, false);
        assertNotNull(feature);
        assertNotNull(feature.getName());
        assertNotNull(feature.getType());
        assertEquals(Type.BOOLEAN, feature.getType());
        assertNotNull(feature.getValue());
        assertEquals(false, feature.getValue().getUnderlyingObject());
    }

    @Test
    void testCurrencyFeature() {
        String name = "some-name";
        Currency currency = Currency.getInstance(Locale.getDefault());
        Feature feature = FeatureFactory.newCurrencyFeature(name, currency);
        assertNotNull(feature);
        assertNotNull(feature.getName());
        assertNotNull(feature.getType());
        assertEquals(Type.CURRENCY, feature.getType());
        assertNotNull(feature.getValue());
        assertEquals(currency, feature.getValue().getUnderlyingObject());
    }

    @Test
    void testBinaryFeature() {
        String name = "some-name";
        ByteBuffer binary = ByteBuffer.allocate(256);
        Feature feature = FeatureFactory.newBinaryFeature(name, binary);
        assertNotNull(feature);
        assertNotNull(feature.getName());
        assertNotNull(feature.getType());
        assertEquals(Type.BINARY, feature.getType());
        assertNotNull(feature.getValue());
        assertEquals(binary, feature.getValue().getUnderlyingObject());
    }

    @Test
    void testURIFeature() {
        String name = "some-name";
        URI uri = URI.create("./");
        Feature feature = FeatureFactory.newURIFeature(name, uri);
        assertNotNull(feature);
        assertNotNull(feature.getName());
        assertNotNull(feature.getType());
        assertEquals(Type.URI, feature.getType());
        assertNotNull(feature.getValue());
        assertEquals(uri, feature.getValue().getUnderlyingObject());
    }

    @Test
    void testDurationFeature() {
        String name = "some-name";
        Duration duration = Duration.ofDays(1);
        Feature feature = FeatureFactory.newDurationFeature(name, duration);
        assertNotNull(feature);
        assertNotNull(feature.getName());
        assertNotNull(feature.getType());
        assertEquals(Type.DURATION, feature.getType());
        assertNotNull(feature.getValue());
        assertEquals(duration, feature.getValue().getUnderlyingObject());
    }

    @Test
    void testTextFeature() {
        String name = "some-name";
        String text = "some text value";
        Feature feature = FeatureFactory.newTextFeature(name, text);
        assertNotNull(feature);
        assertNotNull(feature.getName());
        assertNotNull(feature.getType());
        assertEquals(Type.TEXT, feature.getType());
        assertNotNull(feature.getValue());
        assertEquals(text, feature.getValue().getUnderlyingObject());
    }

    @Test
    void testVectorFeature() {
        String name = "some-name";
        double[] vector = new double[10];
        Arrays.fill(vector, 1d);
        Feature feature = FeatureFactory.newVectorFeature(name, vector);
        assertNotNull(feature);
        assertNotNull(feature.getName());
        assertNotNull(feature.getType());
        assertEquals(Type.VECTOR, feature.getType());
        assertNotNull(feature.getValue());
        assertEquals(vector, feature.getValue().getUnderlyingObject());
    }

    @Test
    void testObjectFeature() {
        String name = "some-name";
        Object object = new Object();
        Feature feature = FeatureFactory.newObjectFeature(name, object);
        assertNotNull(feature);
        assertNotNull(feature.getName());
        assertNotNull(feature.getType());
        assertEquals(Type.UNDEFINED, feature.getType());
        assertNotNull(feature.getValue());
        assertEquals(object, feature.getValue().getUnderlyingObject());
    }
}