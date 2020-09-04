/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.kogito.explainability.model;

import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Factory class for {@link Feature}s
 */
public class FeatureFactory {

    private FeatureFactory() {
    }

    public static Feature newTextFeature(String name, String text) {
        return new Feature(name, Type.TEXT, new Value<>(text));
    }

    public static Feature newFulltextFeature(String name, String text, Function<String, List<String>> tokenizer) {
        List<String> tokens = tokenizer.apply(text);
        List<Feature> tokenFeatures = new ArrayList<>(tokens.size());
        for (String token : tokens) {
            tokenFeatures.add(FeatureFactory.newTextFeature(name, token));
        }
        return FeatureFactory.newCompositeFeature(name, tokenFeatures);
    }

    public static Feature newFulltextFeature(String name, String text) {
        return FeatureFactory.newFulltextFeature(name, text, s -> Arrays.asList(s.split(" ")));
    }

    public static Feature newCategoricalFeature(String name, String category) {
        return new Feature(name, Type.CATEGORICAL, new Value<>(category));
    }

    public static Feature newNumericalFeature(String name, Number number) {
        return new Feature(name, Type.NUMBER, new Value<>(number));
    }

    public static Feature newBooleanFeature(String name, Boolean truthValue) {
        return new Feature(name, Type.BOOLEAN, new Value<>(truthValue));
    }

    public static Feature newCurrencyFeature(String name, Currency currency) {
        return new Feature(name, Type.CURRENCY, new Value<>(currency));
    }

    public static Feature newBinaryFeature(String name, ByteBuffer byteBuffer) {
        return new Feature(name, Type.BINARY, new Value<>(byteBuffer));
    }

    public static Feature newURIFeature(String name, URI uri) {
        return new Feature(name, Type.URI, new Value<>(uri));
    }

    public static Feature newDurationFeature(String name, Duration duration) {
        return new Feature(name, Type.DURATION, new Value<>(duration));
    }

    public static Feature newTimeFeature(String name, LocalTime time) {
        return new Feature(name, Type.TIME, new Value<>(time));
    }

    public static Feature newVectorFeature(String name, double... doubles) {
        return new Feature(name, Type.VECTOR, new Value<>(doubles));
    }

    public static Feature newObjectFeature(String name, Object object) {
        return new Feature(name, Type.UNDEFINED, new Value<>(object));
    }

    public static Feature newCompositeFeature(String name, Map<String, Object> map) {
        List<Feature> features = new LinkedList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            parseFeatureValue(features, entry);
        }
        return newCompositeFeature(name, features);
    }

    public static Feature newCompositeFeature(String name, List<Feature> features) {
        return new Feature(name, Type.COMPOSITE, new Value<>(features));
    }

    private static void parseFeatureValue(List<Feature> features, Map.Entry<String, Object> entry) {
        Object value = entry.getValue();
        String featureName = entry.getKey();
        Feature feature;
        if (value instanceof Map) {
            feature = newCompositeFeature(featureName, (Map<String, Object>) value);
        } else if (value instanceof double[]) {
            feature = newVectorFeature(featureName, (double[]) value);
        } else if (value instanceof LocalTime) {
            feature = newTimeFeature(featureName, (LocalTime) value);
        } else if (value instanceof Duration) {
            feature = newDurationFeature(featureName, (Duration) value);
        } else if (value instanceof URI) {
            feature = newURIFeature(featureName, (URI) value);
        } else if (value instanceof ByteBuffer) {
            feature = newBinaryFeature(featureName, (ByteBuffer) value);
        } else if (value instanceof Currency) {
            feature = newCurrencyFeature(featureName, (Currency) value);
        } else if (value instanceof Boolean) {
            feature = newBooleanFeature(featureName, (Boolean) value);
        } else if (value instanceof Number) {
            feature = newNumericalFeature(featureName, (Number) value);
        } else if (value instanceof String) {
            feature = newTextFeature(featureName, (String) value);
        } else if (value instanceof Feature) {
            feature = (Feature) value;
        } else if (value instanceof List) {
            List<?> items = (List<?>) value;
            if (!items.isEmpty()) {
                if (items.get(0) instanceof Feature) {
                    feature = newCompositeFeature(featureName, (List<Feature>) items);
                } else {
                    Map<String, Object> map = new HashMap<>();
                    int index = 0;
                    for (Object o : (List<?>) value) {
                        map.put(featureName + "_" + index, o);
                        index++;
                    }
                    List<Feature> fs = new ArrayList<>(map.size());
                    for (Map.Entry<String, Object> e : map.entrySet()) {
                        parseFeatureValue(fs, e);
                    }
                    feature = newCompositeFeature(featureName, fs);
                }
            } else {
                feature = newCompositeFeature(featureName, Collections.emptyList());
            }
        } else {
            feature = newObjectFeature(featureName, value);
        }
        features.add(feature);
    }

    /**
     * Create a copy of a {@code Feature} but with a different {@code Value}.
     *
     * @param feature the Feature to copy
     * @param value   the Value to inject
     * @return a copy of the input Feature but having the given Value
     */
    public static Feature copyOf(Feature feature, Value<?> value) {
        return new Feature(feature.getName(), feature.getType(), value);
    }
}
