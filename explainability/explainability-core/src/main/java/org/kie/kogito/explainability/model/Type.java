package org.kie.kogito.explainability.model;

public enum Type {

    TEXT("text"),

    CATEGORICAL("categorical"),

    BINARY("binary"),

    NUMBER("number"),

    BOOLEAN("boolean"),

    URI("uri"),

    TIME("time"),

    DURATION("duration"),

    VECTOR("vector"),

    UNDEFINED("undefined"),

    COMPOSITE("composite"),

    CURRENCY("currency");

    private final String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static Type fromValue(String text) {
        for (Type b : Type.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}