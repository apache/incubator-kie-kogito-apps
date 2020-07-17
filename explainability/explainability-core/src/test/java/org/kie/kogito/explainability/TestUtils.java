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
package org.kie.kogito.explainability;

import java.net.URI;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.kie.kogito.explainability.model.DataDistribution;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.FeatureFactory;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;
import org.kie.kogito.explainability.utils.DataUtils;
import org.apache.commons.lang3.RandomStringUtils;

import static org.junit.jupiter.api.Assertions.fail;

public class TestUtils {

    private final static SecureRandom random = new SecureRandom();

    static {
        random.setSeed(4);
    }

    public static PredictionProvider getFeaturePassModel(int featureIndex) {
        return inputs -> {
            List<PredictionOutput> predictionOutputs = new LinkedList<>();
            for (PredictionInput predictionInput : inputs) {
                List<Feature> features = predictionInput.getFeatures();
                Feature feature = features.get(featureIndex);
                PredictionOutput predictionOutput = new PredictionOutput(
                        List.of(new Output("feature-" + featureIndex, feature.getType(), feature.getValue(),
                                           1d)));
                predictionOutputs.add(predictionOutput);
            }
            return predictionOutputs;
        };
    }

    public static PredictionProvider getSumSkipModel(int skipFeatureIndex) {
        return inputs -> {
            List<PredictionOutput> predictionOutputs = new LinkedList<>();
            for (PredictionInput predictionInput : inputs) {
                List<Feature> features = predictionInput.getFeatures();
                double result = 0;
                for (int i = 0; i < features.size(); i++) {
                    if (skipFeatureIndex != i) {
                        result += features.get(i).getValue().asNumber();
                    }
                }
                PredictionOutput predictionOutput = new PredictionOutput(
                        List.of(new Output("sum-but" + skipFeatureIndex, Type.NUMBER, new Value<>(result), 1d)));
                predictionOutputs.add(predictionOutput);
            }
            return predictionOutputs;
        };
    }

    public static PredictionProvider getEvenFeatureModel(int featureIndex) {
        return inputs -> {
            List<PredictionOutput> predictionOutputs = new LinkedList<>();
            for (PredictionInput predictionInput : inputs) {
                List<Feature> features = predictionInput.getFeatures();
                Feature feature = features.get(featureIndex);
                double v = feature.getValue().asNumber();
                PredictionOutput predictionOutput = new PredictionOutput(
                        List.of(new Output("feature-" + featureIndex, Type.BOOLEAN, new Value<>(v % 2 == 0), 1d)));
                predictionOutputs.add(predictionOutput);
            }
            return predictionOutputs;
        };
    }

    public static PredictionProvider getEvenSumModel(int skipFeatureIndex) {
        return inputs -> {
            List<PredictionOutput> predictionOutputs = new LinkedList<>();
            for (PredictionInput predictionInput : inputs) {
                List<Feature> features = predictionInput.getFeatures();
                double result = 0;
                for (int i = 0; i < features.size(); i++) {
                    if (skipFeatureIndex != i) {
                        result += features.get(i).getValue().asNumber();
                    }
                }
                PredictionOutput predictionOutput = new PredictionOutput(
                        List.of(new Output("sum-even-but" + skipFeatureIndex, Type.BOOLEAN, new Value<>(((int) result) % 2 == 0), 1d)));
                predictionOutputs.add(predictionOutput);
            }
            return predictionOutputs;
        };
    }

    public static PredictionProvider getDummyTextClassifier() {
        return new PredictionProvider() {
            private final List<String> blackList = Arrays.asList("money", "$", "£", "bitcoin");
            @Override
            public List<PredictionOutput> predict(List<PredictionInput> inputs) {
                List<PredictionOutput> outputs = new LinkedList<>();
                for (PredictionInput input : inputs) {
                    boolean spam = false;
                    for (Feature f : input.getFeatures()) {
                        if (!spam && Type.TEXT.equals(f.getType())) {
                            String s = f.getValue().asString();
                            String[] words = s.split(" ");
                            for (String w : words) {
                                if (blackList.contains(w)) {
                                    spam = true;
                                    break;
                                }
                            }
                        }
                    }
                    Output output = new Output("spam", Type.BOOLEAN, new Value<>(spam), 1d);
                    outputs.add(new PredictionOutput(List.of(output)));
                }
                return outputs;
            }
        };
    }

    public static Feature getRandomFeature() {
        Feature f;
        int r = random.nextInt(12);
        String name = "f-" + random.nextFloat();
        if (r == 0) {
            ByteBuffer buffer = ByteBuffer.allocate(random.nextInt(256));
            f = FeatureFactory.newBinaryFeature(name, buffer);
        } else if (r == 1) {
            f = FeatureFactory.newTextFeature(name, randomString());
        } else if (r == 2) {
            Map<String, Object> map = new HashMap<>();
            while (random.nextBoolean()) {
                map.put("s-" + random.nextInt(), randomString());
            }
            f = FeatureFactory.newCompositeFeature(name, map);
        } else if (r == 3) {
            f = FeatureFactory.newCategoricalFeature(name, randomString());
        } else if (r == 4) {
            f = FeatureFactory.newObjectFeature(name, getRandomFeature());
        } else if (r == 5) {
            f = FeatureFactory.newBooleanFeature(name, random.nextBoolean());
        } else if (r == 6) {
            f = FeatureFactory.newNumericalFeature(name, random.nextDouble());
        } else if (r == 7) {
            f = FeatureFactory.newDurationFeature(name, Duration.ofDays(random.nextInt(30)));
        } else if (r == 8) {
            f = FeatureFactory.newCurrencyFeature(name, Currency.getInstance(Locale.getDefault()));
        } else if (r == 9) {
            f = FeatureFactory.newTimeFeature(name, LocalTime.now());
        } else if (r == 10) {
            f = FeatureFactory.newURIFeature(name, URI.create(randomString().replaceAll(" ", "")));
        } else if (r == 11) {
            double[] doubles = new double[random.nextInt(10) + 1];
            for (int i = 0; i < doubles.length; i++) {
                doubles[i] = random.nextDouble();
            }
            f = FeatureFactory.newVectorFeature(name, doubles);
        } else {
            fail("unexpected feature type selector");
            f = null;
        }
        return f;
    }

    public static String randomString() {
        return RandomStringUtils.random(random.nextInt(5));
    }
}
