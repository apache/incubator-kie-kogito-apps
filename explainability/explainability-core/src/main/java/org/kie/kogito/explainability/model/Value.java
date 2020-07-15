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

import java.util.Arrays;

/**
 * Wrapper class for any kind of value part of a prediction input or output.
 * @param <S>
 */
public class Value<S> {

    private final S underlyingObject;

    public Value(S underlyingObject) {
        this.underlyingObject = underlyingObject;
    }

    public String asString() {
        if (underlyingObject == null) {
            return "";
        } else {
            return String.valueOf(underlyingObject);
        }
    }

    public double asNumber() {
        if (underlyingObject != null) {
            return underlyingObject instanceof Boolean ? (Boolean) underlyingObject ? 1d : 0d : Double.parseDouble(asString());
        } else {
            return Double.NaN;
        }
    }

    public S getUnderlyingObject() {
        return underlyingObject;
    }

    @Override
    public String toString() {
        return "Value{" + underlyingObject + '}';
    }

    public double[] asVector() {
        double[] doubles;
        try {
            doubles = (double[]) underlyingObject;
        } catch (ClassCastException cce) {
            if (underlyingObject instanceof String) {
                int noOfWords = ((String) underlyingObject).split(" ").length;
                doubles = new double[noOfWords];
                Arrays.fill(doubles, 1);
            } else {
                try {
                    double v = asNumber();
                    doubles = new double[1];
                    doubles[0] = v;
                } catch (Exception e) {
                    doubles = new double[0];
                }
            }
        }
        return doubles;
    }
}
