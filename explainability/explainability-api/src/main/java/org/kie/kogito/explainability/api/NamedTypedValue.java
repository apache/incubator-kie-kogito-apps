package org.kie.kogito.explainability.api;

import org.kie.kogito.tracing.typedvalue.TypedValue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NamedTypedValue implements HasNameValue<TypedValue> {

    public static final String NAME_FIELD = "name";
    public static final String VALUE_FIELD = "value";

    @JsonProperty(NAME_FIELD)
    private String name;

    @JsonProperty(VALUE_FIELD)
    private TypedValue value;

    public NamedTypedValue() {
    }

    public NamedTypedValue(String name, TypedValue value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public TypedValue getValue() {
        return value;
    }

}
