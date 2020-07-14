package org.kie.kogito.explainability.model;

import java.util.Arrays;

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
