package org.kie.kogito.explainability.model;

/**
 * A feature represents fixed portions of an input, having a name, a {@link Type} and an associated {@link Value}.
 */
public class Feature {

    private final String name;
    private final Type type;
    private final Value value;

    Feature(String name, Type type, Value value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    /**
     * the name of the feature
     *
     * @return this feature name
     */
    public String getName() {
        return this.name;
    }

    /**
     * the type of the feature
     *
     * @return this feature type
     */
    public Type getType() {
        return type;
    }

    /**
     * the value of the feature
     *
     * @return the feature value
     */
    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Feature{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", value=" + value +
                '}';
    }
}
