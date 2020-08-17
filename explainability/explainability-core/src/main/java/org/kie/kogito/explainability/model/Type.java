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

import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Allowed data types.
 */
public enum Type {

    TEXT("text") {
        @Override
        public Value<?> drop(Value<?> value) {
            return new Value<>("");
        }

        @Override
        public Value<?> perturb(Value<?> value, PerturbationContext perturbationContext) {
            return new Value<>("");
        }

        @Override
        public List<double[]> encode(Value<?> target, Value<?>... values) {
            return encodeEquals(target, values);
        }
    },

    CATEGORICAL("categorical") {
        @Override
        public Value<?> drop(Value<?> value) {
            return new Value<>("");
        }

        @Override
        public Value<?> perturb(Value<?> value, PerturbationContext perturbationContext) {
            String category = value.asString();
            if (!"0".equals(category)) {
                category = "0";
            } else {
                category = "1";
            }
            return new Value<>(category);
        }

        @Override
        public List<double[]> encode(Value<?> target, Value<?>... values) {
            return encodeEquals(target, values);
        }
    },

    BINARY("binary") {
        @Override
        public Value<?> drop(Value<?> value) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(0);
            return new Value<>(byteBuffer);
        }

        @Override
        public Value<?> perturb(Value<?> value, PerturbationContext perturbationContext) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(0);
            return new Value<>(byteBuffer);
        }

        @Override
        public List<double[]> encode(Value<?> target, Value<?>... values) {
            return encodeEquals(target, values);
        }
    },

    NUMBER("number") {
        @Override
        public Value<?> drop(Value<?> value) {
            if (value.asNumber() == 0) {
                value = new Value<>(Double.NaN);
            } else {
                value = new Value<>(0d);
            }
            return value;
        }

        @Override
        public Value<?> perturb(Value<?> value, PerturbationContext perturbationContext) {
            double originalFeatureValue = value.asNumber();
            boolean intValue = originalFeatureValue % 1 == 0;

            // sample from a standard normal distribution and center around feature value
            double normalDistributionSample = perturbationContext.getRandom().nextGaussian();
            if (originalFeatureValue != 0d) {
                normalDistributionSample = normalDistributionSample * originalFeatureValue + originalFeatureValue;
            }
            if (intValue) {
                normalDistributionSample = (int) normalDistributionSample;
                if (normalDistributionSample == originalFeatureValue) {
                    normalDistributionSample = (int) normalDistributionSample * 10d;
                }
            }
            return new Value<>(normalDistributionSample);
        }

        @Override
        public List<double[]> encode(Value<?> target, Value<?>... values) {
            return encodeEquals(target, values);
        }
    },

    BOOLEAN("boolean") {
        @Override
        public Value<?> drop(Value<?> value) {
            return new Value<>(null);
        }

        @Override
        public Value<?> perturb(Value<?> value, PerturbationContext perturbationContext) {
            return new Value<>(!Boolean.getBoolean(value.asString()));
        }

        @Override
        public List<double[]> encode(Value<?> target, Value<?>... values) {
            return encodeEquals(target, values);
        }
    },

    URI("uri") {
        @Override
        public Value<?> drop(Value<?> value) {
            return new Value<>(java.net.URI.create(""));
        }

        @Override
        public Value<?> perturb(Value<?> value, PerturbationContext perturbationContext) {
            return new Value<>(java.net.URI.create(""));
        }

        @Override
        public List<double[]> encode(Value<?> target, Value<?>... values) {
            return encodeEquals(target, values);
        }
    },

    TIME("time") {
        @Override
        public Value<?> drop(Value<?> value) {
            return new Value<>(null);
        }

        @Override
        public Value<?> perturb(Value<?> value, PerturbationContext perturbationContext) {
            LocalTime featureValue = LocalTime.parse(value.asString());
            return new Value<>(featureValue.minusHours(1 + perturbationContext.getRandom().nextInt(23)));
        }

        @Override
        public List<double[]> encode(Value<?> target, Value<?>... values) {
            return encodeEquals(target, values);
        }
    },

    DURATION("duration") {
        @Override
        public Value<?> drop(Value<?> value) {
            return new Value<>(null);
        }

        @Override
        public Value<?> perturb(Value<?> value, PerturbationContext perturbationContext) {
            return new Value<>(Duration.of(0, ChronoUnit.SECONDS));
        }

        @Override
        public List<double[]> encode(Value<?> target, Value<?>... values) {
            return encodeEquals(target, values);
        }
    },

    VECTOR("vector") {
        @Override
        public Value<?> drop(Value<?> value) {
            double[] values = value.asVector();
            if (values.length > 0) {
                Arrays.fill(values, 0);
            }
            return new Value<>(values);
        }

        @Override
        public Value<?> perturb(Value<?> value, PerturbationContext perturbationContext) {
            // randomly set a non zero value to zero (or decrease it by 1)
            double[] vector = value.asVector();
            double[] values = Arrays.copyOf(vector, vector.length);
            if (values.length > 1) {
                int idx = perturbationContext.getRandom().nextInt(values.length);
                if (values[idx] != 0) {
                    values[idx] = 0;
                } else {
                    values[idx]--;
                }
            }
            return new Value<>(values);
        }

        @Override
        public List<double[]> encode(Value<?> target, Value<?>... values) {
            return encodeEquals(target, values);
        }
    },

    UNDEFINED("undefined") {
        @Override
        public Value<?> drop(Value<?> value) {
            if (value.getUnderlyingObject() instanceof Feature) {
                Feature underlyingObject = (Feature) value.getUnderlyingObject();
                value = new Value<>(FeatureFactory.copyOf(underlyingObject, underlyingObject.getType().drop(underlyingObject.getValue())));
            } else {
                value = new Value<>(null);
            }
            return value;
        }

        @Override
        public Value<?> perturb(Value<?> value, PerturbationContext perturbationContext) {
            if (value.getUnderlyingObject() instanceof Feature) {
                Feature underlyingObject = (Feature) value.getUnderlyingObject();
                Type type = underlyingObject.getType();
                Value<?> perturbedValue = type.perturb(underlyingObject.getValue(), perturbationContext);
                value = new Value<>(FeatureFactory.copyOf(underlyingObject, perturbedValue));
            }
            return value;
        }

        @Override
        public List<double[]> encode(Value<?> target, Value<?>... values) {
            return encodeEquals(target, values);
        }
    },

    COMPOSITE("composite") {
        @Override
        public Value<?> drop(Value<?> value) {
            List<Feature> composite = (List<Feature>) value.getUnderlyingObject();
            List<Feature> newFeatures = new ArrayList<>(composite.size());
            for (Feature f : composite) {
                newFeatures.add(FeatureFactory.copyOf(f, f.getType().drop(f.getValue())));
            }
            return new Value<>(newFeatures);
        }

        @Override
        public Value<?> perturb(Value<?> value, PerturbationContext perturbationContext) {
            List<Feature> composite = (List<Feature>) value.getUnderlyingObject();
            List<Feature> newList = new ArrayList<>(List.copyOf(composite));
            int[] indexesToBePerturbed = perturbationContext.getRandom().ints(0, composite.size()).distinct().limit(perturbationContext.getNoOfPerturbations()).toArray();
            for (int index : indexesToBePerturbed) {
                Feature cf = composite.get(index);
                Feature f = FeatureFactory.copyOf(cf, cf.getType().perturb(cf.getValue(), perturbationContext));
                newList.set(index, f);
            }
            return new Value<>(newList);
        }

        @Override
        public List<double[]> encode(Value<?> target, Value<?>... values) {
            List<Feature> composite = (List<Feature>) target.getUnderlyingObject();
            int i = 0;
            List<List<double[]>> multiColumns = new LinkedList<>();
            for (Feature f : composite) {
                int finalI = i;
                List<Value> subValues = Arrays.stream(values).map(v -> (List<Feature>) v.getUnderlyingObject()).map(l -> l.get(finalI).getValue()).collect(Collectors.toList());
                List<double[]> subColumn = f.getType().encode(f.getValue(), subValues.toArray(new Value<?>[0]));
                multiColumns.add(subColumn);
                i++;
            }
            List<double[]> result = new LinkedList<>();
            for (int j = 0; j < values().length; j++) {
                List<Double> vector = new LinkedList<>();
                for (List<double[]> multiColumn : multiColumns) {
                    for (double d : multiColumn.get(i)) {
                        vector.add(d);
                    }
                }
                double[] doubles = new double[vector.size()];
                for (int d = 0; d < doubles.length; d++) {
                    doubles[d] = vector.get(d);
                }
                result.add(doubles);
            }
            return result;
        }
    },

    CURRENCY("currency") {
        @Override
        public Value<?> drop(Value<?> value) {
            return new Value<>(null);
        }

        @Override
        public Value<?> perturb(Value<?> value, PerturbationContext perturbationContext) {
            return new Value<>(Currency.getInstance(Locale.getDefault()));
        }

        @Override
        public List<double[]> encode(Value<?> target, Value<?>... values) {
            return encodeEquals(target, values);
        }
    };

    static List<double[]> encodeEquals(Value<?> target, Value<?>[] values) {
        List<double[]> result = new ArrayList<>(values.length);
        for (Value<?> value : values) {
            double[] data = new double[1];
            if (target.getUnderlyingObject().equals(value.getUnderlyingObject())) {
                data[0] = 1d;
            } else {
                data[0] = 0d;
            }
            result.add(data);
        }
        return result;
    }

    private final String value;

    Type(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public abstract Value<?> drop(Value<?> value);

    public abstract Value<?> perturb(Value<?> value, PerturbationContext perturbationContext);

    public abstract List<double[]> encode(Value<?> target, Value<?>... values);
}